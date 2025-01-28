package com.example.trailsnapv2.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R

/**
 * Fragment responsible for displaying the user's profile information.
 * This fragment shows the user's username, birthday, total distance, and time used.
 * It also allows the user to navigate to the EditProfileFragment to modify their profile.
 */
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory((requireActivity().application as MyApp).database.userDao())
    }

    /**
     * Inflates the layout for the profile fragment and sets up the UI components.
     * The fragment displays the user's profile information such as username, birthday,
     * description, and profile picture. The user can also navigate to the EditProfileFragment
     * or log out of their account.
     *
     * @param inflater The LayoutInflater object to inflate the view.
     * @param container The container for the fragment's view.
     * @param savedInstanceState Any previously saved state for the fragment.
     * @return The view for the profile fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("current_user_id", -1L)

        if (userId != -1L) {
            viewModel.loadUserData(userId)
        } else {
            Log.e("ProfileFragment", "User not Found")
        }

        val usernameTextView: TextView = view.findViewById(R.id.username)
        val birthdayTextView: TextView = view.findViewById(R.id.birthday)
        val profileImageView: ImageView = view.findViewById(R.id.user_image)
        val descriptionTextView: TextView = view.findViewById(R.id.description)
        val settingsButton: Button = view.findViewById(R.id.button_settings)
        val logoutButton: Button = view.findViewById(R.id.button_logout)

        viewModel.username.observe(viewLifecycleOwner) { username ->
            usernameTextView.text = username
        }
        viewModel.birthday.observe(viewLifecycleOwner) { birthday ->
            birthdayTextView.text = birthday
        }
        viewModel.description.observe(viewLifecycleOwner) { description ->
            descriptionTextView.text = description
        }
        viewModel.profilePicture.observe(viewLifecycleOwner) { profilePicture ->
            profileImageView.setImageURI(Uri.parse(profilePicture))
        }

        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_editProfileFragment)
        }

        logoutButton.setOnClickListener {
            val editor = sharedPref.edit()
            editor.remove("current_user_id")
            editor.apply()

            findNavController().navigate(R.id.action_navigation_profile_to_loginFragment)
        }

        return view
    }

}