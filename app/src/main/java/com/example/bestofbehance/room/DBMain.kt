/*
package com.example.bestofbehance.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.widget.EditText





class DBMain(){

    var dbHelper: DBHelper? = null

    var name = etName.getText().toString()
    var email = etEmail.getText().toString()

    var database = dbHelper.WritableDatabase()

    var contentValues = ContentValues()

    fun add(){
        contentValues.put(DBHelper.KEY_NAME, name);
        contentValues.put(DBHelper.KEY_MAIL, email);

        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues)
    }

    fun read(){
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
    }

    fun clear(){
        database.delete(DBHelper.TABLE_CONTACTS, null, null)
    }

}*/
