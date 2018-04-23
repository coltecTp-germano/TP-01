package coltectp.github.io.movieme;

import android.arch.persistence.room.Relation;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Context;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.ContentUris;
import android.net.Uri;
import android.widget.RelativeLayout;

import butterknife.BindView;
import coltectp.github.io.movieme.data.Movie;
import coltectp.github.io.movieme.provider.MovieContract;
import coltectp.github.io.movieme.provider.MovieProvider;

import static butterknife.ButterKnife.bind;
import static coltectp.github.io.movieme.provider.MovieContract.*;

/**
 * Created by Germano Barcelos on 29/03/2018.
 */

public class MovieAdapter extends EmptyRecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static ClickListener clickListener;
    private Context mContext;
    private ArrayList<Movie> mMovies;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
    }

    public void swapList(ArrayList<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mNameMovie.setText(mMovies.get(position).getName());

        holder.mDirector.setText(mMovies.get(position).getDirector());

        if (mMovies.get(position).getReleaseDate() == 0) {
            holder.mReleaseDate.setText(R.string.release_date_undefined);
        } else{
            holder.mReleaseDate.setText(String.valueOf(mMovies.get(position).getReleaseDate()));
        }


        setGenreTextView(holder, mMovies.get(position).getGenre());

        setAgeGroupImageResource(holder, mMovies.get(position).getAgeGroup());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    private void setAgeGroupImageResource (ViewHolder holder, int ageGroup) {
        switch (ageGroup) {
            case MovieEntry.AGE_GROUP_L:
                holder.mAgeGroup.setImageResource(R.drawable.icon_free);
                break;
            case MovieEntry.AGE_GROUP_10:
                holder.mAgeGroup.setImageResource(R.drawable.icon_ten);
                break;
            case MovieEntry.AGE_GROUP_12:
                holder.mAgeGroup.setImageResource(R.drawable.icon_twelve);
                break;
            case MovieEntry.AGE_GROUP_14:
                holder.mAgeGroup.setImageResource(R.drawable.icon_fourteen);
                break;
            case MovieEntry.AGE_GROUP_16:
                holder.mAgeGroup.setImageResource(R.drawable.icon_sixteen);
                break;
            case MovieEntry.AGE_GROUP_18:
                holder.mAgeGroup.setImageResource(R.drawable.icon_eighteen);
                break;
            default:
                holder.mAgeGroup.setImageResource(R.drawable.icon_free);
        }
    }

    private void setGenreTextView (ViewHolder holder, int genre) {
        switch (genre) {
            case MovieEntry.GENRE_ADVENTURE:
                holder.mGenre.setText(R.string.adventure);
                break;
            case MovieEntry.GENRE_ROMANCE:
                holder.mGenre.setText(R.string.romance);
                break;
            case MovieEntry.GENRE_SUSPENSE:
                holder.mGenre.setText(R.string.suspense);
                break;
            case MovieEntry.GENRE_TERROR:
                holder.mGenre.setText(R.string.terror);
                break;
            case MovieEntry.GENRE_FICTION:
                holder.mGenre.setText(R.string.fiction);
                break;
            case MovieEntry.GENRE_OTHER:
                holder.mGenre.setText(R.string.other);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void removeItem(int position) {
        Movie movie = mMovies.get(position);
        long id = movie.getId();

        Uri currentMovie = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
        int count = mContext.getContentResolver().delete(currentMovie, MovieEntry.COLUMN_ID + "=" + id, null);

        notifyItemRemoved(position);
    }

    public void restoreItem(Movie movie, int position) {

        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_NAME, movie.getName());
        values.put(MovieContract.MovieEntry.COLUMN_DIRECTOR, movie.getDirector());
        values.put(MovieContract.MovieEntry.COLUMN_AGE_GROUP, movie.getAgeGroup());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_GENRE, movie.getGenre());
        values.put(MovieContract.MovieEntry.COLUMN_DIRECTOR, movie.getDirector());

        Uri newUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, values);
        mMovies.add(position, movie);
        notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       @BindView(R.id.lm_name_tv) TextView mNameMovie;
       @BindView(R.id.lm_release_date_tv) TextView mReleaseDate;
       @BindView(R.id.lm_genre_tv) TextView mGenre;
       @BindView(R.id.lm_director_tv) TextView mDirector;
       @BindView(R.id.lm_icon_iv) ImageView mAgeGroup;
       @BindView(R.id.view_background) RelativeLayout viewBackground;
       @BindView(R.id.view_foreground) RelativeLayout viewForeground;

        ViewHolder (View view) {
            super(view);
            itemView.setOnClickListener(this);
            bind(this, view);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MovieAdapter.clickListener = clickListener;
    }
}
