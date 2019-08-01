/*
package com.example.bestofbehance.paging

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.PositionalDataSource
import com.example.bestofbehance.gson.CardBinding


internal class CardPositionalDataSource : PositionalDataSource<CardBinding>() {


    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<CardBinding>
    ) {
        Log.d(
            TAG, "loadInitial, requestedStartPosition = " + params.requestedStartPosition +
                    ", requestedLoadSize = " + params.requestedLoadSize
        )
        val result = storage.getInitialData(params.requestedStartPosition, params.requestedLoadSize)
        callback.onResult(result.data, result.position)
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<CardBinding>
    ) {
        Log.d(TAG, "loadRange, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize)
        val result = storage.getData(params.startPosition, params.loadSize)
        callback.onResult(result)
    }

}*/
