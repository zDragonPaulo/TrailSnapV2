package com.example.trailsnapv2.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * ViewModel for handling user registration logic.
 * It interacts with the user database to register a new user.
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.invoke(application).userDao()

    /**
     * Registers a new user by inserting the user's data into the database.
     * The method assigns a unique user ID to the new user based on the current maximum user ID.
     *
     * @param user The [User] object containing the registration data.
     * @return The newly generated user ID if registration is successful, or -1 if an error occurs.
     */
    suspend fun registerUser(user: User): Long {
        return withContext(Dispatchers.IO) {
            try {
                val maxUserId = userDao.getMaxUserId() ?: 0L
                val newUser = user.copy(user_id = maxUserId + 1)
                userDao.insert(newUser)
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Error registering user", e)
                -1L
            }
        }
    }
}