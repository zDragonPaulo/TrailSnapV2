package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao

/**
 * A factory class for creating instances of [EditWalkViewModel].
 *
 * This factory is responsible for providing the necessary dependencies for the [EditWalkViewModel],
 * such as the [WalkDao] and [UserDao] instances. It ensures that the view model is created with
 * the appropriate dependencies for editing a walk.
 *
 * @param walkDao The data access object used to interact with walk-related data.
 * @param userDao The data access object used to interact with user-related data.
 */
class EditWalkViewModelFactory(
    private val walkDao: WalkDao,
    private val userDao: UserDao
) : ViewModelProvider.Factory {

    /**
     * Creates an instance of [EditWalkViewModel] using the provided [WalkDao] and [UserDao].
     *
     * This method is called to instantiate a new [EditWalkViewModel] with the necessary dependencies.
     *
     * @param modelClass The class of the [ViewModel] to be created.
     * @return An instance of [EditWalkViewModel].
     * @throws IllegalArgumentException If the provided [modelClass] is not assignable from [EditWalkViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditWalkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditWalkViewModel(walkDao, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
