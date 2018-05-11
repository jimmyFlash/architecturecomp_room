package com.jimmy.roomwordsample.businesslogic.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.jimmy.roomwordsample.businesslogic.storage.doa.WordDao;
import com.jimmy.roomwordsample.businesslogic.storage.entities.Word;

/**
 * Annotate the class to be a Room database, declare the entities that belong
 * in the database and set the version number. Listing the entities will create
 * tables in the database.
 */
@Database(entities = {Word.class}, version = 1)
public abstract class WordRoomDatabase extends RoomDatabase{

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
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}


