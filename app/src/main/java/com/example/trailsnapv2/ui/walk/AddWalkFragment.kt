package com.example.trailsnapv2.ui.walk

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R
import com.google.android.material.textfield.TextInputLayout

class AddWalkFragment : Fragment() {

    companion object {
        fun newInstance() = AddWalkFragment()
    }

    private val viewModel: AddWalkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_walk, container, false)
        val walkName : TextInputLayout = view.findViewById(R.id.textInputLayoutWalkName)
        val buttonStartWalk : Button = view.findViewById(R.id.startWalk)


        buttonStartWalk.setOnClickListener {
            // TODO: Save the walk to the database


            findNavController().navigate(R.id.action_navigation_add_walk_to_walkFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_add_walk, true)
                    .build())
        }




        return view
    }
}