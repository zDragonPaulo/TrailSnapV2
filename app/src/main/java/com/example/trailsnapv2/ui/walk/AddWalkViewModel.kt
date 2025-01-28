package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel responsible for managing the data and logic for adding a new walk.
 * This ViewModel does not hold any specific data, but it is used in the creation
 * and validation of new walks in the AddWalkFragment.
 */
class AddWalkViewModel : ViewModel() {

    /**
     * Factory class used to create instances of [AddWalkViewModel].
     * This is required for the ViewModelProvider to instantiate the ViewModel.
     * The factory ensures the correct type of ViewModel is created.
     */
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")

        /**
         * Creates a new instance of [AddWalkViewModel].
         *
         * @param modelClass The class of the ViewModel to create.
         * @return A new instance of [AddWalkViewModel].
         * @throws IllegalArgumentException If the given class is not assignable from [AddWalkViewModel].
         */
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddWalkViewModel::class.java)) {
                return AddWalkViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}