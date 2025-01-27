package com.example.trailsnapv2.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val walkDao: WalkDao,
    private val userId: Long
) : ViewModel() {

    // MutableLiveData interno
    private val _historyItems = MutableLiveData<List<Walk>>()

    // LiveData público, apenas de leitura
    val historyItems: LiveData<List<Walk>> get() = _historyItems

    init {
        loadHistoryItems()  // Carregar dados ao inicializar o ViewModel
    }

    fun loadHistoryItems() {
        viewModelScope.launch {
            try {
                // Busca caminhadas para o usuário específico
                val items = walkDao.getWalksByUserId(userId)
                _historyItems.postValue(items)  // Atualiza o LiveData
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Erro ao buscar caminhadas", e)
                _historyItems.postValue(emptyList())  // Emite uma lista vazia em caso de erro
            }
        }
    }
}
