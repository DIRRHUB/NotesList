package com.noteslist.database.room.dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.noteslist.models.Note;

import java.util.List;

public interface WorkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Query("DELETE FROM notes")
    void deleteAll();

    @Query("SELECT * from notes ORDER BY id ASC")
    List<Note> getAllNotes();
}
