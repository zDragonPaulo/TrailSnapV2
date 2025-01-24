// LoginFragment.kt
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
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val usernameEditText: EditText = view.findViewById(R.id.username)
        val passwordEditText: EditText = view.findViewById(R.id.password)
        val loginButton: Button = view.findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (viewModel.loginUser(username, password)) {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_navigation_dashboard)
            } else {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}