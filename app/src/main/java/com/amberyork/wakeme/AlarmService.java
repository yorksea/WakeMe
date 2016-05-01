package com.amberyork.wakeme;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.text.ParseException;

public class AlarmService extends Service {


    private static final String TAG = "AlarmService";

    @Override

    public void onCreate() {

// TODO Auto-generated method stub

       Log.d(TAG,"AlarmService.onCreate()");

    }



    @Override

    public IBinder onBind(Intent intent) {

// TODO Auto-generated method stub

        Log.d(TAG,"AlarmService.onBind()");

        return null;

    }



    @Override

    public void onDestroy() {

// TODO Auto-generated method stub

        super.onDestroy();

        Log.d(TAG,"AlarmService.onDestroy()");

    }



    @Override

    public void onStart(Intent intent, int startId) {

// TODO Auto-generated method stub

        super.onStart(intent, startId);


               Bundle extras = intent.getExtras();
        String alarm_id = "N/A";
        if(extras != null) {
           alarm_id = extras.getString("alarm_id");

        }

        Log.d(TAG,"AlarmService.onStart() ID:"+startId+" AlarmID: "+alarm_id);


        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("IFTTT", MODE_PRIVATE);
        SharedPreferences.Editor edit = userDetails.edit();

        //update key if changed
        String maker_key = userDetails.getString("maker_key", "");


        String aTime = "Alarm time";

        if (startId > 0) { //if it is 0, it's just a test
            //get alarm based on the id since the alarm in the manager has the same id as alarm in db


            DBHelper dbHelper = new DBHelper(getApplicationContext());
            Alarm alarm = dbHelper.getAlarmByID(alarm_id);
            //NOW TRIGGER ALARM based on settings

            String trigger;
            //check if should set lights on
            if (alarm.getTrigger_lights() != 0) {
                trigger = "lights_on";
                new PostClient().execute(trigger, maker_key);
            }

            if (alarm.getTrigger_heat() != 0) {
                trigger = "heat_wakeup";
                new PostClient().execute(trigger, maker_key);
            }


            try {
                aTime = alarm.getSetTimePrettyTime();
            } catch (ParseException e) {
                e.printStackTrace();

            }

            //sleep 10 seconds before alarm sounds so the heat and light have a head start on getting you up

            if (alarm.getTrigger_heat() == 1 || alarm.getTrigger_lights() == 1) {
                //delay alarm sound and activity if heats or lights to give yourself time to wake up from heat or lights
                SystemClock.sleep(5000);
            }

            //now go to AlarmSound to play sound
            Intent i= new Intent (getApplicationContext(), AlarmSound.class);
            i.putExtra("Time",aTime);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            startActivity(i);


        }else{
            //not real
        }


    }



    @Override

    public boolean onUnbind(Intent intent) {

// TODO Auto-generated method stub

        Log.d(TAG,"AlarmService.onUnbind()");
        return super.onUnbind(intent);

    }



}