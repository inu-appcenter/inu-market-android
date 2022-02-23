package com.example.inomtest.dataClass

import java.io.Serializable

//검색후 받아오는 제품 모델
data class ProductResult (var itemId : Int?,
                            var thumbnail : String?,
                            var title : String?,
                            var price : Int?,
                            var likes : Int?):Serializable{
}
//제품 상세보기 자료모델
data class ProductDetails(var itemId: Int?,
                          var title: String?,
                          var contents: String?,
                          var price: Int?,
                          var likes: Int?,
                          // var status: Boolean?,
                          var favorite: Boolean?,
                          //var majorId: Int?,
                          //var majorName: String?,
                          var categoryId: Int,
                          var categoryName: String?,
                          var userId: Int?,
                          var userName: String?,
                          var userImage: String?,
                          var score: Float?,
                          var ImageUrl: String?):Serializable{

                          }