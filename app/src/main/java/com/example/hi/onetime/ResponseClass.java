package com.example.hi.onetime;

/**
 * Created by Hi on 26-06-2017.
 */

public  class ResponseClass {
    String greetings="";
    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String jsonObj) {
        this.greetings = jsonObj;
    }

    public ResponseClass(String jsonObj) {
        this.greetings = jsonObj;
    }


    public ResponseClass() {
    }

}