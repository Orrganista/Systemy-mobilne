package pl.edu.pb.wi.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.UUID;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private Sensor sensorAccelerometer;
    private TextView sensorLightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        String sensorId = (String) getIntent().getSerializableExtra(SensorActivity.KEY_EXTRA_SENSOR);

        sensorLightTextView = findViewById(R.id.sensor_light_label);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        switch (sensorId) {
            case "Latarka":
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                break;
            case "Acc":
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                break;
        }

        if (sensor == null) {
            sensorLightTextView.setText(R.string.missing_sensor);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        String str = getString(R.string.light_sensor_label, currentValue);

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                sensorLightTextView.setText("Latarka: " + str);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                sensorLightTextView.setText("Accelerometer: " + str);
                break;
            default:
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}