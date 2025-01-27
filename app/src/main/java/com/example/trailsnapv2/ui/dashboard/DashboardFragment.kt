package com.example.trailsnapv2.ui.dashboard

import AchievementsAdapter
import android.os.Bundle
import android.util.Log
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
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.entities.SingularAchievement

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

        // Inicializa o ViewModel com UserDao e WalkDao
        val userDao = (requireActivity().application as MyApp).database.userDao()
        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        val userAchievementDao = (requireActivity().application as MyApp).database.userAchievementDao()
        val factory = DashboardViewModelFactory(userDao, walkDao, userAchievementDao)
        dashboardViewModel = ViewModelProvider(this, factory).get(DashboardViewModel::class.java)

        // Configura o RecyclerView e o Adapter
        achievementsAdapter = AchievementsAdapter()
        binding.recyclerViewAchievements.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = achievementsAdapter
        }

        // Configura o botão de configurações
        val buttonSettings: ImageButton = binding.root.findViewById(R.id.button_settings)
        buttonSettings.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_dashboard_to_appSettingsFragment
            )
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        // Obtém o ID do utilizador logado
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("current_user_id", -1L)

        if (userId != -1L) {
            // Carrega os dados do utilizador
            dashboardViewModel.loadUserData(userId)
            dashboardViewModel.loadTotalWalks(userId)
            dashboardViewModel.loadTopAchievements(userId)

            // Observa os dados do utilizador
            dashboardViewModel.userData.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    updateDashboardStats(user.total_distance, user.time_used.toInt())
                    Log.d("YAMETE KUDASAI DISTANCE-KUN", "AMERICA YA , ${user.total_distance}")
                    Log.d("YAMETE KUDASAI TIME-SAN", "AMERICA YA , ${user.time_used}")
                }
            }

            // Observa as conquistas
            dashboardViewModel.userAchievements.observe(viewLifecycleOwner) { userAchievements ->
                dashboardViewModel.singularAchievements.observe(viewLifecycleOwner) { singularAchievements: List<SingularAchievement> ->
                    achievementsAdapter.setUserAchievements(userAchievements, singularAchievements)
                    binding.textNoAchievements.visibility = if (userAchievements.isEmpty()) View.VISIBLE else View.GONE
                }
            }

            // Observa o total de caminhadas
            dashboardViewModel.totalWalks.observe(viewLifecycleOwner) { totalWalks ->
                binding.textTotalWalks.text = getString(R.string.total_walks_text, totalWalks)
            }
        } else {
            Log.e("DashboardFragment", "User not found")
        }
    }

    private fun updateDashboardStats(totalDistance: Double, timeUsed: Int) {
        val formattedDistance = totalDistance / 1000 // Converte para quilômetros
        binding.textTotalDistance.text = getString(R.string.total_distance_text, formattedDistance)
        Log.d("YAMETE KUDASAI DISTANCIA", "AMERICA YA , ${formattedDistance.toString()}")
        binding.textTotalTime.text = getString(R.string.total_time, formatTime(timeUsed))
        Log.d("YAMETE KUDASAI TEMPO", "AMERICA YA , ${formatTime(timeUsed)}")
    }

    private fun formatTime(time: Int): String {
        if (time < 60) {
            return "$time sec"
        } else if ( time < 3600) {
            val minutes = (time / 60).toString()
            val seconds = (time % 60).toString()
            return "$minutes min $seconds sec"
        }
        val hour = (time / 3600).toString()

        return "$hour h"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
