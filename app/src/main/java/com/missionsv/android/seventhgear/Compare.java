package com.missionsv.android.seventhgear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.Map;

public class Compare extends AppCompatActivity {


    //Image- Load
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


    int car1 = -1, car2=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            car1 = b.getInt("car1");
            car2 = b.getInt("car2");

        }

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            car1 = car2 = -1;
            Toast.makeText(Compare.this, "No connection", Toast.LENGTH_SHORT).show();
        }
        if (car1==-1){
            TextView T1 = (TextView) findViewById(R.id.company1);
            T1.setText("No car");
            ImageView I1 = (ImageView) findViewById(R.id.CarImage1);
            I1.setImageResource(R.drawable.no_prev);
        }
        if (car2==-1){
            TextView T1 = (TextView) findViewById(R.id.company2);
            T1.setText("No car");
            ImageView I1 = (ImageView) findViewById(R.id.CarImage2);
            I1.setImageResource(R.drawable.no_prev);
        }

        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren())
                    for (DataSnapshot car : child.getChildren()) {
                        Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                        if (((String) child.getKey()).compareTo("CARS") == 0) {
                            if (Integer.parseInt(car.getKey()) == car1) {
                                ImageView I1 = (ImageView) findViewById(R.id.CarImage1);
                                if (((String) newPost.get("IMAGE LINK")).length() == 0)
                                    I1.setImageResource(R.drawable.no_prev);
                                else
                                    new Compare.ImageDownloader(I1).execute((String) newPost.get("IMAGE LINK"));
                                TextView T1 = (TextView) findViewById(R.id.company1);
                                T1.setText((String) newPost.get("COMPANY"));
                                T1 = (TextView) findViewById(R.id.model1);
                                T1.setText((String) newPost.get("MODEL"));
                                T1 = (TextView) findViewById(R.id.variant1);
                                T1.setText((String) newPost.get("VARIANT"));
                                T1 = (TextView) findViewById(R.id.bodyType1);
                                T1.setText((String) newPost.get("BODY TYPE"));
                                T1 = (TextView) findViewById(R.id.engineCapacity1);
                                if ((Long)newPost.get("ENGINE CAPACITY")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("ENGINE CAPACITY")) + "cc");
                                T1 = (TextView) findViewById(R.id.fuelType1);
                                T1.setText((String) newPost.get("FUEL SUPPORTED"));
                                T1 = (TextView) findViewById(R.id.fuelCapacity1);
                                if ((Long)newPost.get("FUEL TANK CAPACITY")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("FUEL TANK CAPACITY")) + " Litres");
                                T1 = (TextView) findViewById(R.id.seat1);
                                if ((Long)newPost.get("SEATING CAPACITY")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("SEATING CAPACITY")));
                                T1 = (TextView) findViewById(R.id.mileage1);
                                if ((Double)newPost.get("MILEAGE")==0.01)
                                    T1.setText("-");
                                else
                                    T1.setText(Double.toString((Double) newPost.get("MILEAGE")) + " kmpl");
                                T1 = (TextView) findViewById(R.id.transmission1);
                                if ((Long)newPost.get("NO OF GEARS")==0)
                                    T1.setText((String) newPost.get("GEAR TYPE") + "-");
                                else
                                    T1.setText((String) newPost.get("GEAR TYPE") + ", " + Long.toString((Long)newPost.get("NO OF GEARS")) + " Speed");
                                T1 = (TextView) findViewById(R.id.maxSpeed1);
                                if ((Long)newPost.get("MAX SPEED")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("MAX SPEED")) + " kmph");
                                T1 = (TextView) findViewById(R.id.absAir1);
                                T1.setText(((String) newPost.get("ABS")) + " / " + ((String) newPost.get("AIRBAG")));
                                T1 = (TextView) findViewById(R.id.price1);
                                if ((Double)newPost.get("PRICE")==0.01)
                                    T1.setText("Not Available");
                                else if ((Double)newPost.get("PRICE")>=100)
                                    T1.setText("₹ " + String.valueOf((Double) newPost.get("PRICE") /100) + " Cr.");
                                else
                                    T1.setText("₹ " + String.valueOf((Double) newPost.get("PRICE")) + " Lakhs");
                            }
                            if (Integer.parseInt(car.getKey()) == car2) {
                                ImageView I1 = (ImageView) findViewById(R.id.CarImage2);
                                if (((String) newPost.get("IMAGE LINK")).length() == 0)
                                    I1.setImageResource(R.drawable.no_prev);
                                else
                                    new Compare.ImageDownloader(I1).execute((String) newPost.get("IMAGE LINK"));
                                TextView T1 = (TextView) findViewById(R.id.company2);
                                T1.setText((String) newPost.get("COMPANY"));
                                T1 = (TextView) findViewById(R.id.model2);
                                T1.setText((String) newPost.get("MODEL"));
                                T1 = (TextView) findViewById(R.id.variant2);
                                T1.setText((String) newPost.get("VARIANT"));
                                T1 = (TextView) findViewById(R.id.bodyType2);
                                T1.setText((String) newPost.get("BODY TYPE"));
                                T1 = (TextView) findViewById(R.id.engineCapacity2);
                                if ((Long)newPost.get("ENGINE CAPACITY")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("ENGINE CAPACITY")) + "cc");
                                T1 = (TextView) findViewById(R.id.fuelType2);
                                T1.setText((String) newPost.get("FUEL SUPPORTED"));
                                T1 = (TextView) findViewById(R.id.fuelCapacity2);
                                if ((Long)newPost.get("FUEL TANK CAPACITY")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("FUEL TANK CAPACITY")) + " Litres");
                                T1 = (TextView) findViewById(R.id.seat2);
                                if ((Long)newPost.get("SEATING CAPACITY")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("SEATING CAPACITY")));
                                T1 = (TextView) findViewById(R.id.mileage2);
                                if ((Double)newPost.get("MILEAGE")==0.01)
                                    T1.setText("-");
                                else
                                    T1.setText(Double.toString((Double) newPost.get("MILEAGE")) + " kmpl");
                                T1 = (TextView) findViewById(R.id.transmission2);
                                if ((Long)newPost.get("NO OF GEARS")==0)
                                    T1.setText((String) newPost.get("GEAR TYPE") + "-");
                                else
                                    T1.setText((String) newPost.get("GEAR TYPE") + ", " + Long.toString((Long)newPost.get("NO OF GEARS")) + " Speed");
                                T1 = (TextView) findViewById(R.id.maxSpeed2);
                                if ((Long)newPost.get("MAX SPEED")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("MAX SPEED")) + " kmph");
                                T1 = (TextView) findViewById(R.id.absAir2);
                                T1.setText(((String) newPost.get("ABS")) + " / " + ((String) newPost.get("AIRBAG")));
                                T1 = (TextView) findViewById(R.id.price2);
                                if ((Double)newPost.get("PRICE")==0.01)
                                    T1.setText("Not Available");
                                else if ((Double)newPost.get("PRICE")>=100)
                                    T1.setText("₹ " + String.valueOf((Double) newPost.get("PRICE") /100) + " Cr.");
                                else
                                    T1.setText("₹ " + String.valueOf((Double) newPost.get("PRICE")) + " Lakhs");

                            }
                        }
                        else
                            break;
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Compare.this, "Unable to fetch data. Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(Compare.this, compareEntry.class));
        finish();
    }
}
