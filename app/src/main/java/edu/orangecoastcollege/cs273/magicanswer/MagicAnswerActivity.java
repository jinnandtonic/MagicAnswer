package edu.orangecoastcollege.cs273.magicanswer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * <code>MagicAnswerActivity</code> uses the built-in accelerator sensor to pseudo-randomly generate
 * an answer to a user's question, much like a Magic 8 ball.
 *
 * When user "shakes" the device (using the accelerator sensor) a pseudo-random answer is displayed
 */
public class MagicAnswerActivity extends AppCompatActivity {

    MagicAnswer magicAnswer;
    private TextView answerTextView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private ShakeDetector mShakeDetector; // reference to ShakeDetector

    /**
     * Creates an instance of <code>MagicAnswerActivity</code> in the view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_answer);

        // TASK 1: SET THE REFERENCES TO THE LAYOUT ELEMENTS
        answerTextView = (TextView) findViewById(R.id.answerTextView);

        // TASK 2: CREATE A NEW MAGIC ANSWER OBJECT
        magicAnswer = new MagicAnswer(this);

        // TASK 3: REGISTER THE SENSOR MANAGER AND SETUP THE SHAKE DETECTION
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                displayMagicAnswer();
            }
        });
    }

    /**
     * Displays a random answer after a shake event has occurred
     */
    public void displayMagicAnswer() {
        String randomAnswer = magicAnswer.getRandomAnswer();
        answerTextView.setText(randomAnswer);
    }

    /**
     * Overrides the onResume() method to register the shake detector
     */
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Overrides the onStop() method to unregister the shake detector
     */
    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(mShakeDetector, mAccelerometer);
    }
}
