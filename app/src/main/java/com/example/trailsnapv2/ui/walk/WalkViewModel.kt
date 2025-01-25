package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk
import kotlinx.coroutines.launch

class WalkViewModel(private val walkDao: WalkDao) : ViewModel() {

    fun createWalk(name: String, userId: Long, startTime: String, endTime: String, distance: Double) {
        val walk = Walk(0, userId, name, distance, startTime, endTime)
        viewModelScope.launch {
            walkDao.insert(walk)
        }
    }
}