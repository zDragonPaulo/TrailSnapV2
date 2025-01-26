package com.example.trailsnapv2.ui.walk

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk
import kotlinx.coroutines.launch

class EditWalkViewModel(private val walkDao: WalkDao) : ViewModel() {

    fun getWalkById(walkId: Long, onSuccess: (Walk) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val walk = walkDao.getWalkById(walkId)
                if (walk != null) {
                    onSuccess(walk)
                } else {
                    onError("Caminhada não encontrada.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun saveWalk(walk: Walk, onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val id = walkDao.insertWalk(walk)
                if (id > 0) {
                    onSuccess(id) // Retorne o ID salvo
                } else {
                    onError("Erro ao salvar a caminhada.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }

}

