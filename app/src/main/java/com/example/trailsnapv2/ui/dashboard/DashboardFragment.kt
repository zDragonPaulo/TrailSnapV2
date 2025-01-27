package com.example.trailsnapv2.ui.dashboard

import AchievementsAdapter
import DashboardViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.FragmentDashboardBinding
import com.example.trailsnapv2.entities.UserAchievement
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var achievementsAdapter: AchievementsAdapter
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializa o ViewModel com o UserDao
        val userDao = (requireActivity().application as MyApp).database.userDao()
        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        val factory = DashboardViewModelFactory(userDao, walkDao)
        dashboardViewModel = ViewModelProvider(this, factory).get(DashboardViewModel::class.java)

        // Configura o RecyclerView e o adapter
        achievementsAdapter = AchievementsAdapter()
        binding.recyclerViewAchievements.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = achievementsAdapter
        }

        // Obtém o ID do utilizador logado
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("current_user_id", -1L)

        if (userId != -1L) {
            dashboardViewModel.loadUserData(userId)
            dashboardViewModel.loadTotalWalks(userId)
        } else {
            Log.e("DashboardFragment", "User not Found")
        }

        // Observa os dados do utilizador
        dashboardViewModel.userData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                updateDashboardStats(user.total_distance, user.time_used.toInt())
                Log.d("O TEMPO DESTA MERDA", user.time_used.toString())
            }
        }

        // Observa as conquistas
        dashboardViewModel.userAchievements.observe(viewLifecycleOwner) { userAchievements ->
            dashboardViewModel.singularAchievements.observe(viewLifecycleOwner) { singularAchievements ->
                achievementsAdapter.setUserAchievements(userAchievements, singularAchievements)
            }
        }

        // Observa o total de caminhadas
        dashboardViewModel.totalWalks.observe(viewLifecycleOwner) { totalWalks ->
            Log.d("QUE PORRA?", totalWalks.toString())
            binding.textTotalWalks.text = getString(R.string.total_walks_text, totalWalks)
        }

        // Configura o botão de configurações
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

    private fun updateDashboardStats(totalDistance: Double, timeUsed: Int) {
        val formattedDistance = totalDistance / 1000 // Converte para quilômetros

        binding.textTotalDistance.text = getString(R.string.total_distance_text, formattedDistance)
        binding.textTotalTime.text = getString(R.string.total_time, formatTime(timeUsed))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun formatTime(time: Int): String {
        val minutes = time / 60
        return minutes.toString()
    }
}


