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

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.invoke(application).userDao()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    suspend fun loginUser(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUserByUsername(username)
                if (user?.password == password) {
                    _user.postValue(user) // Ensure the user data is set
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


    fun logoutUser() {
        _user.value = null
    }

    suspend fun getUserId(username: String): Long? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByUsername(username)?.user_id
        }
    }

}