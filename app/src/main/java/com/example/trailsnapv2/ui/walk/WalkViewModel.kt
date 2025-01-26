package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk
import kotlinx.coroutines.launch

class WalkViewModel(private val walkDao: WalkDao) : ViewModel() {

    fun createWalk(walkName: String, userId: Long, startTime: Long, endTime: Long, distance: Double) {
        viewModelScope.launch {
            val walk = Walk(
                walk_id = 0, // Room will auto-generate the ID
                user_id = userId,
                walk_name = walkName,
                distance = distance,
                start_time = startTime,
                end_time = endTime
            )
            walkDao.insertWalk(walk)
        }
    }

    class Factory(private val walkDao: WalkDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WalkViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WalkViewModel(walkDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}