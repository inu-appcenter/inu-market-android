package com.example.inomtest

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.inomtest.dataClass.ItemData
import com.example.inomtest.network.InomApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductRepository {
    var _products = MutableLiveData<List<ItemData>>()

    private var lastItemId : Int = 0

    fun loadProductItems(
        accessToken: String,
        size: Int,
        itemId: String?,
        categoryId: String?,
        majorId: String?,
        searchWord: String?
    ): Int {

        val call = InomApi.createApi().loadProducts(
            accessToken, size, itemId, categoryId, majorId, searchWord
        )

        call.enqueue(object : Callback<List<ItemData>> {
            override fun onResponse(
                call: Call<List<ItemData>>,
                response: Response<List<ItemData>>
            ) {
                if (response.isSuccessful) {
                    Log.d("프로덕트레포_성공", "통신결과"+response.code().toString())
                    Log.d(ContentValues.TAG,"카테고리:$categoryId, 학과:$majorId, 토큰:$accessToken")
                    _products.value = response.body()
                    Log.d("홈프_샘플데이터", _products.value.toString())

                    lastItemId = _products.value?.get(9)?.itemId!!
                    Log.d("마지막아이템아이디", lastItemId.toString())
                }

                else {
                    Log.d("프로덕트레포_엘스", "통신결과"+response.code().toString())
                }
            }

            override fun onFailure(call: Call<List<ItemData>>, t: Throwable) {
                Log.d("프로덕트레포_실패", "통신결과: $t")
            }
        })

        Log.d("프_레 리턴값", lastItemId.toString())
        return lastItemId
    }


}