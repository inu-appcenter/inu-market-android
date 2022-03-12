package com.example.inomtest.ui.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inomtest.R
import com.example.inomtest.dataClass.CategoryList
import com.example.inomtest.databinding.FragmentMypageBinding
import com.example.inomtest.databinding.ItemCategoryBinding
import com.example.inomtest.fragment.CategoryFragment
import com.example.inomtest.fragment.LoginFragment


class MyPageFragment: Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private val viewModel : MyPageViewModel by viewModels()
    private lateinit var recyclerViewAdapter: MyPageFragment.RecyclerViewAdapter
    private var buttonList = arrayListOf(
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"판매내역",1),
        CategoryList(R.drawable.ic_baseline_insert_photo_24, "구매내역",2),
        CategoryList(R.drawable.ic_baseline_insert_photo_24,"관심목록",3)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)
        binding.viewModel = viewModel

        //버튼 리싸이클러뷰 세팅
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerViewAdapter.submitList(buttonList)
        binding.mvRecycler.adapter = recyclerViewAdapter
        binding.mvRecycler.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    //버튼 리싸이클러뷰어댑터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {

        private var buttonList = ArrayList<CategoryList>()

        //버튼 리싸이클러뷰 뷰홀더
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
                holder.bind(this.buttonList[position])
                holder.itemView.setOnClickListener {
                    if (buttonList[position].id ==1){
                        it.findNavController().navigate(R.id.action_myPageFragment_to_myProductFragment)
                        val SharedPreferences = activity?.getSharedPreferences("myPro", Context.MODE_PRIVATE)
                        val prefEdit = SharedPreferences?.edit()
                        var titleSub = buttonList[position].name
                        var mode = buttonList[position].id
                        prefEdit?.putInt("myMode",mode)
                        prefEdit?.putString("myTitle",titleSub)
                        prefEdit?.apply()
                    }
                    else if (buttonList[position].id ==2){
                        it.findNavController().navigate(R.id.action_myPageFragment_to_myProductFragment)
                        val SharedPreferences = activity?.getSharedPreferences("myPro", Context.MODE_PRIVATE)
                        val prefEdit = SharedPreferences?.edit()
                        var titleSub = buttonList[position].name
                        var mode = buttonList[position].id
                        prefEdit?.putInt("myMode",mode)
                        prefEdit?.putString("myTitle",titleSub)
                        prefEdit?.apply()
                    }
                    else if (buttonList[position].id ==3){
                        it.findNavController().navigate(R.id.action_myPageFragment_to_myProductFragment)
                        val SharedPreferences = activity?.getSharedPreferences("myPro", Context.MODE_PRIVATE)
                        val prefEdit = SharedPreferences?.edit()
                        var titleSub = buttonList[position].name
                        var mode = buttonList[position].id
                        prefEdit?.putInt("myMode",mode)
                        prefEdit?.putString("myTitle",titleSub)
                        prefEdit?.apply()
                    }
                }
            }
        }
        override fun getItemCount(): Int {
            return buttonList.size
        }
        fun submitList(cateList1: ArrayList<CategoryList>){
            this.buttonList = cateList1
        }
    }
}