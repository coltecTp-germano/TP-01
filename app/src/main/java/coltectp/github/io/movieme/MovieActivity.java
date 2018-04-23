package coltectp.github.io.movieme;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.ShareActionProvider;

import org.w3c.dom.Text;

import butterknife.BindView;
import coltectp.github.io.movieme.provider.MovieContract;

import static butterknife.ButterKnife.bind;

public class MovieActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri mMovieDetailsUri;
    private static final int LOADER_ID = 1;
    @BindView(R.id.generos) TextView mTextGenre;
    @BindView(R.id.idades) ImageView mTextAges;
    @BindView(R.id.ano_lancamento) TextView mTextReleaseDate;
    @BindView(R.id.diretor) TextView mTextDiretor;
    @BindView(R.id.nome) TextView mTextNome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        bind(this);

        Intent intent = getIntent();
        mMovieDetailsUri = intent.getData();

        if (mMovieDetailsUri != null) {
            getSupportLoaderManager().initLoader(LOADER_ID,null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider shareProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        // criar intent que será exibida pelo provider
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");

        StringBuilder movie = new StringBuilder();
        movie   .append(mTextDiretor.getText().toString())
                .append("\n")
                .append(mTextGenre.getText().toString())
                .append("\n")
                .append(mTextNome.getText().toString())
                .append("\n")
                .append(mTextReleaseDate.getText().toString());

        intent.putExtra(Intent.EXTRA_TEXT, movie.toString());

        // exibe a intent
        shareProvider.setShareIntent(intent);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                String[] projection =  {
                        MovieContract.MovieEntry.COLUMN_ID,
                        MovieContract.MovieEntry.COLUMN_AGE_GROUP,
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                        MovieContract.MovieEntry.COLUMN_GENRE
                };


                return new CursorLoader(this, mMovieDetailsUri,
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
        if (data == null || data.getCount() < 1) {
            return;
        }

        switch (loader.getId()) {
            case LOADER_ID:
                if (data.moveToNext()) {
                    setAgeGroupImageResource
                            (data.getInt(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_AGE_GROUP)));
                    setGenreTextView(data.getInt(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE)));
                    mTextNome.setText(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME)));
                    mTextDiretor.setText(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_DIRECTOR)));
                    mTextReleaseDate.setText(String.valueOf
                            (data.getInt(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE))));
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mTextDiretor.setText("");
        mTextGenre.setText("");
        mTextNome.setText("");
        mTextReleaseDate.setText("");
    }

    private void setAgeGroupImageResource (int ageGroup) {
        switch (ageGroup) {
            case MovieContract.MovieEntry.AGE_GROUP_L:
                mTextAges.setImageResource(R.drawable.icon_free);
                break;
            case MovieContract.MovieEntry.AGE_GROUP_10:
                mTextAges.setImageResource(R.drawable.icon_ten);
                break;
            case MovieContract.MovieEntry.AGE_GROUP_12:
                mTextAges.setImageResource(R.drawable.icon_twelve);
                break;
            case MovieContract.MovieEntry.AGE_GROUP_14:
                mTextAges.setImageResource(R.drawable.icon_fourteen);
                break;
            case MovieContract.MovieEntry.AGE_GROUP_16:
                mTextAges.setImageResource(R.drawable.icon_sixteen);
                break;
            case MovieContract.MovieEntry.AGE_GROUP_18:
                mTextAges.setImageResource(R.drawable.icon_eighteen);
                break;
            default:
                mTextAges.setImageResource(R.drawable.icon_free);
        }
    }

    private void setGenreTextView (int genre) {
        switch (genre) {
            case MovieContract.MovieEntry.GENRE_ADVENTURE:
                mTextGenre.setText(R.string.adventure);
                break;
            case MovieContract.MovieEntry.GENRE_ROMANCE:
                mTextGenre.setText(R.string.romance);
                break;
            case MovieContract.MovieEntry.GENRE_SUSPENSE:
                mTextGenre.setText(R.string.suspense);
                break;
            case MovieContract.MovieEntry.GENRE_TERROR:
                mTextGenre.setText(R.string.terror);
                break;
            case MovieContract.MovieEntry.GENRE_FICTION:
                mTextGenre.setText(R.string.fiction);
                break;
            case MovieContract.MovieEntry.GENRE_OTHER:
                mTextGenre.setText(R.string.other);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
