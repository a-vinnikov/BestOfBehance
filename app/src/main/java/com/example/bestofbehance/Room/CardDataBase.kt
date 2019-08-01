package com.example.bestofbehance.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CardData::class], version = 1)
abstract class CardDataBase : RoomDatabase() {

    abstract fun CardDataDao(): CardDao

    companion object {
        private var INSTANCE: CardDataBase? = null

        fun getInstance(context: Context): CardDataBase? {
            if (INSTANCE == null) {
                synchronized(CardDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        CardDataBase::class.java, "card.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}