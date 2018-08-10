package gabrielribeiro.com.br.appmovies;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.List;

import gabrielribeiro.com.br.appmovies.Adapter.MovieAdapter;
import gabrielribeiro.com.br.appmovies.Model.MovieModelResponse;
import gabrielribeiro.com.br.appmovies.database.AppDatabase;
import gabrielribeiro.com.br.appmovies.database.MovieEntry;
import gabrielribeiro.com.br.appmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public MovieAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ProgressBar mProgressIndicator;
    public TextView tvError;
    private AppDatabase mDb;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private boolean isFavoriteList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rvMovie);
        mProgressIndicator = findViewById(R.id.pb_loading_indicator);
        tvError = findViewById(R.id.tvErrorMessage);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDb = AppDatabase.getInstance(getApplicationContext());

        requestPermissions();

        if(!isNetworkAvailable()) {
            showErrorMessage();
        }

        if(isNetworkAvailable()){
            showResults();
            makeMovieDbSearchQuery("popular");
        } else {
            showErrorMessage();
        }

        mAdapter = new MovieAdapter();
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
        showResults();

        if(!isNetworkAvailable()) {
            showErrorMessage();
        }

        if (itemThatWasClickedId == R.id.action_popular && isNetworkAvailable()) {
            showResults();
            isFavoriteList = false;
            makeMovieDbSearchQuery("popular");
            return true;
        }
        if (itemThatWasClickedId == R.id.action_top_rated && isNetworkAvailable()) {
            showResults();
            isFavoriteList = false;
            makeMovieDbSearchQuery("top_rated");
            return true;
        }
        if (itemThatWasClickedId == R.id.action_top_favorites) {
            showResults();
            isFavoriteList = true;
            retrieveMoviesOffline();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ParseJsonToMovieList(String jsonData){

        Gson gson = new Gson();
        try{
            MovieModelResponse response = gson.fromJson( jsonData, MovieModelResponse.class );
            mAdapter.setMovieList(response.getResults());
            mRecyclerView.setAdapter(mAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

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

    private void retrieveMoviesOffline() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<MovieEntry> movies = mDb.taskDao().loadAllMovies();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mRecyclerView.getAdapter() == null) {
                            mAdapter.setMovies(movies);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setMovies(movies);
                        }
                    }
                });
            }
        });
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
            if (resultCode == RESULT_OK && isFavoriteList) {
                retrieveMoviesOffline();
            }
            if(resultCode == RESULT_FIRST_USER) {
                Toast.makeText(getBaseContext(), "Sem conexão com a internet para ver os detalhes", Toast.LENGTH_SHORT).show();
            }
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

}