package com.example.trailsnapv2.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ViewModel responsible for handling the login functionality in the application.
 * It interacts with the UserDao to authenticate users based on their credentials (username and password).
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.invoke(application).userDao()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    /**
     * Attempts to log in the user by checking if the provided username and password match a user in the database.
     * If the login is successful, the user data is set in LiveData and returns `true`.
     * If login fails (incorrect password or user not found), returns `false`.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return A Boolean indicating whether the login was successful or not.
     */
    suspend fun loginUser(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUserByUsername(username)
                if (user?.password == password) {
                    _user.postValue(user)
                    Log.d("LoginViewModel", "Logged in user ID: ${user.user_id}")
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error logging in user", e)
                false
            }
        }
    }

    /**
     * Retrieves the user ID associated with the provided username.
     * This is a helper function used after a successful login.
     *
     * @param username The username to search for in the database.
     * @return The user ID if the user exists, or `null` if the user is not found.
     */
    suspend fun getUserId(username: String): Long? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByUsername(username)?.user_id
        }
    }

}