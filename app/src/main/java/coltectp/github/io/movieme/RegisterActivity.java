package coltectp.github.io.movieme;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import butterknife.BindView;
import coltectp.github.io.movieme.data.Movie;
import coltectp.github.io.movieme.provider.MovieContract;

import static butterknife.ButterKnife.bind;

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.generos) Spinner mSpinnerGenre;
    @BindView(R.id.idades) Spinner mSpinnerAges;
    @BindView(R.id.ano_lancamento) EditText mEditTextReleaseDate;
    @BindView(R.id.diretor) EditText mEditTextDiretor;
    @BindView(R.id.nome) EditText mEditTextNome;

    private int mAge = MovieContract.MovieEntry.AGE_GROUP_L;
    private int mGenre = MovieContract.MovieEntry.GENRE_OTHER;
    private boolean mMovieHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mMovieHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        bind(this);

        mEditTextDiretor.setOnTouchListener(mTouchListener);
        mEditTextNome.setOnTouchListener(mTouchListener);
        mEditTextReleaseDate.setOnTouchListener(mTouchListener);
        mSpinnerAges.setOnTouchListener(mTouchListener);
        mSpinnerGenre.setOnTouchListener(mTouchListener);

        setupAgeSpinner();
        setupGenreSpinner();
    }

    private void setupAgeSpinner () {

        int[] imagesSpinner = new int[]{
                R.drawable.icon_free,
                R.drawable.icon_ten,
                R.drawable.icon_twelve,
                R.drawable.icon_fourteen,
                R.drawable.icon_sixteen,
                R.drawable.icon_eighteen
        };

        AgeSpinnerAdapter mCustomAdapter =
                new AgeSpinnerAdapter(this, imagesSpinner);
        mSpinnerAges.setAdapter(mCustomAdapter);


        mSpinnerAges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    mAge = MovieContract.MovieEntry.AGE_GROUP_L;
                } else if (position == 1) {
                    mAge = MovieContract.MovieEntry.AGE_GROUP_10;
                } else if (position == 2){
                    mAge = MovieContract.MovieEntry.AGE_GROUP_12;
                } else if (position == 3) {
                    mAge = MovieContract.MovieEntry.AGE_GROUP_14;
                } else if (position == 4) {
                    mAge = MovieContract.MovieEntry.AGE_GROUP_16;
                } else if (position == 5) {
                    mAge = MovieContract.MovieEntry.AGE_GROUP_18;
                } else {
                    mAge = MovieContract.MovieEntry.AGE_GROUP_L;
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAge = MovieContract.MovieEntry.AGE_GROUP_L;
            }
        });
    }

    private void setupGenreSpinner () {
        ArrayAdapter<CharSequence> genreSpinnerAdapter = ArrayAdapter
                .createFromResource(this,
                R.array.genres, android.R.layout.simple_spinner_item);

        genreSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSpinnerGenre.setAdapter(genreSpinnerAdapter);

        mSpinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.adventure))) {
                        mGenre = MovieContract.MovieEntry.GENRE_ADVENTURE;
                    } else if (selection.equals(getString(R.string.romance))) {
                        mGenre = MovieContract.MovieEntry.GENRE_ROMANCE;
                    } else if (selection.equals(getString(R.string.terror))){
                        mGenre = MovieContract.MovieEntry.GENRE_TERROR;
                    } else if (selection.equals(getString(R.string.suspense))) {
                        mGenre = MovieContract.MovieEntry.GENRE_SUSPENSE;
                    } else if (selection.equals(getString(R.string.fiction))) {
                        mGenre = MovieContract.MovieEntry.GENRE_FICTION;
                    } else {
                        mGenre = MovieContract.MovieEntry.GENRE_OTHER;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGenre = MovieContract.MovieEntry.GENRE_OTHER;
            }
        });
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_save:
                saveMovie();
                finish();
                return true;
            case android.R.id.home:
                if (!mMovieHasChanged) {
                    NavUtils.navigateUpFromSameTask(RegisterActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(RegisterActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveMovie() {
        String name = mEditTextNome.getText().toString().trim();
        String diretor = mEditTextDiretor.getText().toString().trim();
        int age = mAge;
        int genre = mGenre;

        if (mEditTextDiretor.getText().toString() == null ||
                mEditTextNome.getText().toString() == null ||
                mEditTextReleaseDate.getText().toString() == null) {

        }


        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_NAME, name);
        values.put(MovieContract.MovieEntry.COLUMN_DIRECTOR, diretor);
        values.put(MovieContract.MovieEntry.COLUMN_AGE_GROUP, age);
        values.put(MovieContract.MovieEntry.COLUMN_GENRE, genre);

        int releaseDate;
        if (TextUtils.isEmpty(mEditTextReleaseDate.getText().toString().trim())) {
            releaseDate = 0;
        } else {
            releaseDate = Integer.parseInt(mEditTextReleaseDate.getText().toString().trim());
        }

        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);

        Uri newUri = null;
        newUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
        if (newUri == null ) {
            Toast.makeText(this, R.string.error_insert, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.success_insert, Toast.LENGTH_SHORT).show();
        }
    }
}
