package com.deputy;

import com.deputy.activity.BuildConfig;
import com.deputy.api.RestService;
import com.deputy.api.model.Shift;
import com.deputy.api.request.Controller;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by srilatha on 29/03/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml",constants = BuildConfig.class, sdk=21)
public class GetShiftsApiCallUnitTest {

    private RestService restService;

    @Before
    public void setUp() {

        restService = Controller.createApiCallInstance();

    }

    @Test
    public void getShiftDetailaTest() throws Exception {

        Call<Shift.Collection> call = restService.getShifts();

        Response<Shift.Collection> response = call.execute();

        Assert.assertTrue(response.isSuccessful());

    }

}
