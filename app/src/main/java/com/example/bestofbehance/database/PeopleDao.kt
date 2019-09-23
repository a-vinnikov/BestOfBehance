package com.example.bestofbehance.database

import androidx.room.*
import com.example.bestofbehance.binding.PeopleBinding

@Dao
interface PeopleDao {

    @get:Query("SELECT * from PeopleData ORDER BY added DESC")
    val all: MutableList<PeopleBinding>

    @Query("SELECT * FROM PeopleData WHERE username = :username")
    fun getByUsername(username: String): PeopleBinding

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cardBinding: PeopleBinding)

    @Query("DELETE FROM PeopleData WHERE username = :username")
    fun deleteByUsername(username: String)

}