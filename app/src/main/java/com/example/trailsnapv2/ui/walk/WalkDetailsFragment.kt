package com.example.trailsnapv2.ui.walk

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R

class WalkDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = WalkDetailsFragment()
    }

    private val viewModel: WalkDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_walk_details, container, false)
        val buttonEditWalk : Button = view.findViewById(R.id.editButton)


        buttonEditWalk.setOnClickListener {


            findNavController().navigate(R.id.action_walkDetailsFragment_to_editWalkFragment)
        }


        return view
    }
}