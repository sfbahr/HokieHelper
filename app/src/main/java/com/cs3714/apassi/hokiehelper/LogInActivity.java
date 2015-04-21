package com.cs3714.apassi.hokiehelper;

import android.app.Activity;
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

import com.cs3714.apassi.hokiehelper.vtaccess.Cas;
import com.cs3714.apassi.hokiehelper.vtaccess.WrongLoginException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class LogInActivity extends ActionBarActivity {
    private static final int REQUEST_CODE = 1;
    private EditText pidText;
    private EditText passwordText;

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

    public void onClickLogin(View view) throws WrongLoginException {
        new LoginTask().execute(pidText.getText().toString(),
                passwordText.getText().toString(),
                new File(getCacheDir(), "ssl").toString());
    }

    private void setUpLogInButton() {
        Button button = (Button) findViewById(R.id.toScheduleButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr=37.2286,-80.41629&daddr=37.221606,-80.423771"));
//                startActivityForResult(intent, REQUEST_CODE);

                Intent intent = new Intent(LogInActivity.this , ClassActivity.class);
                startActivity(intent);
            }
        });
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

    private class LoginTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            boolean success = false;
            try {
                success = Cas.login(params[0], params[1], params[2]);
            } catch (WrongLoginException e) {
                e.printStackTrace();
                return false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(LogInActivity.this, "Login succeeded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LogInActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
