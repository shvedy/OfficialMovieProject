package com.example.amitshveber.officialmovieproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class internetScreen extends AppCompatActivity {
    ImageButton searchBtn;
    EditText searchET;
    ArrayList<Movie> searchResulte;
    ArrayAdapter<Movie> searchresulteAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_screen);
        searchET = (EditText) findViewById(R.id.internetSearchET);
        searchBtn = (ImageButton) findViewById(R.id.internetSearchBtn);
        ListView searchLV = (ListView) findViewById(R.id.searchLV);

        searchResulte = new ArrayList<>();
        searchresulteAdap = new ArrayAdapter<>(this, R.layout.movie_item, R.id.movieItem, searchResulte);
        searchLV.setAdapter(searchresulteAdap);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchET.getText().toString();
                search=search.trim();
                if ( search.equals("")){

                    Toast.makeText(internetScreen.this, "Enter Search", Toast.LENGTH_SHORT).show();
                }
                else {


                    DownLoadStrings thread1 = new DownLoadStrings();

                    try {
                        String url = URLEncoder.encode(searchET.getText().toString(), "UTF-8");
                        thread1.execute("http://www.omdbapi.com/?s=" + url);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        searchLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(internetScreen.this, editScreen.class);
                String title = searchResulte.get(position).Title;
                String url = searchResulte.get(position).URL;
                String imdbid = searchResulte.get(position).id;

                intent.putExtra("title", title);
                intent.putExtra("url", url);
                intent.putExtra("imdbid", imdbid);
                startActivity(intent);
                DBContants.fromInternet = true;
                finish();
            }
        });
    }


    public class DownLoadStrings extends AsyncTask<String, Long, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(internetScreen.this);
            dialog.setTitle("Connecting");
            dialog.setMessage("please wait...");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder respone = null;
            if (new checkConnection(internetScreen.this).isNetworkAvailable()) {

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
            }
            if (respone == null)

            {
                respone = new StringBuilder();
                respone.append("no internet");
            }

            return respone.toString();


        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("no internet")) {
                dialog.dismiss();
                Toast.makeText(internetScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

            } else {
                if (s.contains("Movie not found")) {


                    dialog.dismiss();
                    Toast.makeText(internetScreen.this, "Movie not found", Toast.LENGTH_SHORT).show();

                }
                try {
                    JSONObject mainObject = new JSONObject(s);
                    JSONArray Array = mainObject.getJSONArray("Search");

                    searchResulte.clear();
                    for (int i = 0; i < Array.length(); i++) {
                        JSONObject currentObj = Array.getJSONObject(i);
                        String movieName = currentObj.getString("Title");
                        String url = currentObj.getString("Poster");
                        String id = currentObj.getString("imdbID");

                        Movie movie = new Movie(movieName, id, url);
                        searchResulte.add(movie);

                    }

                    searchresulteAdap.notifyDataSetChanged();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            super.onPostExecute(s);


        }

    }


}
