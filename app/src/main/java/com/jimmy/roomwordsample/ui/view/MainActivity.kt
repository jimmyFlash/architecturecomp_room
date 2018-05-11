package com.jimmy.roomwordsample.ui.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.jimmy.roomwordsample.R
import com.jimmy.roomwordsample.ui.adapters.WordListAdapter

import kotlinx.android.synthetic.main.activity_main.*
import com.jimmy.roomwordsample.ui.viewmodel.WordViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.annotation.Nullable
import com.jimmy.roomwordsample.businesslogic.storage.entities.Word
import android.widget.Toast
import com.jimmy.roomwordsample.NewWordActivity
import android.R.attr.data
import android.app.Activity





class MainActivity : AppCompatActivity() {

    lateinit var mWordViewModel: WordViewModel
    val NEW_WORD_ACTIVITY_REQUEST_CODE = 1

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
        val adapter  =  WordListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =  LinearLayoutManager(this)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val word : Word = Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY))
            mWordViewModel.insert(word)
        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show()
        }
    }
}
