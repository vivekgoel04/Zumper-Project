package com.example.vivekgoel.zumperproject.UI;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.vivekgoel.zumperproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by vivekgoel on 5/11/17.
 */

class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private Context context;
    private ArrayList<String> images;
    private RecyclerView mRecyclerView;

    public ImageAdapter(Context context, RecyclerView recyclerView, final LinearLayoutManager linearLayoutManager, ArrayList<String> images) {
        this.context = context;
        mRecyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
        this.images = images;
    }

    @Override
    public int getItemViewType(int position) {
        return images.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image, parent, false);
            return new ImageAdapter.ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new ImageAdapter.LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageAdapter.ItemViewHolder) {
            ImageAdapter.ItemViewHolder itemViewHolder = (ImageAdapter.ItemViewHolder) holder;

            Picasso.with(context).load(images.get(position)).fit().into(itemViewHolder.ivImage);

        } else if (holder instanceof RestaurantAdapter.LoadingViewHolder) {
            RestaurantAdapter.LoadingViewHolder loadingViewHolder = (RestaurantAdapter.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
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