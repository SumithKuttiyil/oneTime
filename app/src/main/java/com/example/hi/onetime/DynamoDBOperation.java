package com.example.hi.onetime;

import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.hi.onetime.Auth.AmazonClientManager;

/**
 * Created by Hi on 16-06-2017.
 */

public class DynamoDBOperation extends AsyncTask<Void, Void, Void> {
    public static Context context;
    public static AmazonClientManager amazonClientManager;
    public static LocationsDO locationsDO;

    public DynamoDBOperation(Context context, AmazonClientManager amazonClientManager, LocationsDO locationsDO){
        this.context=context;
        this.amazonClientManager =amazonClientManager;
        this.locationsDO=locationsDO;
    }

    protected Void doInBackground(Void... voids) {

        dbMapper.save(locationsDO);

        return null;
    }


    AmazonDynamoDBClient ddb = amazonClientManager.ddb();
    DynamoDBMapper dbMapper = new DynamoDBMapper(ddb);

    public static LocationsDO getLocationsDO() {
        return locationsDO;
    }

    public static void setLocationsDO(LocationsDO locationsDO) {
        DynamoDBOperation.locationsDO = locationsDO;
    }



}




