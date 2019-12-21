package robutev3.android.demo.ui;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import robutev3.android.demo.ControlActivity;
import robutev3.android.demo.R;
import robutev3.core.Notes;

/**
 */
public class SoundFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private TextView frequencyTextView;
    private TextView durationTextView;
    private TextView volumeTextView;
    private SeekBar frequencySeekBar;
    private SeekBar durationSeekBar;
    private SeekBar volumeSeekBar;

    private Button beepButton;
    private Button playButton;
    private Button buttonDo;
    private Button buttonRe;
    private Button buttonMi;
    private Button buttonFa;
    private Button buttonSol;
    private Button buttonLa;
    private Button buttonTi;
    private Button buttonDo_P;

    static SoundFragment newInstance() {
        return new SoundFragment();
    }

    public SoundFragment() { /* empty */ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_sound, container, false);
        frequencyTextView = root.findViewById(R.id.frequencyTextView);
        durationTextView = root.findViewById(R.id.durationTextView);
        volumeTextView = root.findViewById(R.id.volumeTextView);
        frequencySeekBar = root.findViewById(R.id.frequencySeekBar);
        durationSeekBar = root.findViewById(R.id.durationSeekBar);
        volumeSeekBar = root.findViewById(R.id.volumeSeekBar);

        frequencySeekBar.setOnSeekBarChangeListener(this);
        durationSeekBar.setOnSeekBarChangeListener(this);
        volumeSeekBar.setOnSeekBarChangeListener(this);

        beepButton = root.findViewById(R.id.beepButton);
        playButton = root.findViewById(R.id.playButton);

        buttonDo = root.findViewById(R.id.buttonDo);
        buttonRe = root.findViewById(R.id.buttonRe);
        buttonMi = root.findViewById(R.id.buttonMi);
        buttonFa = root.findViewById(R.id.buttonFa);
        buttonSol = root.findViewById(R.id.buttonSol);
        buttonLa = root.findViewById(R.id.buttonLa);
        buttonTi = root.findViewById(R.id.buttonTi);
        buttonDo_P = root.findViewById(R.id.buttonDo_P);

        return root;
    }

    private ControlActivity controlActivity = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        controlActivity = (ControlActivity) getActivity();
        assert controlActivity != null;
        beepButton.setOnClickListener(v -> controlActivity.beep());
        playButton.setOnClickListener(v -> controlActivity.play(getFrequency(), getDuration(), getVolume()));

        buttonDo.setOnClickListener(this);
        buttonRe.setOnClickListener(this);
        buttonMi.setOnClickListener(this);
        buttonFa.setOnClickListener(this);
        buttonSol.setOnClickListener(this);
        buttonLa.setOnClickListener(this);
        buttonTi.setOnClickListener(this);
    }

    private int getFrequency() {
        return frequencySeekBar.getProgress() * 10 - 90;
    }

    private int getDuration() {
        return durationSeekBar.getProgress() * 10;
    }

    private int getVolume() {
        return volumeSeekBar.getProgress();
    }

    @Override
    public void onClick(View view) {
        final Notes note;

        if(view == buttonDo) {
            note = Notes.DO;
        } else if(view == buttonRe) {
            note = Notes.RE;
        } else if(view == buttonMi) {
            note = Notes.MI;
        } else if(view == buttonFa) {
            note = Notes.FA;
        } else if(view == buttonSol) {
            note = Notes.SOL;
        } else if(view == buttonLa) {
            note = Notes.LA;
        } else if(view == buttonTi) {
            note = Notes.TI;
        } else if(view == buttonDo_P) {
            note = Notes.DO_P;
        } else {
            throw new IllegalStateException("Unknown note");
        }

        controlActivity.play(note.getFrequency(), getDuration(), getVolume());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar == frequencySeekBar) {
            final int frequency = getFrequency();
            if(frequency < 1000) {
                frequencyTextView.setText(String.format(Locale.US, "%d Hz", frequency));
            } else {
                frequencyTextView.setText(String.format(Locale.US, "%.2f KHz", frequency / 1000d));
            }
        }

        if(seekBar == durationSeekBar) {
            final int duration = getDuration();
            if(duration < 1000) {
                durationTextView.setText(String.format(Locale.US, "%d ms", duration));
            } else {
                durationTextView.setText(String.format(Locale.US, "%.2f s", duration / 1000d));
            }
        }

        if(seekBar == volumeSeekBar) {
            final int volume = getVolume();
            volumeTextView.setText(String.format(Locale.US, "%d %%", volume));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { /* empty */ }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { /* empty */ }
}