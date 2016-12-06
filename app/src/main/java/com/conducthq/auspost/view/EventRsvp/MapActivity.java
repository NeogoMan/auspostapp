package com.conducthq.auspost.view.EventRsvp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.conducthq.auspost.R;
import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.task.ContentTask;
import com.conducthq.auspost.view.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

/**
 * Created by conduct19 on 27/10/2016.
 */

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private Button mOpenWithMaps;
    private GoogleMap mMap;

    String latitude;
    String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        setTopActionBar(R.string.map_title, -1, Constants.BTN_X);
        mOpenWithMaps = (Button) findViewById(R.id.button);

        Bundle extras = getIntent().getExtras();
        latitude = extras.getString(Constants.MAP_LATITUDE);
        longitude = extras.getString(Constants.MAP_LONGITUDE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mOpenWithMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Uri gmmIntentUri = Uri.parse("geo:"+latitude+","+longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.map_google_maps_not_found, Toast.LENGTH_LONG).show();
                }
            }
        });

        if(actionBarClose != null) {
            actionBarClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng( Double.parseDouble(latitude), Double.parseDouble(longitude));
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isFinishing()) {
            MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
