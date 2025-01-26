package com.example.trailsnapv2.ui.walk

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.*

class WalkFragment : Fragment() {

    private lateinit var viewModel: WalkViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var startLocation: Pair<Double, Double>? = null
    private var endLocation: Pair<Double, Double>? = null
    private var startTime: Long = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_walk, container, false)

        val walkNameTextView: TextView = view.findViewById(R.id.walkName)
        val distanceTextView: TextView = view.findViewById(R.id.textDistance)
        val timeTextView: TextView = view.findViewById(R.id.textTime)
        val buttonStartWalk: Button = view.findViewById(R.id.startWalk)
        val buttonFinishWalk: Button = view.findViewById(R.id.finishWalk)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize ViewModel with Factory
        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        val factory = WalkViewModel.Factory(walkDao)
        viewModel = ViewModelProvider(this, factory).get(WalkViewModel::class.java)

        buttonStartWalk.setOnClickListener {
            startTime = System.currentTimeMillis()
            getCurrentLocation { location ->
                startLocation = Pair(location.latitude, location.longitude)
                Log.d("WalkFragment", "Started walk at: $startLocation")
            }
        }

        buttonFinishWalk.setOnClickListener {
            getCurrentLocation { location ->
                endLocation = Pair(location.latitude, location.longitude)
                Log.d("WalkFragment", "Finished walk at: $endLocation")

                // Calculate distance
                val distance = calculateDistance(startLocation, endLocation)
                val elapsedTime = System.currentTimeMillis() - startTime

                // Update UI
                distanceTextView.text = "Distance: ${"%.2f".format(distance)} km"
                timeTextView.text = "Time: ${elapsedTime / 1000} seconds"

                // Save data into database
                saveWalkData("My Walk", distance, elapsedTime)

                // Pass data to EditWalkFragment by Bundle
                val bundle = Bundle().apply {
                    putString("walkName", "My Walk")
                    putFloat("distance", distance.toFloat())
                    putLong("elapsedTime", elapsedTime)
                }

                // Go to EditWalkFragment
                findNavController().navigate(R.id.action_walkFragment_to_editWalkFragment, bundle)
            }

        }

        return view
    }

    private fun saveWalkData(walkName: String, distance: Double, elapsedTime: Long) {
        // Assuming user_id = 1 for testing. You should get it dynamically from your app
        val userId = 1L

        // Use ViewModel to save the walk data
        viewModel.createWalk(
            walkName, userId, startTime, System.currentTimeMillis(), distance
        )
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(callback: (Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                callback(it)
            }
        }
    }

    private fun calculateDistance(
        start: Pair<Double, Double>?,
        end: Pair<Double, Double>?
    ): Double {
        if (start == null || end == null) return 0.0

        val (lat1, lon1) = start
        val (lat2, lon2) = end

        val earthRadius = 6371 // Earth radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}