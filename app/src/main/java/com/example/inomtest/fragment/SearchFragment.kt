package com.example.inomtest.fragment

import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.inomtest.*
import com.example.inomtest.databinding.FragmentSearchBinding
import com.example.inomtest.network.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchFragment : AppCompatActivity(), SearchView.OnQueryTextListener, OnDeleteListener {
    private lateinit var binding: FragmentSearchBinding
    lateinit var navController: NavController
    private lateinit var searchAdapter: RecentSearchAdapter
    private lateinit var db : RecentSearchDatabase
    private lateinit var searchDAO: RecentSearchDAO
    private val recentWordList = mutableListOf<RecentSearchEntity>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchBinding.inflate(layoutInflater)

        setContentView(binding.root)
        db = Room.databaseBuilder(this,RecentSearchDatabase::class.java,"room_db")
            .build()
        searchDAO = db.recentSearchDAO()
        searchAdapter = RecentSearchAdapter(this,recentWordList, this)

        refreshAdapter()

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.apply {
            this.queryHint="검색어를 입력해주세요."
            this.setOnQueryTextListener(this@SearchFragment)
        }

        onQueryTextChange(newText = null)
        with(binding) {
            RecentWordList.adapter = searchAdapter
            RecentWordList.layoutManager = GridLayoutManager(this@SearchFragment, 2)

            searchBtn.setOnClickListener {
                val content = binding.testEdit.text.toString()
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

                        when(responseState){
                            RetrofitManager.RESPONSE_STATE.OKAY->{
                                Log.d(TAG,"api 호출 성공 : $responseBody")
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
    fun insertWord(searchWord:RecentSearchEntity){
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

    //서치뷰 검색어 입력 이벤트
    //키보드의 검색버튼 클릭 되었을때
    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d(TAG, "SearchActivity - onQueryTextSubmit() called / query: $query")

        this.binding.searchView.setQuery("", false)//서치뷰에 빈 값 넣고 검색은 안함
        this.binding.searchView.clearFocus()//키보드가 내려감
        //검색 api호출
        //RetrofitManager.instance.searchWord(searchTerm = term)

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d(TAG, "SearchActivity - onQueryTextChange() called / newText: $newText")

        val userInputText = newText ?: ""//검색창에 무언가 있으면 그대로 넣고 없으면 ""을 넣음
        return true
    }

}