package com.deputy.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deputy.api.RestService;
import com.deputy.api.request.Controller;
import com.deputy.data.DatabaseHandler;
import com.deputy.util.UiUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartShiftActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {//, TaskParent {

    private Toolbar toolbar;
    private String[] navMenuTitles;

    private Button btn_shift_details;
    private Button btn_start_shift;
    private Button btn_end_shift;

    private double latitude;
    private double longitude;
    private String currentDatetime;

    private SharedPreferences sharedpreferences;
    private DatabaseHandler dbHelper;

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_shift);

        initResources();
    }

    /**
     * initResources method initializes the layout resources
     */
    private void initResources() {

        sharedpreferences = getSharedPreferences(getResources().getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
        dbHelper = new DatabaseHandler(this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        setupToolbar(sharedpreferences.getString(getResources().getString(R.string.editor_prefs_business_name), ""));

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        set(navMenuTitles);

        btn_shift_details = (Button) findViewById(R.id.btn_shift_details);
        btn_start_shift = (Button) findViewById(R.id.btn_start_shift);
        btn_end_shift = (Button) findViewById(R.id.btn_end_shift);


        if (sharedpreferences.getBoolean(getResources().getString(R.string.editor_prefs_shift_status), false)) {
            btn_start_shift.setVisibility(View.GONE);
            btn_end_shift.setVisibility(View.VISIBLE);

        }

        btn_shift_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shiftDetails = new Intent(StartShiftActivity.this, ShiftDetailsActivity.class);
                startActivity(shiftDetails);
            }
        });

        btn_start_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendShiftDetails();

            }
        });

        btn_end_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                endShiftTask();
            }
        });

    }

    /**
     * sendShiftDetails will send the shift start details to api
     */
    private void sendShiftDetails() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StartShiftActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);

        } else {
            googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
            googleApiClient.connect();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
                    googleApiClient.connect();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(StartShiftActivity.class.getSimpleName(), "Connected to Google Play Services!");


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();

            startShift();


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(StartShiftActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

    private void startShift() {

        JSONObject jsonObj = new JSONObject();

        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            currentDatetime = df.format(new Date());

            jsonObj.put("time", currentDatetime);
            jsonObj.put("latitude", String.valueOf(latitude));
            jsonObj.put("longitude", String.valueOf(longitude));

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObj.toString());

            if (UiUtil.isNetworkAvailable(this)) {

                RestService apiCall = Controller.createApiCallInstance();

                if (sharedpreferences.getBoolean(getResources().getString(R.string.editor_prefs_shift_status), false)) {

                    Call<String> call = apiCall.endShift(gsonObject);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {

                                btn_start_shift.setVisibility(View.VISIBLE);
                                btn_end_shift.setVisibility(View.GONE);

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(getResources().getString(R.string.editor_prefs_shift_status), false);
                                editor.commit();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });

                } else {

                    Call<String> call = apiCall.startShift(gsonObject);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {

                                btn_start_shift.setVisibility(View.GONE);
                                btn_end_shift.setVisibility(View.VISIBLE);

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(getResources().getString(R.string.editor_prefs_shift_status), true);
                                editor.commit();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });


                }
            } else {
                Toast.makeText(this, "Please check the Internet!", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException jsonEx) {
            jsonEx.printStackTrace();
        }

    }

    /**
     * endShiftTask method performs the end shift
     */
    private void endShiftTask() {

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        googleApiClient.connect();

    }

    /*@Override
    public void onTaskCompleted(BaseSpiceTask task, Object taskTag, Object data) {

        if (data instanceof Response) {

            if (((Response) data).getStatus() == 200) {


            }

        }

    }

    @Override
    public void onTaskError(BaseSpiceTask task, Object taskTag, SpiceException spiceException) {

    }

    @Override
    public void onTaskError(BaseSpiceTask task, Object taskTag, SpiceException spiceException, RetrofitError e) {

    }

    @Override
    public void onNetworkError(BaseSpiceTask task, Object taskTag, SpiceException spiceException) {

    }*/


}
