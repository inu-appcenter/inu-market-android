package com.example.inomtest.fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inomtest.R
import com.example.inomtest.dataClass.*
import com.example.inomtest.databinding.FragmentCategoryBinding
import com.example.inomtest.databinding.ItemCategoryBinding
import kotlin.collections.ArrayList


class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewAdapter:RecyclerViewAdapter
    private lateinit var accessToken: String
    private var cateList = arrayListOf(
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"인기제품",1),
        CategoryList(R.drawable.ic_baseline_insert_photo_24, "도서",2),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"디지털기기",3),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"기프티콘",4),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"의류",5),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"뷰티/미용",6),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"게임/취미",7),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"스포츠/레저",8),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"식물",9),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"음반/티켓",10),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"삽니다",11)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken = arguments?.getString("accessToken").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerViewAdapter.submitList(cateList)
        binding.cateRecycler.adapter = recyclerViewAdapter
        binding.cateRecycler.layoutManager = GridLayoutManager(context, 3)
        binding.backBtn.setOnClickListener{
            it.findNavController().navigate(R.id.action_categoryFragment_to_homeFragment)
        }
    }
    //카테고리 리싸이클러뷰어댑터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {

        private var cateList = ArrayList<CategoryList>()

        //카테고리 리싸이클러뷰 뷰홀더
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomViewHolder(binding)
        }

        inner class CustomViewHolder(val binding: ItemCategoryBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(rArray : CategoryList) {
                with(binding) {
                    categoryImg.setImageResource(rArray.img)
                    nameCategory.text = rArray.name
                }
            }
            }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            if (holder is RecyclerViewAdapter.CustomViewHolder) {
                holder.bind(this.cateList[position])
                holder.itemView.setOnClickListener {
                    it.findNavController().navigate(R.id.action_categoryFragment_to_homeFragment)
                    val SharedPreferences = activity?.getSharedPreferences("access", Context.MODE_PRIVATE)
                    val prefEdit = SharedPreferences?.edit()
                    var category = cateList[position].id
                    prefEdit?.putInt("cate",category)
                    prefEdit?.apply()
                }
            }
        }
        override fun getItemCount(): Int {
            return cateList.size
        }
        fun submitList(cateList1: ArrayList<CategoryList>){
            this.cateList = cateList1
        }
    }
}