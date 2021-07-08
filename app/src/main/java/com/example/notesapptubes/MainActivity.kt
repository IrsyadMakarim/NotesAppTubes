package com.example.notesapptubes

import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.view.*

class MainActivity : AppCompatActivity() {

    var listsNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Notes"

        loadQuery("%")

        var addBtn : FloatingActionButton = findViewById(R.id.addNodeBtn)
        var clndrBtn : FloatingActionButton = findViewById(R.id.calendarBtn)
        addBtn.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
        clndrBtn.setOnClickListener{
            startActivity(Intent(this, CalendarActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()

        loadQuery("%")
    }

    private fun loadQuery(title: String){
        var dbManager = DbManager(this)
        val projections = arrayOf("ID",
        "Title",
        "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listsNotes.clear()
        if (cursor.moveToFirst()){
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listsNotes.add(Note(ID, Title, Description))
            }while (cursor.moveToNext())
        }

        var myNotesAdapter = MyNotesAdapter(this, listsNotes)

        notesLv.adapter = myNotesAdapter

        val total = notesLv.count

        val mActionBar = supportActionBar
        if (mActionBar != null){
            mActionBar.subtitle = "You have $total note(s) in list..."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView

        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
               loadQuery("%" + p0 + "%")
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                loadQuery("%" + p0 + "%")
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null){
            when (item.itemId){
                R.id.action_settings -> {
                    startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                }
                R.id.logout -> {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter: BaseAdapter{
        var listNotesAdapter = ArrayList<Note>()
        var context: Context? = null

        constructor(context: Context, listNotesAdapter: ArrayList<Note>): super(){
            this.listNotesAdapter = listNotesAdapter
            this.context = context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.row, null)
            val myNote = listNotesAdapter[p0]
            myView.titleTv.text = myNote.nodeName
            myView.descTv.text = myNote.nodeDesc

            myView.deleteBtn.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeID.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQuery("%")
            }
            myView.editBtn.setOnClickListener {
                GoToUpdateFun(myNote)
            }
            myView.copyBtn.setOnClickListener {
                val title = myView.titleTv.text.toString()
                val desc = myView.descTv.text.toString()
                val s = title + "\n" + desc
                val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cb.text = s
                Toast.makeText(this@MainActivity, "Copied", Toast.LENGTH_SHORT).show()
            }
            myView.shareBtn.setOnClickListener {
                val title = myView.titleTv.text.toString()
                val desc = myView.descTv.text.toString()
                val s = title + "\n" + desc
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))
            }
            return  myView
        }

        override fun getItem(p0: Int): Any {
            return listNotesAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }
    }

    private fun GoToUpdateFun(myNote : Note){
        var intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("ID", myNote.nodeID)
        intent.putExtra("name", myNote.nodeName)
        intent.putExtra("desc", myNote.nodeDesc)
        startActivity(intent)
    }
}