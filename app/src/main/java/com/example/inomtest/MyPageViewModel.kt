package com.example.inomtest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageViewModel : ViewModel() {
    val nickName = MutableLiveData("")
    val temperature = MutableLiveData(0)

    // load user profile
    fun loadProfile() {

    }

    fun setProfile() {
        nickName.postValue("")

    }

}