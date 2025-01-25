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

class
WalkFragment : Fragment() {

    companion object {
        fun newInstance() = WalkFragment()
    }

    private val viewModel: WalkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_walk, container, false)
        val buttonFinishWalk : Button = view.findViewById(R.id.finishWalk)


        buttonFinishWalk.setOnClickListener {


            findNavController().navigate(R.id.action_walkFragment_to_editWalkFragment)
        }

        return view
    }
}