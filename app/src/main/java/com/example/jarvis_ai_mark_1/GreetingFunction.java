package com.example.jarvis_ai_mark_1;

import java.util.Calendar;

public class GreetingFunction {
    /*
    Function to greet the user whatever time it is
     */
    static String wishMe() {
        String greet = "";
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);

        // If the time is between 6am to 12pm, then Jarvis will say 'Good Morning Sir'
        // If the time is between 12pm to 4pm, then Jarvis will say 'Good Afternoon Sir'
        // If the time is between 4pm to 10pm, then Jarvis will say 'Good Evening Sir'
        // If the time is between 10pm to 6am, then Jarvis will say 'Good Night Sir'
        if (time >= 6 && time < 12) {
            greet = "Good Morning Sir";
        } else if (time >= 12 && time < 16) {
            greet = "Good Afternoon Sir";
        } else if (time >= 16 && time < 22) {
            greet = "Good Evening Sir";
        } else if (time >= 22 && time < 6) {
            greet = "Good Night Sir";
        }

        return greet;
    }
}
