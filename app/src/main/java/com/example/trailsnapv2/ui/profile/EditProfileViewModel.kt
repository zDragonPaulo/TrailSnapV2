package com.example.trailsnapv2.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.entities.User
import com.example.trailsnapv2.ui.profile.EditProfileViewModel.UpdateStatus.FAILURE
import com.example.trailsnapv2.ui.profile.EditProfileViewModel.UpdateStatus.SUCCESS
import kotlinx.coroutines.launch

/**
 * ViewModel class for editing user profile data.
 *
 * This ViewModel handles user profile updates and retrieval of user details,
 * providing LiveData objects for observing profile changes and update status.
 *
 * @property userDao The DAO object used to access and update user data in the database.
 */
class EditProfileViewModel(private val userDao: UserDao) : ViewModel() {

    private val _profilePicture = MutableLiveData<String?>()
    val profilePicture: LiveData<String?> = _profilePicture

    private val _updateStatus = MutableLiveData<UpdateStatus>()
    val updateStatus: LiveData<UpdateStatus> = _updateStatus

    /**
     * Updates the user information in the database.
     *
     * This method launches a coroutine to perform the database update operation.
     * Upon completion, it updates the [updateStatus] LiveData to indicate success or failure.
     *
     * @param user The [User] object containing the updated profile information.
     */
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

    /**
     * Retrieves the user details by their ID.
     *
     * This method returns a LiveData object representing the user and observes its value
     * to update the profile picture in [_profilePicture].
     *
     * @param userId The ID of the user to retrieve.
     * @return A [LiveData] object containing the user details or null if not found.
     */
    fun getUserById(userId: Long): LiveData<User?> {
        return userDao.getUserById(userId).asLiveData().also { userLiveData ->
            userLiveData.observeForever { user ->
                _profilePicture.value = user?.profile_picture
            }
        }
    }

    /**
     * Enum class representing the update status for the user's profile.
     *
     * - [SUCCESS]: Indicates that the update operation was successful.
     * - [FAILURE]: Indicates that the update operation failed.
     */
    enum class UpdateStatus {
        SUCCESS,
        FAILURE
    }
}