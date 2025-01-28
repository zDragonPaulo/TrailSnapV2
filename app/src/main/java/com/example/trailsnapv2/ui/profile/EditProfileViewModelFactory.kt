package com.example.trailsnapv2.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.UserDao

/**
 * A factory for creating instances of [EditProfileViewModel].
 *
 * This factory ensures that the required dependencies, such as [UserDao],
 * are provided when constructing an instance of [EditProfileViewModel].
 *
 * @property userDao The DAO object used to access user data from the database.
 */
class EditProfileViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {

    /**
     * Creates an instance of the specified [ViewModel] class.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return A new instance of [EditProfileViewModel] if the class matches.
     * @throws IllegalArgumentException If the given class is not [EditProfileViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditProfileViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}