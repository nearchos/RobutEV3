package robutev3.android.demo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import robutev3.android.demo.ControlActivity;
import robutev3.android.demo.R;
import robutev3.core.PortSensor;
import robutev3.core.Sensor;
import robutev3.core.SensorUltrasonic;

/**
 */
public class UltrasonicFragment extends Fragment {

    public static final String TAG = "robutev3:demo:main";

    private UltrasonicViewModel ultrasonicViewModel;

    private RadioButton ultrasonicRadioButtonPort1;
    private RadioButton ultrasonicRadioButtonPort2;
    private RadioButton ultrasonicRadioButtonPort3;
    private RadioButton ultrasonicRadioButtonPort4;
    private TextView ultrasonicTextView;
    private Button ultrasonicPollButton;
    private ToggleButton ultrasonicListenToggleButton;

    static UltrasonicFragment newInstance() {
        return new UltrasonicFragment();
    }

    public UltrasonicFragment() { /* empty */ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ultrasonicViewModel = ViewModelProviders.of(requireActivity()).get(UltrasonicViewModel.class);
    }

    private PortSensor portSensor = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_ultrasonic, container, false);
        ultrasonicRadioButtonPort1 = root.findViewById(R.id.ultrasonic_radio_button_port_1);
        ultrasonicRadioButtonPort2 = root.findViewById(R.id.ultrasonic_radio_button_port_2);
        ultrasonicRadioButtonPort3 = root.findViewById(R.id.ultrasonic_radio_button_port_3);
        ultrasonicRadioButtonPort4 = root.findViewById(R.id.ultrasonic_radio_button_port_4);
        ultrasonicTextView = root.findViewById(R.id.ultrasonic_text_view);
        ultrasonicPollButton = root.findViewById(R.id.ultrasonic_button_poll);
        ultrasonicListenToggleButton = root.findViewById(R.id.ultrasonic_toggle_button_listen);

        ultrasonicRadioButtonPort1.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portSelectionChanged(PortSensor.ONE); });
        ultrasonicRadioButtonPort2.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portSelectionChanged(PortSensor.TWO); });
        ultrasonicRadioButtonPort3.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portSelectionChanged(PortSensor.THREE); });
        ultrasonicRadioButtonPort4.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portSelectionChanged(PortSensor.FOUR); });

        if(ultrasonicRadioButtonPort1.isChecked()) portSensor = PortSensor.ONE;
        else if(ultrasonicRadioButtonPort2.isChecked()) portSensor = PortSensor.TWO;
        else if(ultrasonicRadioButtonPort3.isChecked()) portSensor = PortSensor.THREE;
        else if(ultrasonicRadioButtonPort4.isChecked()) portSensor = PortSensor.FOUR;

        ultrasonicPollButton.setOnClickListener(v -> {
            if(portSensor == null) {
                Toast.makeText(getActivity(), R.string.Select_a_port, Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Polling ultrasonic sensor at port: " + portSensor);
                controlActivity.poll(portSensor, Sensor.Type.ULTRASONIC, SensorUltrasonic.Mode.DISTANCE_CENTIMETERS);
            }
        });

        ultrasonicListenToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                controlActivity.listenToUltrasonic(portSensor, ultrasonicViewModel);
            } else {
                controlActivity.unlistenToUltrasonic(portSensor, ultrasonicViewModel);
            }
        });

        ultrasonicViewModel.getUltrasonicDistance().observe(getViewLifecycleOwner(), ultrasonicDistance -> {
            ultrasonicTextView.setText(getString(R.string.Ultrasonic_distance, ultrasonicDistance.getDistance(), ultrasonicDistance.getDistanceUnit().name()));
        });

        return root;
    }

    private void portSelectionChanged(final PortSensor portSensor) {
        final PortSensor oldPortSensor = this.portSensor;
        this.portSensor = portSensor;
        Log.d(TAG, "Switching ultrasonic sensor from port: " + oldPortSensor + " to: " + portSensor);

        if(oldPortSensor != null && !ultrasonicListenToggleButton.isChecked()) {
            Log.d(TAG, "unlistenToUltrasonic at port: " + oldPortSensor);
            controlActivity.unlistenToUltrasonic(oldPortSensor, ultrasonicViewModel);
        }
        if(portSensor != null && ultrasonicListenToggleButton.isChecked()){
            Log.d(TAG, "listenToUltrasonic at port: " + portSensor);
            controlActivity.listenToUltrasonic(portSensor, ultrasonicViewModel);
        }
    }

    private ControlActivity controlActivity = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        controlActivity = (ControlActivity) getActivity();
        assert controlActivity != null;
    }
}