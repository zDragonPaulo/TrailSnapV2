package com.example.trailsnapv2.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.R
import com.example.trailsnapv2.AppDatabase

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val walkDao = database.walkDao()
        val userId = getCurrentUserId()  // Obtém o ID do usuário logado
        HistoryViewModelFactory(walkDao, userId)  // Passa o userId para o ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_history, container, false)
        val recyclerView: RecyclerView = binding.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = WalkHistoryAdapter()
        recyclerView.adapter = adapter

        // Observar os dados do histórico
        viewModel.historyItems.observe(viewLifecycleOwner, Observer { items ->
            if (items.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No walks found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.submitList(items)  // Atualiza a RecyclerView com os dados
            }
        })

        return binding
    }

    private fun getCurrentUserId(): Long {
        // Recupera o ID do usuário logado do SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        return sharedPref.getLong("current_user_id", -1L).also { userId ->
            if (userId == -1L) {
                // Exibe uma mensagem de erro e redireciona para o login se necessário
                Toast.makeText(requireContext(), "Usuário não autenticado. Faça login novamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}