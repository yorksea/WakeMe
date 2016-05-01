package com.amberyork.wakeme;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/*
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.view.View;
 */


public class MotionService extends Service implements SensorEventListener {
    // final static String MY_ACTION = "MY_ACTION";
    final static String MY_ACTION = "com.amberyork.wakeme.MotionService.MY_ACTION";
    private TextView output;
    private String reading;
    private SensorManager mgr;
    private List<Sensor> sensorList;
    static final String TAG = "MotionService";
    int binSeconds = 60;//TODO make this not hardcoded
    Intent intent = new Intent(MY_ACTION);
float noise = 0.20f;//noise threshold

    //motion variables needed for averaging,max, etc
    private ArrayList<Float> binX, binY, binZ;
    private Float lastX = 0.0f;
    private Float lastY = 0.0f;
    private Float lastZ = 0.0f;

    //I am working with epoch because it allows me to round into bins easily
    private int lastEpochMin,thisEpochMin;//to detect bin change
    private long binStartEpoch,binStopEpoch; //for recording actual first and last data point time
    private String binXLabelEpoch;

    //for file io naming
    long sessionStartEpoch  = System.currentTimeMillis() /1000;




    @Override
    //public void onStartCommand() {
    public void onCreate() {
        Log.d(TAG, "onStartCommand");
        mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorList = mgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
        for (Sensor sensor : sensorList) {
            mgr.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }


        //in prep of filling in when motion detected
        this.binX = new ArrayList<Float>();
        this.binY = new ArrayList<Float>();
        this.binZ = new ArrayList<Float>();


        this.lastEpochMin = 0; //0 to insure diffrent from current minute when checked, this != will determine new bin

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mgr.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO  Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder builder = new StringBuilder();

        float diff = Math.abs(lastX-event.values[0])+Math.abs(lastY-event.values[1])+Math.abs(lastZ-event.values[2]);
        if ((diff) > noise){
            //record raw data for testing purposes
            long mTime = System.currentTimeMillis();
            String dataLineRaw = diff+",raw,"+mTime+","+event.values[0]+","+event.values[1]+","+event.values[2];
            writeDataLine(dataLineRaw,"MoveRawData.txt");
        }

        lastX = event.values[0];
        lastY = event.values[1];
        lastZ = event.values[2];


        for (int i = 0; i < event.values.length; i++) {
            builder.append("   [");
            builder.append(i);
            builder.append("] = ");
            builder.append(event.values[i]);
            builder.append("\n");
        }

        reading = builder.toString();
       // Log.d(TAG, "onSensorChanged deltaX"+reading);

        //Send back reading to Activity
        // intent.putExtra("measurement", reading);
        // sendBroadcast(intent);

        //--------BIN STUFF-----------
        //get current minute to compare to bin minute
        long epoch = System.currentTimeMillis() /1000; //divide by 1000 to get seconds
        double epochDouble = ((Number)epoch).doubleValue()/binSeconds;
        //instead of using floor to round to minutes, just explicit cast to int since negative value isn't an issue
        int epochInt = (int)epochDouble;//in minutes
        this.thisEpochMin = epochInt;
        //to get back to string (multiply back to binSeconds and date format
        String dateAsText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(epochInt * binSeconds * 1000L));

        //first just broadcast the onsensorchange velocity before getting into the minute data stuff
        double current_velocity = ( event.values[0] +  event.values[1]+ event.values[2])/3;
        intent.putExtra("onChangeData", epoch+","+current_velocity);
        //intent.putExtra("timestamp", this.binStopEpoch);
        sendBroadcast(intent);


