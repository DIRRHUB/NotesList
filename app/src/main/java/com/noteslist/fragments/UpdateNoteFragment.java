package com.noteslist.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.noteslist.R;
import com.noteslist.databinding.FragmentUpdateNoteBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class UpdateNoteFragment extends Fragment {
    private final String PATTERN = "dd-MM-yyyy-kk-mm";
    private final String ID = "id";
    private final String TITLE = "title";
    private final String DESCRIPTION = "description";
    private final String DATE = "date";
    private FragmentUpdateNoteBinding binding;
    private String title, description;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        id = requireArguments().getInt(ID);
        title = requireArguments().getString(TITLE);
        description = requireArguments().getString(DESCRIPTION);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdateNoteBinding.inflate(getLayoutInflater());
        binding.textTitle.getEditText().setText(title);
        binding.textDescription.getEditText().setText(description);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_menu, menu);
    }

    @SuppressLint("SimpleDateFormat")
    private void save(){
        String newTitle = Objects.requireNonNull(binding.textTitle.getEditText()).getText().toString();
        String newDescription = Objects.requireNonNull(binding.textDescription.getEditText()).getText().toString();
        if(!(title.equals(newTitle) && description.equals(newDescription))){
            Bundle bundle = new Bundle();
            bundle.putInt(ID, id);
            bundle.putString(TITLE, newTitle);
            bundle.putString(DESCRIPTION, newDescription);
            Calendar calendarNow = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
            String newDate = sdf.format(calendarNow.getTime());
            bundle.putString(DATE, newDate);
            Navigation.findNavController(requireView()).navigate(R.id.listFragment, bundle);
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.listFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.save){
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}