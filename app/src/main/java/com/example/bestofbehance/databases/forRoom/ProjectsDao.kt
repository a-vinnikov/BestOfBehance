package com.example.bestofbehance.databases.forRoom

import androidx.room.*
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.ProjectsBinding

@Dao
interface ProjectsDao {

    @get:Query("SELECT * from ProjectsData ORDER BY published DESC")
    val all: MutableList<ProjectsBinding>

    @Query("SELECT * FROM ProjectsData WHERE id = :id")
    fun getById(id: Int): ProjectsBinding

    @Update
    fun update(cardBinding: ProjectsBinding)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cardBinding: ProjectsBinding)

    @Query("DELETE FROM ProjectsData")
    fun deleteAll()

    @Query("DELETE FROM ProjectsData")
    fun deleteAllProjects()

    @Query("DELETE FROM ProjectsData WHERE id = :id")
    fun deleteById(id: Int)

}