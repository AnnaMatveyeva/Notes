package matveyeva.notes


import android.app.Activity
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.main_card_view.*
import matveyeva.notes.R.id.editNote

const val INTENT_NOTE_ID = "note_id"

class noteActivity : AppCompatActivity() {

    lateinit var db: SQLiteDatabase
    lateinit var cursor: Cursor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val textN = intent.getStringExtra(INTENT_NOTE_ID)
        if(textN!=null){
            try{
                val helper = DatabaseHelper(this)
                db = helper.readableDatabase
                cursor = db.query("NOTES",arrayOf("_id","NOTETEXT"),"NOTETEXT = ?",arrayOf(textN),null,null,null)
                cursor.moveToFirst()
                editNote.append(cursor.getString(1))
                Log.v("noteTag",cursor.getString(1))
                cursor.close()
                db.close()
            }catch(ex:SQLiteException){
                Toast.makeText(this,"try again",Toast.LENGTH_SHORT)
            }
        }


        fabDone.setOnClickListener{ view ->
            try{
                val helper  = DatabaseHelper(this)
                db = helper.writableDatabase
                if(textN!=null){
                    var cv = ContentValues()
                    cv.put("NOTETEXT",editNote.text.toString())
                    db!!.update("NOTES", cv, "NOTETEXT = ?", arrayOf(textN))
                }else helper.insertStuff(db,"NOTES", editNote.text.toString())
                db.close()
                onBackPressed()
            }catch (ex:SQLiteException){
                Toast.makeText(this,"try again",Toast.LENGTH_SHORT)
            }
        }

    }


}
