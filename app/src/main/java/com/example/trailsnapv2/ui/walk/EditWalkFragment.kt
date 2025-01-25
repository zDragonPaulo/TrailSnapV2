package com.example.trailsnapv2.ui.walk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R

class EditWalkFragment : Fragment() {

    companion object {
        fun newInstance() = EditWalkFragment()
    }

    private val viewModel: EditWalkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_edit_walk, container, false)
        val buttonSaveWalk : Button = view.findViewById(R.id.saveButton)


        buttonSaveWalk.setOnClickListener {
            //TODO: Save the edit walk in Database





            findNavController().navigate(R.id.action_editWalkFragment_to_walkDetailsFragment)
        }




        return view
    }
}