package com.example.trailsnapv2.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trailsnapv2.entities.Walk

class HistoryViewModel : ViewModel() {
    private val _historyItems = MutableLiveData<List<Walk>>()
    val historyItems: LiveData<List<Walk>> get() = _historyItems

    init {
        _historyItems.value = listOf()
    }

    fun addHistoryItem(item: Walk) {
        val updatedList = _historyItems.value?.toMutableList() ?: mutableListOf()
        updatedList.add(item)
        _historyItems.value = updatedList
    }
}