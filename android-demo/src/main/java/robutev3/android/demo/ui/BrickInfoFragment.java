package robutev3.android.demo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import robutev3.android.demo.R;

/**
 */
public class BrickInfoFragment extends Fragment {

    private BrickInfoViewModel brickInfoViewModel;

    private TextView connectingTextView;
    private TextView startingTextView;
    private ProgressBar progressBar;

    static BrickInfoFragment newInstance() {
        return new BrickInfoFragment();
    }

    public BrickInfoFragment() { /* empty */ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brickInfoViewModel = ViewModelProviders.of(requireActivity()).get(BrickInfoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_brick_info, container, false);
        connectingTextView = root.findViewById(R.id.brick_info_connected);
        startingTextView = root.findViewById(R.id.brick_info_started);
        progressBar = root.findViewById(R.id.brick_info_progress_bar);

        brickInfoViewModel.getBrickInfo().observe(this, brickInfo -> {
            connectingTextView.setText(brickInfo.isConnected() ? R.string.Connected : R.string.Connecting);
            startingTextView.setVisibility(brickInfo.isConnected() ? View.VISIBLE : View.GONE);
            startingTextView.setText(brickInfo.isStarted() ? R.string.Started : R.string.Starting);
            progressBar.setVisibility(brickInfo.isConnected() && brickInfo.isStarted() ? View.GONE : View.VISIBLE);
        });

        return root;
    }
}