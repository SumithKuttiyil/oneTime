package com.example.hi.onetime;

import android.app.Fragment;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by Hi on 30-05-2017.
 */

public class DistanceEvaluatorActivity extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);

        final TabHost mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("first").setIndicator("First").setContent(new Intent(this, MapsActivity.class)));
       // mTabHost.addTab(mTabHost.newTabSpec("second").setIndicator("Second").setContent(new Intent(this , MapsActivity.class )));
        mTabHost.setCurrentTab(0);
        mTabHost.setFocusable(true);
        mTabHost.clearFocus();




    }



}
