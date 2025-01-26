package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk
import kotlinx.coroutines.launch

class AddWalkViewModel(private val walkDao: WalkDao) : ViewModel() {

    fun createWalk(name: String, userId: Long, startTime: Long, endTime: Long, distance: Double) {
        viewModelScope.launch {
            val userExists = walkDao.userExists(userId)
            if (userExists) {
                val walk = Walk(0, userId, name, distance, startTime, endTime)
                walkDao.insertWalk(walk)
            } else {
                // Handle the case where the user does not exist
                // e.g., log an error or show a message to the user
            }
        }
    }

    class Factory(private val walkDao: WalkDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddWalkViewModel::class.java)) {
                return AddWalkViewModel(walkDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}