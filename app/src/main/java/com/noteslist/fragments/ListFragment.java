package com.noteslist.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noteslist.R;
import com.noteslist.adapter.RecyclerAdapter;
import com.noteslist.databinding.FragmentListBinding;
import com.noteslist.models.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListFragment extends Fragment {
    private final String PATTERN = "dd-MM-yyyy-kk-mm";
    private FragmentListBinding binding;
    private List<Note> noteList = new ArrayList<>();
    private RecyclerAdapter recyclerAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
            noteList.add(new Note(sdf.format(dateNow)));
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
            noteList.remove(position);
            recyclerAdapter.notifyItemRemoved(position);
        }
    };
}