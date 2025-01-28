package com.example.trailsnapv2.ui.walk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * A ViewModel class to manage the editing and saving of walk data.
 *
 * This ViewModel interacts with the `WalkDao` and `UserDao` to handle CRUD operations for walks
 * and update associated user statistics.
 *
 * @property walkDao Data access object for walk-related operations.
 * @property userDao Data access object for user-related operations.
 */
class EditWalkViewModel(
    private val walkDao: WalkDao,
    private val userDao: UserDao
) : ViewModel() {

    /**
     * Retrieves a walk by its ID and invokes the appropriate callback.
     *
     * @param walkId The ID of the walk to retrieve.
     * @param onSuccess Callback invoked with the retrieved walk if found.
     * @param onError Callback invoked with an error message if the walk is not found or an exception occurs.
     */
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

    /**
     * Saves a walk to the database. If the walk already exists, it is updated; otherwise, it is inserted.
     *
     * @param walk The walk entity to save.
     * @param onSuccess Callback invoked with the walk ID upon successful save or update.
     * @param onError Callback invoked with an error message if the operation fails.
     */
    fun saveWalk(walk: Walk, onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (walk.walk_id != null && walkDao.getWalkById(walk.walk_id) != null) {
                    updateWalk(walk, onSuccess = { onSuccess(walk.walk_id!!) }, onError)
                } else {
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

    /**
     * Updates user statistics, such as total distance and time used.
     *
     * @param userId The ID of the user to update.
     * @param distance The distance to add to the user's total distance.
     * @param timeUsed The time to add to the user's total time used.
     * @param onSuccess Callback invoked upon successful update.
     * @param onError Callback invoked with an error message if the operation fails.
     */
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

    /**
     * Updates an existing walk in the database.
     *
     * @param walk The walk entity to update.
     * @param onSuccess Callback invoked upon successful update.
     * @param onError Callback invoked with an error message if the update fails.
     */
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
