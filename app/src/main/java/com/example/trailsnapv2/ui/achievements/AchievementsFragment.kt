package com.example.trailsnapv2.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.databinding.FragmentAchievementsBinding
import com.example.trailsnapv2.entities.SingularAchievement

class AchievementsFragment : Fragment() {

    private lateinit var binding: FragmentAchievementsBinding
    private lateinit var adapter: AchievementsAdapter

    private val viewModel: AchievementsViewModel by viewModels {
        AchievementsViewModelFactory((requireActivity().application as MyApp).database.singularAchievementDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAchievementsBinding.inflate(inflater, container, false)

        // Insert dummy data only if needed
        insertDummyAchievementsIfNeeded()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView with GridLayoutManager
        adapter = AchievementsAdapter()
        binding.recyclerViewAchievements.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.recyclerViewAchievements.adapter = adapter

        // Load achievements from the ViewModel
        loadAchievements()
    }

    private fun loadAchievements() {
        val achievements = viewModel.getAllAchievements()
        adapter.submitList(achievements)
    }

    private fun insertDummyAchievementsIfNeeded() {
        if (viewModel.getAllAchievements().isEmpty()) {
            val dummyAchievements = listOf(
                SingularAchievement(0, "First Steps", "Walked 1 kilometer"),
                SingularAchievement(0, "First 5K", "Walked 5 kilometers"),
                SingularAchievement(0, "10K Milestone", "Walked 10 kilometers"),
                SingularAchievement(0, "Half Marathon", "Walked 25 kilometers"),
                SingularAchievement(0, "Full Marathon", "Walked 50 kilometers"),
                SingularAchievement(0, "Century Walker", "Walked 100 kilometers"),
                SingularAchievement(0, "Super Walker", "Walked 250 kilometers"),
                SingularAchievement(0, "Explorer", "Walked 500 kilometers"),
                SingularAchievement(0, "Walking Pro", "Walked 1000 kilometers"),
                SingularAchievement(0, "First Walk", "Completed your first walk"),
                SingularAchievement(0, "First 5W", "Completed 5 walks"),
                SingularAchievement(0, "10W Milestone", "Completed 10 walks"),
                SingularAchievement(0, "25 Walks Challenge", "Completed 25 walks"),
                SingularAchievement(0, "Walking Enthusiast", "Completed 50 walks"),
                SingularAchievement(0, "Walking Fanatic", "Completed 100 walks"),
                SingularAchievement(0, "Walking Maniac", "Completed 250 walks"),
                SingularAchievement(0, "Walking Machine", "Completed 500 walks"),
                SingularAchievement(0, "Walking Legend", "Completed 1000 walks"),
            )
            dummyAchievements.forEach { viewModel.insertAchievement(it) }
        }
    }
}
