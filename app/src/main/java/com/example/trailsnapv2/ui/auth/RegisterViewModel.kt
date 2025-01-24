package com.example.trailsnapv2.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao: UserDao = AppDatabase.invoke(application).userDao()

    fun registerUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insert(user)
        }
    }
}
