package com.missionsv.android.seventhgear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.InputStream;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2,fab3, fab4;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    public static int aCar, bCar, nCar, aUp, bUp, nUp, load=0;


    //Image - Load
    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)findViewById(R.id.fab3);
        fab4 = (FloatingActionButton)findViewById(R.id.fab4);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);


        //homepage clicks
        RelativeLayout relativeclic1 =(RelativeLayout)findViewById(R.id.View1);
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (load>=2) {
                    //startActivity(new Intent(MainActivity.this,Car.class));
                    Intent intent = new Intent(MainActivity.this, Car.class);
                    Bundle b = new Bundle();
                    b.putInt("up", aUp); //id
                    b.putInt("car", -1);
                    b.putString("activity", "MainActivity");
                    intent.putExtras(b); //Put id to next Intent
                    startActivity(intent);
                    finish();
                }
            }
        });

        RelativeLayout relativeclic2 =(RelativeLayout)findViewById(R.id.View2);
        relativeclic2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (load>=2) {
                    Intent intent = new Intent(MainActivity.this, Car.class);
                    Bundle b = new Bundle();
                    b.putInt("up", bUp);
                    b.putInt("car", -1);
                    b.putString("activity", "MainActivity");
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }
            }
        });

        RelativeLayout relativeclic3 =(RelativeLayout)findViewById(R.id.View3);
        relativeclic3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (load>=2) {
                    Intent intent = new Intent(MainActivity.this, Car.class);
                    Bundle b = new Bundle();
                    b.putInt("car", aCar);
                    b.putInt("up", -1);
                    b.putString("activity", "MainActivity");
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }
            }
        });


        RelativeLayout relativeclic4 =(RelativeLayout)findViewById(R.id.View4);
        relativeclic4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (load>=2) {
                    Intent intent = new Intent(MainActivity.this, Car.class);
                    Bundle b = new Bundle();
                    b.putInt("car", bCar);
                    b.putInt("up", -1);
                    b.putString("activity", "MainActivity");
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }
            }
        });



        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            ((TextView) findViewById(R.id.txtCar)).setText("No Connection");
            ((TextView) findViewById(R.id.textView7)).setText("No Connection");
            ((TextView) findViewById(R.id.textView8)).setText("No Connection");
            ((TextView) findViewById(R.id.textView10)).setText("No Connection");
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.no_prev);
            ((ImageView) findViewById(R.id.imageView6)).setImageResource(R.drawable.no_prev);
            ((ImageView) findViewById(R.id.imageView7)).setImageResource(R.drawable.no_prev);
            ((ImageView) findViewById(R.id.imageView8)).setImageResource(R.drawable.no_prev);
        }

        //Homepage Load
        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int count=0;
                Random rand = new Random();
                nCar = nUp = 0;
                for (DataSnapshot child : snapshot.getChildren())
                    for (DataSnapshot car : child.getChildren())
                        if (((String) child.getKey()).compareTo("CARS") == 0)
                            nCar++;
                        else
                            nUp++;
                aCar = rand.nextInt(nCar);
                bCar = (aCar + nCar / 2) % nCar;
                aUp = rand.nextInt(nUp);
                bUp = (aUp + nUp / 2) % nUp;

                for (DataSnapshot child : snapshot.getChildren())
                    for (DataSnapshot car : child.getChildren()) {
                        Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                        if (((String) child.getKey()).compareTo("CARS") == 0) {
                            if (Integer.parseInt(car.getKey()) == aCar) {
                                TextView T1 = (TextView) findViewById(R.id.textView8);
                                T1.setText((String) newPost.get("COMPANY") + " " + (String) newPost.get("MODEL"));
                                T1 = (TextView) findViewById(R.id.textView9);
                                T1.setText((String) newPost.get("VARIANT"));
                                ImageView I1 = (ImageView) findViewById(R.id.imageView7);
                                if (((String) newPost.get("IMAGE LINK")).length() == 0)
                                    I1.setImageResource(R.drawable.no_prev);
                                else
                                    new ImageDownloader(I1).execute((String) newPost.get("IMAGE LINK"));
                                count++;
                            }
                            if (Integer.parseInt(car.getKey()) == bCar) {
                                TextView T1 = (TextView) findViewById(R.id.textView10);
                                T1.setText((String) newPost.get("COMPANY") + " " + (String) newPost.get("MODEL"));
                                T1 = (TextView) findViewById(R.id.textView12);
                                T1.setText((String) newPost.get("VARIANT"));
                                ImageView I1 = (ImageView) findViewById(R.id.imageView8);
                                if (((String) newPost.get("IMAGE LINK")).length() == 0)
                                    I1.setImageResource(R.drawable.no_prev);
                                else
                                    new ImageDownloader(I1).execute((String) newPost.get("IMAGE LINK"));
                                count++;
                            }
                            if (count >= 2) {
                                count=0;
                                load++;
                                break;
                            }
                        } else {
                            if (Integer.parseInt(car.getKey()) == aUp) {
                                TextView T1 = (TextView) findViewById(R.id.txtCar);
                                T1.setText((String) newPost.get("COMPANY") + " " + (String) newPost.get("MODEL"));
                                T1 = (TextView) findViewById(R.id.textView11);
                                T1.setText((String) newPost.get("VARIANT"));
                                ImageView I1 = (ImageView) findViewById(R.id.img);
                                if (((String) newPost.get("IMAGE LINK")).length() == 0)
                                    I1.setImageResource(R.drawable.no_prev);
                                else
                                    new ImageDownloader(I1).execute((String) newPost.get("IMAGE LINK"));
                                count++;
                            }
                            if (Integer.parseInt(car.getKey()) == bUp) {
                                TextView T1 = (TextView) findViewById(R.id.textView7);
                                T1.setText((String) newPost.get("COMPANY") + " " + (String) newPost.get("MODEL"));
                                T1 = (TextView) findViewById(R.id.textView2);
                                T1.setText((String) newPost.get("VARIANT"));
                                ImageView I1 = (ImageView) findViewById(R.id.imageView6);
                                if (((String) newPost.get("IMAGE LINK")).length() == 0)
                                    I1.setImageResource(R.drawable.no_prev);
                                else
                                    new ImageDownloader(I1).execute((String) newPost.get("IMAGE LINK"));
                                count++;
                            }
                            if (count >= 2) {
                                count=0;
                                load++;
                                break;
                            }
                        }
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Unable to fetch data. Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            isFabOpen=false;
        } else {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            isFabOpen = true;
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1: //Contact us
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
                break;
            case R.id.fab2: //Filter
                startActivity(new Intent(MainActivity.this, filter.class));
                finish();
                break;
            case R.id.fab3: //About
                startActivity(new Intent(MainActivity.this, About.class));
                break;
            case R.id.fab4: //Compare
                startActivity(new Intent(MainActivity.this, compareEntry.class));
                break;
        }
    }


    //Back key exit
    Boolean doubleBack = false;
    @Override
    public void onBackPressed(){
        if(doubleBack) {
            super.onBackPressed();
            return;
        }
        this.doubleBack=true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }},2000);
    }

}