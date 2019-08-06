package com.example.bestofbehance.room

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bestofbehance.gson.CardBinding
import androidx.room.RoomDatabase
import androidx.room.Room
import java.util.concurrent.Executors


@Database(entities = [CardBinding::class], version = 3)
abstract class CardDataBase : RoomDatabase() {

    abstract fun cardDao(): CardDao


    class Migration1To2: Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }
    class Migration2To3: Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }

    companion object {
        @JvmField
        val migration_1_2 = Migration1To2()
        val migration_2_3 = Migration2To3()


        private var INSTANCE: CardDataBase? = null
        private val DB_NAME = "movies.db"
        fun getDatabase(context: Context): CardDataBase? {
            if (INSTANCE == null) {
                synchronized(CardDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            CardDataBase::class.java, DB_NAME
                        ).allowMainThreadQueries().addMigrations(migration_1_2, migration_2_3).allowMainThreadQueries()
                            .addCallback(callback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }
        private val callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                ioThread {
                    println("Done")
                    /*for (i in 0 until database!!.size) {
                    INSTANCE?.cardDao()?.insert(database!![i])
                    println(INSTANCE?.cardDao()?.all?.value?.get(i)?.id)
                }*/ }

            }
        }
        private val IO_EXECUTOR = Executors.newSingleThreadExecutor()
        fun ioThread(f : () -> Unit) {
            IO_EXECUTOR.execute(f)
        }

    }


}
