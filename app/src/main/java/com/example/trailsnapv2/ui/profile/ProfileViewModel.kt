package com.example.trailsnapv2.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _username = MutableLiveData<String>().apply {
        value = "John Doe"
    }
    val username: LiveData<String> = _username

    private val _birthday = MutableLiveData<String>().apply {
        value = "January 1, 1990"
    }
    val birthday: LiveData<String> = _birthday

    private val _totalDistance = MutableLiveData<String>().apply {
        value = "100 km"
    }
    val totalDistance: LiveData<String> = _totalDistance

    private val _timeUsed = MutableLiveData<String>().apply {
        value = "50 hours"
    }
    val timeUsed: LiveData<String> = _timeUsed
}