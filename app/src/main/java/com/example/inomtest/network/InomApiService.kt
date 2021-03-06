package com.example.inomtest.network

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.inomtest.dataClass.ItemData
import com.example.inomtest.dataClass.NotificationData
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

interface InomApiService {
    @GET("/api/items")
    fun loadProducts(
        @Header("Authorization") accessToken: String,
        @Query ("size") size: Int,
        @Query ("itemId") itemId: String?,
        @Query ("categoryId") categoryId: Int?,
        @Query ("majorId") majorId: Int?,
        @Query ("searchWord") searchWord: String?,
    )
    : Call<List<ItemData>>

    // 로그인
    @POST("/api/users/login")
    fun login(
        @Body jsonparams: RequestBody
    ): Call<Void>

    // 회원가입
    @POST("/api/users")
    fun signUp(
        @Body jsonparams: RequestBody
    ): Call<Void>

    //검색
    @GET("/api/items")
    fun search(
        @Header("Authorization") accessToken: String,
        @Query("searchWord") searchTerm: String,
        @Query("size")size:Int): Call<JsonElement>

    //상세조회
    @GET("/api/items/{itemId}")
    fun productDetails(
        @Header("Authorization") accessToken: String,
        @Query("itemId")itemId: Int): Call<JsonElement>

    //알림
    @GET("/api/notifications")
    fun notification(
        @Header("Authorization") accessToken: String,
        @Query("notificationId") notificationId: Int): Call<List<NotificationData>>

    //채팅방생성
    @POST("/api/items/{itemId}/chat/rooms")
    fun chatRoom(
        @Header("Authorization") accessToken: String,
        @Path ("itemId")itemId: String,
        @Body roomId: Int
    ): Call<Void>
}

object InomApi {
    // private const val baseUrl = "https://inu-market.cf/"  // 베이스 URL
    private const val baseUrl = "http://117.16.191.59:8080"

    private var retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    init{
        //로깅인터셉터 설정 추가하였습니다
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(TAG, "InomApi - log() called / message: $message")
                when {
                    message.isJsonArray() ->
                        Log.d(TAG, JSONArray(message).toString(4))
                    message.isJsonObject() ->
                        Log.d(
                            TAG,
                            JSONObject(message).toString(4)
                        )//json형태이거나 배열이면 로그를 찍을 때 들여쓰기를 해줍니다!
                    else -> {
                        try {
                            Log.d(TAG, JSONObject(message).toString(4))
                        } catch (e: Exception) {
                            Log.d(TAG, message)
                        }
                    }
                }
            }

        })
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)
            .build()

        //Retrofit : type-safe한 HTTP 클라이언트 라이브러리
        //retrofit은 Okhttp 클라이언트를 디폴트로 선언
        //다른 HTTP 모듈보다 빠름
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder)
            .build()
    }


    fun createApi(): InomApiService {
        return retrofit.create(
            InomApiService::class.java
        )
    }

    fun String.isJsonObject(): Boolean {
        return this.startsWith("{") && this.endsWith("}")

    }
    fun String.isJsonArray(): Boolean {
        return this.startsWith("[") && this.endsWith("]")
    }


}