package com.example.bestofbehance.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.RoomDatabase
import androidx.room.Room
import com.example.bestofbehance.binding.ProjectsBinding

private const val DB_NAME = "ProjectsData.db"

@Database(entities = [ProjectsBinding::class], version = 4)
abstract class ProjectsDataBase : RoomDatabase() {

    abstract fun getProjectsDao(): ProjectsDao


    class Migration1To2 : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ProjectsData ADD COLUMN added TEXT DEFAULT ''")
        }
    }

    class Migration2To3 : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ProjectsData ADD COLUMN thumbnail TEXT DEFAULT ''")
        }
    }

    class Migration3To4 : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ProjectsData ADD COLUMN url TEXT DEFAULT ''")
        }
    }

    companion object {
        @JvmField
        var migration_1_2 = Migration1To2()
        var migration2to3 = Migration2To3()
        var migration3to4 = Migration3To4()
        var INSTANCE: ProjectsDataBase? = null

        fun getDatabase(context: Context): ProjectsDataBase? {
            if (INSTANCE == null) {
                synchronized(ProjectsDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ProjectsDataBase::class.java,
                            DB_NAME
                        ).addMigrations(
                            migration_1_2,
                            migration2to3,
                            migration3to4
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE
        }
    }

}
