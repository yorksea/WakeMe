package com.amberyork.wakeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MenuFragment  extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Read xml file and return View object.

        // inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)

        View view= inflater.inflate(R.layout.menu_fragment, container, false);


        //set all the nav buttons to go to their activities
        Button button = (Button) view.findViewById(R.id.home_but);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //set listener for alarm list button
        Button button2 = (Button) view.findViewById(R.id.alarm_list_but);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (getActivity().getApplicationContext(), AlarmListActivity.class);
                startActivity(intent);
            }
        });

        Button button3 = (Button) view.findViewById(R.id.trigger_test_but);
        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (getActivity().getApplicationContext(), AlarmTriggerActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


}