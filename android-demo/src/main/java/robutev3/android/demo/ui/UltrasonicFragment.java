package robutev3.android.demo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

    private UltrasonicViewModel ultrasonicViewModel;

    private Spinner ultrasonicPortSelectorSpinner;
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
        ultrasonicPortSelectorSpinner = root.findViewById(R.id.ultrasonic_port_selector_spinner);
        ultrasonicTextView = root.findViewById(R.id.ultrasonic_text_view);
        ultrasonicPollButton = root.findViewById(R.id.ultrasonic_button_poll);
        ultrasonicListenToggleButton = root.findViewById(R.id.ultrasonic_toggle_button_listen);

        final String [] portOptions = getResources().getStringArray(R.array.SensorPortsArray);
        ultrasonicPortSelectorSpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, portOptions));
        ultrasonicPortSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        portSensor = PortSensor.ONE;
                        break;
                    case 2:
                        portSensor = PortSensor.TWO;
                        break;
                    case 3:
                        portSensor = PortSensor.THREE;
                        break;
                    case 4:
                        portSensor = PortSensor.FOUR;
                        break;
                    case 0:
                    default:
                        portSensor = null;
                        Toast.makeText(requireContext(), R.string.Select_a_port, Toast.LENGTH_SHORT).show();
                }
                if(portSensor == null || !ultrasonicListenToggleButton.isChecked()) {
                    controlActivity.unlistenToUltrasonic(portSensor, ultrasonicViewModel);
                } else {
                    controlActivity.listenToUltrasonic(portSensor, ultrasonicViewModel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* not used */ }
        });

        ultrasonicPollButton.setOnClickListener(v -> {
            if(portSensor == null) {
                Toast.makeText(getActivity(), R.string.Select_a_port, Toast.LENGTH_SHORT).show();
            } else {
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

        ultrasonicViewModel.getUltrasonicDistance().observe(this, ultrasonicDistance -> {
            ultrasonicTextView.setText(getString(R.string.Ultrasonic_distance, ultrasonicDistance.getDistance(), ultrasonicDistance.getDistanceUnit().name()));
        });

        return root;
    }

    private ControlActivity controlActivity = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        controlActivity = (ControlActivity) getActivity();
        assert controlActivity != null;
    }
}