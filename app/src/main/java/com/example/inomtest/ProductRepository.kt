package com.example.inomtest

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.inomtest.dataClass.ItemData
import com.example.inomtest.network.InomApi
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductRepository {
    var _products = MutableLiveData<List<ItemData>>()

    fun loadProductItems(
        accessToken: String,
        size: Int,
        itemId: String?,
        categoryId: String?,
        majorId: String?,
        searchWord: String?) {
        // parameter["page"] = page.toString()

        /*val paramObject = JSONObject()
        paramObject.put("size", 1)
        paramObject.put("itemId", null)
        paramObject.put("categoryId", "pushToken")
        paramObject.put("majorId", "pushToken")
        paramObject.put("searchWord", "pushToken")
        val request = RequestBody.create(MediaType.parse("application/json"),paramObject.toString())*/

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
                    Log.d(ContentValues.TAG,"$categoryId, $accessToken")
                    _products.value = response.body()
                    Log.d("홈프_샘플데이터", _products.value.toString())
                }

                else {
                    Log.d("프로덕트레포_엘스", "통신결과"+response.code().toString())
                }
            }

            override fun onFailure(call: Call<List<ItemData>>, t: Throwable) {
                Log.d("프로덕트레포_실패", "통신결과: $t")
            }
        })
    }
}