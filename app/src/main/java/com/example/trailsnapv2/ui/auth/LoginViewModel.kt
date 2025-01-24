package com.example.trailsnapv2.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.trailsnapv2.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.invoke(application).userDao()

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