package com.amberyork.wakeme;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
TODO:
add LOG
incorporate smart_period
edit theme colors
 */


public class MainActivity extends AppCompatActivity {
    //for sqlite db
    private static final String TAG = "MainActivity LOG";
    MyReceiver myReceiver = null;
    private PendingIntent pendingIntent;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Log.d(TAG, "Main Activity Started");

        //DO only once by checking userpref flag: start any alarms in db already
        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("IFTTT", MODE_PRIVATE);
        SharedPreferences.Editor edit = userDetails.edit();

        String alarm_import_flag = userDetails.getString("initial_alarms", "");
        //update key if changed
        if (alarm_import_flag != "1") {
            edit.clear();
            edit.putString("initial_alarms", "1");
            edit.commit();

            //make the alarms
            AlarmServiceManager.makeAllAlarmServices(getApplicationContext());
        }


        //stuff for services


        //Start motion service
        /* disabling to turn off motion service logging for now
        if you want to add this back , uncomment the start and stop service calls in onresume onstop etc
        i = new Intent(this, MotionService.class);
        Log.d(TAG, "onCreate/startService");
*/



    } //end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_motion_display:
                Intent intent = new Intent(this, MotionDisplayActivity.class);
                this.startActivity(intent);
                toast("Switch to Motion Display");
                break;
            case R.id.action_alarm_trigger:
                Intent alarmTriggerIntent = new Intent(this, AlarmTriggerActivity.class);
                this.startActivity(alarmTriggerIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }




    //toast for testing
    private void toast(String aToast){
        Toast.makeText(getApplicationContext(), aToast, Toast.LENGTH_LONG).show();
    }

    //------------------------manipulate dates----------------------------
    public static String TimeStampConverter(final String inputFormat,
                                            String inputTimeStamp, final String outputFormat)
            throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(
                inputFormat).parse(inputTimeStamp));
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume/registering receiver");
        //Register BroadcastReceiver to receive accelerometer data from service
        //if (myReceiver == null){
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MotionService.MY_ACTION);
       // startService(i);
        registerReceiver(myReceiver, intentFilter);
        //}
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause/unregistering receiver");
//        stopService(i);


        if (myReceiver != null){
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (myReceiver != null){
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        //stopService(i);
    }



    //this is to process time bins of accelerator sensor data and broadcast statistics which can be stored in db
    private class MyReceiver extends BroadcastReceiver {
        static final String TAG = "MyReceiver";

        @Override
        public void onReceive(Context arg0, Intent arg1) {
           // Log.d(TAG, "onReceive");
            //String measurement = arg1.getStringExtra("measurement");

            //ok, this is not the best, I am passing one big string and spitting it.  I works, but it's a hammer
            //TODO make more getExtras()
           // String dataString = arg1.getStringExtra("binData");
           // Log.d(TAG,"Recieved movement = "+dataString);
           // String[] binData = dataString.split(","); //split on comma
           // int startEpoch = Integer.valueOf(binData[0]);
           // int stopEpoch = Integer.valueOf(binData[1]);
          //  double move_avg = Double.valueOf(binData[2]);
          //  Log.d(TAG,"Recieved Bin Data= velocity avg="+move_avg+", epoch start="+startEpoch+", stop="+stopEpoch);
        }

    }


}
