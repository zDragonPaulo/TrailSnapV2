package com.example.trailsnapv2.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R
import com.example.trailsnapv2.entities.User

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val usernameEditText: EditText = view.findViewById(R.id.username)
        val passwordEditText: EditText = view.findViewById(R.id.password)
        val confirmPasswordEditText: EditText = view.findViewById(R.id.confirm_password)
        val registerButton: Button = view.findViewById(R.id.register_button)
        val alreadyHaveAcountButton: Button = view.findViewById(R.id.register_bypass_button)

        alreadyHaveAcountButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

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
                    profile_picture = "" // Use an empty string or a valid path
                )
                Log.d("RegisterFragment", "Registering user: $user")
                viewModel.registerUser(user)
                Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}