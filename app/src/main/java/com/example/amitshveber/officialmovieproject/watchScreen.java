package com.example.amitshveber.officialmovieproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class watchScreen extends AppCompatActivity {
    TextView titleTV;
    TextView descriptionTV;
    ImageView movieImg;
    ImageButton okBtn;
    Button detailsBtn;
    String year;
    String rate;
    String runTime;
    String actors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_screen);
        titleTV = (TextView) findViewById(R.id.titleWV);
        descriptionTV = (TextView) findViewById(R.id.descriptionWV);

        Intent intent = getIntent();
        titleTV.setText(intent.getStringExtra("title"));
        descriptionTV.setText(intent.getStringExtra("desc"));
        downloadPicture tread1 = new downloadPicture();
        tread1.execute(intent.getStringExtra("url"));
        year = intent.getStringExtra("year");
        rate = intent.getStringExtra("rate");
        runTime = intent.getStringExtra("runTime");
        actors = intent.getStringExtra("actors");


        okBtn = (ImageButton) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        detailsBtn = (Button) findViewById(R.id.detailsBtn);
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(watchScreen.this);
                dialog.setContentView(R.layout.costum_dialog);
                dialog.setTitle("Details");
                TextView text = (TextView) dialog.findViewById(R.id.yearCostumDialog);
                TextView text1 = (TextView) dialog.findViewById(R.id.rateCostumDialog);
                TextView text2 = (TextView) dialog.findViewById(R.id.runtimeCostumDialog);
                TextView text3 = (TextView) dialog.findViewById(R.id.actorsCostumDialog);


                text.setText("Year : " + year);
                text1.setText("Rate : " + rate);
                text2.setText("Run Timt : " + runTime);
                text3.setText("Actors : " + actors);


                dialog.show();
            }


        });
    }

    public class downloadPicture extends AsyncTask<String, Void, Bitmap> {

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

            movieImg = (ImageView) findViewById(R.id.imgWV);
            movieImg.setImageBitmap(ImageResult);
            findViewById(R.id.activity_watch_screen).setFocusable(true);
            findViewById(R.id.activity_watch_screen).getDrawingCache(true);


        }

    }

}
