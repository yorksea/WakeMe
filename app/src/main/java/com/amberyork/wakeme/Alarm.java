package com.amberyork.wakeme;

import java.text.ParseException;

public class Alarm {


    int alarm_id; //don't need this for creation now, it autoincrements
    String set_time;
    String triggered_at;
    String created_at;
    int smart_period; // not used yet but included here for future
    int trigger_lights;
    int trigger_heat;
    int trigger_sound;
    int alarm_active;

    // constructors
    public Alarm(){
    }

    /**
     *  @description constructor NEW ALARM (new alarm input - no id/"created at" needed because this is done on insert)
     *  also doesn't use triggered_at because that hasn't happened yet
     *  @example      Alarm newAlarm = new Alarm(set_time,boolLights, boolHeat, boolSound);

     */
    public Alarm(String set_time, int trigger_lights, int trigger_heat, int trigger_sound) {
        //this.alarm_id = alarm_id;
        // this.created_at = created_at;
        this.set_time = set_time;
      //  this.triggered_at = triggered_at;  //hasn't happened yet
        this.trigger_lights = trigger_lights;
        this.trigger_heat = trigger_heat;
        this.trigger_sound = trigger_sound;
    }

    //add active or not, this is the constructor used when a new alarm is added
    public Alarm(String set_time, int trigger_lights, int trigger_heat, int trigger_sound,int alarm_active) {
        //this.alarm_id = alarm_id;
        // this.created_at = created_at;
        this.set_time = set_time;
        //  this.triggered_at = triggered_at;  //hasn't happened yet
        this.trigger_lights = trigger_lights;
        this.trigger_heat = trigger_heat;
        this.trigger_sound = trigger_sound;
        this.alarm_active = alarm_active;
    }




    // constructor ALL (if making an object with all variables, like from a return of query)
    public Alarm(int alarm_id, int alarm_active, String created_at, String set_time, String triggered_at, int trigger_lights, int trigger_heat, int trigger_sound) {
        this.alarm_id = alarm_id;
        this.created_at = created_at;
        this.set_time = set_time;
        this.triggered_at = triggered_at;
        this.trigger_lights = trigger_lights;
        this.trigger_heat = trigger_heat;
        this.trigger_sound = trigger_sound;
        this.alarm_active = alarm_active;
    }



    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getSet_time() {
        return set_time;
    }

    public void setSet_time(String set_time) {
        this.set_time = set_time;
    }

    public String getTriggered_at() {
        return triggered_at;
    }

    public void setTriggered_at(String triggered_at) {
        this.triggered_at = triggered_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getSmart_period() {
        return smart_period;
    }

    public void setSmart_period(int smart_period) {
        this.smart_period = smart_period;
    }

    public int getTrigger_lights() {
        return trigger_lights;
    }

    public void setTrigger_lights(int trigger_lights) {
        this.trigger_lights = trigger_lights;
    }

    public int getTrigger_heat() {
        return trigger_heat;
    }

    public void setTrigger_heat(int trigger_heat) {
        this.trigger_heat = trigger_heat;
    }

    public int getTrigger_sound() {
        return trigger_sound;
    }

    public void setTrigger_sound(int trigger_sound) {
        this.trigger_sound = trigger_sound;
    }

    public int getAlarm_active() {
        return alarm_active;
    }

    public void setAlarm_active(int alarm_active) {
        this.alarm_active = alarm_active;
    }


    //extra formatting stuff
   public String getSetTimePretty() throws ParseException {
       //change from ISO to human easily readable
        String set_time_pretty = MainActivity.TimeStampConverter("yyyy-MM-dd HH:mm:ssZ", this.set_time,
                "dd MMM yyyy hh:mm a");
        return set_time_pretty;
   }

    //return time as string, just time not date
    public String getSetTimePrettyTime() throws ParseException {
        //change from ISO to human easily readable
        String set_time_pretty = MainActivity.TimeStampConverter("yyyy-MM-dd HH:mm:ssZ", this.set_time,
                "hh:mm a");
        return set_time_pretty;
    }


}