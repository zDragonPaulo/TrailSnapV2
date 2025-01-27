package com.example.trailsnapv2.ui.dashboard

import AchievementsAdapter
import DashboardViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.FragmentDashboardBinding
import com.example.trailsnapv2.entities.UserAchievement


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var achievementsAdapter: AchievementsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the RecyclerView and adapter
        achievementsAdapter = AchievementsAdapter()
        binding.recyclerViewAchievements.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = achievementsAdapter
        }

        // Observe data from ViewModel
        dashboardViewModel.userAchievements.observe(viewLifecycleOwner) { userAchievements ->
            dashboardViewModel.singularAchievements.observe(viewLifecycleOwner) { singularAchievements ->
                // Update RecyclerView with achievements data
                achievementsAdapter.setUserAchievements(userAchievements, singularAchievements)

                // Update dashboard stats
                updateDashboardStats(userAchievements)
            }
        }

        // Handle settings button click
        val buttonSettings: ImageButton = binding.root.findViewById(R.id.button_settings)
        buttonSettings.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_dashboard_to_appSettingsFragment,
                null,
                null,
                null
            )
        }

        return root
    }

    /**
     * Updates the dashboard's total distance and total walks TextViews.
     */
    private fun updateDashboardStats(userAchievements: List<UserAchievement>) {
        // Replace these calculations with actual logic for distance/walks
        val totalDistance = userAchievements.sumOf { it.progress } // Example logic for distance
        val totalWalks = userAchievements.size

        binding.textTotalDistance.text = getString(R.string.total_distance_text, totalDistance / 100) // Assuming 100% is 1 km
        binding.textTotalWalks.text = getString(R.string.total_walks_text, totalWalks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
