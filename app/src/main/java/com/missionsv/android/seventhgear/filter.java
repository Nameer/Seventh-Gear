package com.missionsv.android.seventhgear;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class filter extends AppCompatActivity {

    Bundle b = new Bundle();
    int pricePos=0;

    ArrayList company = new ArrayList();
    ArrayList bt = new ArrayList();
    ArrayList fuel = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((Spinner) findViewById(R.id.spinPrice)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItem, int pos, long id) {
                pricePos = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                pricePos=0;
            }
        });

        ((Button) findViewById(R.id.btnDone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected())
                    Toast.makeText(filter.this, "No connection", Toast.LENGTH_LONG).show();
                else {
                    DatabaseReference mDatabase2;
                    mDatabase2 = FirebaseDatabase.getInstance().getReference();
                    mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren())
                                for (DataSnapshot car : child.getChildren()) {
                                    Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                                    if (((String) child.getKey()).compareTo("CARS") == 0) {
                                        if (!(company.contains((String) newPost.get("COMPANY"))))
                                            company.add((String) newPost.get("COMPANY"));
                                        if (!(bt.contains((String) newPost.get("BODY TYPE"))))
                                            bt.add((String) newPost.get("BODY TYPE"));
                                    }
                                    else {
                                        if (((CheckBox) findViewById(R.id.checkCompOthers)).isChecked()) {
                                            if (((CheckBox) findViewById(R.id.checkChevrolet)).isChecked() == false)
                                                company.remove("CHEVROLET");
                                            if (((CheckBox) findViewById(R.id.checkFord)).isChecked() == false)
                                                company.remove("FORD");
                                            if (((CheckBox) findViewById(R.id.checkMahindra)).isChecked() == false)
                                                company.remove("MAHINDRA");
                                            if (((CheckBox) findViewById(R.id.checkMaruti)).isChecked() == false)
                                                company.remove("MARUTI");
                                            if (((CheckBox) findViewById(R.id.checkNissan)).isChecked() == false)
                                                company.remove("NISSAN");
                                            if (((CheckBox) findViewById(R.id.checkRenault)).isChecked() == false)
                                                company.remove("RENAULT");
                                            if (((CheckBox) findViewById(R.id.checkToyota)).isChecked() == false)
                                                company.remove("TOYOTA");
                                            if (((CheckBox) findViewById(R.id.checkVolkswagen)).isChecked() == false)
                                                company.remove("VOLKSWAGEN");
                                        }
                                        else {
                                            if (((CheckBox) findViewById(R.id.checkChevrolet)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkFord)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkMahindra)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkMaruti)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkNissan)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkRenault)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkToyota)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkVolkswagen)).isChecked())  {
                                                company.clear();
                                                if (((CheckBox) findViewById(R.id.checkChevrolet)).isChecked())
                                                    company.add("CHEVROLET");
                                                if (((CheckBox) findViewById(R.id.checkFord)).isChecked())
                                                    company.add("FORD");
                                                if (((CheckBox) findViewById(R.id.checkMahindra)).isChecked())
                                                    company.add("MAHINDRA");
                                                if (((CheckBox) findViewById(R.id.checkMaruti)).isChecked())
                                                    company.add("MARUTI");
                                                if (((CheckBox) findViewById(R.id.checkNissan)).isChecked())
                                                    company.add("NISSAN");
                                                if (((CheckBox) findViewById(R.id.checkRenault)).isChecked())
                                                    company.add("RENAULT");
                                                if (((CheckBox) findViewById(R.id.checkToyota)).isChecked())
                                                    company.add("TOYOTA");
                                                if (((CheckBox) findViewById(R.id.checkVolkswagen)).isChecked())
                                                    company.add("VOLKSWAGEN");
                                            }
                                        }
                                        if (((CheckBox) findViewById(R.id.checkBTOthers)).isChecked()) {
                                            if (((CheckBox) findViewById(R.id.checkHatch)).isChecked() == false)
                                                bt.remove("HATCHBACK");
                                            if (((CheckBox) findViewById(R.id.checkSUV)).isChecked() == false)
                                                bt.remove("SUV");
                                            if (((CheckBox) findViewById(R.id.checkSedan)).isChecked() == false)
                                                bt.remove("SEDAN");
                                            if (((CheckBox) findViewById(R.id.checkVan)).isChecked() == false)
                                                bt.remove("VAN");
                                            if (((CheckBox) findViewById(R.id.checkCoupe)).isChecked() == false)
                                                bt.remove("COUPE");
                                            if (((CheckBox) findViewById(R.id.checkJeep)).isChecked() == false)
                                                bt.remove("JEEP");
                                        } else {
                                            if (((CheckBox) findViewById(R.id.checkHatch)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkSUV)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkSedan)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkVan)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkCoupe)).isChecked() ||
                                                    ((CheckBox) findViewById(R.id.checkJeep)).isChecked()) {
                                                bt.clear();
                                                if (((CheckBox) findViewById(R.id.checkHatch)).isChecked())
                                                    bt.add("HATCHBACK");
                                                if (((CheckBox) findViewById(R.id.checkSUV)).isChecked())
                                                    bt.add("SUV");
                                                if (((CheckBox) findViewById(R.id.checkSedan)).isChecked())
                                                    bt.add("SEDAN");
                                                if (((CheckBox) findViewById(R.id.checkVan)).isChecked())
                                                    bt.add("VAN");
                                                if (((CheckBox) findViewById(R.id.checkCoupe)).isChecked())
                                                    bt.add("COUPE");
                                                if (((CheckBox) findViewById(R.id.checkJeep)).isChecked())
                                                    bt.add("JEEP");
                                            }
                                        }
                                        if (((CheckBox) findViewById(R.id.checkDiesel)).isChecked()) fuel.add("Diesel");
                                        if (((CheckBox) findViewById(R.id.checkPetrol)).isChecked()) fuel.add("Petrol");
                                        if (((CheckBox) findViewById(R.id.checkCNG)).isChecked()) fuel.add("CNG");
                                        if (((CheckBox) findViewById(R.id.checkHybrid)).isChecked()) fuel.add("Hybrid");
                                        if (((CheckBox) findViewById(R.id.checkDiesel)).isChecked() == false &&
                                                ((CheckBox) findViewById(R.id.checkPetrol)).isChecked() == false &&
                                                ((CheckBox) findViewById(R.id.checkCNG)).isChecked() == false &&
                                                ((CheckBox) findViewById(R.id.checkHybrid)).isChecked() == false) {
                                            fuel.add("Diesel");
                                            fuel.add("Petrol");
                                            fuel.add("CNG");
                                            fuel.add("Hybrid");
                                        }
                                        b.putStringArrayList("COMPANY", company);
                                        b.putStringArrayList("BODY TYPE", bt);
                                        b.putStringArrayList("FUEL SUPPORTED", fuel);
                                        if (((EditText) findViewById(R.id.txtMileage)).getText().toString().compareTo("") == 0)
                                            b.putInt("Mileage", 0);
                                        else
                                            b.putInt("Mileage", Integer.parseInt(((EditText) findViewById(R.id.txtMileage)).getText().toString()));
                                        if (pricePos == 1) {
                                            b.putInt("Price_Max", 5);
                                            b.putInt("Price_Min", 0);
                                        } else if (pricePos == 2) {
                                            b.putInt("Price_Max", 25);
                                            b.putInt("Price_Min", 5);
                                        } else if (pricePos == 3) {
                                            b.putInt("Price_Max", 50);
                                            b.putInt("Price_Min", 25);
                                        } else if (pricePos == 4) {
                                            b.putInt("Price_Max", 0);
                                            b.putInt("Price_Min", 50);
                                        } else {
                                            b.putInt("Price_Max", 0);
                                            b.putInt("Price_Min", 0);
                                        }
                                        Intent intent = new Intent(filter.this, SearchResultActivity.class);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    }


                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }


        });
    }


    public void onBackPressed()
    {
        startActivity(new Intent(filter.this, MainActivity.class));
        finish();
    }
}
