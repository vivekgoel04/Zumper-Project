package com.example.vivekgoel.zumperproject.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.RestaurantFeed;
import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.Restaurants;
import com.example.vivekgoel.zumperproject.R;
import com.example.vivekgoel.zumperproject.ServerConnection.APIClient;
import com.example.vivekgoel.zumperproject.ServerConnection.APIInterface;
import com.example.vivekgoel.zumperproject.SingletonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ListFragment extends Fragment {
	LinearLayoutManager linearLayoutManager;
	private RecyclerView mRecyclerView;
	private RestaurantAdapter mAdapter;
	private ArrayList<Restaurants> result = null;
	SingletonData singletonData = SingletonData.getInstance( );
	ProgressDialog progressDialog;
	APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);;

	public ListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mRecyclerView.setHasFixedSize(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_map, container, false);

		result = singletonData.getRestaurants();


		mRecyclerView = (RecyclerView) view.findViewById(R.id.rvlistItem);
		linearLayoutManager = new LinearLayoutManager(getContext());
		mRecyclerView.setLayoutManager(linearLayoutManager);

		mAdapter = new RestaurantAdapter(getActivity(), mRecyclerView, linearLayoutManager, result);

		mRecyclerView.setAdapter(mAdapter);

		//Load more data on page scroll(Pagination)
//		if(isConnected()) {
//			//Load more items on scroll
//			mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//				@Override
//				public void onLoadMore() {
//					if (singletonData.getNext_page_token() != null &&
//							!singletonData.getNext_page_token().equals("")) {
//						Log.d("*****", "Load More");
//						result.add(null);
//
//						//Load more data for reyclerview
//						new Handler().postDelayed(new Runnable() {
//							@Override
//							public void run() {
//								Log.d("*****", "Load More 2");
//
//								//Remove loading item
//								if (result.size() > 1)
//									result.remove(result.size() - 1);
//								getData(singletonData.getNext_page_token());
//							}
//						}, 1);
//					}
//				}
//			});
//		}

		return view;
	}

	private void getData(String next_page_token) {
		progressDialog = new ProgressDialog(getContext());
		progressDialog.setMessage("Processing...");
		progressDialog.setCancelable(false);
		progressDialog.show();

		Map<String, String> map = new HashMap<>();
		map.put("query","restaurants+in+San Francisco");
		map.put("key","AIzaSyDQeeDtLLkQZPmvi_9NNS40zfdg7FbyT44");
		map.put("pageToken",next_page_token);


		/**
		 GET Info of Restaurant
		 **/
		Call<RestaurantFeed> call = apiInterface.doGetNextPageRestaurantDetails(map);
		call.enqueue(new Callback<RestaurantFeed>() {

			@Override
			public void onResponse(Call<RestaurantFeed> call, Response<RestaurantFeed> response) {
				Log.d("*****","Inside onResponseB");

				RestaurantFeed rest = response.body();
				for(int i=0; i < rest.getRestaurants().size() ; i++)
					result.add(rest.getRestaurants().get(i));

				singletonData.updateRestaurants(result);
				singletonData.setNext_page_token(rest.getNext_page_token());

				progressDialog.dismiss();
				mAdapter.setData(result);
			}

			@Override
			public void onFailure(Call<RestaurantFeed> call, Throwable t) {
				call.cancel();
			}
		});


	}

	@Override
	public void onResume() {
		super.onResume();
		result = singletonData.getRestaurants();
		mAdapter.setData(result);
	}

	//Checking Internet Connection
	public boolean isConnected() {
		ConnectivityManager cm =
				(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
