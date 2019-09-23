package com.example.bestofbehance.database

import androidx.room.*
import com.example.bestofbehance.binding.CardBinding

@Dao
interface CardDao {

    @get:Query("SELECT * from CardData ORDER BY published DESC")
    val all: MutableList<CardBinding>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cardBinding: CardBinding)

    @Query("DELETE FROM CardData")
    fun deleteAll()

}