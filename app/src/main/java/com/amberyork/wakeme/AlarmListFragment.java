package com.amberyork.wakeme;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;


public class AlarmListFragment extends ListFragment implements OnItemClickListener {
    private static final String TAG = "AlarmListFragment LOG";

    private AlarmAdapter alarmAdapter;
    private ListView alarmListView;

    private ArrayList<Alarm> alarms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_list, container, false);
        Log.d(TAG, "view inflated");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //get data from db through dbhelper
        DBHelper dbHelper = new DBHelper(getActivity());

        alarms = dbHelper.getAllAlarms();

        //now fill list with custom adapter
        Log.d(TAG, "alarms.size(): "+ alarms.size());


        alarmAdapter = new AlarmAdapter(getActivity(), R.layout.alarm_layout,
                alarms);
        setListAdapter(alarmAdapter);

       // makeAllAlarmServices();

        getListView().setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Log.d(TAG,"Item clicked: " + position);
      //  Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_LONG).show();
    }

}