package com.jimmy.roomwordsample.ui.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.jimmy.roomwordsample.R
import com.jimmy.roomwordsample.businesslogic.storage.entities.Word
import com.jimmy.roomwordsample.ui.adapters.WordListAdapter
import com.jimmy.roomwordsample.ui.adapters.WordListAdapter.SwipCallBack
import com.jimmy.roomwordsample.ui.helpers.OnStartDragListener
import com.jimmy.roomwordsample.ui.interactivity.SimpleItemTouchHelperCallback
import com.jimmy.roomwordsample.ui.viewmodel.WordViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() , OnStartDragListener {

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        mItemTouchHelper.startDrag(viewHolder)
    }


    lateinit var mWordViewModel: WordViewModel
    val NEW_WORD_ACTIVITY_REQUEST_CODE = 1

    lateinit var mItemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        fab.setOnClickListener { view ->
           /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/

            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE)
        }


        val recyclerView : RecyclerView = findViewById(R.id.recyclerview)
        val adapter  =  WordListAdapter(this, this, SwipCallBack {
            mWordViewModel.delete(it)

        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager =  LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

        /*
         This class is the contract between ItemTouchHelper and your application. It lets you control
         which touch behaviors are enabled per each ViewHolder and also receive callbacks when user
         performs these actions.
         */
        val callback = SimpleItemTouchHelperCallback(adapter)
         mItemTouchHelper = ItemTouchHelper(callback)
         mItemTouchHelper.attachToRecyclerView(recyclerView)

        /*
        observer for the LiveData returned by getAllWords().
        The onChanged() method fires when the observed data changes and the activity is in the foreground
         */
        mWordViewModel.allWords.observe(this, object : Observer<List<Word>> {
            override fun onChanged(t: List<Word>?) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(t)
            }


        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val word : Word = Word(data!!.getStringExtra(NewWordActivity.EXTRA_REPLY),
                    data.getStringExtra(NewWordActivity.EXTRA_REPLY_MEANING))
            mWordViewModel.insert(word)
        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show()
        }
    }
}
