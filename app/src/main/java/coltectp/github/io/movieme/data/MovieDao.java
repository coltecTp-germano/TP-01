package coltectp.github.io.movieme.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

/**
 * Created by Germano Barcelos on 29/03/2018.
 */

@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Update
    int update(Movie movie);

    @Query("DELETE FROM " + Movie.TABLE_NAME + " WHERE " +  Movie.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Query("SELECT COUNT(*) FROM " + Movie.TABLE_NAME)
    int count();

    @Query("SELECT * FROM " + Movie.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + Movie.TABLE_NAME + " WHERE " + Movie.COLUMN_ID + " = :id")
    Cursor selectById(long id);
}
