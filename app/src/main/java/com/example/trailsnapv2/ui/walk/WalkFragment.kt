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

/**
 * WalkFragment is responsible for managing the walk tracking process.
 * It allows the user to start and finish a walk using either GPS or accelerometer data.
 * The fragment calculates the distance walked and time elapsed during the walk.
 * After completing the walk, it navigates to another fragment to edit the walk details.
 *
 * @see WalkViewModel
 */
class WalkFragment : Fragment() {

    private lateinit var viewModel: WalkViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var startLocation: Pair<Double, Double>? = null
    private var endLocation: Pair<Double, Double>? = null
    private var intermediateLocations = mutableListOf<Pair<Double, Double>>()
    private var totalDistance: Double = 0.0
    private var startTime: Long = 0
    private lateinit var textTime: TextView
    private lateinit var textDistance: TextView
    private var handler: Handler? = null
    private var locationHandler: Handler? = null
    private var runnable: Runnable? = null
    private var locationRunnable: Runnable? = null
    private var elapsedTime: Long = 0
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private var isTrackingAccelerometer = false
    private var distanceWalked = 0.0 // Distance in meters
    private lateinit var accelerometerListener: SensorEventListener

    /**
     * Inflates the layout and sets up the necessary views and listeners.
     *
     * @param inflater LayoutInflater to inflate the fragment's layout.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState The previously saved state of the fragment.
     * @return The root view for this fragment's layout.
     */
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_walk, container, false)

        val walkNameTextView: TextView = view.findViewById(R.id.walkName)
        textDistance = view.findViewById(R.id.textDistance)
        textTime = view.findViewById(R.id.textTime)
        val buttonStartWalk: Button = view.findViewById(R.id.startWalk)
        val buttonFinishWalk: Button = view.findViewById(R.id.finishWalk)
        val walkName = arguments?.getString("walkName")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        walkNameTextView.text = walkName

        (requireActivity().application as MyApp).database.walkDao()
        val factory = WalkViewModel.Factory()
        viewModel = ViewModelProvider(this, factory).get(WalkViewModel::class.java)

        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val selectedTelemetry = sharedPreferences.getString("telemetry", "gps")

        buttonStartWalk.setOnClickListener {
            startTime = System.currentTimeMillis()
            getCurrentLocation { location ->
                startLocation = Pair(location.latitude, location.longitude)
                intermediateLocations.add(startLocation!!)
                Log.d("WalkFragment", "Started walk at: $startLocation")
            }
            startChronometer()

            if (selectedTelemetry == "gps") {
                startLocationUpdates()
            } else if (selectedTelemetry == "accelerometer") {
                startAccelerometerTracking()
            }
        }

        buttonFinishWalk.setOnClickListener {

            stopChronometer()

            if (selectedTelemetry == "gps") {
                stopLocationUpdates()
            } else if (selectedTelemetry == "accelerometer") {
                stopAccelerometerTracking()
            }

            getCurrentLocation { location ->
                endLocation = Pair(location.latitude, location.longitude)
                Log.d("WalkFragment", "Finished walk at: $endLocation")

                val distance = if (selectedTelemetry == "gps") {
                    val lastDistance = calculateDistance(intermediateLocations.last(), endLocation)
                    totalDistance += lastDistance
                    totalDistance
                } else {
                    distanceWalked
                }

                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime

                textDistance.text = "${"%.0f".format(distance)} m"
                textTime.text = formatTime(elapsedTime)


                val bundle = Bundle().apply {
                    putLong("walkId", -1L)
                    putString("walkName", walkName)
                    putFloat("distance", distance.toFloat())
                    putLong("startTime", startTime)
                    putLong("endTime", endTime)
                }

                findNavController().navigate(R.id.action_walkFragment_to_editWalkFragment, bundle)
            }
        }

        return view
    }

    /**
     * Starts periodic updates of the user's location using the FusedLocationProviderClient.
     * The distance walked is calculated based on the change in location.
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationHandler = Handler(Looper.getMainLooper())
        locationRunnable = object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                getCurrentLocation { location ->
                    val currentLocation = Pair(location.latitude, location.longitude)
                    if (intermediateLocations.isNotEmpty()) {
                        val lastLocation = intermediateLocations.last()
                        val incrementalDistance = calculateDistance(lastLocation, currentLocation)
                        totalDistance += incrementalDistance
                    }
                    intermediateLocations.add(currentLocation)
                    textDistance.text = "${"%.0f".format(totalDistance)} m"
                    Log.d("WalkFragment", "Intermediate location: $currentLocation, Total distance: $totalDistance m")
                }
                locationHandler?.postDelayed(this, 1000)
            }
        }
        locationHandler?.post(locationRunnable!!)
    }

    /**
     * Stops the location updates by removing the runnable from the handler.
     */
    private fun stopLocationUpdates() {
        locationHandler?.removeCallbacks(locationRunnable!!)
    }

    /**
     * Starts tracking the user's walk using the device's accelerometer.
     * The distance walked is estimated based on detected accelerometer events.
     */
    private fun startAccelerometerTracking() {
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        accelerometerListener = object : SensorEventListener {
            @SuppressLint("SetTextI18n")
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val (x, y, z) = event.values
                    val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

                    if (acceleration > 2.5) {
                        distanceWalked += 0.6
                        textDistance.text = "${"%.0f".format(distanceWalked)} m"
                        Log.d("WalkFragment", "Distance walked: $distanceWalked m")
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        isTrackingAccelerometer = true
    }

    /**
     * Stops tracking the accelerometer by unregistering the accelerometer listener.
     */
    private fun stopAccelerometerTracking() {
        if (isTrackingAccelerometer) {
            sensorManager.unregisterListener(accelerometerListener)
            isTrackingAccelerometer = false
        }
    }

    /**
     * Gets the current location of the user using the FusedLocationProviderClient.
     *
     * @param callback Callback function that will be invoked with the current location.
     */
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

    /**
     * Calculates the distance between two geographical coordinates using the Haversine formula.
     *
     * @param start The starting geographical coordinates.
     * @param end The ending geographical coordinates.
     * @return The calculated distance in meters.
     */
    private fun calculateDistance(
        start: Pair<Double, Double>?,
        end: Pair<Double, Double>?
    ): Double {
        if (start == null || end == null) return 0.0

        val (lat1, lon1) = start
        val (lat2, lon2) = end

        val earthRadius = 6371000
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    /**
     * Starts the chronometer to track the elapsed time of the walk.
     */
    private fun startChronometer() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                elapsedTime = currentTime - startTime
                textTime.text = formatTime(elapsedTime)
                handler?.postDelayed(this, 1000)
            }
        }
        handler?.post(runnable!!)
    }

    /**
     * Stops the chronometer.
     */
    private fun stopChronometer() {
        handler?.removeCallbacks(runnable!!)
    }

    /**
     * Formats the elapsed time in milliseconds into a readable string format (hh:mm:ss).
     *
     * @param elapsedTime The elapsed time in milliseconds.
     * @return A formatted time string.
     */
    @SuppressLint("DefaultLocale")
    private fun formatTime(elapsedTime: Long): String {
        val hours = (elapsedTime / 3600000).toInt()
        val minutes = ((elapsedTime % 3600000) / 60000).toInt()
        val seconds = ((elapsedTime % 60000) / 1000).toInt()
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
