package com.deputy;

import com.deputy.activity.BuildConfig;
import com.deputy.api.RestService;
import com.deputy.api.model.Business;
import com.deputy.api.request.Controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;

/**
 * Created by srilatha on 28/03/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml",constants = BuildConfig.class, sdk=21)
public class GetBusinessApiCallUnitTest {

    private RestService restService;

    @Before
    public void setUp() {

        restService = Controller.createApiCallInstance();

    }

    @Test
    public void getBusinessDetailsTest() throws Exception {

        Call<Business> call = restService.getBusiness();

        Response<Business> response = call.execute();

        Assert.assertTrue(response.isSuccessful());

        assertEquals(response.body().getName(), "Deputy");

    }

}
