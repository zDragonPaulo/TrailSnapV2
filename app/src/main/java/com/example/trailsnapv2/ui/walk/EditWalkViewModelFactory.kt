package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.WalkDao

class EditWalkViewModelFactory(private val walkDao: WalkDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditWalkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditWalkViewModel(walkDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
