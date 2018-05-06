package com.jimmy.roomwordsample.repository.doa;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.jimmy.roomwordsample.repository.entities.Word;

import java.util.List;

/**
 * In the DAO (data access object), you specify SQL queries and associate them with method calls.
 * The compiler checks the SQL and generates queries from convenience annotations for common queries, such as @Insert.
 *
 * The DAO must be an interface or abstract class.
 *
 * By default, all queries must be executed on a separate thread.
 * oom uses the DAO to create a clean API for your code.
 */
@Dao
public interface WordDao {

    //Declare a method to insert one word
    @Insert
    void insert(Word word);

    //Declare a method to delete all the words
    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("SELECT * from word_table ORDER BY word ASC")
    List<Word> getAllWords();
}
