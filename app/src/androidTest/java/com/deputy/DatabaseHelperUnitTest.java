package com.deputy;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.deputy.api.model.Shift;
import com.deputy.data.DatabaseHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by srilatha on 29/03/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseHelperUnitTest {
    private DatabaseHandler database;


    @Before
    public void setUp() throws Exception {
        InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseHandler.DATABASE_NAME);
        database = new DatabaseHandler(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void shouldAddShift() throws Exception {
        Shift shiftObj = new Shift();
        shiftObj.setId(1);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String currentDatetime = df.format(new Date());
        shiftObj.setStart(currentDatetime);
        database.addShiftDetails(shiftObj);

        List<Shift> shiftDetails = database.getAllShiftDetails();
        assertEquals(shiftDetails.size(),1);
        assertEquals(shiftDetails.get(0).getStart(),currentDatetime);
    }
}
