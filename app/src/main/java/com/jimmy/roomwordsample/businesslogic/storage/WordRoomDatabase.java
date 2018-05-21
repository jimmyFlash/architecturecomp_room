package com.jimmy.roomwordsample.businesslogic.storage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jimmy.roomwordsample.businesslogic.storage.doa.WordDao;
import com.jimmy.roomwordsample.businesslogic.storage.entities.Word;

import java.util.List;
import java.util.Objects;

/**
 * Annotate the class to be a Room database, declare the entities that belong
 * in the database and set the version number. Listing the entities will create
 * tables in the database.
 */
@Database(entities = {Word.class}, version = 1)
public abstract class WordRoomDatabase extends RoomDatabase{

    private static final String FIRST_RUN = "firstRun";
    private static SharedPreferences sharedP;

    //Define the DAOs that work with the database. Provide an abstract "getter" method for each @Dao
    public abstract WordDao wordDao();


    private static WordRoomDatabase INSTANCE;

    /**
     * Make the WordRoomDatabase a singleton to prevent having multiple instances of the database
     * opened at the same time.
     * @param context
     * @return
     */
    public static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    /*
                    uses Room's database builder to create a RoomDatabase object in the application
                     context from the WordRoomDatabase class and names it "word_database"
                     */
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
         sharedP = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        // you cannot do Room database operations on the UI thread, onOpen() creates and executes an AsyncTask to add content to the database
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            if(sharedP.getBoolean(FIRST_RUN, true)){
                SharedPreferences.Editor editor = sharedP.edit();
                editor.putBoolean(FIRST_RUN, false);
                editor.apply();
                new PopulateDbAsync(INSTANCE).execute();
            }
        }
    };

    /*
     AsyncTask that deletes the contents of the database, then populates it with the two words "Hello" and "World"
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, List<Word>> {

        private final WordDao mDao;

        PopulateDbAsync(WordRoomDatabase db) {
            mDao = db.wordDao();
        }

        @Override
        protected List<Word> doInBackground(final Void... params) {
            LiveData<List<Word>> listWordsLiveData = Objects.requireNonNull(mDao.getAllWords());

            List<Word> list = listWordsLiveData.getValue();


                mDao.deleteAll();
                Word word = new Word("Android", "Mobile operating system developed by Google," +
                        " based on a modified version of the Linux kernel");
                mDao.insert(word);
                word = new Word("IOS", "Mobile operating system created and developed by Apple Inc. exclusively for its hardware");
                mDao.insert(word);

            return list;
        }

        @Override
        protected void onPostExecute(List<Word> aVoid) {

//            Log.e("++++++++++++", aVoid + "");

        }
    }



}


