package com.example.bestofbehance.paging

import android.content.ClipData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.bestofbehance.binding.CardBinding

class TestDataSourceFactory(val dataSource: PageKeyedDataSource<Int, CardBinding>) : DataSource.Factory<Int, CardBinding>() {

    var itemLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, CardBinding>>? = null

    override fun create(): DataSource<Int, CardBinding> {
        val itemDataSource = dataSource
        itemLiveDataSource?.postValue(itemDataSource)
        return itemDataSource
    }
}