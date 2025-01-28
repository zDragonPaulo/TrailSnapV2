package com.example.trailsnapv2.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Walk
import kotlinx.coroutines.launch

/**
 * ViewModel that handles the data and logic for the HistoryFragment.
 * It loads the walk history data for a specific user and provides it to the UI.
 *
 * @param walkDao The DAO used to access the walk data in the database.
 * @param userId The ID of the user whose walk history is to be fetched.
 */
class HistoryViewModel(
    private val walkDao: WalkDao,
    private val userId: Long
) : ViewModel() {

    private val _historyItems = MutableLiveData<List<Walk>>()

    val historyItems: LiveData<List<Walk>> get() = _historyItems

    init {
        loadHistoryItems()
    }

    /**
     * Loads the walk history items from the database using the provided user ID.
     * This method is called by the ViewModel to fetch the data asynchronously.
     * If the data is successfully fetched, it updates the `_historyItems` LiveData.
     * If there is an error, it logs the error and sets an empty list.
     */
    fun loadHistoryItems() {
        viewModelScope.launch {
            try {
                val items = walkDao.getWalksByUserId(userId)
                _historyItems.postValue(items)
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error getting walks", e)
                _historyItems.postValue(emptyList())
            }
        }
    }
}
