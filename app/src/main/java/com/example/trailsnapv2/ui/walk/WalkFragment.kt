package com.example.trailsnapv2.ui.walk

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R

class WalkFragment : Fragment() {

    private lateinit var viewModel: WalkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_walk, container, false)
        val walkNameTextView: TextView = view.findViewById(R.id.walkName)
        val distanceInput: TextInputLayout = view.findViewById(R.id.textInputLayoutDistance)
        val timeInput: TextInputLayout = view.findViewById(R.id.textInputLayoutTime)
        val buttonFinishWalk: Button = view.findViewById(R.id.finishWalk)

        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        val factory = WalkViewModel.Factory(walkDao)
        viewModel = ViewModelProvider(this, factory).get(WalkViewModel::class.java)

        val walkId = arguments?.getLong("walkId") ?: 0L
        viewModel.getWalkById(walkId).observe(viewLifecycleOwner) { walk ->
            walk?.let {
                walkNameTextView.text = it.walk_name
                distanceInput.editText?.setText(it.distance.toString())
            }
        }

        buttonFinishWalk.setOnClickListener {
            val distance = distanceInput.editText?.text.toString().toDoubleOrNull()
            val time = timeInput.editText?.text.toString().toLongOrNull()

            if (distance != null && time != null) {
                findNavController().navigate(R.id.action_walkFragment_to_editWalkFragment)
            } else {
                // Show error message if input is invalid
            }
        }

        return view
    }
}