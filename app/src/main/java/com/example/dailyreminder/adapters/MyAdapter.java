package com.example.dailyreminder.adapters;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dailyreminder.R;
import com.example.dailyreminder.activities.EditActivity;
import com.example.dailyreminder.interfaces.NotesDao;
import com.example.dailyreminder.models.AppDatabase;
import com.example.dailyreminder.models.Notes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.dailyreminder.activities.MainActivity.alarmManagers;
import static com.example.dailyreminder.activities.MainActivity.mNotificationManager;
import static com.example.dailyreminder.activities.MainActivity.pendingIntent;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Notes> notesList;
    private Context context;
    private NotesDao notesDao;
    MyViewHolder vh;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, datetime;
        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Notes> notesList, Context context) {
        this.notesList = notesList;
        this.context = context;
        notesDao = (NotesDao) AppDatabase.getInstance(context).notesDao();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_note, parent, false);
        vh = new MyViewHolder(view);
        vh.title = (TextView) view.findViewById(R.id.textView3);
        vh.datetime = (TextView) view.findViewById(R.id.textView6);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(notesList.get(position).getTitle());
        holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String[] split = notesList.get(position).getText().split(" ");
        SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
        Date dt1= null;
        try {
            dt1 = format1.parse(split[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2=new SimpleDateFormat("EEEE");
        String finalDay=format2.format(dt1);

        holder.datetime.setText(finalDay+" "+split[1]);
        holder.datetime.setPaintFlags(holder.datetime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("title",notesList.get(position).getTitle());
                intent.putExtra("date",notesList.get(position).getText());
                intent.putExtra("id",notesList.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                notesDao.delete(notesList.get(position));
                                notesList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, notesList.size());

                    mNotificationManager.cancel(position);

                    if (alarmManagers != null) {
                        alarmManagers.cancel(pendingIntent);
                    }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
