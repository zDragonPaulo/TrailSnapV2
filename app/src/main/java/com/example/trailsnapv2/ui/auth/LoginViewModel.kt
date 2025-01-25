package com.example.trailsnapv2.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.trailsnapv2.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ViewModel responsible for handling the login logic of the user.
 * This ViewModel interacts with the database to verify the user's credentials and perform login validation.
 * It uses a background thread to perform the login check to ensure it does not block the main thread.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.invoke(application).userDao()

    /**
     * Attempts to log in a user by validating their username and password.
     * This method runs in the background using a coroutine to avoid blocking the main thread.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password provided by the user attempting to log in.
     * @return Boolean indicating whether the login was successful (true if valid, false otherwise).
     */
    suspend fun loginUser(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUserByUsername(username)
                user?.password == password
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error logging in user", e)
                false
            }
        }
    }
}