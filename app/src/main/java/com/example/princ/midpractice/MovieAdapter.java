package com.example.princ.midpractice;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {

    Movie movie;
    TextView tvTitle, tvReleaseDate;
    ImageView imageView;
    ImageButton favButton;
    Context ctx;
    DataUpdate dUpdate;

    public MovieAdapter(@NonNull Context context, int resource, List<Movie> objects, SearchMoviesActivity searchMoviesActivity) {
        super(context, resource, objects);
        this.ctx = context;
        dUpdate=searchMoviesActivity;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        movie = getItem(position);

        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_movies_layout, parent, false);
        }
        Log.d("demo", "getView: "+convertView.findViewById(R.id.tvTitle).getClass());
        tvTitle = convertView.findViewById(R.id.tvTitle);
        tvReleaseDate = convertView.findViewById(R.id.tvReleaseDate);
        imageView = convertView.findViewById(R.id.imageView);
        favButton = convertView.findViewById(R.id.favButton);
        tvTitle.setText(movie.movie_name);
        tvReleaseDate.setText(movie.release_date);
        if (isConnected()) {
            Picasso.with(convertView.getContext()).load("http://image.tmdb.org/t/p/w154"+movie.poster_path).into(imageView);
        } else {
            Toast.makeText(ctx, "No Internet", Toast.LENGTH_SHORT).show();
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", "onClick: "+position);
                dUpdate.updateFavorite(position);
            }
        });

        if(movie.isFavorite){
                favButton.setBackgroundColor(Color.GREEN);
        }else{
            favButton.setBackgroundColor(Color.GRAY);
        }
//hi
        return convertView;

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    public interface DataUpdate{
        void updateFavorite(int pos);
    }
}
