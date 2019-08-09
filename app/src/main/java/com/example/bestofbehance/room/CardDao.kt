package com.example.bestofbehance.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bestofbehance.binding.CardBinding

@Dao
interface CardDao {

    @get:Query("SELECT * from CardData ORDER BY id ASC")
    val all: LiveData<MutableList<CardBinding>>

    @Query("SELECT * FROM CardData WHERE id = :id")
    fun getById(id: Int): CardBinding

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cardBinding: CardBinding)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(cardBinding: CardBinding)

    @Query("DELETE FROM CardData")
    fun deleteAll()

}