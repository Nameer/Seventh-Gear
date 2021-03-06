package com.missionsv.android.seventhgear;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static com.missionsv.android.seventhgear.R.id.txtId;
import static com.missionsv.android.seventhgear.R.id.txtURL;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtId;
        TextView txtCar;
        TextView txtVariant;
        TextView txtURL;
        ImageView Img;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtId = (TextView) itemView.findViewById(R.id.txtId);
            this.txtCar = (TextView) itemView.findViewById(R.id.txtCar);
            this.txtVariant = (TextView) itemView.findViewById(R.id.txtVariant);
            this.txtURL = (TextView) itemView.findViewById(R.id.txtURL);
            this.Img= (ImageView) itemView.findViewById(R.id.img);
        }
    }

    public CustomAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(SearchResultActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

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
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView txtId = holder.txtId;
        TextView txtCar = holder.txtCar;
        TextView txtVariant = holder.txtVariant;
        TextView txtURL = holder.txtURL;
        ImageView Img = holder.Img;

        txtId.setText(Integer.toString(dataSet.get(listPosition).getId()));
        txtCar.setText(dataSet.get(listPosition).getCar());
        txtVariant.setText(dataSet.get(listPosition).getVariant());
        txtURL.setText(dataSet.get(listPosition).getURL());
        // set image here...!!!!!

        if (((String) txtURL.getText()).length() == 0)
            Img.setImageResource(R.drawable.no_prev);
        else
            new ImageDownloader(Img).execute((String) txtURL.getText());
        /*try {
            new ImageDownloader(Img).execute(URL);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Image setting", e.getMessage());
        }*/
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
