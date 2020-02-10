package robutev3.android.demo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import robutev3.android.demo.ControlActivity;
import robutev3.android.demo.R;

/**
 * @author Nearchos
 * Created: 09-Feb-20
 */
public class TankFragment extends Fragment {

    private static final String TAG = "robutev3:android-demo";

    static TankFragment newInstance() { return new TankFragment(); }

    public TankFragment() { /* empty */ }

    private ControlActivity controlActivity = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        controlActivity = (ControlActivity) getActivity();
        assert controlActivity != null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tank, container, false);

        final Button buttonForward = root.findViewById(R.id.buttonForward);
        final Button buttonBackward = root.findViewById(R.id.buttonBackward);
        final Button buttonLeft = root.findViewById(R.id.buttonLeft);
        final Button buttonRight = root.findViewById(R.id.buttonRight);
        final Button buttonStop = root.findViewById(R.id.buttonStop);

        buttonForward.setOnClickListener(v -> controlActivity.tankForward());
        buttonBackward.setOnClickListener(v -> controlActivity.tankBackward());
        buttonLeft.setOnClickListener(v -> controlActivity.tankLeft());
        buttonRight.setOnClickListener(v -> controlActivity.tankRight());
        buttonStop.setOnClickListener(v -> controlActivity.tankStop());

        return root;
    }
}