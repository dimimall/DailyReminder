package com.example.dailyreminder.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dailyreminder.R;
import com.example.dailyreminder.interfaces.NotesDao;
import com.example.dailyreminder.models.AppDatabase;
import com.example.dailyreminder.models.Notes;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    EditText title,date;
    Button update;
    private NotesDao notesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = (EditText)  findViewById(R.id.editText3);
        date = (EditText)  findViewById(R.id.editText4);
        update = (Button) findViewById(R.id.save);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notesDao = (NotesDao) AppDatabase.getInstance(getApplicationContext()).notesDao();


        Intent intent = getIntent();
        String title_txt = intent.getStringExtra("title");
        String date_txt = intent.getStringExtra("date");
        final int id = intent.getIntExtra("id",0);

        title.setText(title_txt);
        date.setText(date_txt);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SingleDateAndTimePickerDialog.Builder(EditActivity.this)
                        // .bottomSheet()
                        // .curved() .title("Select Date")
                        .titleTextColor(getResources().getColor(R.color.colorWhite))
                        .minutesStep(1)
                        .backgroundColor(getResources().getColor(R.color.colorLightBlue))
                        .mainColor(getResources().getColor(R.color.colorLightGray))
                        .listener(new SingleDateAndTimePickerDialog.Listener()
                        {
                            @Override public void onDateSelected(Date datetitme)
                            {
                                // selectpackages();
                                String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";
                                // String DATE_FORMAT_NOW1 = "yyyy-MM-dd HH:mm:ss";

                                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                                //  SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_NOW1);
                                date.setText(sdf.format(datetitme));

                            }
                        }).display();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = title.getText().toString();
                String date_txt = date.getText().toString();
                Notes notes = new Notes(note,date_txt);
                notesDao.update(notes.getTitle(),notes.getText(),id);
                title.setText(null);
                date.setText(null);
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
