package com.example.trailsnapv2.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R
import kotlinx.coroutines.launch

/**
 * A fragment responsible for handling user login functionality.
 * It allows the user to enter a username and password, and attempts to authenticate the user.
 * Upon successful login, the user is redirected to the dashboard.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    /**
     * Inflates the login fragment layout and initializes UI components like the username,
     * password input fields, and login button.
     * Sets up a click listener on the login button to initiate the login process.
     *
     * @param inflater The LayoutInflater used to inflate the fragment's view.
     * @param container The container that holds the fragment's view.
     * @param savedInstanceState The saved instance state of the fragment.
     * @return The root view of the fragment.
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


        val createAccount : TextView = view.findViewById(R.id.create_account_button)
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                val success = viewModel.loginUser(username, password)
                if (success) {
                    val userId = viewModel.getUserId(username)
                    userId?.let {
                        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putLong("current_user_id", userId)
                            apply()
                        }
                    }

                    Toast.makeText(context, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_navigation_dashboard)
                } else {
                    Toast.makeText(context, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
                }
            }
        }
        createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }
}