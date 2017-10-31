package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 *
 * @author Ryan Millett
 * @version 1.0
 */
public class ShakeDetector implements SensorEventListener {

    private static final long ELAPSED_TIME = 1000L;
    private static final float THRESHOLD = 20;

    private long mPreviousEvent;

    private OnShakeListener mListener;

    /**
     * Overloaded constructor for ShakeDetector class
     * @param listener
     */
    public ShakeDetector(OnShakeListener listener) {
        mListener = listener;
    }

    /**
     *
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // ensure that event came from Accelerometer, no other sensors
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // get forces in x, y, z direction
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Neutralize the effect of gravity (subtract from each value)
            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            // compute the net force
            float netForce = (float) Math.sqrt(
                    Math.pow(gForceX, 2) + Math.pow(gForceY, 2) + Math.pow(gForceZ, 2));

            // if (net force > threshold)
            if (netForce >= THRESHOLD) {
                // if (current time > previous + elapsed time)
                long currentTime = System.currentTimeMillis();
                if (currentTime > mPreviousEvent + ELAPSED_TIME) {
                    // reset previous event to current time
                    mPreviousEvent = currentTime;

                    // register shake event
                    mListener.onShake();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // NOT USED
    }

    /**
     * An interface for others to implement whenever a true shake occurs
     */
    public interface OnShakeListener {
        void onShake();
    }
}
