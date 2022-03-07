package com.example.inomtest.dataClass

import android.graphics.drawable.Drawable
import com.example.inomtest.databinding.ItemLoadingBinding
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

// 상품 검색
data class ItemData(
    @SerializedName("itemId")
    var itemId: Int = 0,

    @SerializedName("title")
    var title: String = "제목 초기화",

    @SerializedName("mainImageUrl")
    var mainImageUrl: String = "내용 초기화",

    @SerializedName("price")
    var price: Int = 0,

    @SerializedName("favoriteCount")
    var favoriteCount: Int = 0,

    @SerializedName("status")
    var status: String = "상태 초기화",

    @SerializedName("createdAt")
    var createdAt: String,

    @SerializedName("updatedAt")
    var updatedAt: String,
)

//상품 상세
data class ItemDetail (
    @SerializedName("itemId")
    var itemId: Int = 0,

    @SerializedName("title")
    var title: String = "제목 초기화",

    @SerializedName("contents")
    var contents: String = "설명 초기화",

    @SerializedName("price")
    var price: Int = 0,

    @SerializedName("favoriteCount")
    var favoriteCount: Int = 0,

    @SerializedName("status")
    var status: String = "상태 초기화",

    @SerializedName("createdAt")
    var createdAt: String,

    @SerializedName("updatedAt")
    var updatedAt: String,

    @SerializedName("major")
    var major: Major,

    @SerializedName("category")
    var category: Category,

    @SerializedName("seller")
    var seller: Seller,

    @SerializedName("imageUrls")
    var imageUrls: List<String>
    )


data class Major (
    @SerializedName("majorID")
    var majorID: Int,

    @SerializedName("name")
    var name: String
)

data class Category (
    @SerializedName("categoryID")
    var categoryID: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("iconUrl")
    var iconUrl: String
)

data class Seller (
    @SerializedName("userID")
    var userID: Int,

    @SerializedName("nickName")
    var nickName: String,

    @SerializedName("imageUrl")
    var imageUrl: String,

    @SerializedName("score")
    var score: Float,

    @SerializedName("pushToken")
    var pushToken: String
)
