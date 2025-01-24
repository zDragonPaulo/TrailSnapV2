// LoginViewModel.kt
package com.example.trailsnapv2.ui.auth

import androidx.lifecycle.ViewModel
import com.example.trailsnapv2.dao.UserDao

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

    fun loginUser(username: String, password: String): Boolean {
        // Aqui você pode adicionar lógica para verificar a senha de forma segura
        val user = userDao.getUserByUsername(username)
        return user != null && user.password == password
    }
}