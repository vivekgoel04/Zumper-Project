package com.example.vivekgoel.zumperproject.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.Restaurants;
import com.example.vivekgoel.zumperproject.MainActivity;
import com.example.vivekgoel.zumperproject.R;
import com.example.vivekgoel.zumperproject.listener.OnLoadMoreListener;

import java.util.ArrayList;

/**
 * Created by vivekgoel on 5/11/17.
 */

//Adapter for Search Fragment
class RestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private final LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private Context context;
    private ArrayList<Restaurants> result;
    private RecyclerView mRecyclerView;

    public RestaurantAdapter(Context context, RecyclerView recyclerView, final LinearLayoutManager linearLayoutManager, ArrayList<Restaurants> result) {
        this.context = context;
        mRecyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
        this.result = result;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }

        });
    }

    public void setData(ArrayList<Restaurants> result) {
        this.result = result;
        notifyDataSetChanged();
        setLoaded();
    }
    public void clearAdapter() {
        if(result != null)
            result.clear();
        linearLayoutManager.scrollToPositionWithOffset(7,20);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return result.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            itemViewHolder.tvTitle.setText(result.get(position).getName());

            if(result.get(position).getOpening_hours().isOpen_now())
                itemViewHolder.tvOpen.setText("Yes");
            else
                itemViewHolder.tvOpen.setText("No");

            itemViewHolder.tvRating.setText(Float.toString(result.get(position).getRating()));
            if(result.get(position).getIcon() == null)
                itemViewHolder.ivImage.setImageResource(R.mipmap.no_image);
            else
                Glide.with(context).load(result.get(position)
                        .getIcon()).into(itemViewHolder.ivImage);

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return result == null ? 0 : result.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvRating;
        private ImageView ivImage;
        private TextView tvOpen;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOpen = (TextView) itemView.findViewById(R.id.tvOpen);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);

            final Context context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnected()) {
                        ((MainActivity) context).onItemClick(result.get(getAdapterPosition()), 1);
                    }
                    else
                        buildDialog(context).show();
                }
            });
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
    public boolean isConnected() {
        //Checking Internet Connection
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet connection.");
        builder.setMessage("You have no internet connection");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }
}