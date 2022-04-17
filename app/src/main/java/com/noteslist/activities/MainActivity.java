package com.noteslist.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.noteslist.R;
import com.noteslist.databinding.ActivityMainBinding;
import com.noteslist.models.NoteViewModel;
import com.noteslist.utils.ConnectionLiveData;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NoteViewModel noteViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkConnection();
    }

    private void checkConnection() {
        ConnectionLiveData connectionLiveData = new ConnectionLiveData(getApplicationContext());
        connectionLiveData.observe(this, connection -> {
            if(connection.getIsConnected()){
                doGetRequest();
            } else {
                binding.progressBarCircle.setVisibility(View.GONE);
                Snackbar.make(binding.fragmentContainerView, R.string.no_connection, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void doGetRequest() {
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
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