package com.noteslist.database.retrofit.result;

import com.noteslist.models.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "https://my-json-server.typicode.com/DIRRHUB/NotesList/master/db.json/";
    @GET("notes")
    Call<List<Note>> getNotes();
}
