package com.example.trailsnapv2.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.R
import com.example.trailsnapv2.AppDatabase

class HistoryFragment : Fragment() {

    private val userId: Long = 1 // You need to get the actual logged-in user's ID

    private val viewModel: HistoryViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val walkDao = database.walkDao()
        HistoryViewModelFactory(walkDao, userId)  // Pass userId here
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_history, container, false)
        val recyclerView: RecyclerView = binding.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = WalkHistoryAdapter()
        recyclerView.adapter = adapter


        // Observe the historyItems LiveData
        viewModel.historyItems.observe(viewLifecycleOwner, Observer { items ->
            if (items.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No walks found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.submitList(items)  // Update RecyclerView with the filtered walks
            }
        })

        return binding
    }
}

