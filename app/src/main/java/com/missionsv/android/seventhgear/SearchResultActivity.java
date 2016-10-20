package com.missionsv.android.seventhgear;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import static android.R.attr.id;
import static com.google.android.gms.internal.zzapz.boo;
import static com.missionsv.android.seventhgear.MainActivity.bCar;

public class SearchResultActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result2);

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DataModel>();
        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Bundle b = getIntent().getExtras();
                ArrayList company = (ArrayList) b.get("COMPANY");
                ArrayList bt = (ArrayList) b.get("BODY TYPE");
                ArrayList fuel =  (ArrayList) b.get("FUEL SUPPORTED");
                int found=0;

                for (DataSnapshot child : snapshot.getChildren())
                    for (DataSnapshot car : child.getChildren()) {
                        Map<String, Object> newPost = (Map<String, Object>) car.getValue();
                        if (((String) child.getKey()).compareTo("CARS") == 0) {
                            if (b == null) {
                                startActivity(new Intent(SearchResultActivity.this, filter.class));
                                Toast.makeText(SearchResultActivity.this, "Error occurred. Try again", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                if (company.contains((String) newPost.get("COMPANY")) &&
                                        bt.contains((String) newPost.get("BODY TYPE")) &&
                                        fuel.contains((String) newPost.get("FUEL SUPPORTED")) &&
                                        ((Double) newPost.get("MILEAGE"))>=((double) b.getInt("Mileage")))
                                {
                                    if (((Double) newPost.get("PRICE"))>=((double)b.getInt("Price_Min")))
                                        if (b.getInt("Price_Max")==0 || ((Double) newPost.get("PRICE")) < ((double)b.getInt("Price_Max")))
                                         {
                                             data.add(new DataModel(Integer.parseInt(car.getKey()),
                                                     ((String) newPost.get("COMPANY"))+ ((String) newPost.get("MODEL")),
                                                     (String) newPost.get("VARIANT"),
                                                     (String) newPost.get("URL")));
                                             found++;
                                         }
                                }
                            }
                        }
                        else
                            break;
                    }
                Toast.makeText(SearchResultActivity.this, "Found " + Integer.toString(found) + " Cars", Toast.LENGTH_SHORT).show();
                if (found==0)
                    onBackPressed();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        adapter = new CustomAdapter(data);//
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);//
        adapter.notifyDataSetChanged();

    }


    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            /*TextView tv=(TextView) v.findViewById(R.id.textView3);
            String call=(String) tv.getText();
            Intent intent = new Intent(Intent.ACTION_DIAL);                                          //itest
            intent.setData(Uri.parse("tel:+91"+call));
            v.getContext().startActivity(intent);
            this.context.startActivity(intent);    //itestend*/

            Activity activity = (Activity)v.getContext();
            //FragmentActivity activity = (FragmentActivity)v.getContext();
            //FragmentManager manager = activity.getSupportFragmentManager();


            Intent intent = new Intent(activity, Car.class);
            Bundle b = new Bundle();
            TextView txtId=(TextView) v.findViewById(R.id.txtId);
            b.putInt("car",Integer.parseInt((String) txtId.getText()));
            b.putInt("up", -1);
            intent.putExtras(b);
            v.getContext().startActivity(intent);
            this.context.startActivity(intent);
        }

        private void removeItem(View v) {

        }
    }


    public void onBackPressed()
    {
        startActivity(new Intent(SearchResultActivity.this, filter.class));
        finish();
    }

}
