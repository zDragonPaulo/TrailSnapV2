package com.example.trailsnapv2.ui.dashboard

import AchievementsAdapter
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
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.FragmentDashboardBinding
import com.example.trailsnapv2.MyApp
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var achievementsAdapter: AchievementsAdapter
    private lateinit var dashboardViewModel: DashboardViewModel
    private var userId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // Configura o RecyclerView
        achievementsAdapter = AchievementsAdapter()
        binding.recyclerViewAchievements.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = achievementsAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o botão de configurações
        val buttonSettings: ImageButton = binding.root.findViewById(R.id.button_settings)
        buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_appSettingsFragment)
        }

        // Recupera o ID do usuário
        userId = getCurrentUserId()
        if (userId == -1L) return

        // Inicializa o ViewModel
        val userDao = (requireActivity().application as MyApp).database.userDao()
        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        val userAchievementDao = (requireActivity().application as MyApp).database.userAchievementDao()
        val factory = DashboardViewModelFactory(userDao, walkDao, userAchievementDao)
        dashboardViewModel = ViewModelProvider(this, factory).get(DashboardViewModel::class.java)

        // Configura os observadores
        observeViewModel()

        // Inicializa os dados
        initializeDashboardData()
    }

    override fun onResume() {
        super.onResume()

        // Atualiza os dados sempre que o fragmento volta à tela
        refreshDashboardData()
    }

    private fun initializeDashboardData() {
        lifecycleScope.launch {
            // Carrega os dados iniciais
            dashboardViewModel.loadUserData(userId)
            dashboardViewModel.loadTotalWalks(userId)
            dashboardViewModel.loadTopAchievements(userId)
        }
    }

    private fun refreshDashboardData() {
        lifecycleScope.launch {
            // Atualiza os dados existentes
            dashboardViewModel.loadUserData(userId)
            dashboardViewModel.loadTotalWalks(userId)
            dashboardViewModel.loadTopAchievements(userId)
        }
    }

    private fun observeViewModel() {
        // Observa os dados do usuário
        dashboardViewModel.userData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                updateDashboardStats(user.total_distance, user.time_used.toInt())
                Log.d("DashboardFragment", "User data loaded: Distance=${user.total_distance}, Time=${user.time_used}")
            }
        }

        // Observa as conquistas
        dashboardViewModel.userAchievements.observe(viewLifecycleOwner) { userAchievements ->
            dashboardViewModel.singularAchievements.observe(viewLifecycleOwner) { singularAchievements ->
                achievementsAdapter.setUserAchievements(userAchievements, singularAchievements)
                achievementsAdapter.notifyDataSetChanged() // Força atualização do RecyclerView
                binding.textNoAchievements.visibility = if (userAchievements.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Observa o total de caminhadas
        dashboardViewModel.totalWalks.observe(viewLifecycleOwner) { totalWalks ->
            binding.textTotalWalks.text = getString(R.string.total_walks_text, totalWalks)
        }
    }

    private fun updateDashboardStats(totalDistance: Double, timeUsed: Int) {
        val formattedDistance = totalDistance / 1000 // Converte para quilômetros
        binding.textTotalDistance.text = getString(R.string.total_distance_text, formattedDistance)
        binding.textTotalTime.text = getString(R.string.total_time, formatTime(timeUsed))
    }

    private fun formatTime(time: Int): String {
        return when {
            time < 60 -> "$time sec"
            time < 3600 -> "${time / 60} min ${time % 60} sec"
            else -> "${time / 3600} h"
        }
    }

    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        return sharedPref.getLong("current_user_id", -1L).also { id ->
            if (id == -1L) {
                Toast.makeText(
                    requireContext(),
                    "Usuário não encontrado. Por favor, faça login novamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
