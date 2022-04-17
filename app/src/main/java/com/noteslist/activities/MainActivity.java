package com.noteslist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.noteslist.databinding.ActivityMainBinding;
import com.noteslist.models.NoteViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BroadcastReceiver networkReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //TODO NetworkChangeReceiver
        doGetRequest();
    }

    private void doGetRequest() {
        NoteViewModel noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        int size = noteViewModel.getSize();
        noteViewModel.doGetRequest();

        if (size > 0) {
            new CountDownTimer(5000, 10) {
                public void onTick(long millisUntilFinished) {
                    binding.progressBarHorizontal.setProgress((int) ((5000-millisUntilFinished)/50));
                }
                public void onFinish() {
                    binding.progressBarHorizontal.setProgress(100);
                }
            }.start();
        } else {
            new CountDownTimer(5000, 10) {
                public void onTick(long millisUntilFinished) {
                    binding.progressBarCircle.setVisibility(View.VISIBLE);
                }
                public void onFinish() {
                    binding.progressBarCircle.setVisibility(View.GONE);
                    binding.progressBarHorizontal.setProgress(100);
                }
            }.start();
        }
    }
}
