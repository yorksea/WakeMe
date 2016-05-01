package com.amberyork.wakeme;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class AlarmSound extends AppCompatActivity {
    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_sound);

        //simple activity.  Turn on screen, sound alarm

        //get default ringtone
        //TODO customize alarm sound

        //Display Time of Alarm
        String setTime ;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            setTime= null;
        } else {
            setTime= extras.getString("Time");
            TextView tvTime = (TextView) findViewById(R.id.setTimeDisplay);
            tvTime.setText(setTime);
        }
        ringtone = RingtoneManager.getRingtone(this, Settings.System.DEFAULT_RINGTONE_URI);
        ringtone.play();

    }

    public void stopAlarm (View view){

        ringtone.stop();

        //now go to alarm list
        Intent intent = new Intent (getApplicationContext(), AlarmListActivity.class);
        startActivity(intent);


    }

}
