package com.calgen.prodek.fadflicks.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.Review;
import com.calgen.prodek.fadflicks.model.ReviewResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurupad on 07-Sep-16.
 * You asked me to change it for no reason.
 */

// TODO: 07-Sep-16 LOGIC : add min of two reviewList and show more button if more exists
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, ReviewResponse review) {
        this.context = context;
        reviewList = review.getReviewResponses();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie_reviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.reviewerName.setText(review.getAuthor());

        holder.reviewText.setText(review.getContent());
        holder.reviewText.setMaxLines(3);
        holder.reviewText.setEllipsize(TextUtils.TruncateAt.END);

    }

    @Override
    public int getItemCount() {
        int size = reviewList.size();
        return (size > 2) ? 3 : size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = ViewHolder.class.getSimpleName();
        //@formatter:off
        @BindView(R.id.reviewer_name) TextView reviewerName;
        @BindView(R.id.review) TextView reviewText;
        //@formatter:on
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}