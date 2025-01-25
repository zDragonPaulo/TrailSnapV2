package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk

class WalkViewModel(private val walkDao: WalkDao) : ViewModel() {

    fun getWalkById(walkId: Long): LiveData<Walk> {
        return walkDao.getWalkById(walkId)
    }

    class Factory(private val walkDao: WalkDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WalkViewModel::class.java)) {
                return WalkViewModel(walkDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}