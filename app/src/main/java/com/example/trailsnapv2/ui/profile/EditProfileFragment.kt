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
import androidx.lifecycle.Observer
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

        // Inicializar os componentes da interface
        usernameEditText = view.findViewById(R.id.edit_username)
        passwordEditText = view.findViewById(R.id.edit_password)
        descriptionEditText = view.findViewById(R.id.edit_user_description)
        birthdayEditText = view.findViewById(R.id.edit_birthday)
        profileImageView = view.findViewById(R.id.edit_user_image)
        val selectImageButton: Button = view.findViewById(R.id.button_select_image)
        val saveButton: Button = view.findViewById(R.id.button_save)

        // Configurar o campo de aniversário
        configureBirthdayEditText()

        // Ação para selecionar imagem
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }

        // Ação para salvar o perfil
        saveButton.setOnClickListener {
            // Use the existing profile picture if no new image is selected
            val imagePath = if (selectedImageUri != null) {
                saveImageToInternalStorage(selectedImageUri!!).also {
                    Log.d("EditProfileFragment", "New image saved at: $it")
                }
            } else {
                viewModel.profilePicture.value ?: "" // Use the existing profile picture from ViewModel
            }

            // Check if the imagePath is valid
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
                total_distance = 0.0, // Placeholder value
                time_used = 0L, // Placeholder value
                creation_date = "", // Placeholder value
                profile_picture = imagePath // Use the path to the saved image
            )
            viewModel.updateUser(updatedUser)
        }

        // Observar mudanças no status da atualização
        viewModel.updateStatus.observe(viewLifecycleOwner, Observer { status ->
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
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        userId = sharedPref.getLong("current_user_id", -1L)

        if (userId != -1L) {
            // Busca os dados do usuário logado
            viewModel.getUserById(userId).observe(viewLifecycleOwner, Observer { user ->
                user?.let {
                    // Popula os campos do formulário com os dados do usuário
                    usernameEditText.setText(it.username)
                    passwordEditText.setText(it.password)
                    descriptionEditText.setText(it.user_description)
                    birthdayEditText.setText(it.birthday)
                    selectedImageUri = it.profile_picture?.let { uriString -> Uri.parse(uriString) }
                    Log.d("IMAGEM, TÁ TUDO BEM?", selectedImageUri.toString())
                    // Handle profile picture correctly
                    if (!it.profile_picture.isNullOrEmpty()) {
                        val file = File(it.profile_picture)
                        if (file.exists()) {
                            // Use the file path directly for the image
                            selectedImageUri = Uri.fromFile(file) // Convert file path to Uri
                            profileImageView.setImageURI(selectedImageUri)
                        } else {
                            // Fallback to placeholder if the file doesn't exist
                            profileImageView.setImageResource(R.drawable.ic_profile)
                        }
                    } else {
                        // Placeholder for no profile picture
                        profileImageView.setImageResource(R.drawable.ic_profile)
                    }
                }
            })
        } else {
            // Redireciona para o login se o ID do usuário não for encontrado
            Toast.makeText(requireContext(), getString(R.string.user_not_found), Toast.LENGTH_SHORT).show()
        }
    }

    private fun configureBirthdayEditText() {
        // Adiciona um TextWatcher para inserir barras automaticamente
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
            file.absolutePath // Return the file path after saving
        } catch (e: IOException) {
            e.printStackTrace()
            null // Return null if an error occurs
        }
    }
}