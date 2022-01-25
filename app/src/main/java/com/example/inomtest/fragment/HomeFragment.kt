package com.example.inomtest.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inomtest.MainViewModel
import com.example.inomtest.RecyclerItemAdapter
import com.example.inomtest.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var model : MainViewModel

    private lateinit var recyclerItemAdapter: RecyclerItemAdapter

    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(MainViewModel::class.java)

        model.loadProductItems(page)

        binding.rvItemList.apply {
            binding.rvItemList.layoutManager = LinearLayoutManager(context)
            recyclerItemAdapter = RecyclerItemAdapter()
            binding.rvItemList.adapter = recyclerItemAdapter
        }
//        list.add(ItemData(ContextCompat.getDrawable(requireContext(), R.drawable.image_sample)!!, "제목1", "가격1"))
//        list.add(ItemData(ContextCompat.getDrawable(requireContext(), R.drawable.image_sample)!!, "제목2", "가격2"))
//        list.add(ItemData(ContextCompat.getDrawable(requireContext(), R.drawable.image_sample)!!, "제목3", "가격3"))

        model.getAll().observe(viewLifecycleOwner, Observer {
            recyclerItemAdapter.setList(it.items)
            recyclerItemAdapter.notifyItemRangeChanged((page - 1) * 10, 10)
        })

        // 스크롤 리스너
        binding.rvItemList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.rvItemList.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    recyclerItemAdapter.deleteLoading()

                }
            }
        })
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}