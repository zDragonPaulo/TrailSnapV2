package com.example.trailsnapv2.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.invoke(application).userDao()

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