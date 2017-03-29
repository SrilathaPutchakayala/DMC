package com.deputy.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deputy.adapter.ShiftDetailsAdapter;
import com.deputy.api.RestService;
import com.deputy.api.model.Shift;
import com.deputy.api.request.Controller;
import com.deputy.data.DatabaseHandler;
import com.deputy.util.DbBitmapUtility;
import com.deputy.util.UiUtil;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShiftDetailsActivity extends BaseActivity{//} implements TaskParent {

    private Toolbar toolbar;
    private String[] navMenuTitles;

    private ListView lv_shift_details;

    private ShiftDetailsAdapter shiftDetailsAdapter;
    private DatabaseHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_details);

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

        dbHelper = new DatabaseHandler(this);

        lv_shift_details = (ListView) findViewById(R.id.lv_shift_details);

        fetchShiftDetails();

        lv_shift_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Shift shiftObj = (Shift) parent.getItemAtPosition(position);
                Intent displayShiftIntent = new Intent(ShiftDetailsActivity.this, DisplayShiftActivity.class);
                displayShiftIntent.putExtra("ShiftObj", shiftObj);
                startActivity(displayShiftIntent);
                finish();

            }
        });

    }

    private void fetchShiftDetails() {

        if (UiUtil.isNetworkAvailable(ShiftDetailsActivity.this)) {

            RestService apiCall = Controller.createApiCallInstance();
            Call<Shift.Collection> call = apiCall.getShifts();

            call.enqueue(new Callback<Shift.Collection>() {
                @Override
                public void onResponse(Call<Shift.Collection> call, Response<Shift.Collection> response) {
                    List<Shift> shiftDetails = response.body();

                    for (int i = 0; i < shiftDetails.size(); i++) {
                        Shift shiftObj = shiftDetails.get(i);

                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        shiftObj.setImageInBytes(DbBitmapUtility.getBytes(shiftObj.getImage()));

                        dbHelper.addShiftDetails(shiftObj);

                    }

                    populateView(shiftDetails);
                }

                @Override
                public void onFailure(Call<Shift.Collection> call, Throwable t) {

                }
            });

            /*GetShiftDetailsTask getShiftsTask = new GetShiftDetailsTask(this, this);
            getShiftsTask.startTask();*/

        } else {

            List<Shift> shiftDetailsList = dbHelper.getAllShiftDetails();
            if (shiftDetailsList.size() > 0) {
                populateView(shiftDetailsList);
            }
        }

    }

    /*@Override
    public void onTaskCompleted(BaseSpiceTask task, Object taskTag, Object data) {

        if (data instanceof Shift.Collection) {
            List<Shift> shiftDetails = (ArrayList<Shift>) data;

            for (int i = 0; i < shiftDetails.size(); i++) {
                Shift shiftObj = shiftDetails.get(i);

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                shiftObj.setImageInBytes(DbBitmapUtility.getBytes(shiftObj.getImage()));

                dbHelper.addShiftDetails(shiftObj);

            }

            populateView(shiftDetails);
        }

    }*/

    /**
     * populateView method displays the Shift details
     *
     * @param shiftDetails
     */
    private void populateView(List<Shift> shiftDetails) {
        if (shiftDetails != null) {
            shiftDetailsAdapter = new ShiftDetailsAdapter(shiftDetails, ShiftDetailsActivity.this);
            lv_shift_details.setAdapter(shiftDetailsAdapter);
            shiftDetailsAdapter.notifyDataSetChanged();

        }
    }

    /*@Override
    public void onTaskError(BaseSpiceTask task, Object taskTag, SpiceException spiceException) {

    }

    @Override
    public void onTaskError(BaseSpiceTask task, Object taskTag, SpiceException spiceException, RetrofitError e) {

    }

    @Override
    public void onNetworkError(BaseSpiceTask task, Object taskTag, SpiceException spiceException) {

    }*/
}
