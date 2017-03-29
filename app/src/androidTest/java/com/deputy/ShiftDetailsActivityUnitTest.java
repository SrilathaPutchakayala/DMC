package com.deputy;

import android.support.test.rule.ActivityTestRule;
import android.widget.ListView;
import com.deputy.activity.R;
import com.deputy.activity.ShiftDetailsActivity;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by srilatha on 29/03/2017.
 */
public class ShiftDetailsActivityUnitTest {

    @Rule
    public ActivityTestRule<ShiftDetailsActivity> main = new ActivityTestRule<ShiftDetailsActivity>(ShiftDetailsActivity.class);

    @Test
    public void testShouldLaunchTheShiftDetailsActivityAndFindItemsInTheList() throws Exception {
        ListView listview = (ListView) main.getActivity().findViewById(R.id.lv_shift_details);

        assertEquals(listview.getCount(), 1);
    }



}
