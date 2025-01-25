package com.example.trailsnapv2.ui.walk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R
import com.google.android.material.textfield.TextInputLayout

class AddWalkFragment : Fragment() {

    private lateinit var viewModel: WalkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_walk, container, false)
        val walkName: TextInputLayout = view.findViewById(R.id.textInputLayoutWalkName)
        val buttonStartWalk: Button = view.findViewById(R.id.startWalk)

        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return WalkViewModel(walkDao) as T
            }
        }).get(WalkViewModel::class.java)

        buttonStartWalk.setOnClickListener {
            val name = walkName.editText?.text.toString()
            val userId = 1L // Example user ID
            val startTime = "2025-01-25T15:00:00" // Example start time
            val endTime = "2025-01-25T16:00:00" // Example end time
            val distance = 5.0 // Example distance

            viewModel.createWalk(name, userId, startTime, endTime, distance)
            findNavController().navigate(R.id.action_navigation_add_walk_to_walkFragment)
        }

        return view
    }
}