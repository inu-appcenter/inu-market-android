package com.example.inomtest.network

import android.app.Notification
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import com.example.inomtest.dataClass.ItemData
import com.example.inomtest.dataClass.NotificationData
import com.example.inomtest.dataClass.ProductResult
import com.example.inomtest.fragment.SearchFragment
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {
    companion object {
        val instance = RetrofitManager()
    }

    //검색 api 호출
    fun searchWord(completion:(RESPONSE_STATE, ArrayList<ProductResult>?)->Unit){
        //레트로핏 인터페이스 가져오기
        val iRetrofit :InomApiService = InomApi.createApi()

        //검색어, 액세스 토큰 가져오기
        val SharedPreferences = App.instance.getSharedPreferences("access", Context.MODE_PRIVATE)
        var access = SharedPreferences.getString("accessToken", "")
        var searchTerm = SharedPreferences.getString("searchWord","")
        var term = searchTerm.let {
            it
        }?:""
        val callSearch = iRetrofit.search(accessToken = access.toString(), searchTerm = term, size = 50).let{
            it
        }?: return

        callSearch.enqueue(object: retrofit2.Callback<JsonElement>{
            //응답성공시
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitMasager - onResponce() called / t ${response.raw()}")
                if(response.code()!=200){
                    Toast.makeText(App.instance,"${response.code()} 에러입니다.",Toast.LENGTH_SHORT).show()
                }else{
                    Log.d(TAG, "검색어 : $term, 토큰 : $access")
                    //정보 받아오기
                    response.body()?.let {
                        var parsedDataArray = ArrayList<ProductResult>()
                        val body = it.asJsonArray
                        body.forEach{ rItem ->
                            val rItemObject = rItem.asJsonObject
                            val itemId = rItem.asJsonObject.get("itemId").asInt
                            val thumbnail = rItemObject.get("mainImageUrl").asString
                            val price = rItemObject.get("price").asInt
                            val title = rItemObject.get("title").asString
                            val likes = rItemObject.get("favoriteCount")?.asInt
                            //Log.d(TAG,"$thumbnail $price $title $likes")
                            val reItem = ProductResult(
                                itemId = itemId,
                                thumbnail = thumbnail,
                                title = title,
                                price = price,
                                likes = likes?:0
                            )
                            parsedDataArray.add(reItem)
                            //Log.d(TAG,"$reItem")
                        }
                        completion(RESPONSE_STATE.OKAY, parsedDataArray)
                    }
                }
            }

            //응답실패시
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitMasager - onResponce() called / t $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

        })
    }

    //알림 api 호출
    fun notifiApi(completion:(RetrofitManager.RESPONSE_STATE, String)->Unit){
        //레트로핏 인터페이스 가져오기
        val iRetrofit : InomApiService = InomApi.createApi()

        //Id, 액세스 토큰 가져오기
        val SharedPreferences = App.instance.getSharedPreferences("access", Context.MODE_PRIVATE)
        var access = SharedPreferences.getString("accessToken", "")
        val SharedPreferences1 = App.instance.getSharedPreferences("notify", Context.MODE_PRIVATE)
        var Id = SharedPreferences1.getInt("notificationId",1)
        val callSearch = iRetrofit.notification(accessToken = access.toString(), notificationId = Id).let{
            it
        }?: return

        callSearch.enqueue(object: retrofit2.Callback<List<NotificationData>>{
            //응답성공시
            override fun onResponse(call: Call<List<NotificationData>>, response: Response<List<NotificationData>>) {
                Log.d(ContentValues.TAG, "RetrofitMasager - onResponce() called / t ${response.raw()}")
                if(response.code()!=200){
                    Toast.makeText(App.instance,"${response.code()} 에러입니다.",Toast.LENGTH_SHORT).show()
                }else{
                    var Id = response.headers().get("notificationId")?.toInt()
                    Log.d(ContentValues.TAG, "알림아이디 : $Id, 토큰 : $access")
                    completion(RetrofitManager.RESPONSE_STATE.OKAY, response.raw().toString())
                    val SharedPreferences = App.instance.getSharedPreferences("notify", Context.MODE_PRIVATE)
                    val prefEdit = SharedPreferences?.edit()
                    prefEdit?.putInt("notificationId",Id.let { it }?:1)
                    prefEdit?.apply()
                }
            }

            //응답실패시
            override fun onFailure(call: Call<List<NotificationData>>, t: Throwable) {
                Log.d(ContentValues.TAG, "RetrofitMasager - onResponce() called / t $t")
                completion(RetrofitManager.RESPONSE_STATE.FAIL, t.toString())
            }

        })
    }

    //상세보기api 호출
