package com.example.bestofbehance.databases

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.bestofbehance.binding.CardBinding
import timber.log.Timber

object CacheDBMain {

    val values = ContentValues()
    init {
        Timber.plant(Timber.DebugTree())
    }

    fun add(context: Context, binding: CardBinding) {
        val database = DBCache(context).writableDatabase
        values.put(DBCache.KEY_ID, binding.id)
        values.put(DBCache.KEY_BIGIMAGE, binding.bigImage)
        values.put(DBCache.KEY_AVATAR, binding.avatar)
        values.put(DBCache.KEY_ARTISTNAME, binding.artistName)
        values.put(DBCache.KEY_ARTNAME, binding.artName)
        values.put(DBCache.KEY_VIEWS, binding.views)
        values.put(DBCache.KEY_APPRECIATIONS, binding.appreciations)
        values.put(DBCache.KEY_COMMENTS, binding.comments)
        values.put(DBCache.KEY_USERNAME, binding.username)

        database.insert(DBCache.TABLE_CACHE, null, values)
        Timber.d( "Successful added: ID =  ${binding.id}")
        database.close()
    }

    @SuppressLint("Recycle", "BinaryOperationInTimber")
    fun read(context: Context, myCallBack: (result: MutableList<CardBinding>) -> Unit) {
        val database = DBCache(context).writableDatabase
        val list: MutableList<CardBinding> = mutableListOf()
        val cursor = database.rawQuery("SELECT * FROM ${DBCache.TABLE_CACHE}", null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(DBCache.KEY_ID)
            val bigImageIndex = cursor.getColumnIndex(DBCache.KEY_BIGIMAGE)
            val avatarIndex = cursor.getColumnIndex(DBCache.KEY_AVATAR)
            val artistIndex = cursor.getColumnIndex(DBCache.KEY_ARTISTNAME)
            val artIndex = cursor.getColumnIndex(DBCache.KEY_ARTNAME)
            val viewsIndex = cursor.getColumnIndex(DBCache.KEY_VIEWS)
            val appreciationsIndex = cursor.getColumnIndex(DBCache.KEY_APPRECIATIONS)
            val commentsIndex = cursor.getColumnIndex(DBCache.KEY_COMMENTS)
            val usernameIndex = cursor.getColumnIndex(DBCache.KEY_USERNAME)
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
                            ", username = " + cursor.getString(usernameIndex)
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
                        cursor.getString(usernameIndex)

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
        val database = DBCache(context).writableDatabase
        database.delete(DBCache.TABLE_CACHE, null, null)
        /*database.execSQL("DELETE FROM ${DBCache.TABLE_CACHE}")
        database.execSQL("VACUUM")*/
        database.close()
    }

    fun delete(context: Context, id: Int) {
        val database = DBCache(context).writableDatabase
        database.execSQL("DELETE FROM ${DBCache.TABLE_CACHE} WHERE ${DBCache.KEY_ID}= '$id'")
        Timber.d( "Row deleted: ID = $id")
        database.close()
    }

    fun find(context: Context, id: Int): Int? {
        val database = DBCache(context).writableDatabase
        var check: Int? = null
        val cursor = database.query(DBCache.TABLE_CACHE, arrayOf(DBCache.KEY_ID), null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(DBCache.KEY_ID)
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
        DBCache(context).writableDatabase?.close()
    }

    fun update(context: Context, binding: CardBinding) {
        val database = DBCache(context).writableDatabase
        val id = binding.id
        val avatar = binding.avatar
        val bigImage = binding.bigImage
        val artistName = binding.artistName
        val artName = binding.artName
        val views = binding.views
        val appreciations = binding.appreciations
        val comments = binding.comments
        val username = binding.username

        database.execSQL("UPDATE ${DBCache.TABLE_CACHE} SET ${DBCache.KEY_BIGIMAGE}= '$bigImage', ${DBCache.KEY_AVATAR}= '$avatar', ${DBCache.KEY_ARTISTNAME}= '$artistName', ${DBCache.KEY_ARTNAME}= '$artName', ${DBCache.KEY_VIEWS}= '$views', ${DBCache.KEY_APPRECIATIONS}= '$appreciations', ${DBCache.KEY_COMMENTS}= '$comments', ${DBCache.KEY_USERNAME}= '$username' WHERE ${DBCache.KEY_ID}= '$id'")
        Timber.d("Row update: ID = $id")

        database.close()
    }

}