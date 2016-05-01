package com.amberyork.wakeme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;



public class AlarmEditActivity extends AppCompatActivity    implements
        View.OnClickListener {

    Button setDatePicker, setTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    int alarm_index;
    Alarm thisAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setDatePicker=(Button)findViewById(R.id.set_date);
        setTimePicker=(Button)findViewById(R.id.set_time);

        //handles for the ui elements I will be calling in save data and for use in date/time picker
        txtDate=(EditText)findViewById(R.id.alarm_date);
        txtTime=(EditText)findViewById(R.id.alarm_time);

        setDatePicker.setOnClickListener(this);
        setTimePicker.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v == setDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            //I'm sure there is a better way to get month text name from Calendar but I didn't want to make a new instance
                            String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
                            txtDate.setText(dayOfMonth  + " " + monthNames[(monthOfYear)] + " " + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }


        if (v == setTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            int hour = hourOfDay % 12; //mod it so it turns to 12 hour time
                            if (hour == 0){  hour = 12;}

                            //user logic as to whether am or pm
                            txtTime.setText( String.format("%02d:%02d %s", hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                            /* TODO for future formatting
                            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                            txtTime.setText(timeFormat.format(ts));
                             */
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }



    }

    //button labeled cancel uses this onclick
    public void goToAlarmListActivity (View view){
        Intent intent = new Intent (getApplicationContext(), AlarmListActivity.class);
        startActivity(intent);
    }

    public void saveNewAlarm (View view){
        //get values from ui

        //these int are standins for boolean values in Sqlite
        int boolSound;
        int boolLights;
        int boolHeat;
        int boolAlarmActive;
        String set_time = null;
        //testing output of date and time picker txtDate
        //toast("date; " + txtDate.getText());
       // toast("time; " + txtTime.getText());

        //------------convert display timestamp to ISO format for storage-----------
        try {
            String inputTimeStamp = ""+txtDate.getText().toString()+" "+ txtTime.getText().toString();

            final String inputFormat = "dd MMM yyyy hh:mm a";//E is short day of week
            final String outputFormat = "yyyy-MM-dd HH:mm:ssZ";//ISO format
            set_time = MainActivity.TimeStampConverter(inputFormat, inputTimeStamp,
                    outputFormat);

           // toast("Set time: "+ set_time);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //-------------convert to int---------

       // final CheckBox checkSound = (CheckBox) findViewById(R.id.triggerSound);
        final CheckBox checkLights = (CheckBox) findViewById(R.id.triggerLights);
        final CheckBox checkHeat = (CheckBox) findViewById(R.id.triggerHeat);
        final Switch alarmActive = (Switch) findViewById(R.id.alarm_active);

            //take output of ischecked which is true/false and store as int (fake boolean)

        //not using alexa right now
        boolSound = 0;
       /* if (checkSound.isChecked()) {
            boolSound = 1;
        } else {
            boolSound = 0;
        }
        */


        if (checkLights.isChecked()) {
            boolLights = 1;
        } else {
            boolLights = 0;
        }

        if (checkHeat.isChecked()) {
            boolHeat = 1;
        } else {
            boolHeat = 0;
        }

        if (alarmActive.isChecked()) {
            boolAlarmActive = 1;
        } else {
            boolAlarmActive = 0;
        }

        //testing
        // toast("sound; " + boolSound);

        //make a new alarm row in db
        // Creating alarm
        if(set_time != null) {
            //if time was set, then make alarm item
            Alarm newAlarm = new Alarm(set_time, boolLights, boolHeat, boolSound, boolAlarmActive);

            //now update alarm list fragment to use all alarms in db
            //show data from db for demo purposes and then toast it
            DBHelper DbHelper = new DBHelper(getApplicationContext());
            DbHelper.createAlarm(newAlarm);

            //this should show up when goign back to alarm list
        }

        //go back to alarm list view
        Intent intent = new Intent (getApplicationContext(), AlarmListActivity.class);
        startActivity(intent);
    }

    //toast for testing
    private void toast(String aToast){
        Toast.makeText(getApplicationContext(), aToast,Toast.LENGTH_LONG).show();
    }



}
