package matveyeva.notes

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import android.support.v7.widget.helper.ItemTouchHelper
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.main_card_view.*
import kotlinx.android.synthetic.main.main_card_view.view.*

/**
 * Extensions for simpler launching of Activities
 */

class Main : AppCompatActivity(),NoteListener {

    lateinit var db: SQLiteDatabase
    lateinit var cursor: Cursor
    val users = ArrayList<String>()
    lateinit var adapter:RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            val intent = Intent(this, noteActivity::class.java)
            startActivity(intent)
        }
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                try{
                    val helper = DatabaseHelper(this@Main)
                    helper.deleteTask(viewHolder.itemView.textNote.text.toString())
                }catch(ex:SQLiteException){
                    Toast.makeText(this@Main,"Try again",Toast.LENGTH_SHORT)
                }
                val adapter = recyclerView.adapter as RecyclerAdapter
//                adapter.removeAt(viewHolder.adapterPosition)
//                users.remove(viewHolder.adapterPosition.toString())
                onResume()

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)


    }

    override fun noteSelected(textNote: String) {
//        val id = position+1
        val intent = Intent(this@Main, noteActivity::class.java)
        intent.putExtra(INTENT_NOTE_ID,textNote)
        startActivity(intent)
    }


    fun getNotes(){
        users.clear()
        try{
            var helper: SQLiteOpenHelper = DatabaseHelper(this@Main)
            db = helper.readableDatabase
            cursor = db.query("NOTES", arrayOf("_id","NOTETEXT"),null,null,null,null,null)

            cursor.moveToFirst()
            while (cursor.isAfterLast == false)
            {
                users.add(cursor.getString(1))
                cursor.moveToNext()
            }
            cursor.close()
            db.close()
        } catch (ex: SQLiteException) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT)
        }


        val layoutManager :RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(users)
        adapter.addListener(this)
        recyclerView.adapter = adapter
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }



    override fun onResume() {
        super.onResume()
        getNotes()

    }
}
