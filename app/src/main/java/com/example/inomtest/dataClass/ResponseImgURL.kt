package com.example.inomtest.dataClass

import com.google.gson.annotations.SerializedName

data class ResponseImgURL(
    @SerializedName("imageUrls")
    var imageUrls : ArrayList<String>
)

data class ResponseItemId(
    @SerializedName("itemId")
    var itemId : Int
)
