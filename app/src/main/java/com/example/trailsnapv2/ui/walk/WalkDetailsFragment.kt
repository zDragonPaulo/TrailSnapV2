package com.example.trailsnapv2.ui.walk

import android.net.Uri
import android.os.Bundle
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

/**
 * Fragment that displays the details of a specific walk. It fetches walk details from the database
 * using the walk ID passed in as an argument and presents information such as walk name, distance,
 * duration, and an associated image. It also provides the option to edit the walk details.
 */
class WalkDetailsFragment : Fragment() {

    companion object {
    }

    /**
     * Inflates the view and initializes UI elements. Retrieves the walk details from the database
     * and displays them. If the walk is found, its name, distance, time, and image are shown.
     * A button is also provided to navigate to the EditWalkFragment for editing the walk.
     *
     * @param inflater The LayoutInflater used to inflate the view.
     * @param container The parent view that the fragment’s UI will be attached to.
     * @param savedInstanceState A Bundle containing the fragment's previously saved state, if any.
     * @return The root view for the fragment’s layout.
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
                distanceTextView.text = "Distância: %.2f km".format(distance)
                timeTextView.text = "Tempo: ${formatTime(elapsedTime)}"
                imageView.setImageURI(Uri.parse(walk.photo_path))
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.walk_not_found), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        buttonEditWalk.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("walkId", walkId)
            }
            findNavController().navigate(R.id.action_walkDetailsFragment_to_editWalkFragment, bundle)
        }

        return view
    }

    /**
     * Formats a given time in seconds into a human-readable format (e.g., hours, minutes, seconds).
     *
     * @param seconds The time in seconds to format.
     * @return A string representing the formatted time.
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
