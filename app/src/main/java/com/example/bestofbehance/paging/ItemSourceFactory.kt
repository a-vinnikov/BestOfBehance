package com.example.bestofbehance.paging

import androidx.paging.DataSource
import androidx.paging.PagedList
import com.example.bestofbehance.binding.CardBinding
import kotlinx.coroutines.Deferred

class ItemSourceFactory(private val asyncItems: Deferred<MutableList<CardBinding>>, private val onEmptyAction: () -> Unit) : DataSource.Factory<Int, CardBinding>() {

    override fun create(): ItemDataSource { return ItemDataSource(asyncItems, onEmptyAction) }

    companion object {
        fun providePagingConfig(): PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(5)
            .setInitialLoadSizeHint(48)
            .setPageSize(10)
            .build()
    }
}