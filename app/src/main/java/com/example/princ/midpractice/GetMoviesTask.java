package com.example.princ.midpractice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    IMovie iMovie;
    Context ctx;
    ProgressDialog pDlg;

    public GetMoviesTask(IMovie iMovie,Context ctx) {
        this.iMovie = iMovie;
        this.ctx=ctx;
        pDlg = new ProgressDialog(ctx);
        pDlg.setMessage("Loading Related Movies...");
        pDlg.setCancelable(false);
        pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDlg.show();
    }

    HttpURLConnection con;
    @Override
    protected ArrayList<Movie> doInBackground(String... strings) {
        ArrayList<Movie> result = new ArrayList<>();

        try {
            URL url = new URL(strings[0]);
            con = (HttpURLConnection) url.openConnection();
            con.connect();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(con.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                JSONArray results = root.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieJson = results.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.rating = movieJson.getString("vote_average");
                    movie.movie_name = movieJson.getString("title");
                    movie.overview = movieJson.getString("overview");
                    movie.release_date = movieJson.getString("release_date");
                    movie.popularity=movieJson.getString("popularity");
                    movie.poster_path=movieJson.getString("poster_path");
                    result.add(movie);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> s) {
        iMovie.handleMovies(s);
        pDlg.dismiss();
    }

    public static interface IMovie {
        public void handleMovies(ArrayList<Movie> s);
    }
}
