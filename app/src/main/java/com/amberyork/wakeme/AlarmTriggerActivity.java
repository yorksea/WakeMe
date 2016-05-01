package com.amberyork.wakeme;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmTriggerActivity extends AppCompatActivity {
    private static final String TAG = "AlarmTriggerActivityLOG";
    private String maker_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_trigger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("IFTTT", MODE_PRIVATE);
        maker_key = userDetails.getString("maker_key", "");

        EditText edit_text = (EditText) findViewById(R.id.maker_key_text);
        edit_text.setText("*************");

        //set click for save key button to actually save the key
        Button button = (Button) findViewById(R.id.maker_key_but);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveMakerKey();
            }
        });
    }
    //--------------------------TESTING POST----------------------------


    //this function is used when a trigger button is pushed to activate the http POST
    public void clickFunc(View v){
        String trigger;
        String idString = "no id";

        Button b = (Button)v;
        //display which button pressed
        Log.d(TAG, "Button pressed: " + b.getText().toString());

        //Log.d(TAG, "Key: " +maker_key);
        //don't know why I couldn't just do this
        //String maker_key =(EditText)findViewById(R.id.maker_key_text).getText().toString();

        //this section activates a post with the trigger based on the id of button click
        //I like this better instead of switching all the cases of button press
        Resources res = v.getResources();     // get resources
        //use of resources to convert getID() result to string through getResourceEntryName

        int id = v.getId();
        if(res != null) {
            idString = res.getResourceEntryName(id); // get id string entry
            Log.d(TAG, idString);
            trigger = idString.replace("but_","");

            //got trigger now instantiate and send off post with trigger
            new PostClient().execute(trigger,maker_key);
            toast("Triggered "+trigger);
            Log.d("TRIGGERED: ",trigger);


        }
    }

    //toast for testing
    private void toast(String aToast){
        Toast.makeText(getApplicationContext(), aToast, Toast.LENGTH_LONG).show();
    }


    private void saveMakerKey(){
        EditText edit_text = (EditText) findViewById(R.id.maker_key_text);
        CharSequence edit_text_value = edit_text.getText();
        maker_key = edit_text_value.toString();

        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("IFTTT", MODE_PRIVATE);
        SharedPreferences.Editor edit = userDetails.edit();

        edit.putString("maker_key", maker_key);
        edit.commit();
        Log.d("TEST","committed key change");
        }


}//AlarmTriggerActivity
