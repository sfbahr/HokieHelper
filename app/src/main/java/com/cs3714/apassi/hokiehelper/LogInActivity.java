package com.cs3714.apassi.hokiehelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cs3714.apassi.hokiehelper.vtaccess.schedule.*;
import com.cs3714.apassi.hokiehelper.vtaccess.Cas;
import com.cs3714.apassi.hokiehelper.vtaccess.ScheduleScraper;
import com.cs3714.apassi.hokiehelper.vtaccess.exceptions.WrongLoginException;
import com.cs3714.apassi.hokiehelper.vtaccess.exceptions.HokieSpaTimeoutException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class LogInActivity extends ActionBarActivity {
    private static final int REQUEST_CODE = 1;
    private EditText pidText;
    private EditText passwordText;

    /** Reference to the file name */
    public static final String classFileName = "classes.txt";

    /** Reference to the file name */
    public static final String examsFileName = "exams.txt";

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pidText = (EditText) findViewById(R.id.userNameEditText);
        passwordText = (EditText) findViewById(R.id.passwordEditText);

        setUpLogInButton();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.d("LogIn", data.getData().toString());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickLogin(View view) {
        progress = ProgressDialog.show(LogInActivity.this, "Loading Schedule", "loading...", true);
        new LoginTask().execute(pidText.getText().toString(),
                passwordText.getText().toString(),
                new File(getFilesDir(), "ssl").toString());
    }

    private void setUpLogInButton() {
        Button button = (Button) findViewById(R.id.toScheduleButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr=37.2286,-80.41629&daddr=37.221606,-80.423771"));
//                startActivityForResult(intent, REQUEST_CODE);

                populateExams();
                Intent intent = new Intent(LogInActivity.this , ClassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateExams() {

        List<Course> courses = new ArrayList<Course>();

        Course c = new Course("19943", "3771", "Fine Arts",
                3, 35, "Teacher", "M W", "8:00am", "9:00am", "SQUIR 343");

        courses.add(c);

        c = new Course("19943", "3771", "Intro To Unix",
                3, 35, "Teacher", "T TR", "8:00am", "9:00am", "DURHM 23");

        courses.add(c);

        c = new Course("19943", "3771", "Data Structures",
                3, 35, "Teacher", "M W", "10:00am", "11:00am", "ENGEL 23");

        courses.add(c);

        c = new Course("19943", "3771", "Physics",
                3, 35, "Teacher", "T TR", "2:00pm", "3:00pm", "ROB 23");

        courses.add(c);

        save(courses);
        saveExams(courses);
    }

    public boolean save(List<Course> courses){
        File file = new File(getFilesDir(), classFileName);
        file.delete();

        try {
            FileOutputStream outputFileStream = new FileOutputStream(file);
            ObjectOutputStream outputObjectStream = new ObjectOutputStream(outputFileStream);

            for(int i = 0; i < courses.size(); i++)
                outputObjectStream.writeObject(courses.get(i));
            outputObjectStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveExams(List<Course> exams) {

        File file = new File(getFilesDir(), examsFileName);
        file.delete();

        try {
            FileOutputStream outputFileStream = new FileOutputStream(file);
            ObjectOutputStream outputObjectStream = new ObjectOutputStream(outputFileStream);

            for(int i = 0; i < exams.size(); i++)
                outputObjectStream.writeObject(exams.get(i));
            outputObjectStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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

    private class LoginTask extends AsyncTask<String, Integer, ArrayList<Course>> {
        @Override
        protected ArrayList<Course> doInBackground(String... params) {
            try {
                Cas cas = new Cas(params[0].toCharArray(), params[1].toCharArray(), params[2]);

                ScheduleScraper s = new ScheduleScraper(cas);
                Schedule schedule = new Schedule();
                String semester = "201501";
                s.retrieveSchedule(schedule, semester);
                ArrayList<Course> c = new ArrayList<Course>();
                s.retrieveExamSchedule(semester, c);
                Log.d("Schedule", "Save: " + save(schedule.getAllCourses()));
                saveExams(c);
                return schedule.getAllCourses();

            } catch (HokieSpaTimeoutException e) {
                e.printStackTrace();
            } catch (WrongLoginException e) {
                e.printStackTrace();
            }
            return new ArrayList<Course>();
        }

        @Override
        protected void onPostExecute(ArrayList<Course> course) {
            progress.dismiss();
            if (course.size() <= 0) {
                Toast.makeText(LogInActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(LogInActivity.this, ClassActivity.class);
            startActivity(intent);
        }

        /**
         * Method to save the current in memory data structure
         * that contains all the password entries to "pwman.dat" file.
         */
        public boolean save(List<Course> courses){
            File file = new File(getFilesDir(), classFileName);
            file.delete();

            try {
                FileOutputStream outputFileStream = new FileOutputStream(file);
                ObjectOutputStream outputObjectStream = new ObjectOutputStream(outputFileStream);

                for(int i = 0; i < courses.size(); i++)
                    outputObjectStream.writeObject(courses.get(i));
                outputObjectStream.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean saveExams(List<Course> exams) {

            File file = new File(getFilesDir(), examsFileName);
            file.delete();

            try {
                FileOutputStream outputFileStream = new FileOutputStream(file);
                ObjectOutputStream outputObjectStream = new ObjectOutputStream(outputFileStream);

                for(int i = 0; i < exams.size(); i++)
                    outputObjectStream.writeObject(exams.get(i));
                outputObjectStream.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
