package com.example.vivekgoel.zumperproject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.Restaurants;
import com.example.vivekgoel.zumperproject.Service.RestaurantSearchService;
import com.example.vivekgoel.zumperproject.UI.DetailFragment;
import com.example.vivekgoel.zumperproject.UI.MapFragment;
import com.example.vivekgoel.zumperproject.UI.ListFragment;

public class MainActivity extends FragmentActivity implements TabListener {
	ViewPager viewPager;
	ActionBar actionBar;
	ProgressDialog progressDialog;
	MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Processing...");
		progressDialog.setCancelable(false);

		if(isConnected()) {

			Intent intent = new Intent(this, RestaurantSearchService.class);
			startService(intent);
		}
		else
			buildDialog(this).show();

		viewPager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fragmentManager = getSupportFragmentManager();

		myAdapter = new MyAdapter(fragmentManager);
		viewPager.setAdapter(myAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				//Log.d("Vivek", "onPageSelected at "+" position "+arg0);
				actionBar.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				//Log.d("Vivek", "onPageScrolled at "+" position "+arg0+" from "+arg1+" with number of pixels= "+arg2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				if(arg0== ViewPager.SCROLL_STATE_IDLE) {
						myAdapter.notifyDataSetChanged();

				}
				if(arg0== ViewPager.SCROLL_STATE_DRAGGING) {
					//Log.d("Vivek","onPageScrollStateChanged Dragging");
				}if(arg0== ViewPager.SCROLL_STATE_SETTLING) {
					//Log.d("Vivek","onPageScrollStateChanged Settling");
				}
			}
		});

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab1=actionBar.newTab();
		tab1.setText("Map");
		tab1.setTabListener(this);

		Tab tab2=actionBar.newTab();
		tab2.setText("List");
		tab2.setTabListener(this);

		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		//Log.d("Vivek", "onTabSelected at "+" position "+tab.getPosition()+" name "+tab.getText());
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		//Log.d("Vivek", "onTabUnselected at "+" position "+tab.getPosition()+" name "+tab.getText());

	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	//Checking Internet Connection
	public boolean isConnected() {
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

	public void onItemClick(Restaurants restaurants, int tab) {

		// Create fragment and give it an argument for the selected item
		DetailFragment newDetailFragment = new DetailFragment();
		Bundle args = new Bundle();
		args.putSerializable("Restaurant Info", restaurants);
		newDetailFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack so the user can navigate back
		if(tab == 0)
			transaction.replace(R.id.fragment_container_map, newDetailFragment);
		else
			transaction.replace(R.id.fragment_container_list, newDetailFragment);

		// Commit the transaction
		transaction.commit();

		transaction.addToBackStack(null);

	}
}

class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		public Fragment getItem(int i) {
			// TODO Auto-generated method stub
			Log.d("VIVZ", "get Item is called "+i);

			Fragment fragment=null;
			if(i==0) {
				fragment = new MapFragment();

			}
			else if(i==1) {
				fragment = new ListFragment();

			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}



