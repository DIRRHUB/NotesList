package com.noteslist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noteslist.R;
import com.noteslist.models.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private final String PATTERN = "dd-MM-yyyy-kk-mm";
    private final Context context;
    private OnItemClickListener listener;

    private List<Note> notes = new ArrayList<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Note note = notes.get(position);
        viewHolder.title.setText(note.getTitle());
        viewHolder.description.setText(note.getDescription());

        Calendar calendar = Calendar.getInstance();
        Calendar calendarNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        try {
            calendar.setTime(Objects.requireNonNull(sdf.parse(note.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayNote = calendar.get(Calendar.DAY_OF_MONTH);
        int monthNote = calendar.get(Calendar.MONTH);
        int yearNote = calendar.get(Calendar.YEAR);

        int dayNow = calendarNow.get(Calendar.DAY_OF_MONTH);
        int monthNow = calendarNow.get(Calendar.MONTH);
        int yearNow = calendarNow.get(Calendar.YEAR);
        if(dayNote==dayNow && monthNote==monthNow && yearNote==yearNow){
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            viewHolder.date.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes));
        } else {
            viewHolder.date.setText(dayNote + "." + String.format("%02d", monthNote) + "." + yearNote);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListContent(List<Note> notes) {
        Collections.reverse(notes);
        this.notes = notes;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public Note getNoteAtPosition(int position){
        return notes.get(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, description, date;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.textTitle);
            this.description = itemView.findViewById(R.id.textDescription);
            this.date = itemView.findViewById(R.id.textDate);
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (listener != null && pos!=RecyclerView.NO_POSITION)
                    listener.onItemClick(notes.get(pos));
            });
        }
    }
}
