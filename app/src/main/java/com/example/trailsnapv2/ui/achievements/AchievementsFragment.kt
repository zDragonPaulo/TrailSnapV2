package com.example.trailsnapv2.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
        viewModel.clearAllAchievements()
        insertDummyAchievements()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        adapter = AchievementsAdapter()
        binding.recyclerViewAchievements.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAchievements.adapter = adapter

        // Carregar conquistas
        loadAchievements()

        // Testar inserção de conquistas fictícias
        insertDummyAchievements()
    }

    private fun loadAchievements() {
        val achievements = viewModel.getAllAchievements()
        adapter.submitList(achievements)
    }

    private fun insertDummyAchievements() {
        val dummyAchievements = listOf(
            SingularAchievement(0, "Primeira Conquista", "Descrição da primeira conquista"),
            SingularAchievement(0, "Segunda Conquista", "Descrição da segunda conquista"),
            SingularAchievement(0, "Terceira Conquista", "Descrição da terceira conquista")
        )
        dummyAchievements.forEach { viewModel.insertAchievement(it) }
    }

}
