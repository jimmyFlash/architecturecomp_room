package com.jimmy.roomwordsample

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

import kotlinx.android.synthetic.main.activity_new_word.*
import android.text.TextUtils
import android.content.Intent
import android.widget.EditText





class NewWordActivity : AppCompatActivity() {

    /**
     * companion object allows constant members invocation in other classes
     */
    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
        const val EXTRA_REPLY_MEANING = "com.example.android.wordlistsql.MEANING"
    }

     var mEditWordView: EditText? = null
     var mEditWordMeaningView: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        setSupportActionBar(toolbar)

        val button : Button = findViewById(R.id.button_save)

        mEditWordView = findViewById(R.id.edit_word)
        mEditWordMeaningView = findViewById(R.id.edit_word_meaning)
        button.setOnClickListener({

            val replyIntent = Intent()
            if (TextUtils.isEmpty(mEditWordView?.text.toString()) || TextUtils.isEmpty(mEditWordMeaningView?.text.toString())) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = mEditWordView?.text.toString()
                val meaning = mEditWordMeaningView?.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                replyIntent.putExtra(EXTRA_REPLY_MEANING, meaning)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        })
    }

}
