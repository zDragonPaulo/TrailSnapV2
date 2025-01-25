package com.example.trailsnapv2.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val usernameTextView: TextView = view.findViewById(R.id.username)
        val birthdayTextView: TextView = view.findViewById(R.id.birthday)
        val totalDistanceTextView: TextView = view.findViewById(R.id.total_distance)
        val timeUsedTextView: TextView = view.findViewById(R.id.time_used)
        val settingsButton: Button = view.findViewById(R.id.button_settings)

        // Observe the ViewModel data and update the UI
        viewModel.username.observe(viewLifecycleOwner) {
            usernameTextView.text = it
        }
        viewModel.birthday.observe(viewLifecycleOwner) {
            birthdayTextView.text = it
        }
        viewModel.totalDistance.observe(viewLifecycleOwner) {
            totalDistanceTextView.text = it
        }
        viewModel.timeUsed.observe(viewLifecycleOwner) {
            timeUsedTextView.text = it
        }

        // Set up the button click listener to navigate to the edit profile page
        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_editProfileFragment,
                null,
                null,
                null)
        }

        return view
    }
}