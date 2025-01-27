package com.example.trailsnapv2.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.WalkDao

class HistoryViewModelFactory(
    private val walkDao: WalkDao,
    private val userId: Long  // Aceita userId aqui
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(walkDao, userId) as T
    }
}
