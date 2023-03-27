package scm.cbsarkar2.floatball;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

//the following code is copied and modified from Tilting Test - Topic 7

public class GameLevel extends AppCompatActivity {
    public static MainActivity mainActivity;
    private TiltingView tv;
    private Display display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Removal of title bar copied from https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        tv = new TiltingView(this);
        this.setContentView(tv);

        display = ((WindowManager)
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensor = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null) {
            Log.d("Tilting", "sensor of interest not detected.");
        }


    }
    private SensorManager mgr;
    private Sensor sensor;
    private final SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            Log.d("Tilting", event.values[0] + ", " + event.values[1] + ", "
                    + event.values[2]);

            float ax = event.values[0] * 0.2f;
            float ay = event.values[1] * 0.2f;


            switch (display.getRotation()) {
                case Surface.ROTATION_0:
                    tv.update(-ax, ay);
                    break;
                case Surface.ROTATION_90:
                    tv.update(ay,ax);
                    break;
                case Surface.ROTATION_180:
                    tv.update(ax,-ay);
                    break;
                case Surface.ROTATION_270:
                    tv.update(-ay,-ax);
                    break;
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        if (sensor != null)
            mgr.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (sensor != null) mgr.unregisterListener(listener, sensor);
        super.onPause();
    }

    public void updateScore(int score) {
        mainActivity.updateScore(score);
    }

}