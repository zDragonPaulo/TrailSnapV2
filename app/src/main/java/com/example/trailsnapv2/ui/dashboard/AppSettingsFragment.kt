package com.example.trailsnapv2.ui.dashboard

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.FragmentAppSettingsBinding
import java.util.Locale

/**
 * A fragment that allows the user to configure app settings, including:
 * - Language selection
 * - Dark mode toggle
 * - Telemetry type selection (GPS or accelerometer)
 *
 * The fragment loads the current settings from SharedPreferences and allows the user
 * to apply changes. Upon applying the settings, the app restarts to reflect the new settings.
 */
class AppSettingsFragment : Fragment() {

    private var _binding: FragmentAppSettingsBinding? = null
    private val binding get() = _binding!!

    private var selectedLanguageCode: String = "en" // Default language
    private var isDarkModeEnabled: Boolean = false
    private var selectedTelemetry: String = "gps"

    /**
     * Inflates the fragment layout and prepares the UI components.
     * @param inflater The LayoutInflater used to inflate the view.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState A bundle containing any saved instance state of the fragment.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Initializes the settings UI based on the saved preferences.
     * Sets up listeners for user interaction and handles the application of new settings.
     * @param view The root view of the fragment.
     * @param savedInstanceState A bundle containing any saved instance state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", AppCompatActivity.MODE_PRIVATE)

        selectedLanguageCode = sharedPreferences.getString("languageCode", "en") ?: "en"
        isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false)
        selectedTelemetry = sharedPreferences.getString("telemetry", "gps") ?: "gps"

        val mode = if (isDarkModeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)

        binding.languageSpinner.setSelection(
            if (selectedLanguageCode == "pt") 0 else 1
        )
        binding.themeSwitch.isChecked = isDarkModeEnabled

        when (selectedTelemetry) {
            "accelerometer" -> binding.telemetryRadioGroup.check(R.id.telemetry_accelerometer)
            else -> binding.telemetryRadioGroup.check(R.id.telemetry_gps)
        }

        binding.applySettingsButton.setOnClickListener {
            Log.d("AppSettingsFragment", "Button clicked")

            selectedLanguageCode = when (binding.languageSpinner.selectedItemPosition) {
                0 -> "pt"
                1 -> "en"
                else -> "en"
            }

            isDarkModeEnabled = binding.themeSwitch.isChecked

            val selectedTelemetry = when (binding.telemetryRadioGroup.checkedRadioButtonId) {
                R.id.telemetry_accelerometer -> "accelerometer"
                else -> "gps"
            }

            applySettings(selectedTelemetry)
        }
    }


    /**
     * Saves the user's settings (language, dark mode, and telemetry) and applies them.
     * This method updates the app's appearance (dark mode, language) and restarts the activity.
     *
     * @param telemetry The selected telemetry type (GPS or accelerometer).
     */

    private fun applySettings(telemetry: String) {
        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("languageCode", selectedLanguageCode)
        editor.putBoolean("darkMode", isDarkModeEnabled)
        editor.putString("telemetry", telemetry)

        editor.apply()

        val mode = if (isDarkModeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)

        updateLocale(selectedLanguageCode)

        activity?.recreate()
    }

    /**
     * Updates the app's locale to reflect the selected language.
     * This change applies the new language to the app without restarting the app entirely.
     *
     * @param languageCode The ISO code of the language to set for the app.
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

    /**
     * Cleans up the binding reference when the fragment's view is destroyed to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
