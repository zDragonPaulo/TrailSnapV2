package com.example.trailsnapv2.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R
import com.example.trailsnapv2.entities.User
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * A Fragment that handles editing the user profile.
 *
 * This fragment provides functionality for users to update their profile details such as username,
 * password, description, birthday, and profile picture. Changes are saved to the database
 * through the associated ViewModel.
 */
class EditProfileFragment : Fragment() {

    private val viewModel: EditProfileViewModel by viewModels {
        EditProfileViewModelFactory((requireActivity().application as MyApp).database.userDao())
    }

    private lateinit var profileImageView: ImageView
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var birthdayEditText: EditText
    private var selectedImageUri: Uri? = null
    private var userId: Long = 0L
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            profileImageView.setImageURI(selectedImageUri)
        }
    }

    /**
     * Inflates the fragment layout and initializes UI elements and listeners.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        usernameEditText = view.findViewById(R.id.edit_username)
        passwordEditText = view.findViewById(R.id.edit_password)
        descriptionEditText = view.findViewById(R.id.edit_user_description)
        birthdayEditText = view.findViewById(R.id.edit_birthday)
        profileImageView = view.findViewById(R.id.edit_user_image)
        val selectImageButton: Button = view.findViewById(R.id.button_select_image)
        val saveButton: Button = view.findViewById(R.id.button_save)

        configureBirthdayEditText()

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }

        saveButton.setOnClickListener {
            val imagePath = if (selectedImageUri != null) {
                saveImageToInternalStorage(selectedImageUri!!).also {
                    Log.d("EditProfileFragment", "New image saved at: $it")
                }
            } else {
                viewModel.profilePicture.value ?: ""
            }

            if (imagePath.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Failed to save image. Please try again.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedUser = User(
                user_id = userId,
                username = usernameEditText.text.toString(),
                password = passwordEditText.text.toString(),
                user_description = descriptionEditText.text.toString(),
                birthday = birthdayEditText.text.toString(),
                total_distance = 0.0,
                time_used = 0L,
                creation_date = "",
                profile_picture = imagePath
            )
            viewModel.updateUser(updatedUser)
        }

        viewModel.updateStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                EditProfileViewModel.UpdateStatus.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.profile_updated_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }

                EditProfileViewModel.UpdateStatus.FAILURE -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.failed_to_update_profile),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return view
    }

    /**
     * Handles the logic to load user data when the fragment is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        userId = sharedPref.getLong("current_user_id", -1L)

        if (userId != -1L) {
            viewModel.getUserById(userId).observe(viewLifecycleOwner) { user ->
                user?.let {
                    usernameEditText.setText(it.username)
                    passwordEditText.setText(it.password)
                    descriptionEditText.setText(it.user_description)
                    birthdayEditText.setText(it.birthday)
                    selectedImageUri = it.profile_picture?.let { uriString -> Uri.parse(uriString) }

                    Log.d("EditProfileFragment", "Profile picture: ${it.profile_picture}")

                    if (!it.profile_picture.isNullOrEmpty()) {
                        val file = File(it.profile_picture)
                        if (file.exists()) {
                            selectedImageUri = Uri.fromFile(file)
                            profileImageView.setImageURI(selectedImageUri)
                        } else {
                            profileImageView.setImageResource(R.drawable.ic_profile)
                        }
                    } else {
                        profileImageView.setImageResource(R.drawable.ic_profile)
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.user_not_found), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Configures the birthday input field to automatically format text as a date (DD/MM/YYYY).
     */
    private fun configureBirthdayEditText() {
        birthdayEditText.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true

                val text = s.toString().replace("/", "")
                val formatted = StringBuilder()

                for (i in text.indices) {
                    formatted.append(text[i])
                    if ((i == 1 || i == 3) && i != text.lastIndex) {
                        formatted.append("/")
                    }
                }

                birthdayEditText.removeTextChangedListener(this)
                birthdayEditText.setText(formatted.toString())
                birthdayEditText.setSelection(formatted.length)
                birthdayEditText.addTextChangedListener(this)

                isUpdating = false
            }
        })
    }

    /**
     * Loads the user data from the ViewModel and populates the UI.
     */
    private fun saveImageToInternalStorage(uri: Uri): String? {
        val context = requireContext()
        val contentResolver = context.contentResolver
        val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}