package gabrielribeiro.com.br.appmovies.Adapter;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gabrielribeiro.com.br.appmovies.Model.MovieDetailModel;
import gabrielribeiro.com.br.appmovies.MovieDetailActivity;
import gabrielribeiro.com.br.appmovies.R;
import gabrielribeiro.com.br.appmovies.database.MovieEntry;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieDetailModel> movieListResults;

    public void setMovies(List<MovieEntry> movieEntries) {
        movieListResults = new ArrayList<>();
        for (int i = 0; i < movieEntries.size(); i++) {
            MovieDetailModel model = new MovieDetailModel();
            model.setId(movieEntries.get(i).getIdMovie());
            model.setOriginal_title(movieEntries.get(i).getOriginalTitle());
            model.setRelease_date(movieEntries.get(i).getReleaseDate());
            model.setOverview(movieEntries.get(i).getSinopse());
            model.setOriginal_title(movieEntries.get(i).getOriginalTitle());
            model.setPoster(movieEntries.get(i).getPoster());
            movieListResults.add(model);
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mImageView = v.findViewById(R.id.ivListItemMovieCartas);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
            int position = getAdapterPosition();

            intent.putExtra("ORIGINAL_TITLE", movieListResults.get(position).getOriginal_title());
            intent.putExtra("POSTER_PATH", movieListResults.get(position).getPoster_path());
            intent.putExtra("SINOPSE", movieListResults.get(position).getOverview());
            intent.putExtra("VOTE_AVARAGE", movieListResults.get(position).getVote_average());
            intent.putExtra("RELEASE_DATE", movieListResults.get(position).getRelease_date());
            intent.putExtra("ID", movieListResults.get(position).getId());
            intent.putExtra("POSTER", movieListResults.get(position).getPoster());

            v.getContext().startActivity(intent);
        }
    }

    public MovieAdapter() { }

    public void setMovieList(List<MovieDetailModel> myDataset) {
        movieListResults = myDataset;
        this.notifyDataSetChanged();
    }


    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(movieListResults.get(position).getPoster_path() != null) {
            Picasso.with(holder.mImageView.getContext()).load("http://image.tmdb.org/t/p/w342" + movieListResults.get(position).getPoster_path()).into(holder.mImageView);
        } else {
            byte[] posterImage = movieListResults.get(position).getPoster();
            holder.mImageView.setImageBitmap(BitmapFactory.decodeByteArray
                    (posterImage,0,posterImage.length));
        }
    }

    @Override
    public int getItemCount() {
        return movieListResults.size();
    }

}

