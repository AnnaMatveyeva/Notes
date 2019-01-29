package matveyeva.notes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import matveyeva.notes.R

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE NOTES(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "NOTETEXT TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE)
        onCreate(db)
    }

    fun deleteTask(text:String): Boolean {
        val db = this.writableDatabase
        val success = db.delete(DATA_TABLE, "NOTETEXT = ?", arrayOf(text)).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }
    fun insertStuffWithId(db : SQLiteDatabase, tableName:String,id:Int,noteText: String){
        var cv:ContentValues = ContentValues()
        cv.put("NOTETEXT",noteText)
        db.insert(tableName,null,cv)
    }
    fun insertStuff(db : SQLiteDatabase, tableName:String,noteText: String){
        var cv:ContentValues = ContentValues()
        cv.put("NOTETEXT",noteText)
        db.insert(tableName,null,cv)
    }


    companion object {

         val DATABASE_VERSION = 1
         val DATABASE_NAME = "NOTESApp"
         val DATA_TABLE = "NOTES"

    }
}
