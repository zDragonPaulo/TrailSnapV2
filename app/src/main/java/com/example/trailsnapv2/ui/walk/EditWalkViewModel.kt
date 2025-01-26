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
                    Log.d("WalkViewModel", "Walk recuperado: $walk")

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
                if (walk.walk_id != null && walkDao.getWalkById(walk.walk_id) != null) {
                    updateWalk(walk,
                        onSuccess = { onSuccess(walk.walk_id!!) },
                        onError = onError)
                } else {
                    // A operação de inserção é assíncrona e feita no viewModelScope
                    val id = walkDao.insertWalk(walk)
                    if (id > 0) {
                        onSuccess(id) // Retorne o ID salvo
                    } else {
                        onError("Erro ao salvar a caminhada.")
                    }
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }



    fun updateWalk(walk: Walk, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val rowsUpdated = walkDao.updateWalk(walk)
                if (rowsUpdated > 0) {
                    onSuccess()
                } else {
                    onError("Erro ao atualizar a caminhada.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }


}

