package com.example.dailyreminder.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.dailyreminder.models.Notes;

import java.util.List;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes")
    List<Notes> getAll();
    @Insert
    void insertAll(Notes... notes);
    @Delete
    void delete(Notes notes);
    @Update
    void updateAll(Notes... notes);
}
