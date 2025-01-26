package com.example.trailsnapv2.ui.walk

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class WalkFragment : Fragment() {

    private lateinit var viewModel: WalkViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var startLocation: Pair<Double, Double>? = null
    private var endLocation: Pair<Double, Double>? = null
    private var startTime: Long = 0
    private lateinit var textTime: TextView
    private lateinit var textDistance: TextView
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var elapsedTime: Long = 0
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private var isTrackingAccelerometer = false
    private var distanceWalked = 0.0 // Used for accelerometer-based distance

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_walk, container, false)

        val walkNameTextView: TextView = view.findViewById(R.id.walkName)
        textDistance = view.findViewById(R.id.textDistance)
        textTime = view.findViewById(R.id.textTime) // Using the textTime existing TextView
        val buttonStartWalk: Button = view.findViewById(R.id.startWalk)
        val buttonFinishWalk: Button = view.findViewById(R.id.finishWalk)
        val walkName = arguments?.getString("walkName")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        walkNameTextView.text = walkName
        // Initialize ViewModel with Factory
        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        val factory = WalkViewModel.Factory(walkDao)
        viewModel = ViewModelProvider(this, factory).get(WalkViewModel::class.java)

        // Load the telemetry preference (either "gps" or "accelerometer")
        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val selectedTelemetry = sharedPreferences.getString("telemetry", "gps") // Default to "gps"

        buttonStartWalk.setOnClickListener {
            startTime = System.currentTimeMillis()
            getCurrentLocation { location ->
                startLocation = Pair(location.latitude, location.longitude)
                Log.d("WalkFragment", "Started walk at: $startLocation")
            }
            startChronometer()

            if (selectedTelemetry == "accelerometer") {
                startAccelerometerTracking()
            }
        }

        buttonFinishWalk.setOnClickListener {
            getCurrentLocation { location ->
                endLocation = Pair(location.latitude, location.longitude)
                Log.d("WalkFragment", "Finished walk at: $endLocation")

                // Calculate distance
                val distance = if (selectedTelemetry == "gps") {
                    calculateDistance(startLocation, endLocation)
                } else {
                    distanceWalked // Use accelerometer distance
                }

                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime

                // Update UI
                textDistance.text = "${"%.2f".format(distance)} km"
                textTime.text = formatTime(elapsedTime)
                Log.d("WalkFragment", "Distance: $distance km")
                stopChronometer()
                stopAccelerometerTracking()

                // Pass data to EditWalkFragment by Bundle
                val bundle = Bundle().apply {
                    putString("walkName", walkName)
                    putFloat("distance", distance.toFloat())
                    putLong("startTime", startTime)
                    putLong("endTime", endTime)
                }

                // Go to EditWalkFragment
                findNavController().navigate(R.id.action_walkFragment_to_editWalkFragment, bundle)
            }
        }

        return view
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(callback: (Location) -> Unit) {
        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val selectedTelemetry = sharedPreferences.getString("telemetry", "gps")

        if (selectedTelemetry == "gps") {
            // Use GPS for location tracking
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
    }

    private lateinit var accelerometerListener: SensorEventListener

    private fun startAccelerometerTracking() {
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        accelerometerListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val (x, y, z) = event.values
                    val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

                    if (acceleration > 2.5) { // Lowered threshold for step detection
                        distanceWalked += 0.6 // Each step assumed to be 0.8 meters
                        textDistance.text = "${"%.2f".format(distanceWalked / 1000)} km" // Update UI immediately
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        isTrackingAccelerometer = true
    }

    private fun stopAccelerometerTracking() {
        if (isTrackingAccelerometer) {
            sensorManager.unregisterListener(accelerometerListener)
            isTrackingAccelerometer = false
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

    private fun startChronometer() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                elapsedTime = currentTime - startTime
                textTime.text = formatTime(elapsedTime)
                handler?.postDelayed(this, 1000) // Update every 1 second
            }
        }
        handler?.post(runnable!!)
    }

    private fun stopChronometer() {
        handler?.removeCallbacks(runnable!!)
    }

    private fun formatTime(elapsedTime: Long): String {
        val hours = (elapsedTime / 3600000).toInt()
        val minutes = ((elapsedTime % 3600000) / 60000).toInt()
        val seconds = ((elapsedTime % 60000) / 1000).toInt()
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
