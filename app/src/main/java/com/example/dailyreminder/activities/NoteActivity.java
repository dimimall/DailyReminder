package com.example.dailyreminder.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dailyreminder.R;
import com.example.dailyreminder.Utils.utils;
import com.example.dailyreminder.interfaces.NotesDao;
import com.example.dailyreminder.models.AppDatabase;
import com.example.dailyreminder.models.Notes;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    EditText note_text,datetime;
    Button save;

    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private String lattitude,longitude;
    private NotesDao notesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notesDao = (NotesDao) AppDatabase.getInstance(getApplicationContext()).notesDao();

        initialViews();

        datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SingleDateAndTimePickerDialog.Builder(NoteActivity.this)
                        // .bottomSheet()
                        // .curved() .title("Select Date")
                        .titleTextColor(getResources().getColor(R.color.colorWhite))
                        .minutesStep(1)
                        .backgroundColor(getResources().getColor(R.color.colorLightBlue))
                        .mainColor(getResources().getColor(R.color.colorLightGray))
                        .listener(new SingleDateAndTimePickerDialog.Listener()
                        {
                            @Override public void onDateSelected(Date date)
                            {
                                // selectpackages();
                                String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";
                                // String DATE_FORMAT_NOW1 = "yyyy-MM-dd HH:mm:ss";

                                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                                //  SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_NOW1);
                                datetime.setText(sdf.format(date));
                                //getDateDifference(stringDate);

                                // Toast.makeText(getActivity(),""+stringDate,Toast.LENGTH_SHORT).show();
                            }
                        }).display();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = note_text.getText().toString();
                String date = datetime.getText().toString();
                Notes notes = new Notes(note,date);
                notesDao.insertAll(notes);

                note_text.setText(null);
                datetime.setText(null);
                finish();
            }
        });
    }

    public void initialViews(){
        note_text = (EditText)  findViewById(R.id.editText);
        datetime = (EditText) findViewById(R.id.editText2);
        save = (Button) findViewById(R.id.save_edit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("")
                        .setTitle("Save your changes or discard");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
