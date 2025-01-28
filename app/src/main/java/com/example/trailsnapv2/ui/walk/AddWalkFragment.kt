package com.example.trailsnapv2.ui.walk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trailsnapv2.MyApp
import com.example.trailsnapv2.R
import com.example.trailsnapv2.ui.auth.LoginViewModel
import com.google.android.material.textfield.TextInputLayout

/**
 * Fragment responsible for adding a new walk.
 * This fragment allows the user to enter a name for the walk and start it.
 * It observes the current user's data and passes the walk name to the next fragment to start the walk.
 */
class AddWalkFragment : Fragment() {

    private lateinit var addWalkViewModel: AddWalkViewModel
    private val userViewModel: LoginViewModel by activityViewModels()
    private var userId: Long = 0L

    /**
     * Inflates the layout for the AddWalkFragment and sets up the UI components.
     * The user is asked to input a name for the walk, and upon clicking "Start Walk",
     * the fragment navigates to the WalkFragment to start the walk with the given name.
     * Observes the current user's data and retrieves the user ID for validation.
     *
     * @param inflater The LayoutInflater object to inflate the view.
     * @param container The container for the fragment's view.
     * @param savedInstanceState Any previously saved state for the fragment.
     * @return The view for the AddWalkFragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_walk, container, false)
        val walkName: TextInputLayout = view.findViewById(R.id.textInputLayoutWalkName)
        val buttonStartWalk: Button = view.findViewById(R.id.startWalk)

        val walkDao = (requireActivity().application as MyApp).database.walkDao()
        val factory = AddWalkViewModel.Factory()
        addWalkViewModel = ViewModelProvider(this, factory).get(AddWalkViewModel::class.java)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                Log.d("AddWalkFragment", "Observed user ID: ${it.user_id}")
            }
        }

        buttonStartWalk.setOnClickListener {
            val name = walkName.editText?.text.toString()
            val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
            userId = sharedPref.getLong("current_user_id", -1L)

            Log.d("AddWalkFragment", "Retrieved user ID: $userId")

            if (name.isBlank()) {
                walkName.error = getString(R.string.walk_name_empty)
                return@setOnClickListener
            }

            if (userId == 0L) {
                Log.e("AddWalkFragment", "Invalid user_id. Make sure the user is logged in.")
                return@setOnClickListener
            }
            val bundle = Bundle().apply {
                putString("walkName", name)
            }

            findNavController().navigate(R.id.action_navigation_add_walk_to_walkFragment, bundle)
        }

        return view
    }

    /**
     * Observes the user data when the view is created and logs the user ID if available.
     * This function is called after the view has been created to ensure the user data
     * is properly observed and logged.
     *
     * @param view The view that was created for the fragment.
     * @param savedInstanceState The previously saved state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Log.d("AddWalkFragment", "Observed user ID in onViewCreated: ${user.user_id}")
            } else {
                Log.e("AddWalkFragment", "User data is null in onViewCreated.")
            }
        }
    }

}