package com.example.trailsnapv2

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.trailsnapv2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

/**
 * Main activity of the TrailSnap application.
 * This activity manages the main navigation interface and the bottom navigation view.
 * It sets up the navigation controller to manage the transitions between different fragments
 * and ensures that the action bar and bottom navigation are correctly synchronized.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * Called when the activity is first created.
     * Initializes the view binding, sets the content view, and sets up the bottom navigation.
     * It also configures the navigation controller to manage fragment navigation.
     *
     * @param savedInstanceState A bundle that contains the activity's previous saved state, if any.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("languageCode", "en")
        val locale = Locale(languageCode!!)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        resources.updateConfiguration(
            config,
            resources.displayMetrics
        )

        val isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false)
        val mode = if (isDarkModeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_add_walk, R.id.navigation_history, R.id.navigation_achievements, R.id.navigation_profile
            )
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.registerFragment, R.id.loginFragment -> {
                    navView.visibility = View.GONE
                }
                else -> {
                    navView.visibility = View.VISIBLE
                }
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * Handles navigation when the "up" button in the action bar is pressed.
     * Ensures navigation transitions are performed correctly.
     *
     * @return true if navigation was successful; otherwise, false.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
