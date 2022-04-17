package com.noteslist.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.noteslist.database.retrofit.NoteRetrofitDatabase;
import com.noteslist.database.room.NoteRoomDatabase;
import com.noteslist.database.room.dao.NoteDao;
import com.noteslist.models.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteRepository {
    private final NoteDao noteDao;
    private final LiveData<List<Note>> notes;
    private final int size;
    private Call<List<Note>> call;

    public NoteRepository (Application application) {
        NoteRoomDatabase database = NoteRoomDatabase.getInstance(application);
        noteDao = database.getNoteDao();
        notes = noteDao.getAllNotes();
        size = noteDao.getSize();
    }

    public void doGetRequest() {
        NoteRetrofitDatabase databaseOnline = NoteRetrofitDatabase.getInstance();
        call = databaseOnline.getApi().getNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(@NonNull Call<List<Note>> call, @NonNull Response<List<Note>> response) {
                List<Note> notesOnline = response.body();
                for(Note note : notesOnline){
                    new CheckNoteAsyncTask(noteDao).execute(note);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Note>> call, @NonNull Throwable t) {
                Log.e("Response: ", t.toString());
            }
        });
    }

    public void insert(Note note){
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public int getSize(){
        return size;
    }

    private static class CheckNoteAsyncTask extends AsyncTask<Note, Void, Void>{
        private final NoteDao noteDao;

        private CheckNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            String title = notes[0].getTitle();
            String description = notes[0].getDescription();
            String date = notes[0].getDate();
            if(noteDao.getNote(title, description, date)==null) {
                noteDao.insert(notes[0]);
            }
            return null;
        }
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
}
