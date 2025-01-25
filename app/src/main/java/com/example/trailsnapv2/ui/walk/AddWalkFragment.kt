package com.example.trailsnapv2.ui.walk

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R
import com.example.trailsnapv2.ui.auth.LoginViewModel
import com.google.android.material.textfield.TextInputLayout

class AddWalkFragment : Fragment() {

    private lateinit var addWalkViewModel: AddWalkViewModel
    private val userViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_walk, container, false)
        val walkName: TextInputLayout = view.findViewById(R.id.textInputLayoutWalkName)
        val buttonStartWalk: Button = view.findViewById(R.id.startWalk)

        // Initialize AddWalkViewModel
        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        val factory = AddWalkViewModel.Factory(walkDao)
        addWalkViewModel = ViewModelProvider(this, factory).get(AddWalkViewModel::class.java)

        // Observe user data
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                Log.d("AddWalkFragment", "Observed user ID: ${it.user_id}")
            }
        }

        buttonStartWalk.setOnClickListener {
            val name = walkName.editText?.text.toString()
            val userId = userViewModel.user.value?.user_id ?: 0L

            Log.d("AddWalkFragment", "Retrieved user ID: $userId")

            if (name.isBlank()) {
                walkName.error = "Walk name cannot be empty"
                return@setOnClickListener
            }

            if (userId == 0L) {
                Log.e("AddWalkFragment", "Invalid user_id. Make sure the user is logged in.")
                return@setOnClickListener
            }

            val startTime = "2025-01-25T15:00:00" // Example start time
            val endTime = "2025-01-25T16:00:00" // Example end time
            val distance = 5.0 // Example distance

            addWalkViewModel.createWalk(name, userId, startTime, endTime, distance)

            // Navigate to the next fragment
            findNavController().navigate(R.id.action_navigation_add_walk_to_walkFragment)
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe user data
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Log.d("AddWalkFragment", "Observed user ID in onViewCreated: ${user.user_id}")
            } else {
                Log.e("AddWalkFragment", "User data is null in onViewCreated.")
            }
        }
    }

}