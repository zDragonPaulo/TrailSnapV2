package com.example.trailsnapv2.ui.walk

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.R
import com.example.trailsnapv2.entities.Walk

class EditWalkFragment : Fragment() {

    companion object {
        fun newInstance() = EditWalkFragment()
    }
    private var selectedPhotoUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedPhotoUri = result.data?.data

            // Atualize o ImageView com segurança
            view?.findViewById<ImageView>(R.id.imageView)?.let { imageView ->
                imageView.setImageURI(selectedPhotoUri)
            } ?: Log.e("EditWalkFragment", "ImageView not found!")
        }
    }

    private val viewModel: EditWalkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_edit_walk, container, false)

        // Obter o DAO do banco de dados
        val database = AppDatabase.getInstance(requireContext())
        Log.d("EditWalkFragment", "Database instance: $database")
        val walkDao = database.walkDao()

        // Configurar o ViewModel com o Factory
        val viewModelFactory = EditWalkViewModelFactory(walkDao)
        val viewModel: EditWalkViewModel by viewModels { viewModelFactory }

        // Restante do código para lidar com o clique do botão Save
        val buttonSaveWalk: Button = view.findViewById(R.id.saveButton)
        val walkNameEditText: EditText = view.findViewById(R.id.editNameWalk)
        val distanceTextView: TextView = view.findViewById(R.id.distanceText)
        val elapsedTimeTextView: TextView = view.findViewById(R.id.elapsedTimeText)

        // Obter os argumentos do Bundle
        val distance = arguments?.getFloat("distance") ?: 0f
        val startTime = arguments?.getLong("startTime") ?: 0L
        val endTime = arguments?.getLong("endTime") ?: 0L
        val walkName = arguments?.getString("walkName") ?: ""
        val elapsedTime = (endTime - startTime) / 1000
        walkNameEditText.setText(walkName)
        distanceTextView.text = "Distância: %.2f km".format(distance)
        elapsedTimeTextView.text = "Tempo decorrido: ${formatTime(elapsedTime)}"
        val buttonSelectPhoto: Button = view.findViewById(R.id.addPhotoButton)
        val buttonSaveEditedWalk: Button = view.findViewById(R.id.saveButton)

        // Configurar o botão de seleção de imagem
        buttonSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }
        buttonSaveEditedWalk.setOnClickListener {
            val walkNameInput = walkNameEditText.text.toString()
            if (walkNameInput.isEmpty()) {
                Toast.makeText(requireContext(), "O nome da caminhada não pode estar vazio.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            Log.d("EditWalkFragment", "Saving walk with name: $startTime")
            val walk = Walk(
                user_id = 1L, // Substitua pelo ID do usuário válido
                walk_name = walkNameInput,
                distance = distance.toDouble(),
                start_time = startTime/1000,
                end_time = endTime/1000,
                photo_path = selectedPhotoUri?.toString()
            )

            viewModel.saveWalk(walk,
                onSuccess = { walkId ->  // Supondo que o método `saveWalk` retorna o ID gerado
                    Toast.makeText(requireContext(), "Caminhada salva com sucesso!", Toast.LENGTH_LONG).show()
                    val bundle = Bundle().apply {
                        putLong("walkId", walkId) // Enviar o ID para o fragmento de detalhes
                    }
                    findNavController().navigate(R.id.action_editWalkFragment_to_walkDetailsFragment, bundle)
                },
                onError = { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            )

        }

        return view
    }

    fun formatTime(seconds: Long): String {
        return when {
            seconds >= 3600 -> { // Mais que 1 hora
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                if (minutes > 0) "$hours h $minutes min" else "$hours h"
            }
            seconds >= 60 -> { // Mais que 1 minuto
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                if (remainingSeconds > 0) "$minutes min $remainingSeconds s" else "$minutes min"
            }
            else -> { // Menos de 1 minuto
                "$seconds s"
            }
        }
    }


}

