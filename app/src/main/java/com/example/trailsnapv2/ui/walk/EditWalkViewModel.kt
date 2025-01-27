package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class EditWalkViewModel(
    private val walkDao: WalkDao,
    private val userDao: UserDao
) : ViewModel() {

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
                onError(e.message ?: "Erro desconhecido.")
            }
        }
    }

    fun saveWalk(walk: Walk, onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (walk.walk_id != null && walkDao.getWalkById(walk.walk_id) != null) {
                    // Atualizar caminhada existente
                    updateWalk(walk, onSuccess = { onSuccess(walk.walk_id!!) }, onError)
                } else {
                    // Inserir nova caminhada e atualizar estatísticas do usuário
                    val id = walkDao.insertWalk(walk)
                    if (id > 0) {
                        updateUserStats(walk.user_id, walk.distance, (walk.end_time - walk.start_time)/1000,
                            onSuccess = { onSuccess(id) },
                            onError = onError
                        )
                    } else {
                        onError("Erro ao salvar a caminhada.")
                    }
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido.")
            }
        }
    }

    private suspend fun updateUserStats(userId: Long, distance: Double, timeUsed: Long, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val user = userDao.getUserById(userId).firstOrNull()
            if (user != null) {
                val updatedUser = user.copy(
                    total_distance = user.total_distance + distance,
                    time_used = user.time_used + timeUsed
                )
                userDao.updateUser(updatedUser)
                onSuccess()
            } else {
                onError("Usuário não encontrado.")
            }
        } catch (e: Exception) {
            onError(e.message ?: "Erro ao atualizar o usuário.")
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
                onError(e.message ?: "Erro desconhecido.")
            }
        }
    }
}
