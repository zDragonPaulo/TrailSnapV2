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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o spinner de seleção de idioma
        val languageSpinner: Spinner = view.findViewById(R.id.language_spinner)
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> updateLocale("pt") // Português
                    1 -> updateLocale("en") // Inglês
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Não faz nada
            }
        }
    }

    /**
     * Updates the app's locale and restarts the activity to apply changes globally.
     *
     * @param languageCode The ISO language code to set the app's locale.
     */
    private fun updateLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(requireContext().resources.configuration)
        config.setLocale(locale)

        requireContext().resources.updateConfiguration(
            config,
            requireContext().resources.displayMetrics
        )

        // Reinicia a MainActivity para aplicar as alterações no idioma
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
