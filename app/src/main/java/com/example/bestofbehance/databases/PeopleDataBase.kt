package com.example.bestofbehance.databases

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bestofbehance.binding.PeopleBinding

private const val DB_NAME = "PeopleData.db"

@Database(entities = [PeopleBinding::class], version = 3)
abstract class PeopleDataBase : RoomDatabase() {

    abstract fun getPeopleDao(): PeopleDao


    class Migration1To2 : Migration(1, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE PeopleData_new (id TEXT, avatar TEXT, name TEXT, post TEXT, views TEXT, appreciations TEXT, followers TEXT, following TEXT, added TEXT, PRIMARY KEY(id))")
            database.execSQL("INSERT INTO PeopleData_new (id, avatar, name, post, views, appreciations, followers, following, added) SELECT id, avatar, name, post, views, appreciations, followers, following, added FROM PeopleData")
            database.execSQL("DROP TABLE PeopleData")
            database.execSQL("ALTER TABLE PeopleData_new RENAME TO PeopleData")
        }
    }

    class Migration2To3 : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE PeopleData ADD COLUMN username TEXT")
        }
    }

    companion object {
        @JvmField
        val migration_1_2 = Migration1To2()
        val migration_2_3 = Migration2To3()
        var INSTANCE: PeopleDataBase? = null

        fun getDatabase(context: Context): PeopleDataBase? {
            if (INSTANCE == null) {
                synchronized(PeopleDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PeopleDataBase::class.java,
                            DB_NAME
                        ).addMigrations(
                            migration_1_2,
                            migration_2_3
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE
        }
    }

}
