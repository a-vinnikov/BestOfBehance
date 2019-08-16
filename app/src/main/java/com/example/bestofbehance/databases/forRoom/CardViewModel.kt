package com.example.bestofbehance.databases.forRoom/*
package com.example.bestofbehance.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bestofbehance.room.CardData
import com.example.bestofbehance.room.CardDataBase
import com.example.bestofbehance.room.CardRepository
import com.example.bestofbehance.viewModels.ParseForVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CardRepository
    val allData: MutableLiveData<MutableList<CardData>>

    init {
        val cardDao = CardDataBase.getDatabase(application).cardDao()
        repository = CardRepository(cardDao)
        allData = repository.allData
    }

    fun insertData(card: CardData) = viewModelScope.launch(Dispatchers.IO) {
        //repository.insertData(card)
    }
}*/
