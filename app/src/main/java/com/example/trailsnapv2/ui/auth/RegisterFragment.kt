package com.example.trailsnapv2.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.launch

/**
 * A Fragment that handles the user registration process.
 * It allows users to create a new account by providing a username and password.
 * It interacts with the `RegisterViewModel` to handle registration logic.
 */
class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel

    /**
     * Inflates the fragment's layout and sets up the UI components.
     * Binds the UI elements like EditText for username, password, and confirm password.
     * Initializes the ViewModel and sets up click listeners for the register and "already have an account" buttons.
     *
     * @param inflater The LayoutInflater object used to inflate the fragment's layout.
     * @param container The parent view group that the fragment's view will be attached to.
     * @param savedInstanceState A Bundle containing the fragment's previously saved state.
     * @return The root view of the fragment's layout.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        val usernameEditText: EditText = view.findViewById(R.id.username)
        val passwordEditText: EditText = view.findViewById(R.id.password)
        val confirmPasswordEditText: EditText = view.findViewById(R.id.confirm_password)
        val registerButton: Button = view.findViewById(R.id.register_button)
        val alreadyHaveAccountButton: TextView = view.findViewById(R.id.register_bypass_button)

        alreadyHaveAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            if (username == "" || password == "" || confirmPassword == "") {
                Toast.makeText(context, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password == confirmPassword) {
                val user = User(
                    user_id = 0,
                    username = username,
                    password = password,
                    user_description = "",
                    birthday = "",
                    total_distance = 0.0,
                    time_used = 0,
                    creation_date = "",
                    profile_picture = ""
                )
                Log.d("RegisterFragment", "Registering user: $user")
                lifecycleScope.launch {
                    val userId = viewModel.registerUser(user)
                    if (userId !== -1L) {
                        Log.d("RegisterFragment", "Registered user ID: $userId")
                        Toast.makeText(context, getString(R.string.user_registered_successfully), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        Toast.makeText(context,
                            getString(R.string.register_error), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, getString(R.string.password_not_match), Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}