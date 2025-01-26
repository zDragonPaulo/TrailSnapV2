package com.example.trailsnapv2.ui.walk

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk
import kotlinx.coroutines.launch

class EditWalkViewModel(private val walkDao: WalkDao) : ViewModel() {

    fun saveWalk(walk: Walk, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val id = walkDao.insertWalk(walk)
                if (id > 0) {
                    val allWalks = walkDao.getAllWalks()
                    Log.d("EditWalkFragment", "All walks: $allWalks")

                    onSuccess()
                } else {
                    onError("Erro ao salvar a caminhada.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }
}
