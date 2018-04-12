package coltectp.github.io.movieme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AgeSpinnerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int[] spinnerImages;

    public AgeSpinnerAdapter(@NonNull Context context, int[] images) {
        super(context, R.layout.spinner_row);
        this.spinnerImages = images;
        mContext = context;
    }

    @Override
    public int getCount() {
        return this.spinnerImages.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.spinner_row, parent, false);

            mViewHolder.ageGroupImageView = (ImageView) convertView.findViewById(R.id.spinnerImage);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.ageGroupImageView.setImageResource(spinnerImages[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        ImageView ageGroupImageView;
    }
}
