package com.example.trailsnapv2.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk

class HistoryViewModel(private val walkDao: WalkDao, private val userId: Long) : ViewModel() {

    val historyItems: LiveData<List<Walk>> = liveData {
        try {
            // Busca caminhadas para o usuário específico usando userId
            val walks = walkDao.getWalksByUserId(userId)
            emit(walks)  // Emite o resultado para o LiveData
        } catch (e: Exception) {
            Log.e("HistoryViewModel", "Error fetching walks", e)
            emit(emptyList())  // Emite uma lista vazia em caso de erro
        }
    }
}

