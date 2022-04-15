package com.noteslist.database.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.noteslist.database.room.dao.NoteDao;
import com.noteslist.models.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteRoomDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();
}
