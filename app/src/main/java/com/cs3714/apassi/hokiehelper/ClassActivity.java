package com.cs3714.apassi.hokiehelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cs3714.apassi.hokiehelper.vtaccess.schedule.Course;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


public class ClassActivity extends ActionBarActivity implements LocationListener{

    ArrayList<VTClass> classes;

    LocationManager locationManager;
    String locationProvider = LocationManager.NETWORK_PROVIDER;
    private double curLat, curLong = 0;

    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        setUpExamsButton();
        classes = new ArrayList<VTClass>();

        Intent intent = getIntent();
        //ArrayList<Course> courses = (ArrayList<Course>) intent.getExtras().get("courses");

        ArrayList<Course> courses = new ArrayList<Course>();
        load(courses);

        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            VTClass c = new VTClass(course.getName(), course.getDays(),
                    course.getBeginTime() + " " + course.getEndTime(), course.getBuilding(),
                    LocationMap.buildings.get(course.getBuilding()));
            classes.add(c);
            //VTClass c = new VTClass("Mobile Dev", "T TR", "3:30pm - 4:45pm",
            //        "SQUIR",LocationMap.buildings.get("SQUIR"));
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationProvider, 0, 0, this);

        //populateClasses();

        //String [] StringArray = new String[5];
        ArrayList<String> list = new ArrayList<String>();
        final ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);

        for (int i = 0; i < classes.size(); i++) {
            mArrayAdapter.add(classes.get(i).toString());
        }

        ListView listView = (ListView) findViewById(R.id.classListView);
        listView.setAdapter(mArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(ClassActivity.this, MapActivity.class);
//                intent.putExtra("lat", classes.get(position).getCoordinates().latitude);
//                intent.putExtra("lon", classes.get(position).getCoordinates().longitude);
//                startActivity(intent);

                if (classes.get(position).getCoordinates() == null) {
                    Toast.makeText(ClassActivity.this, "Location not present", Toast.LENGTH_SHORT).show();
                    return;
                }

                progress = ProgressDialog.show(ClassActivity.this, "Loading Directions", "loading...", true);
                LatLng start = new LatLng(curLat, curLong);
                new NavigateTask().execute(start, classes.get(position).getCoordinates());
            }
        });
    }

    private void setUpExamsButton() {
        Button button  = (Button) findViewById(R.id.examsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassActivity.this, ExamsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateClasses() {

        VTClass c = new VTClass("Mobile Dev", "T TR", "3:30pm - 4:45pm",
                "SQUIR",LocationMap.buildings.get("SQUIR"));
        classes.add(c);

        c = new VTClass("Large Scale", "T TR", "8:00am - 9:15am",
                "DURHM",LocationMap.buildings.get("DURHM"));

        classes.add(c);

        c = new VTClass("Ecnomics", "M W F", "8:00am - 9:15am",
                "LITRV",LocationMap.buildings.get("LITRV"));

        classes.add(c);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class, menu);
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

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        curLat = location.getLatitude();
        curLong = location.getLongitude();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Method loads in all the entries present in the
     * "pwman.dat" file.
     */
    public void load(List<Course> courses){
        File file = new File(getFilesDir(), LogInActivity.classFileName);

        try {
            FileInputStream inputFileStream = new FileInputStream(file);
            ObjectInputStream inputObjectStream = new ObjectInputStream(inputFileStream);

            Course course = (Course) inputObjectStream.readObject();
            //System.out.println("Following are the expired entries in the database.");

            while(course != null) {
                courses.add(course);
                course = (Course) inputObjectStream.readObject();
            }

            inputObjectStream.close();

        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
    }

    private class NavigateTask extends AsyncTask<LatLng, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(LatLng... params) {
            try {
                LatLng start = params[0];
                LatLng dest = params[1];
                GMapV2Direction md = new GMapV2Direction();
                Document doc = md.getDocument(start, dest, GMapV2Direction.MODE_WALKING);

                ArrayList<String> list = md.getDirections(doc);
                list.add(0, String.valueOf(start.latitude));
                list.add(1, String.valueOf(start.longitude));
                list.add(2, String.valueOf(dest.latitude));
                list.add(3, String.valueOf(dest.longitude));

                return list;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> directions) {

            String sLat = directions.get(0);
            String sLon = directions.get(1);
            String dLat = directions.get(2);
            String dLon = directions.get(3);

            progress.dismiss();

            // Starting google maps intent
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?saddr=" + sLat + "," + sLon +
                    "&daddr=" + dLat + "," + dLon));
            startActivity(intent);
        }
    }
}