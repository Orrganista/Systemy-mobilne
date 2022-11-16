package pl.edu.pb.wi.sensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private static final String SENSOR_TAG = "SensorActivity";
    private boolean countVisible;
    public static final String KEY_EXTRA_SENSOR = "pb.edu.pl.wi.sensor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.acitvity_sensor_menu, menu);

        MenuItem countItem = menu.findItem(R.id.show_count);

        if (countVisible)
            countItem.setTitle(R.string.hide_count);
        else
            countItem.setTitle(R.string.show_count);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_count:
                countVisible = !countVisible;
                this.invalidateOptionsMenu();
                updateCount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateCount() {
        String countString = getString(R.string.sensor_count, sensorList.size());
        if (!countVisible)
            countString = null;
        this.getSupportActionBar().setSubtitle(countString);
    }

    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView sensorIconImageView;
        private TextView sensorNameTextView;
        private LinearLayout layout;
        private Sensor sensor;

        public SensorHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            layout = itemView.findViewById(R.id.sensor_item_layout);
            sensorIconImageView = itemView.findViewById(R.id.sensor_item_icon);
            sensorNameTextView = itemView.findViewById(R.id.sensor_item_name);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            sensorNameTextView.setText(sensor.getName());
            layout.setBackgroundColor(ContextCompat.getColor(this.itemView.getContext(), R.color.black));

            if (sensor.getType() == Sensor.TYPE_LIGHT || sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                layout.setBackgroundColor(ContextCompat.getColor(this.itemView.getContext(), R.color.purple_200));
            }

            if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                layout.setBackgroundColor(ContextCompat.getColor(this.itemView.getContext(), R.color.teal_200));
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent;

            switch (sensor.getType()) {
                case Sensor.TYPE_LIGHT:
                    intent = new Intent(view.getContext(), SensorDetailsActivity.class);
                    intent.putExtra(KEY_EXTRA_SENSOR, "Latarka");
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    intent = new Intent(view.getContext(), SensorDetailsActivity.class);
                    intent.putExtra(KEY_EXTRA_SENSOR, "Acc");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    intent = new Intent(view.getContext(), LocationActivity.class);
                    break;
                default:
                    intent = new Intent(view.getContext(), SensorDetailsActivity.class);
            }

            startActivity(intent);
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> sensorList;

        public SensorAdapter(List<Sensor> sensorList) {
            this.sensorList = sensorList;

            int i = 1;
            for (Sensor s : sensorList) {
                Log.i(SENSOR_TAG, "Sensor " + i + " Producent: " + s.getVendor() + " Max: " + s.getMaximumRange());
                i++;
            }
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.sensor_list_item, parent, false);
            return new SensorHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
            
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }
    }
}