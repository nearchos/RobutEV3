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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import robutev3.android.demo.ControlActivity;
import robutev3.android.demo.R;
import robutev3.core.Color;
import robutev3.core.PortSensor;
import robutev3.core.Sensor;
import robutev3.core.SensorColor;

/**
 */
public class ColorFragment extends Fragment {

    public static final String TAG = "robutev3:demo:color";

    private ColorSensorViewModel colorSensorViewModel;

    private TextView colorTextView;
    private View colorColorView;
    private ToggleButton colorListenToggleButton;

    static ColorFragment newInstance() {
        return new ColorFragment();
    }

    public ColorFragment() { /* empty */ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorSensorViewModel = new ViewModelProvider(requireActivity()).get(ColorSensorViewModel.class);
    }

    private PortSensor portSensor = null;
    private SensorColor.Mode sensorColorMode = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_color, container, false);
        final RadioButton colorRadioButtonPort1 = root.findViewById(R.id.color_radio_button_port_1);
        final RadioButton colorRadioButtonPort2 = root.findViewById(R.id.color_radio_button_port_2);
        final RadioButton colorRadioButtonPort3 = root.findViewById(R.id.color_radio_button_port_3);
        final RadioButton colorRadioButtonPort4 = root.findViewById(R.id.color_radio_button_port_4);

        colorRadioButtonPort1.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portOrModeSelectionChanged(PortSensor.ONE, sensorColorMode); });
        colorRadioButtonPort2.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portOrModeSelectionChanged(PortSensor.TWO, sensorColorMode); });
        colorRadioButtonPort3.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portOrModeSelectionChanged(PortSensor.THREE, sensorColorMode); });
        colorRadioButtonPort4.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portOrModeSelectionChanged(PortSensor.FOUR, sensorColorMode); });

        if(colorRadioButtonPort1.isChecked()) portSensor = PortSensor.ONE;
        else if(colorRadioButtonPort2.isChecked()) portSensor = PortSensor.TWO;
        else if(colorRadioButtonPort3.isChecked()) portSensor = PortSensor.THREE;
        else if(colorRadioButtonPort4.isChecked()) portSensor = PortSensor.FOUR;

        final RadioButton modeRadioButtonColor = root.findViewById(R.id.color_radio_button_mode_color);
        final RadioButton modeRadioButtonReflectedLight = root.findViewById(R.id.color_radio_button_mode_reflected_light);
        final RadioButton modeRadioButtonAmbientLight = root.findViewById(R.id.color_radio_button_mode_ambient_light);

        modeRadioButtonColor.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portOrModeSelectionChanged(portSensor, SensorColor.Mode.COLOR); });
        modeRadioButtonReflectedLight.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portOrModeSelectionChanged(portSensor, SensorColor.Mode.REFLECT); });
        modeRadioButtonAmbientLight.setOnCheckedChangeListener((buttonView, isChecked) -> { if(isChecked) portOrModeSelectionChanged(portSensor, SensorColor.Mode.AMBIENT); });

        if(modeRadioButtonColor.isChecked()) sensorColorMode = SensorColor.Mode.COLOR;
        else if(modeRadioButtonReflectedLight.isChecked()) sensorColorMode = SensorColor.Mode.REFLECT;
        else if(modeRadioButtonAmbientLight.isChecked()) sensorColorMode = SensorColor.Mode.AMBIENT;

        final Button colorPollButton = root.findViewById(R.id.color_button_poll);
        colorPollButton.setOnClickListener(v -> {
            if(portSensor == null) {
                Toast.makeText(getActivity(), R.string.Select_a_port, Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Polling color/" + sensorColorMode + " sensor at port: " + portSensor);
                controlActivity.poll(portSensor, Sensor.Type.COLOR, sensorColorMode);
            }
        });

        colorListenToggleButton = root.findViewById(R.id.color_toggle_button_listen);
        colorListenToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                controlActivity.listenToColor(portSensor, sensorColorMode);
            } else {
                controlActivity.unlistenToColor(portSensor, sensorColorMode);
            }
        });

        colorTextView = root.findViewById(R.id.color_text_view);
        colorColorView = root.findViewById(R.id.color_color_view);
        colorSensorViewModel.getColorLiveData().observe(getViewLifecycleOwner(), colorValue -> {
            colorTextView.setText(getString(R.string.Color_sensed, colorValue.toString()));
            final Color color = colorValue.getColor();
            switch (color) {
                case BLACK:
                    colorColorView.setBackgroundColor(ContextCompat.getColor(controlActivity, R.color.black));
                    break;
                case BLUE:
                    colorColorView.setBackgroundColor(ContextCompat.getColor(controlActivity, R.color.blue));
                    break;
                case GREEN:
                    colorColorView.setBackgroundColor(ContextCompat.getColor(controlActivity, R.color.green));
                    break;
                case YELLOW:
                    colorColorView.setBackgroundColor(ContextCompat.getColor(controlActivity, R.color.yellow));
                    break;
                case RED:
                    colorColorView.setBackgroundColor(ContextCompat.getColor(controlActivity, R.color.red));
                    break;
                case WHITE:
                    colorColorView.setBackgroundColor(ContextCompat.getColor(controlActivity, R.color.white));
                    break;
                case BROWN:
                    colorColorView.setBackgroundColor(ContextCompat.getColor(controlActivity, R.color.brown));
                    break;
                default:
                    colorColorView.setBackgroundResource(R.drawable.bordered_background);
            }
        });

        return root;
    }

    private void portOrModeSelectionChanged(final PortSensor portSensor, final SensorColor.Mode sensorColorMode) {
        final PortSensor oldPortSensor = this.portSensor;
        this.portSensor = portSensor;
        final SensorColor.Mode oldSensorColorMode = this.sensorColorMode;
        this.sensorColorMode = sensorColorMode;
        Log.d(TAG, "Switching color sensor from port/mode: " + oldPortSensor + "/" + oldSensorColorMode + " to: " + portSensor + "/" + sensorColorMode);

        if(oldPortSensor != null && !colorListenToggleButton.isChecked()) {
            Log.d(TAG, "unlistenToColor at port/mode: " + oldPortSensor + "/" + oldSensorColorMode);
            controlActivity.unlistenToColor(oldPortSensor, oldSensorColorMode);
        }
        if(portSensor != null && colorListenToggleButton.isChecked()){
            Log.d(TAG, "listenToColor at port: " + portSensor);
            controlActivity.listenToColor(portSensor, sensorColorMode);
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