//    fun productDetail(completion:(RESPONSE_STATE, ArrayList<ProductResult>?)->Unit){
//        //레트로핏 인터페이스 가져오기
//        val iRetrofit :InomApiService = InomApi.createApi()
//
//        //itemId, 액세스 토큰 가져오기
//        val SharedPreferences = App.instance.getSharedPreferences("access", Context.MODE_PRIVATE)
//        var access = SharedPreferences.getString("accessToken", "")
//
//        val callSearch = iRetrofit.search(accessToken = access.toString()).let{
//            it
//        }?: return
//
//        callSearch.enqueue(object: retrofit2.Callback<JsonElement>{
//            //응답성공시
//            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
//                Log.d(TAG, "RetrofitMasager - onResponce() called / t ${response.raw()}")
//                if(response.code()!=200){
//                    Toast.makeText(App.instance,"${response.code()} 에러입니다.",Toast.LENGTH_SHORT).show()
//                }else{
//                    //Log.d(TAG, "검색어 : $term, 토큰 : $access")
//                    //정보 받아오기
//                    response.body()?.let {
//                        var parsedDataArray = ArrayList<ProductResult>()
//                        val body = it.asJsonArray
//                        body.forEach{ rItem ->
//                            val rItemObject = rItem.asJsonObject
//                            val itemId = rItem.asJsonObject.get("itemId").asInt
//                            val thumbnail = rItemObject.get("mainImageUrl").asString
//                            val price = rItemObject.get("price").asInt
//                            val title = rItemObject.get("title").asString
//                            val likes = rItemObject.get("favoriteCount")?.asInt
//                            //Log.d(TAG,"$thumbnail $price $title $likes")
//                            val reItem = ProductResult(
//                                itemId = itemId,
//                                thumbnail = thumbnail,
//                                title = title,
//                                price = price,
//                                likes = likes?:0
//                            )
//                            parsedDataArray.add(reItem)
//                            //Log.d(TAG,"$reItem")
//                        }
//                        completion(RESPONSE_STATE.OKAY, parsedDataArray)
//                    }
//                }
//            }
//
//            //응답실패시
//            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
//                Log.d(TAG, "RetrofitMasager - onResponce() called / t $t")
//                completion(RESPONSE_STATE.FAIL, null)
//            }
//
//        })
//    }

    //채팅방 api 생성
//    fun chatRoomCreate(completion:(RESPONSE_STATE)->Unit){
//        //레트로핏 인터페이스 가져오기
//        val iRetrofit :InomApiService = InomApi.createApi()
//
//        //액세스 토큰, itemId 가져오기
//        val SharedPreferences = App.instance.getSharedPreferences("access", Context.MODE_PRIVATE)
//        var access = SharedPreferences.getString("accessToken", "")
//        val callChatRoom = iRetrofit.chatRoom(accessToken = access).toString().let{
//            it
//        }?: return
//        //
//
//        callChatRoom.enqueue(object: retrofit2.Callback<JsonElement>{
//            //응답성공시
//            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
//                Log.d(TAG, "RetrofitMasager - onResponce() called / t ${response.raw()}")
//                if(response.code()!=200){
//                    Toast.makeText(App.instance,"${response.code()} 에러입니다.",Toast.LENGTH_SHORT).show()
//                }else{
//                    //정보 받아오기
//                    response.body()?.let {
//                        var parsedDataArray = ArrayList<ProductResult>()
//                        val body = it.asJsonArray
//                        }
//                        completion(RESPONSE_STATE.OKAY)
//                }
//            }
//            //응답실패시
//            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
//                Log.d(TAG, "RetrofitMasager - onResponce() called / t $t")
//                completion(RESPONSE_STATE.FAIL)
//            }
//
//        })
//    }

    // profile
    fun getProfileData(){
        CoroutineScope(Dispatchers.IO).async {

        }
    }

    enum class RESPONSE_STATE{
        OKAY,
        FAIL
    }
}