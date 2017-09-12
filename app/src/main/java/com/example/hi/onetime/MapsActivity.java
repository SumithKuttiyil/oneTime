package com.example.hi.onetime;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Path;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.example.hi.onetime.Auth.AmazonClientManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzf;
import  com.google.android.gms.play_services.*;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.Unit;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements LocationListener, PlaceSelectionListener ,OnMapReadyCallback{

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public  Context mContext;
    private TextView locationTextView;
    private TextView attributionsTextView;
    LatLng userLocation;
    LatLng meetingPlace;
    String meetplaceName;
    float distance[]=new float[1];
    Button button;
    private static final String LOG_TAG = "PlaceSelectionListener";


    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationTextView = (TextView) findViewById(R.id.textView3);
        attributionsTextView = (TextView) findViewById(R.id.txt_attributions);
        button=(Button)findViewById(R.id.button);
        final ListView listView=(ListView) findViewById(R.id.list);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Search a Location");
        autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.getText().equals("See the distance for others")){
                    CustomAdapter adapter = new CustomAdapter(MapsActivity.this,  new ListContact().getContactList());
                    adapter.getItemId(R.id.checkBox);
                    ArrayList<DataModel> dataModelsList = new ArrayList();
                    ArrayList<DataModel> finalData = new ArrayList();
                    DataModel dataModel= new DataModel();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference("onetime-170912");

                    for(int i=0;i<adapter.getCount();++i){
                        dataModel=(DataModel) adapter.getItem(i);
                        //dataModelsList.add((DataModel) adapter.getItem(0));
                        if (dataModel.checked==true)
                            dataModelsList.add(dataModel);
                    }
                    for(DataModel model:dataModelsList){
                        String s = String.format("%.1f", distance[0]/1000);
                        model.setDistance("Distance for him :"+s+"KM");
                        model.setDuration("Time taken :2 Hours");
                        finalData.add(model);
                        // Create a new child with a auto-generated ID.
                        DatabaseReference childRef = myRef.push();
                        // Set the child's data to the value passed in from the text box.
                       DatabaseReference kid=childRef.push();
                        kid.setValue("Meet at"+meetplaceName);
                        kid=childRef.push();
                        kid.setValue(model.name);
                       //kid=childRef.push();
                       //kid.setValue(model.phoneNumber);
                    }
                    CustomAdapter adapt = new CustomAdapter(MapsActivity.this,  finalData);
                    listView.setAdapter(adapt);
                    button.setVisibility(View.GONE);

                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // Add a marker in Sydney and move the camera
        Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
         userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
        mMap.getMinZoomLevel();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        mMap.getMinZoomLevel();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
    @Override
    public void onPlaceSelected(Place place) {

        meetplaceName=place.getName().toString();
         meetingPlace = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
        mMap.addMarker(new MarkerOptions().position(meetingPlace).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(meetingPlace, 16));
        mMap.getMinZoomLevel();
         distance=new float[1];
        GeoApiContext geoApiContext=new GeoApiContext();
        geoApiContext.setApiKey(getString(R.string.google_maps_key));
        Location.distanceBetween(userLocation.latitude, userLocation.longitude, meetingPlace.latitude, meetingPlace.longitude, distance);
        try{DistanceMatrix results =  DistanceMatrixApi.getDistanceMatrix(geoApiContext,
                new String[] {"chengannur"}, new String[] {"bangalore"}).units(Unit.METRIC).await();}catch (Exception e){

        }
         AmazonClientManager clientManager = null;
        clientManager = new AmazonClientManager(this);
       // DynamoDBOperation dynamoDBOperation=new DynamoDBOperation(this, clientManager);
        LocationsDO locationsdo=new LocationsDO();
        locationsdo.setUserId(place.getId());
        locationsdo.setItemId("demo-itemId"+place.getId());
        locationsdo.setCategory(place.getAddress().toString());
        locationsdo.setLatitude(place.getLatLng().latitude);
        locationsdo.setLongitude(place.getLatLng().longitude);
        locationsdo.setName(place.getName().toString());
        locationsdo.setName(place.getName().toString());
        //dynamoDBOperation.setLocationsDO(locationsdo);






        Log.i(LOG_TAG, "Place Selected: " + place.getName());
        /*locationTextView.setText(getString(R.string.formatted_place_data, place
                .getName(), place.getAddress(), place.getPhoneNumber(), place
                .getWebsiteUri(), distance[0], place.getId()));*/
        if (!TextUtils.isEmpty(place.getAttributions())){
            attributionsTextView.setText(String.valueOf(distance[0]));
            //attributionsTextView.setText(Html.fromHtml(place.getAttributions().toString()));
        }
        button.setVisibility(View.VISIBLE);

    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }


}
