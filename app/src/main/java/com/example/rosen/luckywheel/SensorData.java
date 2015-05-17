package com.example.rosen.luckywheel;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.util.Log;

/**
 * Created by Rosen on 27.1.2015 г..
 */
public class SensorData implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private TriggerEventListener triggerEventListener;
    private WheelStatus wheelStatus;
    private boolean isUsed = false;
    //TODO: set permison for accelerometer

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    private final int VECTOR_BONUS_CONSTANT = 5;//*three

    public SensorData(Context context, WheelStatus wheelStatus){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.wheelStatus = wheelStatus;
    }
    public void isUsed(boolean isUsed){
        this.isUsed = isUsed;
    }
    public void registerListeners(){
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegisterListeners(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double length = Math.sqrt((x*x) + (y*y) + (z*z));

            if (length > 12){
                Log.i("vectorLength", length + "");
                Log.i("sendSpeed", (int) Math.abs(length) * VECTOR_BONUS_CONSTANT + "");
                if(isUsed){
                    wheelStatus.wheelSpeed((int)Math.abs(length) * VECTOR_BONUS_CONSTANT);//moje i bez math ABS da se probva
                }
                wheelStatus.wheelWasSpined(true);
            }

//            long curTime = System.currentTimeMillis();
//
//            if ((curTime - lastUpdate) > 100) {
//                long diffTime = (curTime - lastUpdate);
//                lastUpdate = curTime;
//
//                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
//
//                if (speed > SHAKE_THRESHOLD) {
//
//                }
//
//                Log.i("asd", ""+ speed);
//                last_x = x;
//                last_y = y;
//                last_z = z;
//            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {//TODO: check what does that do

    }
}
