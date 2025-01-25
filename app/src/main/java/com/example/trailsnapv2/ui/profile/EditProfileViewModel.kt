package com.example.trailsnapv2.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.launch

class EditProfileViewModel(private val userDao: UserDao) : ViewModel() {

    fun updateUser(user: User) {
        viewModelScope.launch {
            val rowsUpdated = userDao.updateUser(user)
            if (rowsUpdated > 0) {
                // Update was successful
                // You can add additional logic here, such as notifying the user
                // For example, you could use a LiveData to notify the fragment
                _updateStatus.postValue(UpdateStatus.SUCCESS)
            } else {
                // Update failed
                // Handle the failure case, such as showing an error message
                _updateStatus.postValue(UpdateStatus.FAILURE)
            }
        }
    }

    private val _updateStatus = MutableLiveData<UpdateStatus>()
    val updateStatus: LiveData<UpdateStatus> = _updateStatus

    enum class UpdateStatus {
        SUCCESS,
        FAILURE
    }
}