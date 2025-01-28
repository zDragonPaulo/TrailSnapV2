package com.example.trailsnapv2.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }

        saveButton.setOnClickListener {
            val imagePath = selectedImageUri?.let { saveImageToInternalStorage(it) }
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
                        getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }

                EditProfileViewModel.UpdateStatus.FAILURE -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.failed_to_update_profile), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return view
    }

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
                    profileImageView.setImageURI(selectedImageUri)
                }
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.user_not_found), Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        val context = requireContext()
        val contentResolver = context.contentResolver
        val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
