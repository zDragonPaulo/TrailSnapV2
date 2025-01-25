package com.example.trailsnapv2.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.invoke(application).userDao()

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