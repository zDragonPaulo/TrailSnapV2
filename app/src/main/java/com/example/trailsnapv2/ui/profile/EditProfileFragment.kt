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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R
import com.example.trailsnapv2.entities.User

class EditProfileFragment : Fragment() {

    private val viewModel: EditProfileViewModel by viewModels {
        EditProfileViewModelFactory((requireActivity().application as MyApp).database.userDao())
    }

    private lateinit var profileImageView: ImageView
    private var selectedImageUri: Uri? = null

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

        val usernameEditText: EditText = view.findViewById(R.id.edit_username)
        val passwordEditText: EditText = view.findViewById(R.id.edit_password)
        val descriptionEditText: EditText = view.findViewById(R.id.edit_user_description)
        val birthdayEditText: EditText = view.findViewById(R.id.edit_birthday)
        profileImageView = view.findViewById(R.id.edit_user_image)
        val selectImageButton: Button = view.findViewById(R.id.button_select_image)
        val saveButton: Button = view.findViewById(R.id.button_save)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }

        saveButton.setOnClickListener {
            val updatedUser = User(
                user_id = 1L, // Assuming user ID is 1, replace with actual user ID
                username = usernameEditText.text.toString(),
                password = passwordEditText.text.toString(),
                user_description = descriptionEditText.text.toString(),
                birthday = birthdayEditText.text.toString(),
                total_distance = 0.0, // Placeholder value
                time_used = 0L, // Placeholder value
                creation_date = "", // Placeholder value
                profile_picture = selectedImageUri.toString() // Use the selected image URI
            )
            viewModel.updateUser(updatedUser)
        }

        viewModel.updateStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                EditProfileViewModel.UpdateStatus.SUCCESS -> {
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                EditProfileViewModel.UpdateStatus.FAILURE -> {
                    Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return view
    }
}