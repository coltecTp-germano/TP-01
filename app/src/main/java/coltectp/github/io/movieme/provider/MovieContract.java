package coltectp.github.io.movieme.provider;

/**
 * Created by Germano Barcelos on 29/03/2018.
 */

import android.content.ContentResolver;
import android.provider.BaseColumns;
import android.net.Uri;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "coltectp.github.io.movieme";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movies";

    public MovieContract(){}

    public static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIE);

        public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + PATH_MOVIE;

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_DIRECTOR = "director";
        public static final String COLUMN_AGE_GROUP = "age_group";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        /*Aventura, Romance, Terror, Suspense, Ficcao*/
        public static final int GENRE_ADVENTURE = 0;
        public static final int GENRE_ROMANCE = 1;
        public static final int GENRE_TERROR = 2;
        public static final int GENRE_SUSPENSE = 3;
        public static final int GENRE_FICTION = 4;
        public static final int GENRE_OTHER = 5;

        public static boolean isValidGenre (int genre) {
            return genre == GENRE_ADVENTURE
                    || genre == GENRE_ROMANCE
                    || genre == GENRE_TERROR
                    || genre == GENRE_SUSPENSE
                    || genre == GENRE_FICTION
                    || genre == GENRE_OTHER;
        }

        public static final int AGE_GROUP_L = 6;
        public static final int AGE_GROUP_10 = 7;
        public static final int AGE_GROUP_12 = 8;
        public static final int AGE_GROUP_14 = 9;
        public static final int AGE_GROUP_16 = 10;
        public static final int AGE_GROUP_18 = 11;

        public static boolean isValidAgeGroup (int ageGroup) {
            return ageGroup == AGE_GROUP_L
                    || ageGroup == AGE_GROUP_10
                    || ageGroup == AGE_GROUP_12
                    || ageGroup == AGE_GROUP_14
                    || ageGroup == AGE_GROUP_16
                    || ageGroup == AGE_GROUP_18;
        }


    }
}
