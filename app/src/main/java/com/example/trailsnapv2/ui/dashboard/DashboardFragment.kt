package com.example.trailsnapv2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.FragmentDashboardBinding

/**
 * Fragment that displays the dashboard of the application.
 * This fragment is responsible for showing the main dashboard UI elements and handling user interactions,
 * such as navigating to the settings screen.
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    /**
     * Called to create the view hierarchy for the fragment.
     * This method inflates the fragment's layout and sets up the view model and click listeners for the UI elements.
     *
     * @param inflater The LayoutInflater used to inflate the fragment's layout.
     * @param container The parent container to which the fragment's UI should be attached.
     * @param savedInstanceState The previously saved instance state, if any.
     * @return The root view of the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonSettings: ImageButton = binding.root.findViewById(R.id.button_settings)
        buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_appSettingsFragment,
                null,
                null,
                null)
        }

        return root
    }

    /**
     * Called when the view hierarchy of the fragment is being destroyed.
     * This method is used to clean up the binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
