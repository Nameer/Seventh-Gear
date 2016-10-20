package com.missionsv.android.seventhgear;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;
import static com.missionsv.android.seventhgear.MainActivity.aCar;

public class compareEntry extends AppCompatActivity {

    boolean var1 = false, var2 = false;
    int car1=-1, car2=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_entry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Spinner company1 = (Spinner) findViewById(R.id.company1);
        final Spinner company2 = (Spinner) findViewById(R.id.company2);
        final Spinner model1 = (Spinner) findViewById(R.id.model1);
        final Spinner model2 = (Spinner) findViewById(R.id.model2);
        final Spinner variant1 = (Spinner) findViewById(R.id.variant1);
        final Spinner variant2 = (Spinner) findViewById(R.id.variant2);
        final Button btnCompare = (Button) findViewById(R.id.btnCompare);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, "Network connectivity failed", Toast.LENGTH_LONG).show();
        }


        //Company load
        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                List<String> company = new ArrayList<String>();

                for (DataSnapshot child : snapshot.getChildren())
                    for (DataSnapshot car : child.getChildren()) {
                        Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                        if (((String) child.getKey()).compareTo("CARS") == 0)
                            if (company.contains((String) newPost.get("COMPANY")) == false)
                                company.add((String) newPost.get("COMPANY"));
                            else {
                            }
                        else
                            break;
                    }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(compareEntry.this, android.R.layout.simple_spinner_dropdown_item, company);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                company1.setAdapter(dataAdapter);
                company1.setClickable(true);
                company2.setAdapter(dataAdapter);
                company2.setClickable(true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(compareEntry.this, "Unable to fetch data. Try again", Toast.LENGTH_SHORT).show();
            }
        });

        //Model1 load
        company1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItem, int pos, long id) {
                DatabaseReference mDatabase2;
                mDatabase2 = FirebaseDatabase.getInstance().getReference();
                mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        List<String> model = new ArrayList<String>();

                        for (DataSnapshot child : snapshot.getChildren())
                            for (DataSnapshot car : child.getChildren()) {
                                Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                                if (((String) child.getKey()).compareTo("CARS") == 0)
                                    if (model.contains((String) newPost.get("MODEL")) == false && ((String) newPost.get("COMPANY")).compareTo(company1.getSelectedItem().toString()) == 0)
                                        model.add((String) newPost.get("MODEL"));
                                    else {
                                    }
                                else
                                    break;
                            }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(compareEntry.this, android.R.layout.simple_spinner_dropdown_item, model);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        model1.setAdapter(dataAdapter);
                        model1.setClickable(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(compareEntry.this, "Unable to fetch data. Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                variant1.setClickable(false);
                btnCompare.setVisibility(View.INVISIBLE);
            }
        });

        //Model2 load
        company2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItem, int pos, long id) {
                DatabaseReference mDatabase2;
                mDatabase2 = FirebaseDatabase.getInstance().getReference();
                mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        List<String> model = new ArrayList<String>();

                        for (DataSnapshot child : snapshot.getChildren())
                            for (DataSnapshot car : child.getChildren()) {
                                Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                                if (((String) child.getKey()).compareTo("CARS") == 0)
                                    if (model.contains((String) newPost.get("MODEL")) == false && ((String) newPost.get("COMPANY")).compareTo(company2.getSelectedItem().toString()) == 0)
                                        model.add((String) newPost.get("MODEL"));
                                    else {
                                    }
                                else
                                    break;
                            }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(compareEntry.this, android.R.layout.simple_spinner_dropdown_item, model);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        model2.setAdapter(dataAdapter);
                        model2.setClickable(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(compareEntry.this, "Unable to fetch data. Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                variant2.setClickable(false);
                btnCompare.setVisibility(View.INVISIBLE);
            }
        });

        //Variant1 load
        model1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItem, int pos, long id) {
                DatabaseReference mDatabase2;
                mDatabase2 = FirebaseDatabase.getInstance().getReference();
                mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        List<String> variant = new ArrayList<String>();

                        for (DataSnapshot child : snapshot.getChildren())
                            for (DataSnapshot car : child.getChildren()) {
                                Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                                if (((String) child.getKey()).compareTo("CARS") == 0)
                                    if (variant.contains((String) newPost.get("VARIANT")) == false && ((String) newPost.get("COMPANY")).compareTo(company1.getSelectedItem().toString()) == 0 && ((String) newPost.get("MODEL")).compareTo(model1.getSelectedItem().toString()) == 0)
                                        variant.add((String) newPost.get("VARIANT"));
                                    else {
                                    }
                                else
                                    break;
                            }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(compareEntry.this, android.R.layout.simple_spinner_dropdown_item, variant);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        variant1.setAdapter(dataAdapter);
                        variant1.setClickable(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(compareEntry.this, "Unable to fetch data. Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btnCompare.setVisibility(View.INVISIBLE);
            }
        });

        //Variant2 load
        model2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItem, int pos, long id) {
                DatabaseReference mDatabase2;
                mDatabase2 = FirebaseDatabase.getInstance().getReference();
                mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        List<String> variant = new ArrayList<String>();

                        for (DataSnapshot child : snapshot.getChildren())
                            for (DataSnapshot car : child.getChildren()) {
                                Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                                if (((String) child.getKey()).compareTo("CARS") == 0)
                                    if (variant.contains((String) newPost.get("VARIANT")) == false && ((String) newPost.get("COMPANY")).compareTo(company2.getSelectedItem().toString()) == 0 && ((String) newPost.get("MODEL")).compareTo(model2.getSelectedItem().toString()) == 0)
                                        variant.add((String) newPost.get("VARIANT"));
                                    else {
                                    }
                                else
                                    break;
                            }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(compareEntry.this, android.R.layout.simple_spinner_dropdown_item, variant);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        variant2.setAdapter(dataAdapter);
                        variant2.setClickable(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(compareEntry.this, "Unable to fetch data. Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btnCompare.setVisibility(View.INVISIBLE);
            }
        });

        //btnCompare visibility - variant1
        variant1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItem, int pos, long id) {
                var1 = true;
                if (var2)
                    btnCompare.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                var1 = false;
                btnCompare.setVisibility(View.INVISIBLE);
            }
        });

        //btnCompare visibility - variant2
        variant2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItem, int pos, long id) {
                var2 = true;
                if (var1)
                    btnCompare.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                var2 = false;
                btnCompare.setVisibility(View.INVISIBLE);
            }
        });

        //btnCompare

        ((Button) findViewById(R.id.btnCompare)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase2;
                mDatabase2 = FirebaseDatabase.getInstance().getReference();
                mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Intent intent = new Intent(compareEntry.this, Compare.class);
                        Bundle b = new Bundle();
                        for (DataSnapshot child : snapshot.getChildren())
                            for (DataSnapshot car : child.getChildren()) {
                                Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                                if (((String) child.getKey()).compareTo("CARS") == 0) {
                                    if (company1.getSelectedItem().toString().compareTo((String) newPost.get("COMPANY"))==0)
                                        if (model1.getSelectedItem().toString().compareTo((String) newPost.get("MODEL"))==0)
                                            if (variant1.getSelectedItem().toString().compareTo((String) newPost.get("VARIANT"))==0)
                                                b.putInt("car1", Integer.parseInt(car.getKey()));
                                    if (company2.getSelectedItem().toString().compareTo((String) newPost.get("COMPANY"))==0)
                                        if (model2.getSelectedItem().toString().compareTo((String) newPost.get("MODEL"))==0)
                                            if (variant2.getSelectedItem().toString().compareTo((String) newPost.get("VARIANT"))==0)
                                                b.putInt("car2", Integer.parseInt(car.getKey()));
                                } else
                                    break;
                            }
                        if (b.getInt("car1")==b.getInt("car2"))
                            Toast.makeText(compareEntry.this, "Comparison not allowed", Toast.LENGTH_SHORT).show();
                        else {
                            intent.putExtras(b);
                            startActivity(intent);
                            //finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(compareEntry.this, MainActivity.class));
        finish();
    }
}
