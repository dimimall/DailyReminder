package com.example.dailyreminder.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dailyreminder.R;
import com.example.dailyreminder.adapters.MyAdapter;
import com.example.dailyreminder.interfaces.NotesDao;
import com.example.dailyreminder.models.AppDatabase;
import com.example.dailyreminder.models.Notes;
import com.example.dailyreminder.receivers.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Notes> notesList;
    private NotesDao notesDao;
    // Notification ID.
    private static int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private NotificationManager mNotificationManager;
    private AlarmManager alarmManagers;
    private ArrayList<PendingIntent> intentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notesDao = (NotesDao) AppDatabase.getInstance(getApplicationContext()).notesDao();

        notesList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.notesList);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.divider));

        recyclerView.addItemDecoration(itemDecorator);


        alarmManagers = (AlarmManager)getSystemService(ALARM_SERVICE);
        intentArray = new ArrayList<PendingIntent>();


        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),NoteActivity.class);
                startActivity(intent);
            }
        });

        createNotificationChannel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter = new MyAdapter(notesDao.getAll(),MainActivity.this);
        recyclerView.setAdapter(mAdapter);

        long milliseconds = 0;

        for (int i=0; i<notesDao.getAll().size(); i++){

            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);

            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            try {
                Date d = f.parse(notesDao.getAll().get(i).getText());
                milliseconds = d.getTime();
                Log.e("Error","milliseconds "+milliseconds+" "+calendar.getTimeInMillis());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Loop counter `i` is used as a `requestCode`
            NOTIFICATION_ID = i;

            PendingIntent pendingIntent = PendingIntent.
                    getBroadcast(MainActivity.this, NOTIFICATION_ID, intent, 0);

            long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;


            // If the Toggle is turned on, set the repeating alarm with
            // a 15 minute interval.
            if (alarmManagers != null) {
                alarmManagers.setInexactRepeating
                        (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                 calendar.getTimeInMillis()+ milliseconds, repeatInterval,
                                pendingIntent);
            }

            intentArray.add(pendingIntent);

//            else {
//                // Cancel notification if the alarm is turned off.
//                mNotificationManager.cancelAll();
//
//                if (alarmManagers[i] != null) {
//                    alarmManagers[i].cancel(notifyPendingIntent);
//                }
//            }
        }
    }
//    // Call Back method  to get the Message form other Activity
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        // check if the request code is same as what is passed  here it is 2
//        if(requestCode==1)
//        {
//            //notesList= data.getParcelableArrayListExtra("notes");
////            mAdapter = new MyAdapter(notesList,MainActivity.this);
////            recyclerView.setAdapter(mAdapter);
//        }
//    }

    public void createNotificationChannel() {
        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 minutes to " +
                    "stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
