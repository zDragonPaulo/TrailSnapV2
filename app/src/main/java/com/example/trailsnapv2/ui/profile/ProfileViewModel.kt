package com.example.trailsnapv2.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(private val userDao: UserDao) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _birthday = MutableLiveData<String>()
    val birthday: LiveData<String> = _birthday

    private val _totalDistance = MutableLiveData<String>()
    val totalDistance: LiveData<String> = _totalDistance

    private val _timeUsed = MutableLiveData<String>()
    val timeUsed: LiveData<String> = _timeUsed

    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            userDao.getUserById(userId).collect { user: User? ->
                user?.let {
                    _username.value = it.username
                    _birthday.value = it.birthday
                    _totalDistance.value = "${it.total_distance} km"
                    _timeUsed.value = "${it.time_used} hours"
                }
            }
        }
    }
}