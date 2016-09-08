package com.calgen.prodek.fadflicks.Adapter;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.Fragment.ReadMoreDialog;
import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.Parser;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.model.MovieBundle;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gurupad on 02-Sep-16.
 * You asked me to change it for no reason.
 */
public class DetailMovieAdapter extends RecyclerView.Adapter<DetailMovieAdapter.BaseViewHolder> {

    private static final String TAG = DetailMovieAdapter.class.getSimpleName();
    private boolean mIsLargeLayout;
    private MovieBundle movieBundle;
    private Context context;

    public DetailMovieAdapter(Context context, MovieBundle movieBundle) {
        this.context = context;
        this.movieBundle = movieBundle;
        mIsLargeLayout = context.getResources().getBoolean(R.bool.large_layout);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_detail_base, parent, false);
        return new BaseViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Movie movie = movieBundle.movie;

        Picasso.with(context).load(Parser.formatImageUrl(movie.getPosterPath(), context.getString(R.string.image_size_small)))
                .into(holder.poster);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        // TODO: 09-Sep-16 Make use of string formats, like it was done in sunshine
        holder.runtime.setText(movieBundle.movieDetails.getRuntime() + " " + context.getString(R.string.minutes));
        holder.movieRating.setRating((float) (movie.getVoteAverage() / 2));
        holder.plot.setText(movie.getOverview());

        // Now initialize recycler views and assign them new adapters
        ReviewAdapter reviewAdapter = new ReviewAdapter(context, movieBundle.reviewResponse);
        holder.cardReview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.cardReview.setNestedScrollingEnabled(false);
        holder.cardReview.setAdapter(reviewAdapter);

        CreditsAdapter creditsAdapter = new CreditsAdapter(context, movieBundle.credits);
        holder.cardCast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.cardCast.setHasFixedSize(true);
        holder.cardCast.setNestedScrollingEnabled(false);
        holder.cardCast.setAdapter(creditsAdapter);

        VideosAdapter videosAdapter = new VideosAdapter(context, movieBundle.videoResponse);
        holder.cardVideo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.cardVideo.setHasFixedSize(true);
        holder.cardVideo.setNestedScrollingEnabled(false);
        holder.cardVideo.setAdapter(videosAdapter);


    }

    @Override
    public int getItemCount() {
        return (movieBundle.movie == null) ? 0 : 1;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        //@formatter:off
        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.release_date) TextView releaseDate;
        @BindView(R.id.runtime) TextView runtime;
        @BindView(R.id.movie_rating) RatingBar movieRating;
        @BindView(R.id.plot_text) TextView plot;
        @BindView(R.id.read_more_details) Button moreDetails;
        @BindView(R.id.cast_recycler) RecyclerView cardCast;
        @BindView(R.id.trailer_recycler) RecyclerView cardVideo;
        @BindView(R.id.review_recycler) RecyclerView cardReview;
        //@formatter:on
        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.read_more_details)
        public void OnClick() {
            showDialog();
        }

        private void showDialog() {
            FragmentManager fragmentManager = ((FragmentActivity) context).getFragmentManager();
            ReadMoreDialog newFragment = new ReadMoreDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Intent.EXTRA_TEXT, movieBundle);
            newFragment.setArguments(bundle);

            if (mIsLargeLayout) {
                // The device is using a large layout, so show the fragment as a dialog
                newFragment.show(fragmentManager, ReadMoreDialog.TAG);
            } else {
                // The device is smaller, so show the fragment fullscreen
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // For a little polish, specify a transition animation
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                // To make it fullscreen, use the 'content' root view as the container
                // for the fragment, which is always the root view for the activity
                transaction.add(android.R.id.content, newFragment)
                        .addToBackStack(null).commit();
            }
        }
    }
}