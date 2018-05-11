package com.jimmy.roomwordsample.businesslogic.storage.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Word that describes a word Entity. You need a constructor and
 * a "getter" method for the data model class, because that's how
 * Room knows to instantiate your objects.
 *
 * To make the Word class meaningful to a Room database, you need to annotate it.
 * Annotations identify how each part of this class relates to an entry in the database.
 * Room uses this information to generate code.
 *
 * @Entity(tableName = "word_table")
 * Each @Entity class represents an entity in a table. Annotate your class declaration to indicate that it's an entity.
 *    Specify the name of the table if you want it to be different from the name of the class.
 *    @PrimaryKey
 *    Every entity needs a primary key. To keep things simple, each word acts as its own primary key.
 *    @NonNull
 *    Denotes that a parameter, field, or method return value can never be null.
 *    @ColumnInfo(name = "word")
 *    Specify the name of the column in the table if you want it to be different from the name of the member variable.
 *    Every field that's stored in the database needs to be either public or have a "getter" method.
 *      This sample provides a getWord() method.
 */
@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    public Word(@NonNull String word) {this.mWord = word;}

    public String getWord(){return this.mWord;}
}
