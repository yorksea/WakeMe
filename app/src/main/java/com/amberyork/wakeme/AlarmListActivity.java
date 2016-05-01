package com.amberyork.wakeme;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class AlarmListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // toast("started alarm list");

        /* TESTING DISPLAY  - show last alarm entered
        //get data from db and then toast it
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String lastAlarm = dbHelper.getLastAlarmString();

        toast(lastAlarm);
*/

    }



    //just for list activity
    public void goToAlarmEditActivity (View view){
        View parentRow = (View) view.getParent();
       // LinearLayout linearLayout = (LinearLayout) parentRow.getParent();
      //  final int position = linearLayout.getPositionForView(parentRow);

        Intent intent = new Intent (getApplicationContext(), AlarmEditActivity.class);
       // intent.putExtra("alarm_index",position);
        startActivity(intent);
    }

    //just for list activity
    public void goToAlarmNewActivity (View view){
        Intent intent = new Intent (getApplicationContext(), AlarmNewActivity.class);
        startActivity(intent);
    }

    //toast for testing
    private void toast(String aToast){
        Toast.makeText(getApplicationContext(), aToast, Toast.LENGTH_LONG).show();
    }




}
