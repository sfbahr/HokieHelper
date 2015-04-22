package com.cs3714.apassi.hokiehelper;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class ClassActivity extends ActionBarActivity {

    ArrayList<VTClass> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        setUpExamsButton();

        classes = new ArrayList<VTClass>();

        populateClasses();

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

                Intent intent = new Intent(ClassActivity.this, MapActivity.class);
                intent.putExtra("lat", classes.get(position).getCoordinates().latitude);
                intent.putExtra("lon", classes.get(position).getCoordinates().longitude);
                startActivity(intent);
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
}