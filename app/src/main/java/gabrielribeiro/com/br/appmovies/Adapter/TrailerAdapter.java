package gabrielribeiro.com.br.appmovies.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gabrielribeiro.com.br.appmovies.Model.MovieDetailModel;
import gabrielribeiro.com.br.appmovies.Model.TrailerDetailModel;
import gabrielribeiro.com.br.appmovies.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private TrailerDetailModel[] trailerListResults;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mTextView = v.findViewById(R.id.tvMovieTitle);
        }

        @Override
        public void onClick(View v) {
            int AdapterPosition = getAdapterPosition();
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerListResults[AdapterPosition].getKey()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailerListResults[AdapterPosition].getKey()));
            try {
                v.getContext().startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                v.getContext().startActivity(webIntent);
            }
        }
    }


    public TrailerAdapter(TrailerDetailModel[] myDataset) {
        trailerListResults = myDataset;
    }


    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(Integer.toString(position));
    }

    @Override
    public int getItemCount() {
        return trailerListResults.length;
    }

}

