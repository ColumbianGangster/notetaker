package com.example.mouse.notetaker;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by TAE_user2 on 16/03/2016.
 */
public class NoteDateLocaliser {


    public String getLocalDate(Long date){
        DateTime jodadate = new DateTime(date);
        String localizedCalendarDate = DateTimeFormat.mediumDateTime().print(jodadate);
//            Locale locale = new Locale.Builder().build();
        // The default locale is appropriate for tasks that involve presenting data to the user.
        // In this case, you want to use the user's date/time formats, number formats, rules for
        // conversion to lowercase, and so on. In this case, it's safe to use the convenience
        // methods.
//            Locale def = locale.getDefault();
//            DateTimeFormatter formatter = DateTimeFormat.forStyle("MM").withLocale(def);
//            String output = formatter.print(date);
        return localizedCalendarDate;
    }
}
