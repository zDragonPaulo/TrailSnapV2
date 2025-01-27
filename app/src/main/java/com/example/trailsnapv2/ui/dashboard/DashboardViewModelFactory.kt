package com.example.trailsnapv2.ui.dashboard

import DashboardViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao

class DashboardViewModelFactory(
    private val userDao: UserDao,
    private val walkDao: WalkDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(userDao, walkDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
