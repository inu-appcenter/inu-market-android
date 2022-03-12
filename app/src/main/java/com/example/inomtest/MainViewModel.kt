package com.example.inomtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inomtest.dataClass.ItemData

class MainViewModel : ViewModel() {
    private val productRepository = ProductRepository()

    private val _items : MutableLiveData<List<ItemData>>
        get() = productRepository._products
    val items : LiveData<List<ItemData>> = _items

    private var lastItemId : String? = null

    fun loadProductItems(
        accessToken: String,
        size: Int,
        itemId: String?,
        categoryId: String?,
        majorId: String?,
        searchWord: String?
    ): String? {
        lastItemId = productRepository.loadProductItems(accessToken,
            size,
            itemId,
            categoryId,
            majorId,
            searchWord).toString()
        return lastItemId
    }

    fun getAll(): LiveData<List<ItemData>> {
        return items
    }
}