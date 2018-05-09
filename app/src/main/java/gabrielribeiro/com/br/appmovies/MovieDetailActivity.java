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

}
