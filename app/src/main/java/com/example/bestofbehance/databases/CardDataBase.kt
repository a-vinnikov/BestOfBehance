package com.example.bestofbehance.databases

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bestofbehance.binding.CardBinding
import androidx.room.RoomDatabase
import androidx.room.Room

private const val DB_NAME = "CardData.db"

@Database(entities = [CardBinding::class], version = 6)
abstract class CardDataBase : RoomDatabase() {

    abstract fun getCardDao(): CardDao


    class Migration1To4 : Migration(1, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE CardData_new (id text, bigImage text, avatar text,artistName text,artName text,views text,appreciations text,comments text,username text,published integer, PRIMARY KEY(id))")
            database.execSQL("INSERT INTO CardData_new SELECT bigImage, avatar, artistName, artName, views, appreciations, comments, username, published, id FROM CardData")
            database.execSQL("DROP TABLE CardData")
            database.execSQL("ALTER TABLE CardData_new RENAME TO CardData")
        }
    }

    class Migration4to5: Migration(4, 5){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE CardData ADD COLUMN thumbnail TEXT DEFAULT ''")
        }
    }

    class Migration5to6: Migration(5, 6){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE CardData ADD COLUMN url TEXT DEFAULT ''")
        }
    }

    companion object {
        @JvmField
        val migration_1_4 = Migration1To4()
        val migration_4_5 = Migration4to5()
        val migration_5_6 = Migration5to6()
        private var INSTANCE: CardDataBase? = null

        fun getDatabase(context: Context): CardDataBase? {
            if (INSTANCE == null) {
                synchronized(CardDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            CardDataBase::class.java,
                            DB_NAME
                        ).addMigrations(
                            migration_1_4,
                            migration_4_5,
                            migration_5_6
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE
        }
    }

}
