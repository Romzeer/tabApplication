package com.example.krug.myapplication;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romzeer on 10/01/18.
 */

public class MapDelegate implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    ArrayAdapter<MainActivity.Horaire> listAdapter;
    List<MainActivity.Horaire> tableau = new ArrayList<MainActivity.Horaire>();

        /** Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
            * This is where we can add markers or lines, add listeners or move the camera. In this case,
            * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
   // @SuppressLint("MissingPermission")
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //CircleOptions circleOptions = new CircleOptions()
          //      .center(new LatLng(Double.parseDouble(h.Latitude), Double.parseDouble(h.Longitude)))
            //    .radius(Double.parseDouble(h.Marge));
        //Circle circle = mMap.addCircle(circleOptions);
        System.out.println("prout");
        for (MainActivity.Horaire g: tableau) {
            CircleOptions circleOptions = new CircleOptions()
                  .center(new LatLng(Double.parseDouble(g.Latitude), Double.parseDouble(g.Longitude)))
                .radius(Double.parseDouble(g.Marge));
            Circle circle = mMap.addCircle(circleOptions);
        }


       //
       mMap.setMyLocationEnabled(true);

    }

    public void setTableau (List<MainActivity.Horaire> tableau) {
        this.tableau = tableau;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
