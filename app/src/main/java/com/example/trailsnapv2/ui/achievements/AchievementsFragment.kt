package com.example.trailsnapv2.ui.achievements

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.FragmentAchievementsBinding
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * A Fragment that displays the user's achievements in a grid layout. It shows both user-specific
 * achievements and general achievements, updating them when necessary.
 */
class AchievementsFragment : Fragment() {

    private lateinit var binding: FragmentAchievementsBinding
    private lateinit var adapter: AchievementsAdapter

    private val viewModel: AchievementsViewModel by viewModels {
        AchievementsViewModelFactory(
            (requireActivity().application as MyApp).database.singularAchievementDao(),
            (requireActivity().application as MyApp).database.userAchievementDao(),
            (requireActivity().application as MyApp).database.walkDao()
        )
    }



    private var userId: Long = 0L

    /**
     * Inflates the fragment layout and sets up the RecyclerView to display achievements.
     * Initializes the adapter and GridLayoutManager for the RecyclerView.
     *
     * @param inflater The LayoutInflater used to inflate the fragment's view.
     * @param container The parent view group that the fragment's view will be attached to.
     * @param savedInstanceState A Bundle containing any saved instance state.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAchievementsBinding.inflate(inflater, container, false)

        adapter = AchievementsAdapter()
        binding.recyclerViewAchievements.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewAchievements.adapter = adapter

        return binding.root
    }

    /**
     * Retrieves the current user's ID and initializes the achievements.
     * This is called after the view has been created.
     *
     * @param view The view of the fragment.
     * @param savedInstanceState The saved instance state from the previous fragment lifecycle.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the locale to Portuguese for testing
        setLocale(requireContext(), "pt")

        userId = getCurrentUserId()
        if (userId == -1L) return

        initializeAchievements()
    }
    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    /**
     * Called when the fragment is resumed. This will refresh the achievements when returning to this fragment.
     */
    override fun onResume() {
        super.onResume()

        refreshAchievements()
    }

    /**
     * Initializes the user's achievements and sets up observers to handle achievement data updates.
     * This function ensures that default achievements are inserted if necessary.
     */
    private fun initializeAchievements() {
        lifecycleScope.launch {
            viewModel.insertDefaultAchievementsIfNeeded(requireContext())
            viewModel.initializeUserAchievements(userId)

            observeAchievements()
        }
    }

    /**
     * Sets up observers for user achievements and singular achievements.
     * Once data is observed, it updates the RecyclerView with the new achievement data.
     */
    private fun observeAchievements() {
        viewModel.getUserAchievements(userId).observe(viewLifecycleOwner) { userAchievements ->
            viewModel.getAllSingularAchievements().observe(viewLifecycleOwner) { singularAchievements ->
                adapter.setUserAchievements(userAchievements, singularAchievements)
            }
        }
    }

    /**
     * Refreshes the achievements by updating their progress and reloading the data.
     * This method is called when the fragment is resumed.
     */
    private fun refreshAchievements() {
        lifecycleScope.launch {
            viewModel.updateAchievements(userId)

            observeAchievements()
        }
    }

    /**
     * Retrieves the current user's ID from shared preferences.
     * If no user is found, a toast is displayed to notify the user to log in again.
     *
     * @return The ID of the current user, or -1 if no user is found.
     */
    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        return sharedPref.getLong("current_user_id", -1L).also { userId ->
            if (userId == -1L) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.user_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}