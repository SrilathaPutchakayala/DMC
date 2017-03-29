package com.deputy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by srilatha on 8/09/2016.
 */
public class DateUtil {

    public static String getDateFromString(String dateInString, String actualformat, String exceptedFormat) {
        SimpleDateFormat form = new SimpleDateFormat(actualformat);

        String formatedDate = null;
        Date date;
        try {
            date = form.parse(dateInString);
            SimpleDateFormat postFormater = new SimpleDateFormat(exceptedFormat);
            formatedDate = postFormater.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }


}
