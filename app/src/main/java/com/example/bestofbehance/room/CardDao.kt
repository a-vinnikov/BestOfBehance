package com.example.bestofbehance.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDao {

    @get:Query("SELECT * from CardData ORDER BY id ASC")
    val all: LiveData<List<CardData>>

    @Query("SELECT * FROM CardData WHERE id = :id")
    fun getById(id: Int): CardData

    @Insert
    fun insert(cardBinding: CardData)

    @Update
    fun update(cardBinding: CardData)

    @Query("DELETE FROM CardData")
    fun deleteAll()

}