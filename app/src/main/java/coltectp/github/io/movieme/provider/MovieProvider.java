package coltectp.github.io.movieme.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.content.ContentUris;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;

import android.content.UriMatcher;

import coltectp.github.io.movieme.data.Database;
import coltectp.github.io.movieme.data.Movie;
import coltectp.github.io.movieme.data.MovieDao;

import static coltectp.github.io.movieme.provider.MovieContract.*;

/**
 * Created by Germano Barcelos on 29/03/2018.
 */

public class MovieProvider extends ContentProvider {
    private String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE, MOVIE);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = sUriMatcher.match(uri);
        Cursor cursor = null;

        final Context context = getContext();
        if (context == null) {
            return null;
        }

        MovieDao movieDao = Database.getInstance(context).movieDao();

        switch (code) {
            case MOVIE:
                cursor = movieDao.selectAll();
                break;
            case MOVIE_ID:
                cursor = movieDao.selectById(ContentUris.parseId(uri));
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int code = sUriMatcher.match(uri);

        switch (code) {
            case MOVIE:
                return MovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + code);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int code = sUriMatcher.match(uri);

        switch (code) {
            case MOVIE:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = Database.getInstance(context).movieDao()
                        .insert(Movie.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);

            case MOVIE_ID:
                throw new IllegalArgumentException("Cannot insert with " + uri + " with match " + code);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + code);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int code = sUriMatcher.match(uri);
        final Context context = getContext();
        switch (code) {
            case MOVIE:
                final int rowsDeleted = Database.getInstance(context).movieDao()
                        .deleteAll();
                if (rowsDeleted != 0) {
                    Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case MOVIE_ID:
                if (context == null) {
                    return 0;
                }
                final int count =  Database.getInstance(context).movieDao()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + code);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final int code = sUriMatcher.match(uri);
        switch (code) {
            case MOVIE:
                throw new IllegalArgumentException("Cannot update with " + uri + " with match " + code);
            case MOVIE_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Movie movie = Movie.fromContentValues(values);
                movie.setId(ContentUris.parseId(uri));
                final int count = Database.getInstance(context).movieDao()
                        .update(movie);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + code);
        }
    }
}
