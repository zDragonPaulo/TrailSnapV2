package com.example.trailsnapv2.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.R

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val walkDao = database.walkDao()
        val userId = getCurrentUserId()  // Obtém o ID do usuário logado
        HistoryViewModelFactory(walkDao, userId)  // Passa o userId para o ViewModel
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WalkHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_history, container, false)

        // Configurar RecyclerView
        recyclerView = binding.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = WalkHistoryAdapter(requireContext()){ walkId ->
            navigateToDetails(walkId) // Navega para a página de detalhes ao clicar em um item
        }
        recyclerView.adapter = adapter

        // Observar os dados do histórico
        viewModel.historyItems.observe(viewLifecycleOwner, Observer { items ->
            if (items.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.nenhuma_caminhada_localizada),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                adapter.submitList(items)  // Atualiza a RecyclerView com os dados
            }
        })

        return binding
    }
    private fun navigateToDetails(walkId: Long) {
        val bundle = Bundle().apply {
            putLong("walkId", walkId) // Passa o ID da caminhada no bundle
        }
        // Navega usando NavController (se estiver usando Navigation Component)
        findNavController().navigate(R.id.action_navigation_history_to_walkDetailsFragment, bundle)
    }
    override fun onResume() {
        super.onResume()
        // Forçar atualização do histórico
        viewModel.loadHistoryItems()
    }

    private fun getCurrentUserId(): Long {
        // Recupera o ID do usuário logado do SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        return sharedPref.getLong("current_user_id", -1L).also { userId ->
            if (userId == -1L) {
                // Exibe uma mensagem de erro e redireciona para o login se necessário
                Toast.makeText(
                    requireContext(),
                    getString(R.string.user_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
