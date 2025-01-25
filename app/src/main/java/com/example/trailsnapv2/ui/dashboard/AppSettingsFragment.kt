package com.example.trailsnapv2.ui.dashboard

import android.content.res.Configuration
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import java.util.Locale
import com.example.trailsnapv2.R

/**
 * Fragment that manages the application settings related to language preferences.
 * This fragment allows the user to select a language from a spinner and updates
 * the app's locale accordingly to provide a localized user experience.
 */
class AppSettingsFragment : Fragment() {

    /**
     * Called when the view for the fragment is created. This method sets up the
     * listener for the language selection spinner and handles the language change.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState The previously saved instance state, if any.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val languageSpinner: Spinner = view.findViewById(R.id.language_spinner)
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            /**
             * Called when an item is selected from the language spinner.
             * This method updates the app's locale based on the selected language.
             *
             * @param parent The AdapterView where the selection was made.
             * @param view The view within the AdapterView that was clicked.
             * @param position The position of the selected item.
             * @param id The row id of the selected item.
             */
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setLocale("pt")
                    1 -> setLocale("en")
                }
            }

            /**
             * Called when no item is selected in the spinner.
             * This method does nothing in this case.
             *
             * @param parent The AdapterView where the selection was made.
             */
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    /**
     * Changes the app's locale based on the provided language code.
     * This method updates the application's locale and recreates the activity
     * to apply the new language configuration.
     *
     * @param languageCode The language code to set the app's locale (e.g., "pt" for Portuguese, "en" for English).
     */
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)

        requireActivity().recreate()
    }
}
