package coltectp.github.io.movieme.data;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

/**
 * Created by Germano Barcelos on 29/03/2018.
 */

@android.arch.persistence.room.Database(entities = {Movie.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static Database sInstance;

    public static synchronized Database getInstance (Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), Database.class, "MovieMe")
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                Database.class).build();
    }
}
