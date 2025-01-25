package com.example.trailsnapv2.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R
import kotlinx.coroutines.launch

/**
 * Fragment responsible for handling the user login UI and logic.
 * It collects user input for username and password, performs the login verification,
 * and provides feedback to the user via Toast messages.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    /**
     * Inflates the layout for the login screen, initializes UI components, and handles login button click.
     * The login credentials entered by the user are verified by the LoginViewModel.
     * If the login is successful, the user is navigated to the dashboard. Otherwise, an error message is shown.
     *
     * @param inflater The LayoutInflater object used to inflate the view.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     * @return The inflated view for this fragment.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val usernameEditText: EditText = view.findViewById(R.id.username)
        val passwordEditText: EditText = view.findViewById(R.id.password)
        val loginButton: Button = view.findViewById(R.id.register_bypass_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                val success = viewModel.loginUser(username, password)
                if (success) {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_navigation_dashboard)
                } else {
                    Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}