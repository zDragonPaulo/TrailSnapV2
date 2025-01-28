package com.example.trailsnapv2.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.WalkDao

/**
 * A factory class for creating instances of [HistoryViewModel].
 *
 * This factory is required because the [HistoryViewModel] has constructor parameters
 * that are not provided by the default ViewModelProvider. This class helps in providing
 * the necessary dependencies, such as the [WalkDao] and the [userId] to the ViewModel.
 *
 * @param walkDao The DAO used to access the walk data from the database.
 * @param userId The ID of the user whose walk history is to be fetched.
 */
class HistoryViewModelFactory(
    private val walkDao: WalkDao,
    private val userId: Long
) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of [HistoryViewModel] with the provided dependencies.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return A new instance of [HistoryViewModel].
     * @throws IllegalArgumentException If the [modelClass] is not of type [HistoryViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(walkDao, userId) as T
    }
}
