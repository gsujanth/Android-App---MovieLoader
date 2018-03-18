package com.example.princ.midpractice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchMoviesActivity extends AppCompatActivity implements GetMoviesTask.IMovie,MovieAdapter.DataUpdate {

    TextView searchMoviesTV,resultsTV;
    EditText searchMoviesET;
    Button searchButton;
    ListView searchMoviesLV;
    ArrayList<Movie> resultMovies=new ArrayList<>();
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);
        setTitle("Search Movies");

        searchMoviesTV=(TextView) findViewById(R.id.searchMoviesTV);
        resultsTV=(TextView) findViewById(R.id.resultsTV);
        searchMoviesET=(EditText) findViewById(R.id.searchMoviesET);
        searchButton=(Button) findViewById(R.id.searchButton);
        searchMoviesLV=(ListView) findViewById(R.id.searchMoviesLV);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] keywords=searchMoviesET.getText().toString().split(" ");
                String keyword="";
                for(String k:keywords) {
                    keyword=keyword+k+"+";
                }
                keyword = keyword.substring(0, keyword.length() - 1);
                if(isConnected()) {
                    if(keyword.length()>0) {
                        new GetMoviesTask(SearchMoviesActivity.this, SearchMoviesActivity.this).execute("https://api.themoviedb.org/3/search/movie?query=" + keyword + "&api_key=5115e2624b0878a6516b41630d4a7fc1&page=1");
                    }else {
                        Toast.makeText(SearchMoviesActivity.this, "Enter Keyword", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SearchMoviesActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

       searchMoviesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SearchMoviesActivity.this, l+" short click "+i, Toast.LENGTH_SHORT).show();
            }
        });

       searchMoviesLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               Toast.makeText(SearchMoviesActivity.this, l+" long click "+i, Toast.LENGTH_SHORT).show();
               return false;
           }
       });

    }



    @Override
    public void handleMovies(ArrayList<Movie> s) {
        resultMovies.clear();
        if(s!=null&&!s.isEmpty()) {
            resultMovies = s;
            movieAdapter = new MovieAdapter(this, R.layout.search_movies_layout, resultMovies,SearchMoviesActivity.this);
            searchMoviesLV.setAdapter(movieAdapter);
        }else{
            Toast.makeText(this, "No Movies Found", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||(networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void updateFavorite(int pos) {
        boolean flag;
        if(resultMovies.get(pos).isFavorite()){
            flag=false;
        }else{
            flag=true;
        }
            resultMovies.get(pos).setFavorite(flag);
            movieAdapter.notifyDataSetChanged();
    }
}
