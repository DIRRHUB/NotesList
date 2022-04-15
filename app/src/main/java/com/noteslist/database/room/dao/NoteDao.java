package com.noteslist.database.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.noteslist.models.Note;
import java.util.List;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Query("DELETE FROM notes WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * from notes")
    List<Note> getAllNotes();
}
