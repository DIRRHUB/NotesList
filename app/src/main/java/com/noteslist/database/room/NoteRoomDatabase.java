package com.noteslist.database.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.noteslist.database.room.dao.NoteDao;
import com.noteslist.models.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteRoomDatabase extends RoomDatabase {
    private static NoteRoomDatabase instance;

    public abstract NoteDao getNoteDao();

    public static synchronized NoteRoomDatabase getInstance(Context context) {
            if(instance == null){
                instance = Room.databaseBuilder(context.getApplicationContext(), NoteRoomDatabase.class, "note_table")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return instance;
    }
}
