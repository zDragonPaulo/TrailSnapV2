package com.example.trailsnapv2.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.UserDao
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the profile data of a user.
 * This ViewModel retrieves and holds the user's information like username, birthday,
 * description and profile picture, and updates the UI accordingly.
 *
 * @param userDao The DAO used to interact with the database for user-related operations.
 */
class ProfileViewModel(private val userDao: UserDao) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _birthday = MutableLiveData<String>()
    val birthday: LiveData<String> = _birthday

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _profilePicture = MutableLiveData<String>()
    val profilePicture: LiveData<String> = _profilePicture


    /**
     * Loads the user data by userId and updates the LiveData properties.
     * This function will query the database for the user with the given userId
     * and update the LiveData properties with the retrieved values.
     *
     * @param userId The unique identifier for the user whose data needs to be loaded.
     */
    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            userDao.getUserById(userId).collect { user ->
                user?.let {
                    _username.value = it.username
                    _birthday.value = it.birthday
                    _description.value = it.user_description
                    _profilePicture.value = it.profile_picture!!
                }
            }
        }
    }
}