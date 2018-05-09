package gabrielribeiro.com.br.appmovies.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gabrielribeiro.com.br.appmovies.Model.MovieModel2;
import gabrielribeiro.com.br.appmovies.MovieDetailActivity;
import gabrielribeiro.com.br.appmovies.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private MovieModel2[] movieListResults;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        //public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            //mTextView = v.findViewById(R.id.tvListItemMovieTitle);
            mImageView = v.findViewById(R.id.ivListItemMovieCartas);
        }

        @Override
        public void onClick(View v) {
            Log.d("Teste:", "onClick ITEM CLICAAAAADOOO " );
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

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieAdapter(MovieModel2[] myDataset) {
        movieListResults = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.populate(movieListResults[position].getTitle());
        //holder.mTextView.setText(movieListResults[position].getTitle());
        Picasso.with(holder.mImageView.getContext()).load("http://image.tmdb.org/t/p/w342" + movieListResults[position].getPoster_path()).into(holder.mImageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return movieListResults.length;
    }

}

