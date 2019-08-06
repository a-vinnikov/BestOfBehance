package com.example.bestofbehance.room

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.bestofbehance.gson.CardBinding



object DBMain {

    val values = ContentValues()

    fun add(binding: CardBinding, context: Context){
        val database = DBHelper(context).writableDatabase
        values.put(DBHelper.KEY_ID, binding.id)
        values.put(DBHelper.KEY_BIGIMAGE, binding.bigImage)
        values.put(DBHelper.KEY_AVATAR, binding.avatar)
        values.put(DBHelper.KEY_NAME, binding.name)
        values.put(DBHelper.KEY_POST, binding.post)
        values.put(DBHelper.KEY_VIEWS, binding.views)
        values.put(DBHelper.KEY_APPRECIATIONS, binding.appreciations)
        values.put(DBHelper.KEY_COMMENTS, binding.comments)

        database.insert(DBHelper.TABLE_CARDS, null, values)
    }

    fun read(context: Context){
        val database = DBHelper(context).writableDatabase
        val cursor = database.query(DBHelper.TABLE_CARDS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(DBHelper.KEY_ID)
            val bigImageIndex = cursor.getColumnIndex(DBHelper.KEY_BIGIMAGE)
            val avatarIndex = cursor.getColumnIndex(DBHelper.KEY_AVATAR)
            val nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME)
            val postIndex = cursor.getColumnIndex(DBHelper.KEY_POST)
            val viewsIndex = cursor.getColumnIndex(DBHelper.KEY_VIEWS)
            val appreciationsIndex = cursor.getColumnIndex(DBHelper.KEY_APPRECIATIONS)
            val commentsIndex = cursor.getColumnIndex(DBHelper.KEY_COMMENTS)
            do {
                Log.d(
                    "mLog", "ID = " + cursor.getInt(idIndex) +
                            ", bigImage = " + cursor.getString(bigImageIndex) +
                            ", avatar = " + cursor.getString(avatarIndex) +
                            ", name = " + cursor.getString(nameIndex) +
                            ", post = " + cursor.getString(postIndex) +
                            ", views = " + cursor.getString(viewsIndex) +
                            ", appreciations = " + cursor.getString(appreciationsIndex) +
                            ", comments = " + cursor.getString(commentsIndex)
                )
            } while (cursor.moveToNext())
        } else
            Log.d("mLog", "0 rows")

        cursor.close()
    }

    fun clear(context: Context){
        val database = DBHelper(context).writableDatabase
        database.delete(DBHelper.TABLE_CARDS, null, null)
    }

}