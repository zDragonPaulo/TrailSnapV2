package com.example.trailsnapv2.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.R

/**
 * A fragment that displays the user's walking history. This fragment interacts with the
 * HistoryViewModel to fetch and display the list of walks recorded by the user.
 * The fragment provides a RecyclerView to list the walks and navigates to a detailed view
 * when a specific walk is selected.
 */
class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val walkDao = database.walkDao()
        val userId = getCurrentUserId()
        HistoryViewModelFactory(walkDao, userId)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WalkHistoryAdapter

    /**
     * Inflates the fragment's view, sets up the RecyclerView, and binds the data from the ViewModel.
     * This method also initializes the adapter and observes the history items in the ViewModel.
     *
     * @param inflater The LayoutInflater used to inflate the fragment's layout.
     * @param container The parent ViewGroup that the fragment's UI will be attached to.
     * @param savedInstanceState A bundle containing any saved instance state for the fragment.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = binding.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = WalkHistoryAdapter(requireContext()){ walkId ->
            navigateToDetails(walkId)
        }
        recyclerView.adapter = adapter

        viewModel.historyItems.observe(viewLifecycleOwner) { items ->
            if (items.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_walk_found),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                adapter.submitList(items)
            }
        }

        return binding
    }

    /**
     * Navigates to the walk details screen when a walk item is selected.
     * The selected walk's ID is passed in a bundle to the destination fragment.
     *
     * @param walkId The ID of the selected walk.
     */
    private fun navigateToDetails(walkId: Long) {
        val bundle = Bundle().apply {
            putLong("walkId", walkId)
        }
        findNavController().navigate(R.id.action_navigation_history_to_walkDetailsFragment, bundle)
    }

    /**
     * Loads the walk history items whenever the fragment is resumed.
     * This ensures the list of history items is up to date when the fragment becomes visible.
     */
    override fun onResume() {
        super.onResume()
        viewModel.loadHistoryItems()
    }

    /**
     * Retrieves the current user's ID from SharedPreferences.
     * If the user ID is not found, a toast message is shown indicating the user is not logged in.
     *
     * @return The current user's ID.
     */
    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        return sharedPref.getLong("current_user_id", -1L).also { userId ->
            if (userId == -1L) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.user_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
