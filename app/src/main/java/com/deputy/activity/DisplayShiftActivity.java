package com.deputy.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.deputy.api.model.Shift;
import com.deputy.util.DateUtil;

public class DisplayShiftActivity extends BaseActivity implements OnMapReadyCallback {

    private Toolbar toolbar;
    private String[] navMenuTitles;

    private TextView tv_shift_start_date;
    private TextView tv_shift_end_time;
    private TextView tv_shift_start_time;

    SupportMapFragment mapFragment;

    private GoogleMap mMap;
    private double mStartLatitude;
    private double mStartLongitude;

    private double mEndLatitude;
    private double mEndLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_shift);

        initResources();
    }

    /**
     * initResources method initializes the layout resources
     */
    private void initResources() {

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedpreferences = getSharedPreferences(getResources().getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
        setupToolbar(sharedpreferences.getString(getResources().getString(R.string.editor_prefs_business_name), ""));

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        set(navMenuTitles);

        tv_shift_start_date = (TextView) findViewById(R.id.tv_shift_start_date);
        tv_shift_end_time = (TextView) findViewById(R.id.tv_shift_end_time);
        tv_shift_start_time = (TextView) findViewById(R.id.tv_shift_start_time);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Shift shiftObj = (Shift) bundle.getSerializable("ShiftObj");

            String shiftDate = DateUtil.getDateFromString(shiftObj.getStart(), "yyyy-MM-dd'T'hh:mm:ssZ", "dd, MMM yyyy");
            tv_shift_start_date.setText(shiftDate);
            tv_shift_start_time.setText(DateUtil.getDateFromString(shiftObj.getStart(), "yyyy-MM-dd'T'hh:mm:ssZ", "HH:mm"));
            if (!shiftObj.getEnd().equals(""))
                tv_shift_end_time.setText(DateUtil.getDateFromString(shiftObj.getEnd(), "yyyy-MM-dd'T'hh:mm:ssZ", "HH:mm"));

            mStartLatitude = shiftObj.getStartLatitude();
            mStartLongitude = shiftObj.getStartLongitude();
            mEndLatitude = shiftObj.getEndLatitude();
            mEndLongitude = shiftObj.getEndLongitude();
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

    }

    /**
     * This callback is triggered when the map is ready to be used.
     * Manipulates the map once available.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker to Map and move the camera

        LatLng startLatLng = new LatLng(mStartLatitude, mStartLongitude);
        LatLng endLatLng = new LatLng(mEndLatitude, mEndLongitude);
        mMap.addMarker(new MarkerOptions().position(startLatLng));
        mMap.addMarker(new MarkerOptions().position(endLatLng));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 13.5f));
    }

}
