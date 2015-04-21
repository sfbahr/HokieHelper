package com.cs3714.apassi.hokiehelper;

import android.graphics.Color;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import org.w3c.dom.Document;

public class MapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    GMapV2Direction md;

    LatLng fromPosition = LocationMap.buildings.get("SURGE");
    LatLng toPosition = LocationMap.buildings.get("LITRV");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setUpMapIfNeeded();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        md = new GMapV2Direction();
        mMap = ((SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        LatLng coordinates = new LatLng(13.685400079263206, 100.537133384495975);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 16));

        mMap.addMarker(new MarkerOptions().position(fromPosition).title("Start"));
        mMap.addMarker(new MarkerOptions().position(toPosition).title("End"));

        Document doc = md.getDocument(fromPosition, toPosition, GMapV2Direction.MODE_WALKING);
        int duration = md.getDurationValue(doc);
        String distance = md.getDistanceText(doc);
        String start_address = md.getStartAddress(doc);
        String copy_right = md.getCopyRights(doc);

        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);

        for(int i = 0 ; i < directionPoint.size() ; i++) {
            rectLine.add(directionPoint.get(i));
        }

        ArrayList<String> list = md.getDirections(doc);

        TextView tv = (TextView) findViewById(R.id.textview);
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < list.size(); i++)
            build.append(list.get(i) + "\n");

        tv.setText(build.toString());

        mMap.addPolyline(rectLine);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//        setUpMapIfNeeded();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        setUpMapIfNeeded();
//    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLng campus = new LatLng(37.2273948, -80.4220262);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campus, 16));
    }
}
