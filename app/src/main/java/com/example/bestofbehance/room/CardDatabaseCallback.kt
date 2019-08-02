package com.example.bestofbehance.room

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.internal.JsonReaderInternalAccess.INSTANCE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private class WordDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        INSTANCE?.let {scope.launch(Dispatchers.IO) { //populateDatabase(CardDataBase.cardDao())
            }
        }
    }

    fun populateDatabase(cardDao: CardDao) {
        cardDao.deleteAll()

        /*var word = Word("Hello")
        wordDao.insert(word)
        word = Word("World!")
        wordDao.insert(word)*/
    }
}