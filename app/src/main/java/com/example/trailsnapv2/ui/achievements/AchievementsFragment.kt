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
            (requireActivity().application as MyApp).database.userAchievementDao(),
            (requireActivity().application as MyApp).database.walkDao()
        )
    }

    private var userId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the current user ID from SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        userId = sharedPref.getLong("current_user_id", -1L)

        if (userId == -1L) {
            // Handle case where user ID is not found (e.g., show an error or redirect to login)
            // For this example, we'll just log it and return
            println("User ID not found in SharedPreferences")
            return
        }

        // Initialize RecyclerView and Adapter
        adapter = AchievementsAdapter()
        binding.recyclerViewAchievements.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewAchievements.adapter = adapter

        // Insert default achievements if needed
        viewModel.insertDefaultAchievementsIfNeeded()

        // Initialize user achievements (this is done once to prevent duplicate entries)
        viewModel.initializeUserAchievements(userId = userId)

        // Observe and display user achievements
        viewModel.getUserAchievements(userId = userId).observe(viewLifecycleOwner) { userAchievements ->
            viewModel.getAllSingularAchievements().observe(viewLifecycleOwner) { singularAchievements ->
                adapter.setUserAchievements(userAchievements, singularAchievements)
            }

            viewModel.updateAchievements(userId = userId)
        }
    }
}
