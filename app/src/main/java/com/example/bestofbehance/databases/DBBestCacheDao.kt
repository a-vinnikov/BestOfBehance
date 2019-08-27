package com.example.bestofbehance.databases

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.UnixDateConverter
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_APPRECIATIONS
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_ARTISTNAME
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_ARTNAME
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_AVATAR
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_BIGIMAGE
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_COMMENTS
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_ID
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_PUBLISHED
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_USERNAME
import com.example.bestofbehance.databases.DBBestCache.Companion.KEY_VIEWS
import com.example.bestofbehance.databases.DBBestCache.Companion.TABLE_CACHE
import timber.log.Timber

object DBBestCacheDao {

    val values = ContentValues()

    fun add(context: Context, binding: CardBinding) {
        val database = DBBestCache(context).writableDatabase
        values.put(KEY_ID, binding.id)
        values.put(KEY_BIGIMAGE, binding.bigImage)
        values.put(KEY_AVATAR, binding.avatar)
        values.put(KEY_ARTISTNAME, binding.artistName)
        values.put(KEY_ARTNAME, binding.artName)
        values.put(KEY_VIEWS, binding.views)
        values.put(KEY_APPRECIATIONS, binding.appreciations)
        values.put(KEY_COMMENTS, binding.comments)
        values.put(KEY_USERNAME, binding.username)
        values.put(KEY_PUBLISHED, binding.published)

        database.insert(TABLE_CACHE, null, values)
        Timber.d( "Successful added: ID =  ${binding.id}")
        database.close()
    }

    @SuppressLint("BinaryOperationInTimber")
    fun read(context: Context, myCallBack: (result: MutableList<CardBinding>) -> Unit) {
        val database = DBBestCache(context).writableDatabase
        val list: MutableList<CardBinding> = mutableListOf()
        val cursor = database.rawQuery("SELECT * FROM $TABLE_CACHE ORDER BY $KEY_PUBLISHED DESC", null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val bigImageIndex = cursor.getColumnIndex(KEY_BIGIMAGE)
            val avatarIndex = cursor.getColumnIndex(KEY_AVATAR)
            val artistIndex = cursor.getColumnIndex(KEY_ARTISTNAME)
            val artIndex = cursor.getColumnIndex(KEY_ARTNAME)
            val viewsIndex = cursor.getColumnIndex(KEY_VIEWS)
            val appreciationsIndex = cursor.getColumnIndex(KEY_APPRECIATIONS)
            val commentsIndex = cursor.getColumnIndex(KEY_COMMENTS)
            val usernameIndex = cursor.getColumnIndex(KEY_USERNAME)
            val publishedIndex = cursor.getColumnIndex(KEY_PUBLISHED)
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
                            ", date = " + UnixDateConverter.convert(cursor.getInt(publishedIndex).toString())
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
                        cursor.getInt(publishedIndex)
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
        val database = DBBestCache(context).writableDatabase
        //database.delete(TABLE_CACHE, null, null)
        database.execSQL("DELETE FROM $TABLE_CACHE")
        database.execSQL("VACUUM")
        database.close()
    }

}