package group6.seshealthpatient.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import group6.seshealthpatient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{


    private GoogleMap mMap;
    private Boolean mLocationPermissionGranted = false;


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
        getLocationPermission();
        // Inflate the layout for this fragment
        return v;
    }




    private void getLocationPermission(){

        Log.d(TAG, "GET LOCATION PERMISSION");
        String[] permission = {FINE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
            initialiseMap();
        }
        else{
            requestPermissions(permission, PERMISSION_REQUEST_CODE);
        }
    }



    private void initialiseMap(){
        com.google.android.gms.maps.MapFragment mapFragment =
                (com.google.android.gms.maps.MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapFragment.this);
        Log.d(TAG, "INITIALISING MAP");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "REQUEST RESULT CALLED");
        mLocationPermissionGranted = false;

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){

                    for(int i=0; i <grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }

                        mLocationPermissionGranted = true;

                        //if all permission granted, initialise the map
                        initialiseMap();
                    }

                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Log.d(TAG, "ON MAP READY");


        //sample code, meant to be changed
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
