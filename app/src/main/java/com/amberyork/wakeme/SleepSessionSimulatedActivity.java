package com.amberyork.wakeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

//for plotting


public class SleepSessionSimulatedActivity extends AppCompatActivity {
    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_session_simulated);

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

                            //if statement to generate data poitns at different thresholds
                            //   to make it look like different sleep stages
                             if (Y % 100 > 10 && Y %100 < 30) {
                                addEntry(2,5);//rem1
                             } else if (Y % 100 > 60 && Y %100 < 75) {
                                 addEntry(3,7);//rem2
                             } else if (Y % 100 > 75 ) {
                                 addEntry(26,29); //shallow1
                             } else{
                                addEntry(23,27); //shallow2
                             }
                        }
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


    public void goToMainActivity (View view){
        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


}