package coltectp.github.io.movieme;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;
import android.graphics.Color;
import android.content.ContentValues;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import butterknife.BindView;
import coltectp.github.io.movieme.data.Movie;
import coltectp.github.io.movieme.provider.MovieContract;
import coltectp.github.io.movieme.provider.MovieProvider;

import static butterknife.ButterKnife.bind;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_MOVIE = 0;

    private MovieAdapter mMovieAdapter;
    @BindView(R.id.list) EmptyRecyclerView mRecyclerView;
    @BindView(R.id.empty_view) RelativeLayout emptyView;
    @BindView(R.id.relative_layout) RelativeLayout mRelativeLayout;
    @BindView(R.id.fab) FloatingActionButton btn;

    private ArrayList<Movie> mMovies;

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setEmptyView(emptyView);

        mMovies = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(MainActivity.this, mMovies);
        mRecyclerView.setAdapter(mMovieAdapter);

        // No clique do botão
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                                        RegisterActivity.class);
                startActivity(intent);
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mMovieAdapter.setOnItemClickListener(new MovieAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                Movie item = mMovies.get(itemPosition);
                long id = item.getId();

                Uri currentMovie = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                Log.d(LOG_TAG, String.valueOf(currentMovie));
                intent.setData(currentMovie);
                startActivity(intent);
            }
        });

        // Inicia o assincronismo
        getSupportLoaderManager().initLoader(LOADER_MOVIE,null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_MOVIE:
                String[] projection =  {
                    MovieContract.MovieEntry.COLUMN_ID,
                    MovieContract.MovieEntry.COLUMN_AGE_GROUP,
                    MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                    MovieContract.MovieEntry.COLUMN_GENRE
                };

                return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_MOVIE:
                mMovies = getMovies(data);
                mMovieAdapter.swapList(mMovies);
                mRecyclerView.setAdapter(mMovieAdapter);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_MOVIE:
                mMovieAdapter.swapList(null);
                mRecyclerView.setAdapter(mMovieAdapter);
                break;
        }
    }

    public ArrayList<Movie> getMovies(Cursor c) {
        ArrayList<Movie> movies = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                movies.add(createMovieFromCursor(c));
            }while(c.moveToNext());
        }
        return movies;
    }


    public Movie createMovieFromCursor(Cursor c) {
        if (c == null)
            return null;

        Movie movie = new Movie();

        movie.setId(c.getLong(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_ID)));
        movie.setName(c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME)));
        movie.setDirector(c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_DIRECTOR)));
        movie.setGenre(c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_GENRE)));
        movie.setAgeGroup(c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_AGE_GROUP)));
        movie.setReleaseDate(c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));

        return movie;

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MovieAdapter.ViewHolder) {
            String name = mMovies.get(viewHolder.getAdapterPosition()).getName();

            final Movie deletedItem = mMovies.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            mMovieAdapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar
                    .make(mRelativeLayout, name + " " + getResources().getString(R.string.snackbar_message1), Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.snackbar_message2, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mMovieAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.accent));
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

       // MenuItem item = menu.findItem(R.id.menu_item_share);
       // mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertDummyData();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPets() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle(R.string.warning_dialog);
        alertBuilder.setMessage(R.string.delete_all_movies_dialog);

        alertBuilder.setPositiveButton(R.string.responsibilities_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null,  null);
            }
        });

        alertBuilder.setNegativeButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    private void insertDummyData (){
        ArrayList<Movie> movies = new ArrayList<>();
        /*String mName, int mGenre, String mDirector, int mAgeGroup, int mReleaseDate*/
        movies.add(new Movie("Não Vai Dar", 0, "Kay Cannon", 5, 2018));
        movies.add(new Movie("Um lugar silencioso", 2, "John Krasinski", 7, 2018));
        movies.add(new Movie("Miracle Season", 3, "Sean McNamara", 5, 2018));
        movies.add(new Movie("Miracle Season", 3, "Sean McNamara", 6, 2018));
        movies.add(new Movie("Miracle Season", 3, "Sean McNamara", 8, 2018));
        movies.add(new Movie("Miracle Season", 3, "Sean McNamara", 9, 2018));
        movies.add(new Movie("Miracle Season", 3, "Sean McNamara", 10, 2018));

        for (Movie movie:
             movies) {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
            values.put(MovieContract.MovieEntry.COLUMN_NAME, movie.getName());
            values.put(MovieContract.MovieEntry.COLUMN_DIRECTOR, movie.getDirector());
            values.put(MovieContract.MovieEntry.COLUMN_AGE_GROUP, movie.getAgeGroup());
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            values.put(MovieContract.MovieEntry.COLUMN_GENRE, movie.getGenre());

            Uri newUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
        }
    }
}