        //now check if same minute, if so, keep adding motions
        if (epochInt == this.lastEpochMin) {
            //it's the same minute, keep adding motions
            //add event values to bin lists
            binX.add(event.values[0]);
            binY.add(event.values[1]);
            binZ.add(event.values[2]);

            this.binStopEpoch = epoch; //update every time you add a movement so last one added is end

        }else{
            //new minute
            //record end stats,  clear bins,new bin and add first motions

            //if there is anything in the bin, then close it out and do calcs
            if (this.binX.size() > 0 ) {
                //instantiate movement bin class
                MovementBin moveBin = new MovementBin();

                //get averages and N
                moveBin.setBinN(binX.size());

                moveBin.setBinAvX(binAverage(this.binX));
                moveBin.setBinAvY(binAverage(this.binY));
                moveBin.setBinAvZ(binAverage(this.binZ));

                moveBin.setBinStdX(binStd(this.binX));
                moveBin.setBinStdY(binStd(this.binY));
                moveBin.setBinStdZ(binStd(this.binZ));

                moveBin.setBinStartEpoch(this.binStartEpoch);
                moveBin.setBinStopEpoch(this.binStopEpoch);
                moveBin.setBinXlabelEpoch(epochInt);

                moveBin.setBinLength(binSeconds);


                Log.d(TAG,"Old bin closed (min,n, avgX,stdX) : " + moveBin.getBinXLabelString()+
                        ", "+ moveBin.getBinN()+ ", "+ moveBin.getBinAvX() +", "+ moveBin.getBinStdX());

                double average_move = ( moveBin.getBinAvX() +  moveBin.getBinAvY()+ moveBin.getBinAvZ())/3;
                //TODO store moveBin in DB

//TODO uncomment intent and sendBroadcast

                //#HEADER: binStopEpoch,binStartEpoch,average_move
                //intent.putExtra("binData", this.binStartEpoch+","+this.binStopEpoch+","+average_move);
               // //intent.putExtra("timestamp", this.binStopEpoch);
               // sendBroadcast(intent);

                /* WRITE DATA TO SD CARD */
                String dataLine = moveBin.getDataLine();
                writeDataLine(dataLine,"MoveBinData.txt");

            }//if bin has stuff
            //clear list
            this.binX.clear();
            this.binY.clear();
            this.binZ.clear();



            //--------NEW BIN START----------
            //add event values to bin lists
            binX.add(event.values[0]);
            binY.add(event.values[1]);
            binZ.add(event.values[2]);

            //move on in time
            this.lastEpochMin = this.thisEpochMin;


            //add bin start time
            this.binStartEpoch = epoch;
            this.binStopEpoch = epoch; //update every time you add a movement so last one added is end
            this.binXLabelEpoch = dateAsText;


            Log.d(TAG,"New Bin started (min) : " + this.binXLabelEpoch);

        }


        //get motion readings for logging purposes
       // Log.d(TAG,"Motion Detected , date string : " + dateAsText);
//---------END BIN STUFF-----------
    }

    //return array in order avg is 0, variance is 1, stddev is 2

    private double binAverage(List <Float> motions) {
        float sum = 0.0f;
        if(!motions.isEmpty()) {
            for (Float motion : motions) {
                sum += motion;
            }
            return sum / motions.size();
        }
        return sum;
    }

    //avg can be found from abov binAverage
    private double binStd(List <Float> motions)
    {
        double temp = 0;
        double avg = binAverage(motions);

        for(double a :motions)
            temp += (avg-a)*(avg-a);

        double variance = temp/motions.size();
        return Math.sqrt(variance);
    }


    public void writeDataLine(String dataLine,String filename){

        Log.d(TAG,"DATALINE: "+dataLine);

        //check if external storage available
        if (!isExternalStorageAvailable() ) {
            Log.d(TAG,"No External Storage available");
        } else if(isExternalStorageReadOnly()) {
            Log.d(TAG,"storage is read only");
        }


            //myExternalFile = new File(getExternalFilesDir(filepath), filename);
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/WakeMe");
            // File dir = new File ("/storage/extSdCard/WakeMe"); //my path for testing
            dir.mkdirs(); //TODO put in check if exists

            //filename "MovementData_"+sessionStartEpoch+".txt";
            File file = new File(dir, filename+"");



        try {
            FileOutputStream fos = new FileOutputStream(file,true);
            fos.write(dataLine.getBytes());
            //now write CR and LF
           // fos.write(13);
            fos.write(10); //LF \n
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}