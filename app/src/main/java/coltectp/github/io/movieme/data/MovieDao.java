package coltectp.github.io.movieme.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;


import static coltectp.github.io.movieme.provider.MovieContract.MovieEntry.*;

/**
 * Created by Germano Barcelos on 29/03/2018.
 */

@Dao
public interface MovieDao {
    @Insert
    long insert(Movie movie);

    @Update
    int update(Movie movie);

    @Query("DELETE FROM " + TABLE_NAME)
    int deleteAll();

    @Query("DELETE FROM " + TABLE_NAME + " WHERE " +  COLUMN_ID + " = :id")
    int deleteById(long id);

    @Query("SELECT COUNT(*) FROM " + TABLE_NAME)
    int count();

    @Query("SELECT * FROM " + TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :id")
    Cursor selectById(long id);
}
