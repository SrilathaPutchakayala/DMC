package com.deputy.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.deputy.api.RestService;
import com.deputy.api.model.Business;
import com.deputy.api.request.Controller;
import com.deputy.util.UiUtil;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetStartedActivity extends BaseActivity {

    private ImageView iv_splash;

    private Toolbar toolbar;
    private String[] navMenuTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        initResources();

    }

    /**
     * initResources method initializes the layout resources
     */
    private void initResources() {

        iv_splash = (ImageView) findViewById(R.id.iv_splash);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        set(navMenuTitles);

        if (UiUtil.isNetworkAvailable(this)) {

            RestService apiCall = Controller.createApiCallInstance();
            Call<Business> call = apiCall.getBusiness();

            call.enqueue(new Callback<Business>() {
                @Override
                public void onResponse(Call<Business> call, Response<Business> response) {

                    Business businessObj = response.body();

                    SharedPreferences sharedpreferences = getSharedPreferences(getResources().getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(getResources().getString(R.string.editor_prefs_business_name), businessObj.getName());
                    editor.commit();

                    setupToolbar(businessObj.getName());

                    Picasso.with(GetStartedActivity.this)
                            .load(businessObj.getLogo())
                            .into(iv_splash);

                    showNext();

                }

                @Override
                public void onFailure(Call<Business> call, Throwable t) {

                }
            });

           /* GetBusinessDetailsTask task = new GetBusinessDetailsTask(this, this);
            task.startTask();*/
        } else {
            SharedPreferences sharedpreferences = getSharedPreferences(getResources().getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
            setupToolbar(sharedpreferences.getString(getResources().getString(R.string.editor_prefs_business_name), getResources().getString(R.string.app_name)));

            showNext();

        }


    }

    /**
     * showNext() takes the User to next screen where the user can start the shift and can see the shift details
     */
    private void showNext() {

        Thread splashThread = new Thread() {
            public void run() {
                try {
                    sleep(2000);

                    Intent startShiftIntent = new Intent(GetStartedActivity.this, StartShiftActivity.class);
                    startActivity(startShiftIntent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }

    /*
    @Override
    public void onTaskCompleted(BaseSpiceTask task, Object taskTag, Object data) {
        if (data instanceof Business) {



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
