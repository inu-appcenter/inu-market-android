package com.example.inomtest.ui.mypage

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inomtest.R
import com.example.inomtest.dataClass.ItemData
import com.example.inomtest.dataClass.ProductResult
import com.example.inomtest.databinding.FragmentCategoryBinding
import com.example.inomtest.databinding.FragmentMyProductBinding
import com.example.inomtest.databinding.ItemMyproductBinding
import com.example.inomtest.network.App
import com.example.inomtest.network.RetrofitManager
import com.example.inomtest.recyclerview.SearchResultAdapter


class MyProductFragment : Fragment() {
    private var _binding: FragmentMyProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewAdapter1: MyProductFragment.RecyclerViewAdapter1
    private lateinit var accessToken: String
    private var myProList = ArrayList<ItemData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyProductBinding.inflate(inflater, container, false)
        val SharedPreferences = activity?.getSharedPreferences("myPro", Context.MODE_PRIVATE)
        var mode = SharedPreferences?.getInt("myMode", 0)
        var title = SharedPreferences?.getString("myTitle", "")
        binding.myProTop.text = title
        when (mode){
            1-> {
                //판매내역 api호출
                RetrofitManager.instance.myProductApi (completion = {
                        responseState, responseBody ->
                    myProList = responseBody as ArrayList<ItemData>

                    //api 수신 후 화면에 표시
                    when(responseState){
                        RetrofitManager.RESPONSE_STATE.OKAY->{
                            Log.d(ContentValues.TAG,"api 호출 성공 : ${myProList.size}")
                            recyclerViewAdapter1 = RecyclerViewAdapter1()
                            binding.myproRecycler.adapter = recyclerViewAdapter1
                            binding.myproRecycler.layoutManager = LinearLayoutManager(context)
                        }
                        RetrofitManager.RESPONSE_STATE.FAIL->{
                            Log.d(ContentValues.TAG,"api 호출 실패 : $responseBody")
                        }
                    }
                })
            }
            2-> TODO()
            3-> TODO()
        }
        return binding.root
    }


    //구매내역 리싸이클러뷰어댑터
    inner class RecyclerViewAdapter1 : RecyclerView.Adapter<RecyclerViewAdapter1.CustomViewHolder>() {

        private var myProList = mutableListOf<ItemData>()

        //구매내역 리싸이클러뷰 뷰홀더
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val binding = ItemMyproductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomViewHolder(binding)
        }

        inner class CustomViewHolder(val binding: ItemMyproductBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(item : ItemData) {
                with(binding) {
                    binding.nameMyPro.text = item.title
                    binding.priceMyPro.text = item.price.toString()
                    Glide.with(App.instance)
                        .load(item.mainImageUrl)
                        .placeholder(R.drawable.ic_baseline_insert_photo_24)
                        .into(binding.myProImg)
                }
            }
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            if (holder is RecyclerViewAdapter1.CustomViewHolder) {
                holder.bind(this.myProList[position])
            }
        }
        override fun getItemCount(): Int {
            return myProList.size
        }
    }
}