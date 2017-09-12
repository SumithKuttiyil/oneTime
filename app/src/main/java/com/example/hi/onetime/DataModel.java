package com.example.hi.onetime;

import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Hi on 25-05-2017.
 */

public class DataModel {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public DataModel() {
    }

    String name="";
    String phoneNumber="";
    Boolean checked=false;
    String disPic;
    String distance="";
    String duration="";

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDisPic() {
        return disPic;
    }

    public void setDisPic(String disPic) {
        this.disPic = disPic;
    }

    public DataModel(String name, String phoneNumber, String distance , String duration,  Boolean checked, String disPic){
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.checked=checked;
        this.disPic=disPic;
        this.duration=duration;
        this.distance=distance;
    }

    public boolean isChecked()
    {
        return checked;
    }
}
