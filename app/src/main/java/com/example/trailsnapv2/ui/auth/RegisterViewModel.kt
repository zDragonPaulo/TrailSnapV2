package com.example.trailsnapv2.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling user registration logic.
 * This ViewModel interacts with the database to insert a new user into the system.
 * It performs the user registration on a background thread to avoid blocking the UI thread.
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.invoke(application).userDao()

    /**
     * Registers a new user by inserting the provided user data into the database.
     * This method runs in the background using a coroutine to avoid blocking the main thread.
     *
     * @param user The User object containing the data to be registered.
     */
    fun registerUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userDao.insert(user)
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Error registering user", e)
            }
        }
    }
}