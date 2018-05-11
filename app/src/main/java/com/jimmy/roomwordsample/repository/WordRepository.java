package com.jimmy.roomwordsample.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.jimmy.roomwordsample.businesslogic.storage.WordRoomDatabase;
import com.jimmy.roomwordsample.businesslogic.storage.doa.WordDao;
import com.jimmy.roomwordsample.businesslogic.storage.entities.Word;

import java.util.List;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
    }

    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

}
