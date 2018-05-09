package gabrielribeiro.com.br.appmovies.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import gabrielribeiro.com.br.appmovies.Model.MovieDetailModel;
import gabrielribeiro.com.br.appmovies.MovieDetailActivity;
import gabrielribeiro.com.br.appmovies.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private MovieDetailModel[] movieListResults;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mImageView = v.findViewById(R.id.ivListItemMovieCartas);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
            int AdapterPosition = getAdapterPosition();

            intent.putExtra("ORIGINAL_TITLE", movieListResults[AdapterPosition].getOriginal_title());
            intent.putExtra("POSTER_PATH", movieListResults[AdapterPosition].getPoster_path());
            intent.putExtra("SINOPSE", movieListResults[AdapterPosition].getOverview());
            intent.putExtra("VOTE_AVARAGE", movieListResults[AdapterPosition].getVote_average());
            intent.putExtra("RELEASE_DATE", movieListResults[AdapterPosition].getRelease_date());

            v.getContext().startActivity(intent);
        }
    }


    public MovieAdapter(MovieDetailModel[] myDataset) {
        movieListResults = myDataset;
    }


    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(holder.mImageView.getContext()).load("http://image.tmdb.org/t/p/w342" + movieListResults[position].getPoster_path()).into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return movieListResults.length;
    }

}

