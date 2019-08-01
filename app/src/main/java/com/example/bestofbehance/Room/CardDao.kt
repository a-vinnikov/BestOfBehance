package com.example.bestofbehance.Room

import androidx.room.*

@Dao
interface CardDao {

    @get:Query("SELECT * FROM CardData")
    val all: List<CardData>

    @Query("SELECT * FROM CardData WHERE id = :id")
    fun getById(id: Long): CardData

    @Insert
    fun insert(cardBinding: CardData)

    @Update
    fun update(cardBinding: CardData)

    @Delete
    fun delete(cardBinding: CardData)

}