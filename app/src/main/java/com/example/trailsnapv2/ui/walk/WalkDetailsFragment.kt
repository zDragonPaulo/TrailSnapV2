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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.R
import kotlinx.coroutines.launch
import java.io.File

/**
 * A fragment that displays detailed information about a specific walk.
 *
 * This fragment retrieves walk details from the database based on the provided walk ID and displays
 * the name, distance, elapsed time, and an associated photo (if available). Users can navigate to
 * edit the walk details using the provided button.
 */
class WalkDetailsFragment : Fragment() {

    companion object {
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_walk_details, container, false)

        val walkNameTextView: TextView = view.findViewById(R.id.walkNameDetail)
        val distanceTextView: TextView = view.findViewById(R.id.distanteUsedDetail)
        val timeTextView: TextView = view.findViewById(R.id.timeUsedDetail)
        val imageView: ImageView = view.findViewById(R.id.imageView2)
        val buttonEditWalk: Button = view.findViewById(R.id.editButton)

        val database = AppDatabase.getInstance(requireContext())
        val walkDao = database.walkDao()

        val walkId = arguments?.getLong("walkId") ?: 0
        lifecycleScope.launch {
            val walk = walkDao.getWalkById(walkId)
            if (walk != null) {
                walkNameTextView.text = walk.walk_name

                val startTime = walk.start_time
                val endTime = walk.end_time
                val elapsedTime = (endTime - startTime) / 1000
                val distance = walk.distance
                distanceTextView.text = getString(R.string.dist_ncia_2f_km, distance)
                timeTextView.text = getString(R.string.total_time, formatTime(elapsedTime))

                if (!walk.photo_path.isNullOrEmpty()) {
                    val file = File(walk.photo_path)
                    if (file.exists()) {
                        imageView.setImageURI(Uri.fromFile(file))
                    } else {
                        Log.w("WalkDetailsFragment", "Image file not found: ${walk.photo_path}")
                        imageView.setImageResource(R.drawable.ic_profile)
                    }
                } else {
                    imageView.setImageResource(R.drawable.ic_profile)
                }

            } else {
                Toast.makeText(requireContext(), getString(R.string.walk_saved), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        buttonEditWalk.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("walkId", walkId)
            }
            Log.d("WalkDetailsFragment", "Navigating to edit with Walk ID: $walkId")
            findNavController().navigate(R.id.action_walkDetailsFragment_to_editWalkFragment, bundle)
        }

        return view
    }

    /**
     * Formats a time duration in seconds into a human-readable string.
     *
     * @param seconds The total duration in seconds.
     * @return A formatted string, such as "1 h 30 min" or "45 s".
     */
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