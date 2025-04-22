package com.app.weightloss.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Support {
    public static String getFormattedCurrentDate(){
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        return sdf.format(currentDate);
    }
}
