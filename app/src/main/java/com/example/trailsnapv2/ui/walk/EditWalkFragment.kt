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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.R
import com.example.trailsnapv2.entities.Walk
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditWalkFragment : Fragment() {
    private var userId: Long = -1L
    private var selectedPhotoUri: Uri? = null
    private lateinit var walkImageView: ImageView
    private var walkId: Long = -1L

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedPhotoUri = result.data?.data
            walkImageView.setImageURI(selectedPhotoUri)
        }
    }

    private val viewModel: EditWalkViewModel by lazy {
        ViewModelProvider(
            this,
            EditWalkViewModelFactory(
                AppDatabase.getInstance(requireContext()).walkDao(),
                AppDatabase.getInstance(requireContext()).userDao()
            )
        ).get(EditWalkViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_walk, container, false)

        walkId = arguments?.getLong("walkId", -1) ?: -1L
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        userId = sharedPref.getLong("current_user_id", -1L)

        walkImageView = view.findViewById(R.id.imageView)

        if (walkId != -1L) {
            Log.d("EditWalkFragment", "Editing walk with ID: $walkId")
            viewModel.getWalkById(
                walkId,
                onSuccess = { walk -> setupUIForEditing(view, walk) },
                onError = { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
            )
        } else {
            setupUIForNewWalk(view)
        }

        view.findViewById<Button>(R.id.addPhotoButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }

        return view
    }

    private fun setupUIForEditing(view: View, walk: Walk) {
        view.findViewById<EditText>(R.id.editNameWalk).setText(walk.walk_name)
        val elapsedTime = (walk.end_time - walk.start_time) / 1000
        val distance = walk.distance
        view.findViewById<TextView>(R.id.distanceText).text = getString(R.string.dist_ncia_2f_km, distance)
        view.findViewById<TextView>(R.id.elapsedTimeText).text = getString(R.string.total_time, formatTime(elapsedTime))

        // Ensure walk photo is handled properly
        if (!walk.photo_path.isNullOrEmpty()) {
            val file = File(walk.photo_path)
            if (file.exists()) {
                selectedPhotoUri = Uri.fromFile(file) // Convert file path to URI
                walkImageView.setImageURI(selectedPhotoUri)
            } else {
                walkImageView.setImageResource(R.drawable.ic_profile)
            }
        } else {
            walkImageView.setImageResource(R.drawable.ic_profile)
        }

        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveWalk(walk)
        }
    }

    private fun setupUIForNewWalk(view: View) {
        val distance = arguments?.getFloat("distance") ?: 0f
        val startTime = arguments?.getLong("startTime") ?: 0L
        val endTime = arguments?.getLong("endTime") ?: 0L
        val walkName = arguments?.getString("walkName") ?: ""
        val elapsedTime = (endTime - startTime) / 1000

        view.findViewById<EditText>(R.id.editNameWalk).setText(walkName)
        view.findViewById<TextView>(R.id.distanceText).text = getString(R.string.dist_ncia_2f_km, distance)
        view.findViewById<TextView>(R.id.elapsedTimeText).text = getString(R.string.total_time, formatTime(elapsedTime))

        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveNewWalk(view)
        }
    }

    private fun saveWalk(walk: Walk) {
        val walkNameInput = view?.findViewById<EditText>(R.id.editNameWalk)?.text.toString()
        if (walkNameInput.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.walk_name_empty), Toast.LENGTH_LONG).show()
            return
        }

        val imagePath = if (selectedPhotoUri != null) {
            saveImageToInternalStorage(selectedPhotoUri!!)
        } else {
            walk.photo_path // Use existing image path
        }

        val updatedWalk = walk.copy(walk_name = walkNameInput, photo_path = imagePath)
        viewModel.updateWalk(updatedWalk,
            onSuccess = {
                Toast.makeText(requireContext(), getString(R.string.walk_update), Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                Log.e("EditWalkFragment", errorMessage)
            }
        )
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

    private fun saveNewWalk(view: View) {
        val walkNameInput = view.findViewById<EditText>(R.id.editNameWalk).text.toString()
        if (walkNameInput.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.walk_name_empty), Toast.LENGTH_LONG).show()
            return
        }

        val imagePath = selectedPhotoUri?.let { saveImageToInternalStorage(it) }

        val walk = Walk(
            user_id = userId,
            walk_name = walkNameInput,
            distance = arguments?.getFloat("distance")?.toDouble() ?: 0.0,
            start_time = arguments?.getLong("startTime") ?: 0L,
            end_time = arguments?.getLong("endTime") ?: 0L,
            photo_path = imagePath
        )

        viewModel.saveWalk(walk,
            onSuccess = { savedWalkId ->
                Toast.makeText(requireContext(), getString(R.string.walk_saved), Toast.LENGTH_LONG).show()
                val bundle = Bundle().apply { putLong("walkId", savedWalkId) }
                findNavController().navigate(R.id.action_editWalkFragment_to_walkDetailsFragment, bundle)
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                Log.e("EditWalkFragment", errorMessage)
            }
        )
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        val context = requireContext()
        val contentResolver = context.contentResolver
        val fileName = "walk_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Log.d("EditWalkFragment", "Image saved successfully at ${file.absolutePath}")
            file.absolutePath
        } catch (e: IOException) {
            Log.e("EditWalkFragment", "Error saving image", e)
            null
        }
    }
}