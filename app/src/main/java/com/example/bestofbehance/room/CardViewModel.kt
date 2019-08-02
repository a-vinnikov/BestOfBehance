package com.example.bestofbehance.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bestofbehance.room.CardData
import com.example.bestofbehance.room.CardDataBase
import com.example.bestofbehance.room.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CardRepository
    val allData: LiveData<List<CardData>>

    init {
        val cardDao = CardDataBase.getDatabase(application).cardDao()
        repository = CardRepository(cardDao)
        allData = repository.allData
    }

    fun insert(card: CardData) = viewModelScope.launch(Dispatchers.IO) {
        //repository.insert(card)
    }
}