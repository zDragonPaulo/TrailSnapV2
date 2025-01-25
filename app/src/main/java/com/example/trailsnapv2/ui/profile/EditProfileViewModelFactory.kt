package com.example.trailsnapv2.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.UserDao

class EditProfileViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditProfileViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}