package com.example.dailyreminder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.example.dailyreminder.R;
import com.example.dailyreminder.interfaces.NotesDao;
import com.example.dailyreminder.models.AppDatabase;

public class EditActivity extends AppCompatActivity {

    EditText title,date;
    Button update;
    private NotesDao notesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notesDao = (NotesDao) AppDatabase.getInstance(getApplicationContext()).notesDao();

        init();

    }

    public void init(){
        title = (EditText)  findViewById(R.id.editText3);
        date = (EditText)  findViewById(R.id.editText4);
        update = (Button) findViewById(R.id.save);
    }
}
