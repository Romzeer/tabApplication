package com.example.krug.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.maps.SupportMapFragment;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

import mobi.inthepocket.android.beacons.ibeaconscanner.Beacon;
import mobi.inthepocket.android.beacons.ibeaconscanner.Error;
import mobi.inthepocket.android.beacons.ibeaconscanner.IBeaconScanner;


public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener, BeaconConsumer
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private LocationManager lm;
    MapDelegate md = new MapDelegate();
    ListFragment listFragment;
    final List<Horaire> tableau = new ArrayList<Horaire>();
    private BeaconManager beaconManager;
    protected static final String TAG = "MonitoringActivity";



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    class Horaire {
        public String HeuresD;
        public String HeuresF;
        public String Lieu;
        public String Latitude;
        public String Longitude;
        public String Marge;
        public String Cours;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    mViewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_hours:
                    mViewPager.setCurrentItem(1, true);
                    return true;
                case R.id.navigation_user:
                    mViewPager.setCurrentItem(2, true);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

        listFragment = new ListFragment();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int pageId = R.id.navigation_map;
            switch(position) {
                case 1: pageId = R.id.navigation_hours; break;
                case 2: pageId = R.id.navigation_user; break;
            }
            navigation.setSelectedItemId(pageId);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL u = new URL("http://172.16.4.218:8080/courses/student1");
                    HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();

                    InputStream dataIn = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(dataIn);
                    BufferedReader bsr = new BufferedReader(isr);
                    // Log.e("LECT", bsr.readLine());
                    String page = "";
                    String line = bsr.readLine();
                    while (line != null) {
                        page = page + "\n" + line;
                        line = bsr.readLine();
                    }

                    JSONObject o = new JSONObject(page);
                    JSONArray t = o.getJSONArray("cours");



                    for(int i = 0; i <t.length() ; i++) {
                        JSONObject sub = t.getJSONObject(i);
                        Horaire h = new Horaire();
                        h.HeuresD = sub.getString("start");
                        h.HeuresF = sub.getString("end");
                        h.Lieu = sub.getString("place");
                        h.Latitude = sub.getString("latitude");
                        h.Longitude = sub.getString("longitude");
                        h.Marge = sub.getString("range");
                        h.Cours = sub.getString("course");
                        tableau.add(h);
                        //md.createCircle(h);
                    }

                    listFragment.setData(tableau);




                    // le passer au fragment
                } catch(MalformedURLException nfe) {

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("F2A74FC4-7625-44DB-9B08-CB7E130B2029", null, null, null));
        } catch (RemoteException e) {    }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if(position == 0){
                SupportMapFragment mapFragment = new com.google.android.gms.maps.SupportMapFragment();

                mapFragment.getMapAsync(md);

                lm = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                    String[] permissions = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
                    requestPermissions(permissions, 666);
                }
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 500.0f, md);
                md.setTableau(tableau);
                mapFragment.getMapAsync(md);


                return mapFragment;

            }else if (position == 1) {
                return listFragment;

            } else {
                return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 666) {
            if (grantResults.length == 2 && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 500.0f, md);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
