package com.example.trailsnapv2.ui.walk

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.R
import kotlinx.coroutines.launch

class WalkDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = WalkDetailsFragment()
    }

    private val viewModel: WalkDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_walk_details, container, false)

        // Views do layout
        val walkNameTextView: TextView = view.findViewById(R.id.walkNameDetail)
        val distanceTextView: TextView = view.findViewById(R.id.distanteUsedDetail)
        val timeTextView: TextView = view.findViewById(R.id.timeUsedDetail)
        val imageView: ImageView = view.findViewById(R.id.imageView2)
        val buttonEditWalk: Button = view.findViewById(R.id.editButton)

        // Obter o DAO do banco de dados
        val database = AppDatabase.getInstance(requireContext())
        val walkDao = database.walkDao()

        // Buscar os detalhes da caminhada usando o ID recebido
        val walkId = arguments?.getLong("walkId") ?: 0
        lifecycleScope.launch {
            val walk = walkDao.getWalkById(walkId)
            if (walk != null) {
                walkNameTextView.text = walk.walk_name

                val startTime = walk.start_time
                val endTime = walk.end_time
                val elapsedTime = (endTime - startTime) / 1000
                val distance = walk.distance
                distanceTextView.text = "Distância: %.2f km".format(distance)
                timeTextView.text = "Tempo: ${formatTime(elapsedTime)}"
                // Se você salvar a URI da imagem no banco de dados, pode usá-la aqui
                imageView.setImageURI(Uri.parse(walk.photo_path))
            } else {
                Toast.makeText(requireContext(), "Caminhada não encontrada.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack() // Voltar se não encontrar
            }
        }

        // Configurar botão de edição
        buttonEditWalk.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("walkId", walkId)
            }
            Log.d("ATENZZIONE PICKPOCKET DO WALK ID", "Walk ID: $walkId")
            findNavController().navigate(R.id.action_walkDetailsFragment_to_editWalkFragment, bundle)
        }

        return view
    }

    private fun formatTime(seconds: Long): String {
        return when {
            seconds >= 3600 -> {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                if (minutes > 0) "$hours h $minutes min" else "$hours h"
            }
            seconds >= 60 -> {
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                if (remainingSeconds > 0) "$minutes min $remainingSeconds s" else "$minutes min"
            }
            else -> "$seconds s"
        }
    }
}
