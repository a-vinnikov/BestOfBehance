package com.example.bestofbehance.databases.forRoom

import androidx.room.*
import com.example.bestofbehance.binding.CardBinding

@Dao
interface CardDao {

    @get:Query("SELECT * from CardData ORDER BY published DESC")
    val all: MutableList<CardBinding>

/*    @Query("SELECT * FROM CardData WHERE id = :id")
    fun getById(id: Int): CardBinding

    @Update
    fun update(cardBinding: CardBinding)*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cardBinding: CardBinding)


    @Query("DELETE FROM CardData")
    fun deleteAll()

}