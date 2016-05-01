package com.amberyork.wakeme;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by yorksea on 5/1/16.
 */
public class AlarmServiceManager {


    public static void makeAllAlarmServices(Context context) {
        //----------------MANAGE ALARM BASED ON SETTINGS--------------//
        //get this alarm's set time and change to date format, will use milliseconds when setting


        //get current alarm list
        DBHelper dbHelper = new DBHelper(context);

        ArrayList<Alarm> alarms = dbHelper.getAllAlarms();

        for (int pos = 0; pos < alarms.size(); pos++) {

            Alarm alarm = alarms.get(pos);
            AlarmServiceManager.addAlarmToManager(alarm,context);
        }
    }

    public static void addAlarmToManager(Alarm alarm,Context context) {

        try {//for parseexception on date format

            long alarmMs;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date mDate = sdf.parse(alarm.getSet_time());

            //alarmMs is when the alarms service is set to in ms
            alarmMs = mDate.getTime();

            //don't add the alarm if alarmMs already over

            //get current time
            Calendar c = Calendar.getInstance();
            int currentMs = c.get(Calendar.MILLISECOND);

            if (alarmMs > currentMs) {//see if alarm time already happened, if not, add new alarm

                //Alarm service setup
                Intent myIntent = new Intent(context, AlarmService.class);
                myIntent.putExtra("alarm_id", "" + alarm.getAlarm_id());
                PendingIntent pendingIntent = PendingIntent.getService(context, alarm.getAlarm_id(), myIntent, 0);


                //make alarm manager for test
                android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, alarmMs, pendingIntent);
                Log.d("Alarm", "Adding Alarm ID: " + alarm.getAlarm_id() + " Service for: " + alarm.getSet_time() + " ms=" + alarmMs);
            } //end  - see if alarm time already happened

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void deleteAlarm(Alarm alarm,Context context){
        Intent myIntent = new Intent(context, AlarmService.class);
        //make sure to use the update flag when making the pending intent

        PendingIntent pendingIntent = PendingIntent.getService(context, alarm.getAlarm_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       // get the alarmMangager
        android.app.AlarmManager alarmManager = (android.app.AlarmManager)context.getSystemService(context.ALARM_SERVICE);

        //cancel in the alarm manager
        alarmManager.cancel(pendingIntent);
        Log.d("AlarmServiceManager","Revmoved alarm ID: "+alarm.getAlarm_id()+" for: "+alarm.getSet_time());
        //cancel the intent too
        pendingIntent.cancel();
    }
}
