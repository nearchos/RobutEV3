package robutev3.android.demo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import robutev3.android.demo.R;
import robutev3.core.PortMotor;

/**
 */
public class MotorsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "robutev3:android-demo";

    private MotorsViewModel motorsViewModel;

    private Switch lockedPortsBandC;
    private SeekBar portASeekBar;
    private SeekBar portBSeekBar;
    private SeekBar portCSeekBar;
    private SeekBar portDSeekBar;

    static MotorsFragment newInstance() {
        return new MotorsFragment();
    }

    public MotorsFragment() { /* empty */ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        motorsViewModel = ViewModelProviders.of(requireActivity()).get(MotorsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_motors, container, false);
        lockedPortsBandC = root.findViewById(R.id.lockPortsBandC);
        lockedPortsBandC.setOnCheckedChangeListener((buttonView, isChecked) -> motorsViewModel.lockMotorsBandC(isChecked));
        portASeekBar = root.findViewById(R.id.portASeekBar);
        portASeekBar.setOnSeekBarChangeListener(this);
        portBSeekBar = root.findViewById(R.id.portBSeekBar);
        portBSeekBar.setOnSeekBarChangeListener(this);
        portCSeekBar = root.findViewById(R.id.portCSeekBar);
        portCSeekBar.setOnSeekBarChangeListener(this);
        portDSeekBar = root.findViewById(R.id.portDSeekBar);
        portDSeekBar.setOnSeekBarChangeListener(this);

        motorsViewModel.getMotorsInfo().observe(this, motorsInfo -> {
            Log.d(TAG, "motors info changed: " + motorsInfo);
            portASeekBar.setProgress(motorsInfo.getPowerPortA() / 10 + 11);
            portBSeekBar.setProgress(motorsInfo.getPowerPortB() / 10  + 11);
            portBSeekBar.setEnabled(!motorsInfo.isLockedMotorsBandC());
            portCSeekBar.setProgress(motorsInfo.getPowerPortC() / 10  + 11);
            portDSeekBar.setProgress(motorsInfo.getPowerPortD() / 10  + 11);
        });

        return root;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { /* empty */ }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { /* empty */ }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        final int power = seekBar.getProgress() * 10 - 110;
        Log.d(TAG, "power: " + power);
        if(seekBar == portASeekBar) {
            motorsViewModel.setPower(PortMotor.A, power);
        } else if(seekBar == portBSeekBar) {
            motorsViewModel.setPower(PortMotor.B, power);
        } else if(seekBar == portCSeekBar) {
            motorsViewModel.setPower(PortMotor.C, power);
        } else if(seekBar == portDSeekBar) {
            motorsViewModel.setPower(PortMotor.D, power);
        }
    }
}