package com.example.vivekgoel.zumperproject.UI;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vivekgoel.zumperproject.DataModel.RestaurantDetails.Reviews;
import com.example.vivekgoel.zumperproject.R;

import java.util.ArrayList;

/**
 * Created by vivekgoel on 5/11/17.
 */

class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private Context context;
    private ArrayList<Reviews> reviews;
    private RecyclerView mRecyclerView;

    public ReviewsAdapter(Context context, RecyclerView recyclerView, final LinearLayoutManager linearLayoutManager, ArrayList<Reviews> result) {
        this.context = context;
        mRecyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
        this.reviews = result;
    }

    public void setData(ArrayList<Reviews> result, Reviews reviews) {
        this.reviews = result;
        notifyDataSetChanged();
        setLoaded();
    }

    @Override
    public int getItemViewType(int position) {
        return reviews.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item, parent, false);
            return new ReviewsAdapter.ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new ReviewsAdapter.LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReviewsAdapter.ItemViewHolder) {
            ReviewsAdapter.ItemViewHolder itemViewHolder = (ReviewsAdapter.ItemViewHolder) holder;

            itemViewHolder.tvTitle.setText(reviews.get(position).getAuthor_name());
            itemViewHolder.tvOpenNow.setText("Time: ");

            itemViewHolder.tvOpen.setText(reviews.get(position).getRelative_time_description());
            itemViewHolder.tvRating.setText(Float.toString(reviews.get(position).getRating()));
            if(reviews.get(position).getProfile_photo_url() == null)
                itemViewHolder.ivImage.setImageResource(R.mipmap.no_image);
            else
                Glide.with(context).load(reviews.get(position).getProfile_photo_url()).into(itemViewHolder.ivImage);

            itemViewHolder.tvText.setText(reviews.get(position).getText());

        } else if (holder instanceof RestaurantAdapter.LoadingViewHolder) {
            RestaurantAdapter.LoadingViewHolder loadingViewHolder = (RestaurantAdapter.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvRating;
        private ImageView ivImage;
        private TextView tvOpen;
        private TextView tvOpenNow;
        private TextView tvText;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOpen = (TextView) itemView.findViewById(R.id.tvOpen);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            tvOpenNow = (TextView) itemView.findViewById(R.id.tvOpenNow);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);

        }
    }
}