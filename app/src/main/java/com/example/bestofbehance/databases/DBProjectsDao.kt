package com.example.bestofbehance.databases

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_APPRECIATIONS
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_AVATAR
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_BIGIMAGE
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_COMMENTS
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_DATE
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_ID
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_ARTISTNAME
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_ARTNAME
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_USERNAME
import com.example.bestofbehance.databases.DBProjects.Companion.KEY_VIEWS
import com.example.bestofbehance.databases.DBProjects.Companion.TABLE_CARDS
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


object DBProjectsDao {

    val values = ContentValues()
    init {
        Timber.plant(Timber.DebugTree())
    }

    fun add(context: Context, binding: CardBinding) {
        val database = DBProjects(context).writableDatabase
        values.put(KEY_ID, binding.id)
        values.put(KEY_BIGIMAGE, binding.bigImage)
        values.put(KEY_AVATAR, binding.avatar)
        values.put(KEY_ARTISTNAME, binding.artistName)
        values.put(KEY_ARTNAME, binding.artName)
        values.put(KEY_VIEWS, binding.views)
        values.put(KEY_APPRECIATIONS, binding.appreciations)
        values.put(KEY_COMMENTS, binding.comments)
        values.put(KEY_USERNAME, binding.username)
        values.put(KEY_DATE, getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"))

        database.insert(TABLE_CARDS, null, values)
        Timber.d( "Successful added: ID =  ${binding.id}")
        database.close()
    }

    @SuppressLint("Recycle", "BinaryOperationInTimber")
    fun read(context: Context, myCallBack: (result: MutableList<CardBinding>) -> Unit) {
        val database = DBProjects(context).writableDatabase
        val list: MutableList<CardBinding> = mutableListOf()
        val cursor = database.rawQuery("SELECT * FROM $TABLE_CARDS ORDER BY $KEY_DATE DESC", null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val bigImageIndex = cursor.getColumnIndex(KEY_BIGIMAGE)
            val avatarIndex = cursor.getColumnIndex(KEY_AVATAR)
            val artistIndex = cursor.getColumnIndex(KEY_ARTISTNAME)
            val artIndex = cursor.getColumnIndex(KEY_ARTNAME)
            val viewsIndex = cursor.getColumnIndex(KEY_VIEWS)
            val appreciationsIndex = cursor.getColumnIndex(KEY_APPRECIATIONS)
            val commentsIndex = cursor.getColumnIndex(KEY_COMMENTS)
            val dateIndex = cursor.getColumnIndex(KEY_DATE)
            val usernameIndex = cursor.getColumnIndex(KEY_USERNAME)
            do {
                Timber.d(
                    "ID = " + cursor.getInt(idIndex) +
                            ", bigImage = " + cursor.getString(bigImageIndex) +
                            ", avatar = " + cursor.getString(avatarIndex) +
                            ", artistName = " + cursor.getString(artistIndex) +
                            ", artName = " + cursor.getString(artIndex) +
                            ", views = " + cursor.getString(viewsIndex) +
                            ", appreciations = " + cursor.getString(appreciationsIndex) +
                            ", comments = " + cursor.getString(commentsIndex) +
                            ", username = " + cursor.getString(usernameIndex) +
                            ", date = " + cursor.getString(dateIndex)
                )
                list.add(
                    CardBinding(
                        cursor.getInt(idIndex),
                        cursor.getString(bigImageIndex),
                        cursor.getString(avatarIndex),
                        cursor.getString(artistIndex),
                        cursor.getString(artIndex),
                        cursor.getString(viewsIndex),
                        cursor.getString(appreciationsIndex),
                        cursor.getString(commentsIndex),
                        cursor.getString(usernameIndex),
                        null

                    )
                )

            } while (cursor.moveToNext())
        } else
            Timber.d( "0 rows")
        cursor.close()
        database.close()
        myCallBack.invoke(list)
    }

    fun clear(context: Context) {
        val database = DBProjects(context).writableDatabase
        database.delete(TABLE_CARDS, null, null)
        database.close()
    }

    fun delete(context: Context, id: Int) {
        val database = DBProjects(context).writableDatabase
        database.execSQL("DELETE FROM $TABLE_CARDS WHERE $KEY_ID= '$id'")
        Timber.d( "Row deleted: ID = $id")
        database.close()
    }

    fun find(context: Context, id: Int): Int? {
        val database = DBProjects(context).writableDatabase
        var check: Int? = null
        val cursor = database.query(TABLE_CARDS, arrayOf(KEY_ID), null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            do {
                if (cursor.getInt(idIndex) == id) {
                    check = cursor.getInt(idIndex)
                    Timber.d( "Found a match: ID = $check")
                }

            } while (cursor.moveToNext() && check == null)
        }

        cursor.close()
        database.close()
        return check
    }

    fun close(context: Context) {
        DBProjects(context).writableDatabase?.close()
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun update(context: Context, binding: CardBinding) {
        val database = DBProjects(context).writableDatabase
        val id = binding.id
        val avatar = binding.avatar
        val bigImage = binding.bigImage
        val artistName = binding.artistName
        val artName = binding.artName
        val views = binding.views
        val appreciations = binding.appreciations
        val comments = binding.comments
        val username = binding.username

        database.execSQL("UPDATE $TABLE_CARDS SET $KEY_BIGIMAGE= '$bigImage', $KEY_AVATAR= '$avatar', $KEY_ARTISTNAME= '$artistName', $KEY_ARTNAME= '$artName', $KEY_VIEWS= '$views', $KEY_APPRECIATIONS= '$appreciations', $KEY_COMMENTS= '$comments', $KEY_USERNAME= '$username' WHERE $KEY_ID= '$id'")
        Timber.d("Row update: ID = $id")

        database.close()
    }

}