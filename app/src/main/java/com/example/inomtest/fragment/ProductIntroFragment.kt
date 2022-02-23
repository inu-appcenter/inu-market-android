package com.example.inomtest.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.inomtest.R
import com.example.inomtest.databinding.FragmentProductIntroBinding
import com.example.inomtest.network.RetrofitManager


class ProductIntroFragment : AppCompatActivity() {

    private lateinit var binding: FragmentProductIntroBinding
    lateinit var navController: NavController
    private var destinationUid : String? = null

    //검색한 후 상품아이디와 유저정보를 받아오려는 코드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProductIntroBinding.inflate(layoutInflater)
        destinationUid = intent.getStringExtra("destinationUid")
    }

}