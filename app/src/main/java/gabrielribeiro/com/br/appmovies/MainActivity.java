package gabrielribeiro.com.br.appmovies;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import gabrielribeiro.com.br.appmovies.Adapter.MovieAdapter;
import gabrielribeiro.com.br.appmovies.Model.MovieModelResponse;
import gabrielribeiro.com.br.appmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ProgressBar mProgressIndicator;
    public TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rvMovie);
        mProgressIndicator = findViewById(R.id.pb_loading_indicator);
        tvError = findViewById(R.id.tvErrorMessage);

        mRecyclerView.setHasFixedSize(true);

        requestPermissions();

        if(isNetworkAvailable()) {
            makeMovieDbSearchQuery("popular");
            mLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
        }else{
            showErrorMessage();
        }
    }


    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
    }
    public void showResults(){
        mRecyclerView.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
    }

    public class movieQuerySearch extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieDbSearchResults = null;
            try {
                movieDbSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieDbSearchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            mProgressIndicator.setVisibility(View.INVISIBLE);

            if (searchResults != null && !searchResults.equals("")) {

                ParseJsonToMovieList(searchResults);

            } else {
                showErrorMessage();
            }
        }
    }

    private void makeMovieDbSearchQuery(String queryType) {
        URL movieQuery = NetworkUtils.buildUrl(queryType, getString(R.string.public_api_key), null);
        new movieQuerySearch().execute(movieQuery);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemThatWasClickedId = item.getItemId();

        if(!isNetworkAvailable()){
            showErrorMessage();
            return false;
        }else{
            showResults();
        }

        if (itemThatWasClickedId == R.id.action_popular) {
            makeMovieDbSearchQuery("popular");
            return true;
        }
        if (itemThatWasClickedId == R.id.action_top_rated) {
            makeMovieDbSearchQuery("top_rated");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ParseJsonToMovieList(String jsonData){

        Gson gson = new Gson();
        try{
            MovieModelResponse response = gson.fromJson( jsonData, MovieModelResponse.class );
            mAdapter = new MovieAdapter(response.getResults());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED &
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    showResults();
                } else {
                    Toast.makeText(MainActivity.this, "Necessário autorizar as permissões", Toast.LENGTH_SHORT).show();
                    showErrorMessage();
                }
            }
        }
    }
}