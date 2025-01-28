package com.example.trailsnapv2.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.FragmentDashboardBinding
import com.example.trailsnapv2.ui.achievements.AchievementsAdapter
import kotlinx.coroutines.launch

/**
 * Fragment for displaying the user's dashboard, including profile data,
 * total distance walked, total time used, and the user's achievements.
 *
 * The dashboard also includes an option to access the app's settings.
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var achievementsAdapter: AchievementsAdapter
    private lateinit var dashboardViewModel: DashboardViewModel
    private var userId: Long = -1L

    /**
     * Inflates the fragment layout and initializes the RecyclerView for achievements.
     *
     * @param inflater The LayoutInflater object to inflate the view.
     * @param container The container to place the fragment in.
     * @param savedInstanceState Any saved instance state of the fragment.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        achievementsAdapter = AchievementsAdapter()
        binding.recyclerViewAchievements.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = achievementsAdapter
        }

        return binding.root
    }

    /**
     * Initializes the ViewModel, sets up observers, and loads initial data for the dashboard.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState Any saved instance state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonSettings: ImageButton = binding.root.findViewById(R.id.button_settings)
        buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_appSettingsFragment)
        }

        userId = getCurrentUserId()
        if (userId == -1L) return

        val userDao = (requireActivity().application as MyApp).database.userDao()
        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        val userAchievementDao = (requireActivity().application as MyApp).database.userAchievementDao()
        val factory = DashboardViewModelFactory(userDao, walkDao, userAchievementDao)
        dashboardViewModel = ViewModelProvider(this, factory).get(DashboardViewModel::class.java)

        observeViewModel()
        refreshDashboardData()
    }

    /**
     * Refreshes the dashboard data when the fragment is resumed.
     */
    override fun onResume() {
        super.onResume()

        refreshDashboardData()
    }

    /**
     * Refreshes the dashboard data by reloading the user profile, total walks,
     * and top achievements.
     */
    private fun refreshDashboardData() {
        lifecycleScope.launch {
            dashboardViewModel.loadUserData(userId)
            dashboardViewModel.loadTotalWalks(userId)
            dashboardViewModel.loadTopAchievements(userId)
        }
    }

    /**
     * Sets up observers to update the UI when the ViewModel data changes.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {
        dashboardViewModel.userData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                updateDashboardStats(user.total_distance, user.time_used.toInt())
                Log.d("DashboardFragment", "User data loaded: Distance=${user.total_distance}, Time=${user.time_used}")
            }
        }

        dashboardViewModel.userAchievements.observe(viewLifecycleOwner) { userAchievements ->
            dashboardViewModel.singularAchievements.observe(viewLifecycleOwner) { singularAchievements ->
                achievementsAdapter.setUserAchievements(userAchievements, singularAchievements)
                achievementsAdapter.notifyDataSetChanged()
                binding.textNoAchievements.visibility = if (userAchievements.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        dashboardViewModel.totalWalks.observe(viewLifecycleOwner) { totalWalks ->
            binding.textTotalWalks.text = getString(R.string.total_walks_text, totalWalks)
        }
    }

    /**
     * Updates the dashboard stats, including total distance and time used.
     * @param totalDistance The total distance walked in meters.
     * @param timeUsed The total time used in seconds.
     */
    private fun updateDashboardStats(totalDistance: Double, timeUsed: Int) {
        val formattedDistance = totalDistance / 1000 // Converte para quilômetros
        binding.textTotalDistance.text = getString(R.string.total_distance_text, formattedDistance)
        binding.textTotalTime.text = getString(R.string.total_time, formatTime(timeUsed))
    }

    /**
     * Formats the time in seconds into a readable string (seconds, minutes, or hours).
     * @param time The time in seconds.
     * @return The formatted time string.
     */
    private fun formatTime(time: Int): String {
        return when {
            time < 60 -> "$time sec"
            time < 3600 -> "${time / 60} min ${time % 60} sec"
            else -> "${time / 3600} h"
        }
    }

    /**
     * Retrieves the current user's ID from shared preferences.
     * @return The user ID, or -1 if no user is found.
     */
    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        return sharedPref.getLong("current_user_id", -1L).also { id ->
            if (id == -1L) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.user_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Cleans up the binding when the view is destroyed to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
