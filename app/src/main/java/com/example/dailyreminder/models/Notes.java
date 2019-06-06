package com.example.dailyreminder.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Notes implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "text")
    private String text;


    // Constructor
    public Notes(String title, String text){
        this.title = title;
        this.text = text;
    }
    public void setId(int id) { this.id = id; }
    public int getId() {return this.id;}
    public void setTitle(String title) {this.title = title;}
    public String getTitle() { return this.title;}
    public void setText(String text) {this.title = title;}
    public String getText() { return this.text; }


    //parceling part
    protected Notes(Parcel in) {
        // the order needs to be the same as in writeToParcel() method
        this.title = in.readString();
        this.text = in.readString();
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.text);
    }

}
