package com.example.trailsnapv2.ui.dashboard

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.trailsnapv2.MainActivity
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.FragmentAppSettingsBinding
import java.util.Locale

/**
 * Fragment that manages the application settings related to language preferences.
 * This fragment allows the user to select a language from a spinner and updates
 * the app's locale accordingly to provide a localized user experience.
 */
class AppSettingsFragment : Fragment() {

    private var _binding: FragmentAppSettingsBinding? = null
    private val binding get() = _binding!!

    private var selectedLanguageCode: String = "en" // Default language
    private var isDarkModeEnabled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", AppCompatActivity.MODE_PRIVATE)

        // Load saved preferences
        selectedLanguageCode = sharedPreferences.getString("languageCode", "en") ?: "en"
        isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false)

        // Set initial states for UI components
        binding.languageSpinner.setSelection(
            if (selectedLanguageCode == "pt") 0 else 1
        )
        binding.themeSwitch.isChecked = isDarkModeEnabled

        // Set up button click listener
        binding.applySettingsButton.setOnClickListener {
            // Get selected language from spinner
            selectedLanguageCode = when (binding.languageSpinner.selectedItemPosition) {
                0 -> "pt" // Português
                1 -> "en" // English
                else -> "en"
            }

            // Get dark mode status from switch
            isDarkModeEnabled = binding.themeSwitch.isChecked

            // Save and apply the settings
            applySettings()
        }
    }

    /**
     * Saves and applies user settings for language and dark mode.
     */
    private fun applySettings() {
        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save language preference
        editor.putString("languageCode", selectedLanguageCode)

        // Save dark mode preference
        editor.putBoolean("darkMode", isDarkModeEnabled)

        editor.apply()

        // Apply dark mode
        val mode = if (isDarkModeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)

        // Apply language changes
        updateLocale(selectedLanguageCode)

        // Restart the activity to apply changes
        activity?.recreate()
    }

    /**
     * Updates the app's locale to the selected language.
     *
     * @param languageCode The ISO language code to set the app's locale.
     */
    private fun updateLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        resources.updateConfiguration(
            config,
            resources.displayMetrics
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}