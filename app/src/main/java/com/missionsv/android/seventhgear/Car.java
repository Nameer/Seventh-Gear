package com.missionsv.android.seventhgear;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class Car extends AppCompatActivity {

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


    int id = -1;
    String child_name, caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            id = b.getInt("car");
            caller=b.getString("activity");
            if (id==-1) {
                child_name = "UPCOMING";
                id = b.getInt("up");
            }
            else
                child_name="CARS";
        }
        if (id==-1){
            startActivity(new Intent(Car.this, MainActivity.class));
            Toast.makeText(Car.this, "Error occurred. Try again", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Data Load
        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren())
                    for (DataSnapshot car : child.getChildren()) {
                        Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                        if (((String) child.getKey()).compareTo(child_name) == 0) {
                            if (Integer.parseInt(car.getKey()) == id) {
                                ImageView I1 = (ImageView) findViewById(R.id.img);
                                if (((String) newPost.get("IMAGE LINK")).length() == 0)
                                    I1.setImageResource(R.drawable.no_prev);
                                else
                                    new ImageDownloader(I1).execute((String) newPost.get("IMAGE LINK"));
                                TextView T1 = (TextView) findViewById(R.id.textCompany);
                                T1.setText((String) newPost.get("COMPANY"));
                                T1 = (TextView) findViewById(R.id.textModel);
                                T1.setText((String) newPost.get("MODEL"));
                                T1 = (TextView) findViewById(R.id.textVariant);
                                T1.setText((String) newPost.get("VARIANT"));
                                T1 = (TextView) findViewById(R.id.textBodyType);
                                T1.setText((String) newPost.get("BODY TYPE"));
                                T1 = (TextView) findViewById(R.id.textEngineCapacity);
                                if ((Long)newPost.get("ENGINE CAPACITY")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("ENGINE CAPACITY")) + "cc");
                                T1 = (TextView) findViewById(R.id.textFuelSupported);
                                T1.setText((String) newPost.get("FUEL SUPPORTED"));
                                T1 = (TextView) findViewById(R.id.textFuelTank);
                                if ((Long)newPost.get("FUEL TANK CAPACITY")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("FUEL TANK CAPACITY")) + " Litres");
                                T1 = (TextView) findViewById(R.id.textSeating);
                                if ((Long)newPost.get("SEATING CAPACITY")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("SEATING CAPACITY")));
                                T1 = (TextView) findViewById(R.id.textMileage);
                                if ((Double)newPost.get("MILEAGE")==0.01)
                                    T1.setText("-");
                                else
                                    T1.setText(Double.toString((Double) newPost.get("MILEAGE")) + " kmpl");
                                T1 = (TextView) findViewById(R.id.textTransmission);
                                if ((Long)newPost.get("NO OF GEARS")==0)
                                    T1.setText((String) newPost.get("GEAR TYPE") + "-");
                                else
                                    T1.setText((String) newPost.get("GEAR TYPE") + ", " + Long.toString((Long)newPost.get("NO OF GEARS")) + " Speed");
                                T1 = (TextView) findViewById(R.id.textMSpeed);
                                if ((Long)newPost.get("MAX SPEED")==0)
                                    T1.setText("-");
                                else
                                    T1.setText(Long.toString((Long)newPost.get("MAX SPEED")) + " kmph");
                                T1 = (TextView) findViewById(R.id.textABS);
                                T1.setText((String) newPost.get("ABS"));
                                T1 = (TextView) findViewById(R.id.textAirbag);
                                T1.setText((String) newPost.get("AIRBAG"));
                                T1 = (TextView) findViewById(R.id.textPrice);
                                if ((Double)newPost.get("PRICE")==0.01)
                                    T1.setText("Not Available");
                                else if ((Double)newPost.get("PRICE")>=100)
                                    T1.setText("₹ " + String.valueOf((Double) newPost.get("PRICE") /100) + " Cr.");
                                else
                                    T1.setText("₹ " + String.valueOf((Double) newPost.get("PRICE")) + " Lakhs");
                            }
                        } else
                            break;
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Car.this, "Unable to fetch data. Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onBackPressed()
    {
        if (caller.compareTo("MainActivity")==0)
            startActivity(new Intent(Car.this, MainActivity.class));
        finish();
    }
}
