package coltectp.github.io.movieme.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.provider.BaseColumns;
import android.content.ContentValues;

/**
 * Created by Germano Barcelos on 28/03/2018.
 */

@Entity(tableName = Movie.TABLE_NAME)
public class Movie {
    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_DIRECTOR = "director";
    public static final String COLUMN_AGE_GROUP = "age_group";
    public static final String COLUMN_RELEASE_DATE = "release_date";

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
    private String mAgeGroup;

    @ColumnInfo(name = COLUMN_RELEASE_DATE)
    private int mReleaseDate;

    public Movie() {
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
            movie.setAgeGroup(values.getAsString(COLUMN_AGE_GROUP));
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

    public String getAgeGroup() {
        return mAgeGroup;
    }

    public void setAgeGroup(String mAgeGroup) {
        this.mAgeGroup = mAgeGroup;
    }

    public int getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(int mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }
}
