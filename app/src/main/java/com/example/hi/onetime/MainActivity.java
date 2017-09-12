package com.example.hi.onetime;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ListView listView;
    public  ListContact listContact = new ListContact();
    public ArrayList<DataModel> contactList= new ArrayList();
    public Cursor cursor;
    public Button button;
    public SearchView search;
    public ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


         setContentView(R.layout.activity_main);
         button=(Button) findViewById(R.id.button);

        listView=(ListView) findViewById(R.id.list);
        CheckBox checkBox=(CheckBox)findViewById(R.id.checkBox);
        imageView=(ImageView)findViewById(R.id.imageView);

            button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.getText().equals("Click Here to load Contacts")){
                getContactDetails();
                CustomAdapter adapter = new CustomAdapter(MainActivity.this,  contactList);
                listView.setAdapter(adapter);
                button.setText("Add your members");}
                else if(button.getText().equals("Add your members")){
                    search.setVisibility(View.GONE);
                    Adapter adapter=listView.getAdapter();
                    adapter.getItemId(R.id.checkBox);
                    ArrayList<DataModel> dataModelsList = new ArrayList();
                    DataModel dataModel= new DataModel();
                    for(int i=0;i<adapter.getCount();++i){
                        dataModel=(DataModel) adapter.getItem(i);
                      //dataModelsList.add((DataModel) adapter.getItem(0));
                        if (dataModel.checked==true)
                            dataModelsList.add(dataModel);
                    }
                    listContact.setContactList(dataModelsList);
                    CustomAdapter adapt = new CustomAdapter(MainActivity.this,  dataModelsList);
                    listView.setAdapter(adapt);
                    button.setText("Click to know where they are");
                }
                else if (button.getText().equals("Click to know where they are")){
                    Intent intent = new Intent(getApplication(), MapsActivity.class);
                    MainActivity.this.startActivity(intent);

                }
            }
        });


    search=(SearchView)findViewById(R.id.search);
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    CustomAdapter adapter = new CustomAdapter(MainActivity.this,  getFilter(newText));
                    listView.setAdapter(adapter);
                    return false;
                }
            });
       search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
              if(hasFocus)
                  button.setVisibility(View.INVISIBLE);
               else
                   button.setVisibility(View.VISIBLE);

           }
       });

    }
    @Override
    public void onBackPressed() {

        if(button.getText().equals("Click to know where they are")){
            button.setText("Add your members");
            CustomAdapter adapter = new CustomAdapter(MainActivity.this,  contactList);
            listView.setAdapter(adapter);
    }else if(button.getText().equals("Add your members")){
            AlertDialog.Builder builder = new AlertDialog.Builder(listView.getContext());
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    protected void getContactDetails(){

        Uri CONTENT_URI= ContactsContract.Contacts.CONTENT_URI;
        String DISPLAY_NAME=ContactsContract.Contacts.DISPLAY_NAME;
        String PHONE_NUMBER=ContactsContract.Contacts.HAS_PHONE_NUMBER;
        ContentResolver contentResolver=getContentResolver();
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.READ_CONTACTS}, 101);
        cursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        Cursor c = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[] { ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY },
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[] { "com.whatsapp" },
                ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY+" ASC");
        Cursor anCursor;
        String s="";

        while(c.moveToNext()){
            DataModel dataModel=new DataModel();
            anCursor=cursor;

            //imageView=(ImageView) findViewById(R.id.imageView);
            while (anCursor.moveToNext()) {
                if(c.getString(c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY)).equals("IndraPG"))
                    s="";
                if(anCursor.getString(anCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).equals("IndraPG"))
                   s="";
                if (c.getString(c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY)).equals(anCursor.getString(anCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)))) {
                    dataModel.setName(c.getString(c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY)));
                    dataModel.setPhoneNumber(anCursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                     permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                     ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

                    final LayoutInflater factory = getLayoutInflater();



                    final View textEntryView = factory.inflate(R.layout.text, null);
                    imageView = (ImageView) textEntryView.findViewById(R.id.imageView);
                    if ((anCursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)))!=null)
                        dataModel.setDisPic(anCursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)));
                    else
                        dataModel.setDisPic("content://com.android.contacts/contacts/48853/photo");
                    dataModel.setChecked(dataModel.isChecked());
                    contactList.add(dataModel);
                    break;
                }
            }
        }
        c.close();
        cursor.close();
            }

            public ArrayList<DataModel> getFilter(String text){
                ArrayList<DataModel> datamodel=new ArrayList<>();
                for(DataModel data:contactList){
                    if(data.getName().toLowerCase().contains(text))
                       datamodel.add(data);
                }
                return datamodel;
            }


    }



