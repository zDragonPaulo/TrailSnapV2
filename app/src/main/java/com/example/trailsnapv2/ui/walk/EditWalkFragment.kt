package com.example.trailsnapv2.ui.walk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.R
import com.example.trailsnapv2.entities.Walk
import com.google.android.material.textfield.TextInputEditText

class EditWalkFragment : Fragment() {

    companion object {
        fun newInstance() = EditWalkFragment()
    }

    private val viewModel: EditWalkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_edit_walk, container, false)

        // Obter o DAO do banco de dados
        val database = AppDatabase.getInstance(requireContext())
        val walkDao = database.walkDao()

        // Configurar o ViewModel com o Factory
        val viewModelFactory = EditWalkViewModelFactory(walkDao)
        val viewModel: EditWalkViewModel by viewModels { viewModelFactory }

        // Restante do código para lidar com o clique do botão Save
        val buttonSaveWalk: Button = view.findViewById(R.id.saveButton)
        val walkNameEditText: EditText = view.findViewById(R.id.editNameText)
        val distanceTextView: TextView = view.findViewById(R.id.distanceText)
        val elapsedTimeTextView: TextView = view.findViewById(R.id.elapsedTimeText)

        // Obter os argumentos do Bundle
        val distance = arguments?.getFloat("distance") ?: 0f
        val elapsedTime = arguments?.getLong("elapsedTime") ?: 0L
        val walkName = arguments?.getString("walkName") ?: ""

        walkNameEditText.setText(walkName)
        distanceTextView.text = "Distância: %.2f km".format(distance)
        elapsedTimeTextView.text = "Tempo decorrido: %d segundos".format(elapsedTime)

        buttonSaveWalk.setOnClickListener {
            val walkNameInput = walkNameEditText.text.toString()
            if (walkNameInput.isEmpty()) {
                Toast.makeText(requireContext(), "O nome da caminhada não pode estar vazio.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val walk = Walk(
                user_id = 1L, // Substitua pelo ID do usuário válido
                walk_name = walkNameInput,
                distance = distance.toDouble(), // Substitua pelo valor da distância correta
                start_time = elapsedTime, // Substitua pelo valor correto
                end_time = System.currentTimeMillis()
            )

            viewModel.saveWalk(walk,
                onSuccess = {
                    Toast.makeText(requireContext(), "Caminhada salva com sucesso!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_editWalkFragment_to_walkDetailsFragment)
                },
                onError = { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            )
        }

        return view
    }


}

