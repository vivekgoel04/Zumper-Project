package com.example.vivekgoel.zumperproject.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.Restaurants;
import com.example.vivekgoel.zumperproject.DataModel.RestaurantDetails.Details;
import com.example.vivekgoel.zumperproject.DataModel.RestaurantDetails.Result;
import com.example.vivekgoel.zumperproject.DataModel.RestaurantDetails.Reviews;
import com.example.vivekgoel.zumperproject.R;
import com.example.vivekgoel.zumperproject.ServerConnection.APIClient;
import com.example.vivekgoel.zumperproject.ServerConnection.APIInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by vivekgoel on 5/10/17.
 */

public class DetailFragment extends Fragment {
    LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;
    ReviewsAdapter mAdapter;

    LinearLayoutManager linearLayoutManagerImages;
    private RecyclerView mRecyclerViewImages;
    ImageAdapter mAdapterImages;
    private String key = "AIzaSyDQeeDtLLkQZPmvi_9NNS40zfdg7FbyT44";

    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);;
    View view = null;
    Restaurants restaurantInfo;
    private ProgressDialog progressDialog;
    @Bind(R.id.ivImage) ImageView ivImage;
    @Bind(R.id.tvTitle) TextView tvTitle;
    @Bind(R.id.btAddress) Button btAddress;
    @Bind(R.id.tvOpen) TextView tvOpen;
    @Bind(R.id.ivStar) ImageView ivStar;
    @Bind(R.id.tvRating) TextView tvRating;
    @Bind(R.id.tvContactNumber) TextView tvContactNumber;
    @Bind(R.id.tvReviews) TextView tvReviews;

    ArrayList<Reviews> reviews;
    Details details;
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerViewImages.setHasFixedSize(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (getArguments() != null) {
            restaurantInfo = (Restaurants) getArguments().getSerializable("Restaurant Info");
        }

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this,view);

        if(isConnected())
            getData();
        else
            buildDialog(getContext()).show();

        reviews = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvReviews);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        linearLayoutManagerImages = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mRecyclerViewImages = (RecyclerView) view.findViewById(R.id.rvImages);
        mRecyclerViewImages.setLayoutManager(linearLayoutManagerImages);


        btAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Searches for 'Main Street' near San Francisco
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+btAddress.getText());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //Initialize loaders to get Data from server
    public void getData() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("placeid",restaurantInfo.getPlace_id());
        map.put("key",key);

        /**
         GET Info of Restaurant
         **/
        Call<Details> call = apiInterface.doGetDetails(map);
        call.enqueue(new Callback<Details>() {

            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {
                Log.d("*****","Inside onResponse");

                details = response.body();
                reviews = (ArrayList<Reviews>) details.getResult().getReviews();
                setData(details);
                mAdapter = new ReviewsAdapter(getActivity(), mRecyclerView, linearLayoutManager, reviews);
                ArrayList<String> images = processPhotos(details.getResult());
                mAdapterImages = new ImageAdapter(getActivity(),mRecyclerViewImages,linearLayoutManagerImages,images);

                mRecyclerView.setAdapter(mAdapter);
                mRecyclerViewImages.setAdapter(mAdapterImages);
            }

            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                call.cancel();
            }
        });

        progressDialog.dismiss();
    }

    //Processing Html_attributions to get links
    private ArrayList<String> processPhotos(Result result) {
        ArrayList<String> imageList = new ArrayList<>();
        for(int i=0; i < result.getPhotos().size();i++) {
            String str = result.getPhotos().get(i).getHtml_attributions().get(0);
            List<String> list = Arrays.asList(str.split("\""));
            imageList.add("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+result.getPhotos().get(i)
                    .getPhoto_reference()+"&key="+key);
        }
        for(int i=0; i < imageList.size();i++) {
            Log.d("*****","photo: "+imageList.get(i));
        }

        return imageList;
    }

    //Load data into Layout
    public void setData(Details details) {
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        if(restaurantInfo.getIcon() == null)
            ivImage.setImageResource(R.mipmap.no_image);
        else
            Glide.with(getContext()).load(restaurantInfo.getIcon()).into(ivImage);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(restaurantInfo.getName());

        btAddress.setText(restaurantInfo.getFormatted_address());

        if(restaurantInfo.getOpening_hours().isOpen_now())
            tvOpen.setText("Yes");
        else
            tvOpen.setText("No");


        tvRating.setText(Float.toString(restaurantInfo.getRating()));

        tvContactNumber.setText(details.getResult().getFormatted_phone_number()+"\n"+details.getResult().getInternational_phone_number());

        if(details.getResult().getReviews() == null)
            tvReviews.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putSerializable("Restaurant Info", restaurantInfo);
    }

    public boolean isConnected() {
        //Checking Internet Connection
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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