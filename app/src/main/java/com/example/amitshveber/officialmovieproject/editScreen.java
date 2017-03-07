package com.example.amitshveber.officialmovieproject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

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
import java.net.URLConnection;

import static android.R.attr.id;
import static com.example.amitshveber.officialmovieproject.R.id.movieIV;

public class editScreen extends AppCompatActivity implements View.OnClickListener {
    ImageButton addBtn;
    Button urlBtn;
    EditText movieNameET;
    EditText movieDescriptionET;
    EditText movieUrlET;
    mySql SQLTable;
    String movieName;
    String movieDescription;
    String movieURL;
    String url;
    ImageView movieIV;
    int id;
    String year;
    String rate;
    String runTime;
    String actors;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);
        SQLTable = new mySql(this);

        addBtn = (ImageButton) findViewById(R.id.addBtn);
        movieNameET = (EditText) findViewById(R.id.movieNameET);
        movieDescriptionET = (EditText) findViewById(R.id.descriptionET);
        movieUrlET = (EditText) findViewById(R.id.urlET);
        Intent intent = getIntent();
        movieNameET.setText(intent.getStringExtra("title"));
        movieUrlET.setText(intent.getStringExtra("url"));

        String imdbid = intent.getStringExtra("imdbid");

        DownLoadBody tread = new DownLoadBody();
        // tread.execute("http://www.omdbapi.com/?i=" + imdbid);
        urlBtn = (Button) findViewById(R.id.urlBtn);
        if (DBContants.Edit == true) {
            intent = getIntent();
            movieNameET.setText(intent.getStringExtra("title"));
            movieDescriptionET.setText(intent.getStringExtra("description"));
            movieUrlET.setText(intent.getStringExtra("url"));
            id = intent.getIntExtra("id", -1);
        }
        if (DBContants.fromInternet == true) {
            url = movieUrlET.getText().toString();
            //urlBtn.setVisibility(View.INVISIBLE);
            tread.execute("http://www.omdbapi.com/?i=" + imdbid);
            downloadPicture tread1 = new downloadPicture();
            tread1.execute(url);
        }
        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBtn:

                if (DBContants.Edit == true) {


                    movieName = movieNameET.getText().toString();
                    movieDescription = movieDescriptionET.getText().toString();
                    movieURL = movieUrlET.getText().toString();
                    movieName = movieName.trim();

                    if (movieName.equals("")) {
                        Toast.makeText(this, "Enter movie name ", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues insertStringToTble = new ContentValues();
                        insertStringToTble.put(DBContants.titeColumm, movieName);
                        insertStringToTble.put(DBContants.descriptionColumm, movieDescription);
                        insertStringToTble.put(DBContants.ImgColumm, movieURL);
                        String[] idNumber = new String[]{"" + id};
                        SQLTable.getWritableDatabase().update(DBContants.tableName, insertStringToTble, "_id=?", idNumber);
                        Toast.makeText(editScreen.this, "updated", Toast.LENGTH_SHORT).show();
                        DBContants.Edit = false;
                        finish();
                    }

                } else {
                    movieName = movieNameET.getText().toString();
                    movieDescription = movieDescriptionET.getText().toString();
                    movieURL = movieUrlET.getText().toString();
                    movieName = movieName.trim();
                    if (movieName.equals("")) {
                        Toast.makeText(this, "Enter movie name ", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues insertDataToSQL = new ContentValues();
                        insertDataToSQL.put(DBContants.titeColumm, movieName);
                        insertDataToSQL.put(DBContants.descriptionColumm, movieDescription);
                        insertDataToSQL.put(DBContants.ImgColumm, movieURL);
                        insertDataToSQL.put(DBContants.yearColumm, year);
                        insertDataToSQL.put(DBContants.rateColumm, rate);
                        insertDataToSQL.put(DBContants.RuntimeColumm, runTime);
                        insertDataToSQL.put(DBContants.actorsColumm, actors);

                        SQLTable.getWritableDatabase().insert(DBContants.tableName, null, insertDataToSQL);
                        finish();
                    }
                }
                break;
            case R.id.urlBtn:
                url = movieUrlET.getText().toString();


                downloadPicture tread1 = new downloadPicture();
                tread1.execute(url);

                break;
        }


    }

    public class DownLoadBody extends AsyncTask<String, Long, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(editScreen.this);
            dialog.setTitle("Connecting");
            dialog.setMessage("please wait...");
            dialog.setCancelable(true);
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder respone = null;

            try {
                URL website = new URL(params[0]);
                URLConnection connection = website.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                respone = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null)
                    respone.append(inputLine);
                in.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (respone == null) {
                Log.d("ngc", "bgc");
            }

            return respone.toString();


        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject mainObject = new JSONObject(s);
                String plot = mainObject.getString("Plot");
                year = mainObject.getString("Year");
                rate = mainObject.getString("imdbRating");
                runTime = mainObject.getString("Runtime");
                actors = mainObject.getString("Actors");
                movieDescriptionET.setText(plot);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.dismiss();
            super.onPostExecute(s);


        }

    }

    public class downloadPicture extends AsyncTask<String, Void, Bitmap> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(editScreen.this);
            dialog.setTitle("Connecting");
            dialog.setMessage("please wait...");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... params) {

            Bitmap image = null;
            String urlImage = new String(params[0]);
            try {
                InputStream in = new java.net.URL((urlImage)).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap ImageResult) {

            movieIV = (ImageView) findViewById(R.id.movieIV);
            movieIV.setImageBitmap(ImageResult);
            findViewById(R.id.activity_edit_screen).setFocusable(true);
            findViewById(R.id.activity_edit_screen).getDrawingCache(true);
            dialog.dismiss();

        }


    }

}
