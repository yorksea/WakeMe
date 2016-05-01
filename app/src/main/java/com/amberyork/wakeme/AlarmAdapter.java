package com.amberyork.wakeme;

//for use in adapting an alarm class to the list fragment

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class AlarmAdapter extends ArrayAdapter<Alarm> {

    private final Context context;
    private final List<Alarm> objects;



    public AlarmAdapter(Context context, int resource,
                              ArrayList<Alarm> objects) {
        super(context, resource);
        this.context = context;
        this.objects = objects;

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        // Get the data item for this position
        Alarm alarm = getItem(position);
       // Log.d("AlarmAdapter","List Alarm ID: "+alarm.getAlarm_id()+" in position "+position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_layout, parent, false);
        }
        // Lookup view for data population
        Switch tvAlarmActive = (Switch) convertView.findViewById(R.id.alarmActiveList);
        TextView tvTime = (TextView) convertView.findViewById(R.id.AlarmTime);
        CheckBox tvLights = (CheckBox) convertView.findViewById(R.id.triggerLightsBox);
        CheckBox tvHeat = (CheckBox) convertView.findViewById(R.id.triggerHeatBox);

        //cast all these 0|1 stored as int to boolean then set the checkbox
        boolean activeBool = alarm.getAlarm_active() != 0; //cast to int to boolean
        tvAlarmActive.setChecked(activeBool);

        boolean lightsBool = alarm.getTrigger_lights() != 0;
        tvLights.setChecked(lightsBool);

        boolean heatBool = alarm.getTrigger_heat() != 0;
        tvHeat.setChecked(heatBool);

        try {
            tvTime.setText(alarm.getSetTimePretty());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Return the completed view to render on screen



        Button editBut=(Button) convertView.findViewById(R.id.removeAlarm);
        editBut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //based on position remove that id alarm

                //get the alarm id from the tag of button
                Integer id = (Integer) v.getTag();
                removeAlarm(position);//+1 because alarms are 1 base

                //remove from listview
                objects.remove(position);//to go back to 0 base
                notifyDataSetChanged();
            }
        });


        return convertView;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Alarm getItem(int position) {
        return objects.get(position);
    }

    private void removeAlarm(int pos){



        //remove from db
        Alarm alarm = getItem(pos);
        int id = alarm.getAlarm_id();

        Log.d("REMOVE ","list pos:"+pos+" id "+id);

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.removeAlarmByID(id);

        AlarmServiceManager.deleteAlarm(alarm,context);
    }



}