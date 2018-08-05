package gabrielribeiro.com.br.appmovies.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gabrielribeiro.com.br.appmovies.Model.ReviewDetailModel;
import gabrielribeiro.com.br.appmovies.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ReviewDetailModel[] reviewListResults;


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mAuthorName;
        public TextView mComment;


        public ViewHolder(View v) {
            super(v);
            mAuthorName = v.findViewById(R.id.tvAuthorName);
            mComment = v.findViewById(R.id.tvComment);
        }
    }


    public ReviewAdapter(ReviewDetailModel[] myDataset) {
        reviewListResults = myDataset;
    }


    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mAuthorName.setText(reviewListResults[position].getAuthor());
        holder.mComment.setText(reviewListResults[position].getContent());
    }

    @Override
    public int getItemCount() {
        return reviewListResults.length;
    }

}

