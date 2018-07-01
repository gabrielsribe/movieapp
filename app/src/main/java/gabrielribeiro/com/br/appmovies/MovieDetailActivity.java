package gabrielribeiro.com.br.appmovies;

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

import gabrielribeiro.com.br.appmovies.Adapter.TrailerAdapter;
import gabrielribeiro.com.br.appmovies.Model.TrailerModelResponse;
import gabrielribeiro.com.br.appmovies.database.AppDatabase;
import gabrielribeiro.com.br.appmovies.database.MovieEntry;
import gabrielribeiro.com.br.appmovies.utils.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity {

    public TextView tvOriginalTitle;
    public TextView tvSinopse;
    public TextView tvVoteAvarage;
    public TextView tvReleaseDate;
    public ImageView ivPoster;
    public RecyclerView recyclerViewTrailer;
    public RecyclerView.Adapter mTrailerAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public Button mButtonFavorite;

    public String movieId;
    public String originalTitle;
    public String releaseDate;
    public String voteAverage;
    public String sinopse;

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
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTrailer.setLayoutManager(mLayoutManager);
        mButtonFavorite = findViewById(R.id.btnFavorite);

        //setup values from intent
        movieId = getIntent().getStringExtra("ID");
        originalTitle = getIntent().getStringExtra("ORIGINAL_TITLE");
        releaseDate = getIntent().getStringExtra("RELEASE_DATE");
        voteAverage = getIntent().getStringExtra("VOTE_AVARAGE");
        sinopse = getIntent().getStringExtra("SINOPSE");


        tvOriginalTitle.setText(originalTitle);
        tvVoteAvarage.setText(voteAverage);
        tvReleaseDate.setText(releaseDate);
        tvSinopse.setText(sinopse);
        Picasso.with(getBaseContext()).load("http://image.tmdb.org/t/p/w500" + getIntent().getStringExtra("POSTER_PATH"))
                .into(ivPoster);

        mButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteButtonClicked();
            }
        });

        makeTrailerSearchQuery();

    }

    private void makeTrailerSearchQuery() {
        URL movieQuery = NetworkUtils.buildUrl("trailer", getString(R.string.public_api_key), movieId);
        new trailerQuerySearch().execute(movieQuery);
    }

    public void ParseJsonToMovieList(String jsonData){

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

                ParseJsonToMovieList(searchResults);

            } else {
                //showErrorMessage();
            }
        }
    }

    public void onFavoriteButtonClicked() {

        //precisamos verificar se o filme ja existe no bd, caso exista
        //preciso trocar o texto do botão e mudar a action
        //para remover o filme
        //AppExecutors.getInstance().diskIO().execute(new Runnable() {
        //                    @Override
        //                    public void run() {
        //                        // COMPLETED (3) get the position from the viewHolder parameter
        //                        int position = viewHolder.getAdapterPosition();
        //                        List<TaskEntry> tasks = mAdapter.getTasks();
        //                        // COMPLETED (4) Call deleteTask in the taskDao with the task at that position
        //                        mDb.taskDao().deleteTask(tasks.get(position));
        //                        // COMPLETED (6) Call retrieveTasks method to refresh the UI
        //                        retrieveTasks();
        //                    }
        //                });
        //(String originalTitle, int idMovie, String sinopse, String voteAverage,
        //                      String releaseDate, Date updatedAt) {
        final MovieEntry movieEntry = new MovieEntry(originalTitle, movieId, sinopse, voteAverage,
                                     releaseDate);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // COMPLETED (3) Move the remaining logic inside the run method
                mDb.taskDao().insertMovie(movieEntry);
                finish();
            }
        });
    }

}
