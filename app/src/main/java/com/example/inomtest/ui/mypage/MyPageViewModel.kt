package com.example.inomtest.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageViewModel : ViewModel() {
    val nickName = MutableLiveData("이름")
    val temperature = MutableLiveData(0)

    // load user profile
    fun loadProfile() {

    }

    fun setProfile() {
        nickName.postValue("")

    }

}