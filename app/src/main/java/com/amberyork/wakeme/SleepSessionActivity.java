package com.amberyork.wakeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;
//for plotting


public class SleepSessionActivity extends AppCompatActivity {
    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    private static final String TAG = "SleepSessionActivity";
    boolean useSim = Boolean.parseBoolean("True");//checked by default

    MyReceiver myReceiver = null;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_session);

        //use sim or not click binding
        CheckBox checkBox = (CheckBox) findViewById(R.id.useSimData);
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Log.d(TAG, "use Simulated Data is checked");
                    useSim = Boolean.parseBoolean("True");
                }else {
                    Log.d(TAG, "use Simulated Data is unchecked");
                    useSim = Boolean.parseBoolean("False");
                }
            }
        });
        //  view instance setting up for sleep plot
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // data series def
        series = new LineGraphSeries<DataPoint>();


        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        //setting Y to 30 because in the simulation the max that will happen is 29
        viewport.setMaxY(30);
        viewport.setScrollable(true);


        //Start service
        i = new Intent(this, MotionService.class);
        Log.d(TAG, "onCreate/startService");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // This simulates real time sleep motion data that will be stored later


        new Thread(new Runnable() {

            @Override
            public void run() {
                // add 2000 new data points, it will start scrolling after 200 points
                //the 200 point limit is controlled when passed in appendData below
                for (int i = 0; i < 2000; i++) {
                    final double Y = i;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //only add points if sim is wanted
                            if (useSim) {
                                //if statement to generate data poitns at different thresholds
                                //   to make it look like different sleep stages
                                if (Y % 100 > 10 && Y % 100 < 30) {
                                    addEntry(2, 5);//rem1
                                } else if (Y % 100 > 60 && Y % 100 < 75) {
                                    addEntry(3, 7);//rem2
                                } else if (Y % 100 > 75) {
                                    addEntry(26, 29); //shallow1
                                } else {
                                    addEntry(23, 27); //shallow2
                                }
                            }//if useSim
                        }//end run
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();

        //session stuff
        Log.d(TAG, "onResume/registering receiver");
        //Register BroadcastReceiver to receive accelerometer data from service
        //if (myReceiver == null){
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MotionService.MY_ACTION);
        startService(i);
        registerReceiver(myReceiver, intentFilter);
        //}
    }

    // add random data to graph
    private void addEntry(int min, int max) {
       // scroll to end
      //  series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 30d), true, 30);
        series.appendData(new DataPoint(lastX++, (RANDOM.nextInt((max - min) + 1) + min)*1.0), true, 200);
      //the 200 is how many points to display before scrolling

    }

    //these "goTo" methods are used in the menu fragment
    //have to add getActivity(). if in fragment, before get app context or it won't work.
    public void goToAlarmListActivity (View view){
        Intent intent = new Intent (getApplicationContext(), AlarmListActivity.class);
        startActivity(intent);
    }

    //// view sleep chart


    public void goToSleepSessionActivity (View view){
        Intent intent = new Intent (getApplicationContext(), SleepSessionActivity.class);
        startActivity(intent);
    }


    //// main activiy

    public void goToMainActivity (View view){
        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //this is to process time bins of accelerator sensor data and broadcast statistics which can be stored in db
    private class MyReceiver extends BroadcastReceiver {
        static final String TAG = "MyReceiver";

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            Log.d(TAG, "onReceive");
            //String measurement = arg1.getStringExtra("measurement");

            //ok, this is not the best, I am passing one big string and spitting it.  I works, but it's a hammer
            //TODO make more getExtras()

            //for bindata getting
            String dataString= arg1.getStringExtra("binData") !=null? arg1.getStringExtra("binData"):null;

            //for onchange data getting
            //String dataString= arg1.getStringExtra("onChangeData") !=null? arg1.getStringExtra("onChangeData"):null;

            //make sure you have data in the string first
            if (dataString != null) {
                /*for bindata stuff
                //String dataString = arg1.getStringExtra("binData");
                Log.d(TAG, "Recieved movement = " + dataString);
                String[] binData = dataString.split(","); //split on comma
                int startEpoch = Integer.valueOf(binData[0]);
                int stopEpoch = Integer.valueOf(binData[1]);
                double move_avg = Double.valueOf(binData[2]);
                Log.d(TAG, "Recieved Bin Data= velocity avg=" + move_avg + ", epoch start=" + startEpoch + ", stop=" + stopEpoch);

                /* TODO for realtime data display
                //String onChangeString = arg1.getStringExtra("onChangeData");
                Log.d(TAG, "Recieved raw movement = " + dataString);
                String[] onChangeData = dataString.split(","); //split on comma
                int epoch = Integer.valueOf(onChangeData[0]);
                int current_velocity = Integer.valueOf(onChangeData[1]);
                if (!useSim) {//then use the realtime data
                  //  series.appendData(new DataPoint(epoch, current_velocity), true, 200);
                }
                */
            }
        }

    }

    //-------SECTION FOR SERIVES-------
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause/unregistering receiver");
        stopService(i);


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
        stopService(i);
    }



}