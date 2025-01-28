package com.example.trailsnapv2.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.launch

class EditProfileViewModel(private val userDao: UserDao) : ViewModel() {

    private val _profilePicture = MutableLiveData<String?>()
    val profilePicture: LiveData<String?> = _profilePicture

    private val _updateStatus = MutableLiveData<UpdateStatus>()
    val updateStatus: LiveData<UpdateStatus> = _updateStatus

    fun updateUser(user: User) {
        viewModelScope.launch {
            val rowsUpdated = userDao.updateUser(user)
            if (rowsUpdated > 0) {
                _updateStatus.postValue(UpdateStatus.SUCCESS)
            } else {
                _updateStatus.postValue(UpdateStatus.FAILURE)
            }
        }
    }

    fun getUserById(userId: Long): LiveData<User?> {
        return userDao.getUserById(userId).asLiveData().also { userLiveData ->
            userLiveData.observeForever { user ->
                // Update the profilePicture when the user data changes
                _profilePicture.value = user?.profile_picture
            }
        }
    }

    enum class UpdateStatus {
        SUCCESS,
        FAILURE
    }
}