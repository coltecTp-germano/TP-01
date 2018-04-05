package coltectp.github.io.movieme.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.content.ContentValues;

import static coltectp.github.io.movieme.provider.MovieContract.MovieEntry.*;

/**
 * Created by Germano Barcelos on 28/03/2018.
 */

@Entity(tableName = TABLE_NAME)
public class Movie {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long id;

    @ColumnInfo(name = COLUMN_NAME)
    private String mName;

    @ColumnInfo(name = COLUMN_GENRE)
    private int mGenre;

    @ColumnInfo(name = COLUMN_DIRECTOR)
    private String mDirector;

    @ColumnInfo(name = COLUMN_AGE_GROUP)
    private int mAgeGroup;

    @ColumnInfo(name = COLUMN_RELEASE_DATE)
    private int mReleaseDate;

    public Movie() {
    }

    public Movie(String mName, int mGenre, String mDirector, int mAgeGroup, int mReleaseDate) {
        this.mName = mName;
        this.mGenre = mGenre;
        this.mDirector = mDirector;
        this.mAgeGroup = mAgeGroup;
        this.mReleaseDate = mReleaseDate;
    }

    public static Movie fromContentValues(ContentValues values) {
        final Movie movie = new Movie();
        if (values.containsKey(COLUMN_ID)) {
            movie.setId(values.getAsLong(COLUMN_ID));
        }

        if (values.containsKey(COLUMN_NAME)) {
            movie.setName(values.getAsString(COLUMN_NAME));
        }

        if (values.containsKey(COLUMN_GENRE)) {
            movie.setGenre(values.getAsInteger(COLUMN_GENRE));
        }

        if (values.containsKey(COLUMN_DIRECTOR)) {
            movie.setDirector(values.getAsString(COLUMN_DIRECTOR));
        }

        if (values.containsKey(COLUMN_AGE_GROUP)) {
            movie.setAgeGroup(values.getAsInteger(COLUMN_AGE_GROUP));
        }

        if (values.containsKey(COLUMN_RELEASE_DATE)) {
            movie.setReleaseDate(values.getAsInteger(COLUMN_RELEASE_DATE));
        }

        return movie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getGenre() {
        return mGenre;
    }

    public void setGenre(int mGenre) {
        this.mGenre = mGenre;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(String mDirector) {
        this.mDirector = mDirector;
    }

    public int getAgeGroup() {
        return mAgeGroup;
    }

    public void setAgeGroup(int mAgeGroup) {
        this.mAgeGroup = mAgeGroup;
    }

    public int getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(int mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }
}
