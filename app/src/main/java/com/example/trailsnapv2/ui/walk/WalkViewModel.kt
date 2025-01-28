package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel class responsible for handling the data and logic related to the walk functionality.
 * This ViewModel is intended to manage UI-related data in a lifecycle-conscious way for the walk feature.
 * The ViewModel provides data to the associated UI components and handles any business logic related to walks.
 */
class WalkViewModel : ViewModel() {

    /**
     * A factory for creating instances of [WalkViewModel].
     * This factory is needed because [WalkViewModel] requires no parameters to be constructed,
     * and the default constructor is not accessible through [ViewModelProvider].
     */
    class Factory : ViewModelProvider.Factory {

        /**
         * Creates an instance of [WalkViewModel].
         *
         * @param modelClass The class of the ViewModel to be created.
         * @return A new instance of [WalkViewModel].
         * @throws IllegalArgumentException If the given class is not a [WalkViewModel].
         */
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WalkViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WalkViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}