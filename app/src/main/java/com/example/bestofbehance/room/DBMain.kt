package com.example.bestofbehance.room

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.room.DBHelper.Companion.KEY_APPRECIATIONS
import com.example.bestofbehance.room.DBHelper.Companion.KEY_AVATAR
import com.example.bestofbehance.room.DBHelper.Companion.KEY_BIGIMAGE
import com.example.bestofbehance.room.DBHelper.Companion.KEY_COMMENTS
import com.example.bestofbehance.room.DBHelper.Companion.KEY_DATE
import com.example.bestofbehance.room.DBHelper.Companion.KEY_ID
import com.example.bestofbehance.room.DBHelper.Companion.KEY_NAME
import com.example.bestofbehance.room.DBHelper.Companion.KEY_POST
import com.example.bestofbehance.room.DBHelper.Companion.KEY_VIEWS
import com.example.bestofbehance.room.DBHelper.Companion.TABLE_CARDS
import java.text.SimpleDateFormat
import java.util.*


object DBMain {

    val values = ContentValues()

    fun add(context: Context, binding: CardBinding) {
        val database = DBHelper(context).writableDatabase
        values.put(DBHelper.KEY_ID, binding.id)
        values.put(DBHelper.KEY_BIGIMAGE, binding.bigImage)
        values.put(DBHelper.KEY_AVATAR, binding.avatar)
        values.put(DBHelper.KEY_NAME, binding.name)
        values.put(DBHelper.KEY_POST, binding.post)
        values.put(DBHelper.KEY_VIEWS, binding.views)
        values.put(DBHelper.KEY_APPRECIATIONS, binding.appreciations)
        values.put(DBHelper.KEY_COMMENTS, binding.comments)
        values.put(DBHelper.KEY_DATE, getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"))

        database.insert(TABLE_CARDS, null, values)

        Log.d("Increase", "Successful added: ID =  ${binding.id}")
        database.close()
    }

    @SuppressLint("Recycle")
    fun read(context: Context, myCallBack: (result: MutableList<CardBinding>) -> Unit) {
        val database = DBHelper(context).writableDatabase
        val list: MutableList<CardBinding> = mutableListOf()
        val cursor = database.rawQuery("SELECT * FROM $TABLE_CARDS ORDER BY $KEY_DATE DESC", null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(DBHelper.KEY_ID)
            val bigImageIndex = cursor.getColumnIndex(DBHelper.KEY_BIGIMAGE)
            val avatarIndex = cursor.getColumnIndex(DBHelper.KEY_AVATAR)
            val nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME)
            val postIndex = cursor.getColumnIndex(DBHelper.KEY_POST)
            val viewsIndex = cursor.getColumnIndex(DBHelper.KEY_VIEWS)
            val appreciationsIndex = cursor.getColumnIndex(DBHelper.KEY_APPRECIATIONS)
            val commentsIndex = cursor.getColumnIndex(DBHelper.KEY_COMMENTS)
            val dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE)
            do {
                Log.d(
                    "Read", "ID = " + cursor.getInt(idIndex) +
                            ", bigImage = " + cursor.getString(bigImageIndex) +
                            ", avatar = " + cursor.getString(avatarIndex) +
                            ", name = " + cursor.getString(nameIndex) +
                            ", post = " + cursor.getString(postIndex) +
                            ", views = " + cursor.getString(viewsIndex) +
                            ", appreciations = " + cursor.getString(appreciationsIndex) +
                            ", comments = " + cursor.getString(commentsIndex) +
                            ", date = " + cursor.getString(dateIndex)
                )
                list.add(
                    CardBinding(
                        cursor.getInt(idIndex),
                        cursor.getString(bigImageIndex),
                        cursor.getString(avatarIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(postIndex),
                        cursor.getString(viewsIndex),
                        cursor.getString(appreciationsIndex),
                        cursor.getString(commentsIndex),
                        null

                    )
                )

            } while (cursor.moveToNext())
        } else
            Log.d("Read", "0 rows")
        cursor.close()
        database.close()
        myCallBack.invoke(list)
    }

    fun clear(context: Context) {
        val database = DBHelper(context).writableDatabase
        database.delete(TABLE_CARDS, null, null)
        database.close()
    }

    fun delete(context: Context, id: Int) {
        val database = DBHelper(context).writableDatabase
        database.execSQL("DELETE FROM $TABLE_CARDS WHERE $KEY_ID= '$id'")
        Log.d("Delete", "Row deleted: ID = $id")
        database.close()
    }

    fun find(context: Context, id: Int): Int? {
        val database = DBHelper(context).writableDatabase
        var check: Int? = null
        val cursor = database.query(TABLE_CARDS, arrayOf(KEY_ID), null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            do {
                if (cursor.getInt(idIndex) == id) {
                    check = cursor.getInt(idIndex)
                    Log.d("Found", "Found a match: ID = $check")
                }

            } while (cursor.moveToNext() && check == null)
        } else
            Log.d("Found", "No matches found")

        cursor.close()
        database.close()
        return check
    }

    fun close(context: Context) {
        DBHelper(context).writableDatabase?.close()
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun update(context: Context, binding: CardBinding) {
        val database = DBHelper(context).writableDatabase
        val id = binding.id
        val avatar = binding.avatar
        val bigImage = binding.bigImage
        val name = binding.name
        val post = binding.post
        val views = binding.views
        val appreciations = binding.appreciations
        val comments = binding.comments

        database.execSQL("UPDATE $TABLE_CARDS SET $KEY_BIGIMAGE= '$bigImage', $KEY_AVATAR= '$avatar', $KEY_NAME= '$name', $KEY_POST= '$post', $KEY_VIEWS= '$views', $KEY_APPRECIATIONS= '$appreciations', $KEY_COMMENTS= '$comments' WHERE $KEY_ID= '$id'")
        Log.d("Update", "Row update: ID = $id")

        database.close()
    }

}