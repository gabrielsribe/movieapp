package gabrielribeiro.com.br.appmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    public TextView tvOriginalTitle;
    public TextView tvSinopse;
    public TextView tvVoteAvarage;
    public TextView tvReleaseDate;
    public ImageView ivPoster;

    /* intent.putExtra("ORIGINAL_TITLE", movieListResults[AdapterPosition].getOriginal_title());
            intent.putExtra("POSTER_PATH", movieListResults[AdapterPosition].getPoster_path());
            intent.putExtra("SINOPSE", movieListResults[AdapterPosition].getOverview());
            intent.putExtra("VOTE_AVARAGE", movieListResults[AdapterPosition].getVote_average());
            intent.putExtra("RELEASE_DATE", movieListResults[AdapterPosition].getRelease_date());*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);

        ivPoster = findViewById(R.id.movieArt);
        tvOriginalTitle = findViewById(R.id.tvOriginalTitle);
        tvVoteAvarage = findViewById(R.id.tvVoteAvarage);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvSinopse = findViewById(R.id.tvSinopse);

        tvOriginalTitle.setText(getIntent().getStringExtra("ORIGINAL_TITLE"));
        tvVoteAvarage.setText(getIntent().getStringExtra("VOTE_AVARAGE"));
        tvReleaseDate.setText(getIntent().getStringExtra("RELEASE_DATE"));
        tvSinopse.setText(getIntent().getStringExtra("SINOPSE"));
        Picasso.with(getBaseContext()).load("http://image.tmdb.org/t/p/w500" + getIntent().getStringExtra("POSTER_PATH"))
                .into(ivPoster);

    }

    public void showData(){

        tvOriginalTitle.setText(getIntent().getStringExtra("ORIGINAL_TITLE"));
        tvVoteAvarage.setText(getIntent().getStringExtra("VOTE_AVARAGE"));
        tvReleaseDate.setText(getIntent().getStringExtra("RELEASE_DATE"));
        tvSinopse.setText(getIntent().getStringExtra("SINOPSE"));
        Picasso.with(getBaseContext()).load("http://image.tmdb.org/t/p/w342" + getIntent().getStringExtra("POSTER_PATH"))
                .into(ivPoster);
    }

}
