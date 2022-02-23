package com.example.inomtest.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.inomtest.dataClass.ProductResult
import com.example.inomtest.dataClass.room.OnDeleteListener
import com.example.inomtest.dataClass.room.RecentSearchDAO
import com.example.inomtest.dataClass.room.RecentSearchDatabase
import com.example.inomtest.dataClass.room.RecentSearchEntity
import com.example.inomtest.databinding.FragmentSearchBinding
import com.example.inomtest.network.RetrofitManager
import com.example.inomtest.recyclerview.RecentSearchAdapter
import com.example.inomtest.recyclerview.RecyclerItemAdapter
import com.example.inomtest.recyclerview.SearchResultAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchFragment : AppCompatActivity(), OnDeleteListener {
    private lateinit var binding: FragmentSearchBinding
    lateinit var navController: NavController
    private lateinit var searchAdapter: RecentSearchAdapter
    private lateinit var recylerAdapter: SearchResultAdapter
    private lateinit var db : RecentSearchDatabase
    private lateinit var searchDAO: RecentSearchDAO
    private val recentWordList = mutableListOf<RecentSearchEntity>()
    private var productList = ArrayList<ProductResult>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchBinding.inflate(layoutInflater)

        setContentView(binding.root)
        db = Room.databaseBuilder(this, RecentSearchDatabase::class.java,"room_db")
            .build()
        searchDAO = db.recentSearchDAO()
        searchAdapter = RecentSearchAdapter(this,recentWordList, this)

        refreshAdapter()

        with(binding) {
            RecentWordList.adapter = searchAdapter
            RecentWordList.layoutManager = GridLayoutManager(this@SearchFragment, 2)

            searchBtn.setOnClickListener {
                val content = binding.testEdit.text.toString()
                //검색어 api 호출부에 넘겨주기
                val SharedPreferences = getSharedPreferences("access", MODE_PRIVATE)
                val prefEdit = SharedPreferences.edit()
                prefEdit?.putString("searchWord",content)
                prefEdit?.apply()

                if (content.isNotEmpty()) {
                    val recentWord1 = RecentSearchEntity(null, content)
                    insertWord(recentWord1)
                    //검색 api호출
                    RetrofitManager.instance.searchWord(completion = {
                        responseState, responseBody ->
                        productList = responseBody as ArrayList<ProductResult>

                        //api 수신 후 화면에 표시
                        when(responseState){
                            RetrofitManager.RESPONSE_STATE.OKAY->{
                                Log.d(TAG,"api 호출 성공 : ${productList.size}")
                                searchResult.visibility = View.VISIBLE
                                recentHistory.visibility = View.INVISIBLE
                                recylerAdapter = SearchResultAdapter()
                                recylerAdapter.submitList(productList)
                                searchResult.adapter = recylerAdapter
                                searchResult.layoutManager = LinearLayoutManager(this@SearchFragment)
                            }
                            RetrofitManager.RESPONSE_STATE.FAIL->{
                                Toast.makeText(this@SearchFragment, "api 호출 에러입니다.",Toast.LENGTH_SHORT).show()
                                Log.d(TAG,"api 호출 실패 : $responseBody")
                            }
                        }
                    })
                }
            }
        }
        //recylerAdapter.setOnclick()_리싸이클러뷰 어댑터에서 뷰홀더 파일을 따로 파서 클릭리스너 수정해야함. 클릭이 되지 않음
    }
    //room 데이터 수정사항 업데이트
    fun refreshAdapter(){
        CoroutineScope(Dispatchers.IO).launch {
            recentWordList.clear()
            recentWordList.addAll(searchDAO.getAll())
            withContext(Dispatchers.Main){
                searchAdapter.notifyDataSetChanged()
            }
        }
    }
    //room 데이터 추가
    fun insertWord(searchWord: RecentSearchEntity){
        CoroutineScope(Dispatchers.IO).launch {
            searchDAO.insert(searchWord)
            refreshAdapter()
        }
    }
    //리사이클러뷰 삭제 _ 어댑터 파일과 연결
    override fun onDeleteListener(recentWord: RecentSearchEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            searchDAO.delete(recentWord)
            refreshAdapter()
        }
    }

}