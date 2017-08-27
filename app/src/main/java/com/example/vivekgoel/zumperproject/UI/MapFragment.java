package com.example.vivekgoel.zumperproject.UI;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.RestaurantFeed;
import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.Restaurants;
import com.example.vivekgoel.zumperproject.MainActivity;
import com.example.vivekgoel.zumperproject.R;
import com.example.vivekgoel.zumperproject.SingletonData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ArrayList<Restaurants> result = null;
    RestaurantFeed restaurantFeed;
    public MyBroadcastReceiver broadcastReceiver;
    public Toast toast;
    SupportMapFragment mapFragment;
    SingletonData singletonData = SingletonData.getInstance();
    private View view;
    ProgressDialog progressDialog;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("Orientation Change", true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_list, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        broadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("vivek.goel.DATA_LOADED");
        getActivity().registerReceiver(broadcastReceiver, filter);

        if (savedInstanceState != null) {
            if(savedInstanceState.getBoolean("Orientation Change"))
            setMarkers();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setMarkers() {
        if (mapFragment == null)
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker and move the camera.
        LatLng latLng = new LatLng(37.79332, -122.392761);

        //Setting camera properties
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        result = singletonData.getRestaurants();

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getGeometry().getLocation().getLat() != null && result.get(i)
                    .getGeometry().getLocation().getLng() != null) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(result.get(i).getGeometry()
                        .getLocation().getLat(), result.get(i).getGeometry().getLocation()
                        .getLng()))).setTitle(result.get(i).getName());
            }
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            int position;

            @Override
            public void onInfoWindowClick(Marker marker) {
                String name = marker.getTitle();
                for (int i = 0; i < result.size(); i++) {
                    if (name.equals(result.get(i).getName())) {
                        position = i;
                        break;
                    }
                }
                if (isConnected()) {
                    ((MainActivity) getContext()).onItemClick(result.get(position), 0);
                } else
                    buildDialog(getContext()).show();
            }
        });

    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("vivek.goel.DATA_LOADED")) {

                restaurantFeed = (RestaurantFeed) intent.getSerializableExtra("restaurantFeed");

                if (result != null && restaurantFeed.getRestaurants() != null) {
                    result.clear();
                } else
                    result = (ArrayList<Restaurants>) restaurantFeed.getRestaurants();
            }
            singletonData.updateRestaurants(result);
            singletonData.setNext_page_token(restaurantFeed.getNext_page_token());
            Log.d("*****",""+singletonData.getNext_page_token());
            setMarkers();
        }
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
