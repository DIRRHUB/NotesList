package com.noteslist.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noteslist.R;
import com.noteslist.adapter.RecyclerAdapter;

import com.noteslist.databinding.FragmentListBinding;
import com.noteslist.models.Note;
import com.noteslist.models.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ListFragment extends Fragment {
    private final String PATTERN = "dd-MM-yyyy-kk-mm";
    private final String ID = "id";
    private final String TITLE = "title";
    private final String DESCRIPTION = "description";
    private final String DATE = "date";
    private FragmentListBinding binding;
    private NoteViewModel noteViewModel;
    private RecyclerAdapter recyclerAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(getLayoutInflater());
        binding.floatingActionButton.setOnClickListener(listener);
        getBundle();
        setAdapter();
        noteViewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            if(notes.isEmpty()){
                binding.textData.setVisibility(View.VISIBLE);
            } else {
                binding.textData.setVisibility(View.GONE);
            }
            recyclerAdapter.setListContent(notes);
        });
        recyclerAdapter.setOnClickListener(note -> sendBundle(note));
        return binding.getRoot();
    }

    private void sendBundle(Note note){
        Bundle bundle = new Bundle();
        bundle.putInt(ID, note.getId());
        bundle.putString(TITLE, note.getTitle());
        bundle.putString(DESCRIPTION, note.getDescription());
        Navigation.findNavController(requireView()).navigate(R.id.updateNoteFragment, bundle);
    }

    private void getBundle(){
        if(getArguments()!=null){
            int id = getArguments().getInt(ID);
            String title = getArguments().getString(TITLE);
            String description = getArguments().getString(DESCRIPTION);
            String date = getArguments().getString(DATE);
            Note updatedNote = new Note(title, description, date);
            updatedNote.setId(id);
            noteViewModel.update(updatedNote);
        }
    }

    private void setAdapter() {
        recyclerAdapter = new RecyclerAdapter(this.getContext());
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
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
            Note note = new Note("Новая заметка", "", sdf.format(dateNow));
            noteViewModel.insert(note);
            layoutManager.scrollToPosition(0);
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
            noteViewModel.delete(recyclerAdapter.getNoteAtPosition(position));
            recyclerAdapter.notifyItemRemoved(position);
        }
    };
}