package com.example.bestofbehance.paging

import android.os.Handler
import androidx.paging.ItemKeyedDataSource
import com.example.bestofbehance.binding.CardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import android.os.Looper
import java.util.concurrent.Executor


class ItemDataSource(
    private val asyncItems: Deferred<MutableList<CardBinding>>,
    private val onEmptyAction: () -> Unit
) : ItemKeyedDataSource<Int, CardBinding>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val items = mutableListOf<CardBinding>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<CardBinding>
    ) {
        launch {
            val items = asyncItems.await()
            if (items.isEmpty()) {
                onEmptyAction()
            }

            this@ItemDataSource.items.clear()
            this@ItemDataSource.items.addAll(items)

            val subList =
                getSubList(items, params.requestedInitialKey ?: 0, params.requestedLoadSize)
            callback.onResult(subList)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<CardBinding>) {
        val index = params.key + 1

        val subList = getSubList(items, index, params.requestedLoadSize)
        callback.onResult(subList)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<CardBinding>) {
        val index = params.key - 1

        val subList = getSubList(items, index, params.requestedLoadSize, true)
        callback.onResult(subList)
    }

    override fun getKey(item: CardBinding): Int =
        items.indexOfFirst { it.id == item.id }

    private fun getSubList(items: List<CardBinding>, index: Int, requestedLoadSize: Int, before: Boolean = false): List<CardBinding> {

        val fromIndex = inRange(index, 0, items.size)
        val toIndex = when {
            before -> inRange(fromIndex - requestedLoadSize, 0, items.size)
            else -> inRange(fromIndex + requestedLoadSize, 0, items.size)
        }

        return if (fromIndex == toIndex) { // Can only mean list is empty
            emptyList()
        } else {
            items.subList(fromIndex, toIndex)
        }
    }

    private fun inRange(position: Int, start: Int, end: Int): Int {
        if (position < start) return start
        if (position > end) return end
        return position
    }

}