package com.missionsv.android.seventhgear;


public class DataModel {


    Integer id;
    String car;
    String variant;
    String URL;

    public DataModel(Integer id, String car, String variant, String URL) {

        this.id=id;
        this.car=car;
        this.variant=variant;
        this.URL=URL;
    }


    public Integer getId() { return id;  }
    public String getCar() { return car;  }
    public String getVariant() { return variant; }
    public String getURL() {
        return URL;
    }

}