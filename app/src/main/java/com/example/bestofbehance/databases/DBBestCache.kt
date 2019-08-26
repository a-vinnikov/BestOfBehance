package com.example.bestofbehance.databases


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBBestCache(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table $TABLE_CACHE($KEY_ID integer primary key,$KEY_BIGIMAGE text,$KEY_AVATAR text,$KEY_ARTISTNAME text,$KEY_ARTNAME text,$KEY_VIEWS text,$KEY_APPRECIATIONS text,$KEY_COMMENTS text,$KEY_USERNAME text,$KEY_PUBLISHED integer)"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists $TABLE_CACHE")
        onCreate(db)
    }

    companion object {
        val DATABASE_VERSION = 6
        val DATABASE_NAME = "cacheDB"
        val TABLE_CACHE = "cache"

        val KEY_ID = "_id"
        val KEY_BIGIMAGE = "bigImage"
        val KEY_AVATAR = "avatar"
        val KEY_ARTISTNAME = "artistName"
        val KEY_ARTNAME = "artName"
        val KEY_VIEWS = "views"
        val KEY_APPRECIATIONS = "appreciations"
        val KEY_COMMENTS = "comments"
        val KEY_USERNAME = "username"
        val KEY_PUBLISHED = "published"
    }
}