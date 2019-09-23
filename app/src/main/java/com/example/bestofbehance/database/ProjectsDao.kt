package com.example.bestofbehance.database

import androidx.room.*
import com.example.bestofbehance.binding.ProjectsBinding

@Dao
interface ProjectsDao {

    @get:Query("SELECT * from ProjectsData ORDER BY added DESC")
    val all: MutableList<ProjectsBinding>

    @Query("SELECT * FROM ProjectsData WHERE id = :id")
    fun getById(id: Int): ProjectsBinding

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cardBinding: ProjectsBinding)

    @Query("DELETE FROM ProjectsData WHERE id = :id")
    fun deleteById(id: Int)

}