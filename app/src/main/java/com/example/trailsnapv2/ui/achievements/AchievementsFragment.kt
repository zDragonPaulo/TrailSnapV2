package com.example.trailsnapv2.ui.achievements

import AchievementsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.databinding.FragmentAchievementsBinding
import kotlinx.coroutines.launch

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

        // Configurar RecyclerView
        adapter = AchievementsAdapter()
        binding.recyclerViewAchievements.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewAchievements.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar o ID do usuário
        userId = getCurrentUserId()
        if (userId == -1L) return

        // Inicializar conquistas (apenas uma vez)
        initializeAchievements()
    }

    override fun onResume() {
        super.onResume()

        // Atualizar conquistas ao voltar para o fragmento
        refreshAchievements()
    }

    private fun initializeAchievements() {
        lifecycleScope.launch {
            // Inserir conquistas padrão, se necessário
            viewModel.insertDefaultAchievementsIfNeeded()

            // Inicializar conquistas do usuário
            viewModel.initializeUserAchievements(userId)

            // Configurar observadores de conquistas
            observeAchievements()
        }
    }

    private fun observeAchievements() {
        // Observar conquistas do usuário e conquistas gerais
        viewModel.getUserAchievements(userId).observe(viewLifecycleOwner) { userAchievements ->
            viewModel.getAllSingularAchievements().observe(viewLifecycleOwner) { singularAchievements ->
                // Atualizar dados na RecyclerView
                adapter.setUserAchievements(userAchievements, singularAchievements)
            }
        }
    }

    private fun refreshAchievements() {
        lifecycleScope.launch {
            // Atualizar o progresso das conquistas
            viewModel.updateAchievements(userId)

            // Forçar recarregamento dos dados
            observeAchievements()
        }
    }

    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        return sharedPref.getLong("current_user_id", -1L).also { userId ->
            if (userId == -1L) {
                Toast.makeText(
                    requireContext(),
                    "Usuário não encontrado. Por favor, faça login novamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
