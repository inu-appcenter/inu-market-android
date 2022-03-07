package com.example.inomtest.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inomtest.network.RetrofitManager

class MyPageViewModel : ViewModel() {
    val nickName = MutableLiveData("이름")
    val temperature = MutableLiveData(0)

    // load user profile
    suspend fun loadProfile() {
        val service = RetrofitManager
    }

}