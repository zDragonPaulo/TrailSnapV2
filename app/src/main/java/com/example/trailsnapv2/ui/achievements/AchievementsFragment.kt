package com.example.trailsnapv2.ui.achievements

import AchievementsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.databinding.FragmentAchievementsBinding

class AchievementsFragment : Fragment() {

    private lateinit var binding: FragmentAchievementsBinding
    private lateinit var adapter: AchievementsAdapter

    private val viewModel: AchievementsViewModel by viewModels {
        AchievementsViewModelFactory(
            (requireActivity().application as MyApp).database.singularAchievementDao(),
            (requireActivity().application as MyApp).database.userAchievementDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and Adapter
        adapter = AchievementsAdapter()
        binding.recyclerViewAchievements.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewAchievements.adapter = adapter

        // Insert default achievements if needed
        viewModel.insertDefaultAchievementsIfNeeded()

        // Initialize user achievements (this is done once to prevent duplicate entries)
        viewModel.initializeUserAchievements(userId = 1L)  // Replace with actual user ID

        // Observe and display user achievements
        viewModel.getUserAchievements(userId = 1L).observe(viewLifecycleOwner) { userAchievements ->
            viewModel.getAllSingularAchievements().observe(viewLifecycleOwner) { singularAchievements ->
                adapter.setUserAchievements(userAchievements, singularAchievements)
            }
        }
    }
}
