package com.jimmy.roomwordsample.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.jimmy.roomwordsample.businesslogic.storage.entities.Word;
import com.jimmy.roomwordsample.repository.WordRepository;

import java.util.List;

/*
A ViewModel holds your app's UI data in a lifecycle-conscious way that survives
 configuration changes. Separating your app's UI data from your Activity and
 Fragment classes lets you better follow the single responsibility principle:
 Your activities and fragments are responsible for drawing data to the screen,
 while your ViewModel can take care of holding and processing all
 the data needed for the UI.

In the ViewModel, use LiveData for changeable data that the UI will use or display.
Using LiveData has several benefits:

You can put an observer on the data (instead of polling for changes) and only update the
the UI when the data actually changes.
The Repository and the UI are completely separated by the ViewModel.
There are no database calls from the ViewModel, making the code more testable.

Important: ViewModel is not a replacement for the onSaveInstanceState() method,
 because the ViewModel does not survive a process shutdown
 https://medium.com/google-developers/viewmodels-persistence-onsaveinstancestate-restoring-ui-state-and-loaders-fc7cc4a6c090
 */
public class WordViewModel extends AndroidViewModel {

//    private member variable to hold a reference to the repository
    private WordRepository mRepository;

//    Add a private LiveData member variable to cache the list of words
    private LiveData<List<Word>> mAllWords;

    public WordViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() { return mAllWords; }

    /*
    wrapper insert() method that calls the Repository's insert() method.
     In this way, the implementation of insert() is completely hidden from the UI.
     */
    public void insert(Word word) { mRepository.insert(word); }
}
