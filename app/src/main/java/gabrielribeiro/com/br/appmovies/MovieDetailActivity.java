package gabrielribeiro.com.br.appmovies;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import gabrielribeiro.com.br.appmovies.Adapter.TrailerAdapter;
import gabrielribeiro.com.br.appmovies.Model.TrailerModelResponse;
import gabrielribeiro.com.br.appmovies.database.AppDatabase;
import gabrielribeiro.com.br.appmovies.database.MovieEntry;
import gabrielribeiro.com.br.appmovies.utils.ImageConverter;
import gabrielribeiro.com.br.appmovies.utils.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity {

    public TextView tvOriginalTitle;
    public TextView tvSinopse;
    public TextView tvVoteAvarage;
    public TextView tvReleaseDate;
    public ImageView ivPoster;
    public MovieEntry movieObject;
    public RecyclerView recyclerViewTrailer;
    public RecyclerView.Adapter mTrailerAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public Button mButtonFavorite;
    List<MovieEntry> listMovies;
    public Boolean isMoviePresent = false;

    public byte[] poster;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);

        //set database instance
        mDb = AppDatabase.getInstance(getApplicationContext());

        //get view referencies
        ivPoster = findViewById(R.id.movieArt);
        tvOriginalTitle = findViewById(R.id.tvOriginalTitle);
        tvVoteAvarage = findViewById(R.id.tvVoteAvarage);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvSinopse = findViewById(R.id.tvSinopse);
        recyclerViewTrailer = findViewById(R.id.recyclerViewTrailer);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTrailer.setLayoutManager(mLayoutManager);
        mButtonFavorite = findViewById(R.id.btnFavorite);

        //setup values from intent
        movieObject = new MovieEntry(getIntent().getStringExtra("ORIGINAL_TITLE"),
                getIntent().getStringExtra("ID"),
                getIntent().getStringExtra("SINOPSE"),
                getIntent().getStringExtra("VOTE_AVARAGE"),
                getIntent().getStringExtra("RELEASE_DATE"));

        //setup view values
        tvOriginalTitle.setText(movieObject.getOriginalTitle());
        tvVoteAvarage.setText(movieObject.getVoteAverage());
        tvReleaseDate.setText(movieObject.getReleaseDate());
        tvSinopse.setText(movieObject.getSinopse());

        mButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteButtonClicked();
            }
        });


        if(isNetworkAvailable()) {
            Picasso.with(getBaseContext()).load("http://image.tmdb.org/t/p/w342" + getIntent().getStringExtra("POSTER_PATH"))
                    .into(ivPoster);
            makeTrailerSearchQuery();
        } else {
            movieObject.setPoster(getIntent().getByteArrayExtra("POSTER"));
            ivPoster.setImageBitmap(ImageConverter.ConvertByteArrayToBitmap(movieObject.getPoster()));
        }

        verifyIfmovieExists();
    }

    private void makeTrailerSearchQuery() {
        URL movieQuery = NetworkUtils.buildUrl("trailer", getString(R.string.public_api_key), movieObject.getIdMovie());
        new trailerQuerySearch().execute(movieQuery);
    }

    public void ParseJsonToTrailerList(String jsonData){

        Gson gson = new Gson();
        try{
            TrailerModelResponse response = gson.fromJson( jsonData, TrailerModelResponse.class );
            mTrailerAdapter = new TrailerAdapter(response.getResults());
            recyclerViewTrailer.setAdapter(mTrailerAdapter);
            mTrailerAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class trailerQuerySearch extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressIndicator.setVisibility(View.VISIBLE);
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
            //mProgressIndicator.setVisibility(View.INVISIBLE);

            if (searchResults != null && !searchResults.equals("")) {

                ParseJsonToTrailerList(searchResults);

            } else {
                //showErrorMessage();
            }
        }
    }

    public void onFavoriteButtonClicked() {

        poster = ImageConverter.ConvertImageToByteArray(((BitmapDrawable) ivPoster.getDrawable()).getBitmap());
        movieObject.setPoster(poster);


        if(isMoviePresent) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.taskDao().deleteMovie(movieObject.getIdMovie());
                    finish();
                }
            });
        } else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.taskDao().insertMovie(movieObject);
                    finish();
                }
            });
        }
    }

    public void verifyIfmovieExists() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String movieid = movieObject.getIdMovie();
                listMovies =  mDb.taskDao().searchMovie(movieObject.getIdMovie());

                if(listMovies.size() > 0) {
                    mButtonFavorite.setText("Desfavoritar");
                    isMoviePresent = true;
                }
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

}
