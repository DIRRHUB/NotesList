package com.noteslist.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noteslist.R;
import com.noteslist.adapter.RecyclerAdapter;
import com.noteslist.database.room.NoteRoomDatabase;

import com.noteslist.database.room.dao.NoteDao;
import com.noteslist.databinding.FragmentListBinding;
import com.noteslist.models.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ListFragment extends Fragment {
    private final String PATTERN = "dd-MM-yyyy-kk-mm";
    private FragmentListBinding binding;
    private NoteDao noteDao;
    private List<Note> noteList = new ArrayList<>();
    private RecyclerAdapter recyclerAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoteRoomDatabase db = Room.databaseBuilder(requireContext(), NoteRoomDatabase.class, "NoteDatabase")
                .allowMainThreadQueries().build();
        noteDao = db.getNoteDao();
        noteList = noteDao.getAllNotes();
        Collections.sort(noteList, comparatorList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(getLayoutInflater());
        binding.floatingActionButton.setOnClickListener(listener);
        setAdapter();
        return binding.getRoot();
    }

    private void setAdapter() {
        recyclerAdapter = new RecyclerAdapter(this.getContext(), noteList);
        binding.recyclerView.setAdapter(recyclerAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelper);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
    }

    @SuppressLint("SimpleDateFormat")
    private final View.OnClickListener listener = view -> {
        if(view.getId()==R.id.floating_action_button){
            Date dateNow = Calendar.getInstance().getTime();
            Log.i("date", dateNow.toString());
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
            Note note = new Note(sdf.format(dateNow));
            noteList.add(0, note);
            noteDao.insert(note);
            layoutManager.scrollToPosition(0);
            recyclerAdapter.notifyItemInserted(0);
        }
    };

    private final ItemTouchHelper.SimpleCallback touchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            noteDao.deleteById(noteList.get(position).getId());
            noteList.remove(position);
            recyclerAdapter.notifyItemRemoved(position);
        }
    };

    @SuppressLint("SimpleDateFormat")
    private final Comparator<Note> comparatorList = (Comparator<Note>) (n1, n2) -> {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar1.setTime(Objects.requireNonNull(sdf.parse(n1.getDate())));
            calendar2.setTime(Objects.requireNonNull(sdf.parse(n2.getDate())));
            if(calendar1.before(calendar2)){
                return 1;
            } else {
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    };
}