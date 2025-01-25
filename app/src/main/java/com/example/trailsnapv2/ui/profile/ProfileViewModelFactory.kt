package com.example.trailsnapv2.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.UserDao

/**
 * A factory class for creating instances of the [ProfileViewModel].
 * This factory class is needed because [ProfileViewModel] requires a parameter
 * (a [UserDao]) in its constructor, and ViewModelProvider needs a factory
 * to properly instantiate it with this parameter.
 *
 * @param userDao The DAO used to interact with the database for user-related operations.
 */
class ProfileViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of [ProfileViewModel] with the provided [UserDao].
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A new instance of [ProfileViewModel] if the [modelClass] is correct.
     * @throws IllegalArgumentException If the [modelClass] is not assignable from [ProfileViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}