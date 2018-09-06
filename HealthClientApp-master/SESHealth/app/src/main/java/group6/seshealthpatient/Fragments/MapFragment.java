package group6.seshealthpatient.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import group6.seshealthpatient.R;

/**
 * Created by Anh on 02/09/2018.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    // Global variables
    private GoogleMap mMap;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private InputMethodManager inputMethodManager;
    private View fragmentView;
    private Context fragmentContext;
    private Marker marker = null;
    private LocationManager locationManager;
    private Intent locationSettingIntent;
    //private PlaceAutocompleteFragment placeAutocompleteFragment;

    //XML contents
    private EditText searchText;
    private ImageButton searchButton;


    // Static variables
    private static final float DEFAULT_ZOOM = 17f;
    private static final String TAG = "MapFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int PERMISSION_REQUEST_CODE = 321;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // initialising global variables
        fragmentView = v;
        fragmentContext = v.getContext();
        searchText = v.findViewById(R.id.search_text_view);
        searchButton = v.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchLocation();
            }
        });

        //using getChildFragmentManager instead of getFragmentManager
        // because it is a fragment inside a fragment
        /*placeAutocompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = place.getLatLng();
                moveCamera(latLng, DEFAULT_ZOOM, place.getAddress().toString());
            }
            @Override
            public void onError(Status status) {
            }
        });*/

        // Ask the user for location permission right after the fragment opens
        getLocationPermission();

        initSearchTextView();

        // Inflate the layout for this fragment
        return v;
    }



    // A function that initialise the searchTextView
    private void initSearchTextView(){
        Log.d(TAG, "initSearchTextView");

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i == EditorInfo.IME_ACTION_SEARCH
                        || i == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    // Execute the method for searching
                    searchLocation();
                }
                return false;
            }
        });
    }


    // This function reads user input and look for the location based on the given text
    private void searchLocation(){
        Log.d(TAG, "SEARCH LOCATION");
        String userInput = searchText.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList = new ArrayList<>();

        try{
            addressList = geocoder.getFromLocationName(userInput, 1);
        }
        catch(IOException e){
            Log.d(TAG, "Geolocation error: " + e.getMessage());
        }

        if(addressList.size() > 0){

            Address locatedAddress = addressList.get(0);
            Log.d(TAG, "Location found: " + locatedAddress.toString());

            // Hide the keyboard right after user hit enter key
            hideKeyBoard();

            LatLng latLng = new LatLng(locatedAddress.getLatitude(), locatedAddress.getLongitude());
            moveCamera(latLng, DEFAULT_ZOOM, locatedAddress.getAddressLine(0));

        }
    }


    // This function hides the keyboard
    private void hideKeyBoard(){
        inputMethodManager = (InputMethodManager)
                fragmentContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(fragmentView.getWindowToken(), 0);
    }


    // A function that asks users for location permission
    private void getLocationPermission(){
        Log.d(TAG, "GET LOCATION PERMISSION");
        String[] permission = {FINE_LOCATION};

        // If permission granted
        if(ContextCompat.checkSelfPermission(
                this.getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
            initialiseMap();
        }
        // Otherwise, request for permission
        else{
            requestPermissions(permission, PERMISSION_REQUEST_CODE);
        }
    }


    // A function that gets the location of the device
    private void getDeviceLocation(){
        Log.d(TAG, "GET DEVICE LOCATION CALLED");

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this.getContext());

        try{
            if(mLocationPermissionGranted){

                final Task location = fusedLocationProviderClient.getLastLocation();
                final Geocoder geocoder = new Geocoder(getContext());

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "FOUND LOCATION SUCCESSFUL");

                            if(gpsStatus()){
                                Location currentLocation = (Location) task.getResult();
                                List<Address> addressList = new ArrayList<>();

                                if(currentLocation != null){

                                    Log.d(TAG, "CURRENT LOCATION FOUND");
                                    // Initialise latLng object with the current position
                                    LatLng latLng =
                                            new LatLng(currentLocation.getLatitude(),
                                                    currentLocation.getLongitude());

                                    try{
                                        addressList = geocoder
                                                .getFromLocation(currentLocation.getLatitude(),
                                                        currentLocation.getLongitude(), 1);
                                    }
                                    catch(IOException e){
                                        Log.d(TAG, "Geolocation error: " + e.getMessage());
                                    }

                                    // Move the camera to the current position
                                    moveCamera(latLng, DEFAULT_ZOOM, "Current location: " +
                                            addressList.get(0).getAddressLine(0));
                                }

                            }
                            else{
                                Toast.makeText(fragmentContext, "Please enable GPS", Toast.LENGTH_LONG).show();
                                locationSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(locationSettingIntent);
                            }
                        }
                        else {
                            Log.d(TAG, "LOCATION NOT FOUND");
                        }
                    }
                });
            }
        }
        catch(SecurityException e){
            Log.d(TAG, "SECURITY EXCEPTION!!!");
        }
    }



    public boolean gpsStatus(){
        locationManager = (LocationManager) fragmentContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    // A function that moves the camera to the position of the given object and
    // zoom into the map with corresponding zoom value
    private void moveCamera(LatLng latLng, float zoom, String string){
        Log.d(TAG, "MOVE CAMERA CALLED");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(marker != null){
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(string));

    }


    // This function initialise the google map and put it on presentation
    private void initialiseMap(){

        // The support library has the same name as the class
        com.google.android.gms.maps.MapFragment mapFragment =
                (com.google.android.gms.maps.MapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        // getMapAsync calls the 'onMapReady' automatically
        mapFragment.getMapAsync(MapFragment.this);

        Log.d(TAG, "INITIALISING MAP");
    }


    // Ask for location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "REQUEST PERMISSION RESULT CALLED");
        mLocationPermissionGranted = false;

        // User chose ALLOW
        if (requestCode == PERMISSION_REQUEST_CODE) {
            Log.d(TAG, "PERMISSION ALLOWED");

            Log.d(TAG, "ALL PERMISSION CHECKED");
            initialiseMap();
            mLocationPermissionGranted = true;
        }
        // User chose DENY
        else{
            Log.d(TAG, "PERMISSION DENIED");
            return;
        }
    }


    // Set the location button visible on the map
    private void enableLocationButton(){
        try {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(MapFragment.this);
            mMap.setOnMyLocationClickListener(MapFragment.this);
            Log.d(TAG, "LOCATION BUTTON ENABLED");
        }
        catch (SecurityException e){
            Log.d(TAG, "LOCATION BUTTON NOT ENABLED");
        }
    }

    // Automatically gets called at the start of the fragment
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "ON MAP READY");
        Toast.makeText(fragmentContext, "ON MAP READY", Toast.LENGTH_LONG).show();

        // Initialise the current map with googleMap
        mMap = googleMap;

        // Makes the location button appear
        enableLocationButton();

    }



    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.d(TAG, "onMyLocationClick");
    }


    // Gets called automatically whenever the location button is clicked
    @Override
    public boolean onMyLocationButtonClick() {

        Log.d(TAG, "onMyLocationButtonClick");

        if(mLocationPermissionGranted){
            getDeviceLocation();
        }

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

}
