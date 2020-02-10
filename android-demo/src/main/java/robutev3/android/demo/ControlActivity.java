package robutev3.android.demo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import robutev3.android.BrickService;
import robutev3.android.Device;
import robutev3.android.EV3Service;
import robutev3.android.SensedValue;
import robutev3.android.demo.ui.BrickInfoViewModel;
import robutev3.android.demo.ui.Distance;
import robutev3.android.demo.ui.MotorsViewModel;
import robutev3.android.demo.ui.SectionsPagerAdapter;
import robutev3.android.demo.ui.UltrasonicViewModel;
import robutev3.core.PortSensor;
import robutev3.core.Sensor;
import robutev3.core.SensorUltrasonic;
import robutev3.core.SoundFile;

public class ControlActivity extends AppCompatActivity {

    public static final String TAG = "robutev3:demo:control";

    private ViewPager viewPager;

    private BrickInfoViewModel brickInfoViewModel;
    private MotorsViewModel motorsViewModel;
    private UltrasonicViewModel ultrasonicViewModel;

    private BrickService brickService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        brickInfoViewModel = ViewModelProviders.of(this).get(BrickInfoViewModel.class);
        motorsViewModel = ViewModelProviders.of(this).get(MotorsViewModel.class);
        ultrasonicViewModel = ViewModelProviders.of(this).get(UltrasonicViewModel.class);

        registerToEv3Events();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Get Device parameter
        final Device device = getIntent().getParcelableExtra(Device.EXTRA_DEVICE);
        assert device != null;
        Toast.makeText(this, "Starting EV3 service ...", Toast.LENGTH_SHORT).show();
        // Bind to EV3Service - start on a separate thread as this is a slow operation
        final Intent intent = new Intent(ControlActivity.this, EV3Service.class);
        intent.putExtra(Device.EXTRA_DEVICE, device);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private int powerPortA = 0;
    private int powerPortB = 0;
    private int powerPortC = 0;
    private int powerPortD = 0;

    @Override
    protected void onResume() {
        super.onResume();
        motorsViewModel.getMotorsInfo().observe(this, motorsInfo -> {
            if(motorsInfo.getPowerPortA() != powerPortA) {
                powerPortA = motorsInfo.getPowerPortA();
                if(powerPortA == 0) {
                    brickService.brick().motor().portA().stop().coast().go();
                } else {
                    brickService.brick().motor().portA().turnIndefinitely(powerPortA).go();
                }
            }

            if(motorsInfo.isLockedMotorsBandC()) {
                if(motorsInfo.getPowerPortB() != powerPortB) {
                    powerPortB = powerPortC = motorsInfo.getPowerPortB();
                    if(powerPortB == 0) {
                        brickService.brick().motor().portsBandC().stop().coast().go();
                    } else {
                        brickService.brick().motor().portsBandC().turnIndefinitely(powerPortB);
                    }
                }
            } else {
                if(motorsInfo.getPowerPortB() != powerPortB) {
                    powerPortB = motorsInfo.getPowerPortB();
                    if(powerPortB == 0) {
                        brickService.brick().motor().portB().stop().coast().go();
                    } else {
                        brickService.brick().motor().portB().turnIndefinitely(powerPortB).go();
                    }
                }

                if(motorsInfo.getPowerPortC() != powerPortC) {
                    powerPortC = motorsInfo.getPowerPortC();
                    if(powerPortC == 0) {
                        brickService.brick().motor().portC().stop().coast().go();
                    } else {
                        brickService.brick().motor().portC().turnIndefinitely(powerPortC).go();
                    }
                }
            }

            if(motorsInfo.getPowerPortD() != powerPortD) {
                powerPortD = motorsInfo.getPowerPortD();
                if(powerPortD == 0) {
                    brickService.brick().motor().portD().stop().coast().go();
                } else {
                    brickService.brick().motor().portD().turnIndefinitely(powerPortD).go();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Stop motors is running
        if(brickService != null) {
            Log.d(TAG, "Stopping all motors");
            brickService.brick().motor().stopAll().coast().go();
        }
        // Unbind from EV3Service
        unbindService(connection);
        brickInfoViewModel.setDisconnected();
    }

    public void beep() {
        brickService.brick().sound().beep();
    }

    public void play(final int frequency, final int duration, final int volume) {
        brickService.brick().sound().beep(frequency, duration, volume);
    }

    public void play(final SoundFile soundFile, final int volume) {
        brickService.brick().sound().play(soundFile, volume);
    }

    public void tankForward() {
        brickService.brick().motor().portsBandC().turnIndefinitely(20).go();
    }

    public void tankBackward() {
        brickService.brick().motor().portsBandC().turnIndefinitely(-20).go();
    }

    public void tankStop() {
        brickService.brick().motor().stopAll();
    }

    public void tankLeft() {
        brickService.brick().motor().stopAll();
        brickService.brick().motor().portB().turnDegrees(360, 20).brake();
        brickService.brick().motor().portC().turnDegrees(360, -20).brake();
    }

    public void tankRight() {
        brickService.brick().motor().stopAll();
        brickService.brick().motor().portB().turnDegrees(360, -20).brake();
        brickService.brick().motor().portC().turnDegrees(360, 20).brake();
    }

    public void poll(final PortSensor portSensor, final Sensor.Type sensorType, final Sensor.Mode sensorMode) {
        switch (sensorType) {
            case TOUCH:
                throw new RuntimeException("Operation not implemented"); // todo
            case COLOR:
                throw new RuntimeException("Operation not implemented"); // todo
            case ULTRASONIC:
                {
                    final SensorUltrasonic.DistanceSensible distanceSensible = brickService.brick().sensor().port(portSensor).ultrasonic();
                    if(sensorMode == SensorUltrasonic.Mode.DISTANCE_CENTIMETERS) {
                        final int distanceInCm = distanceSensible.centimeters();
                        Log.d(TAG, "ultrasonic - distanceInCm: " + distanceInCm);
                        ultrasonicViewModel.setDistance(distanceInCm, Distance.DistanceUnit.cm);
                    } else if(sensorMode == SensorUltrasonic.Mode.DISTANCE_INCHES) {
                        final int distanceInInches = distanceSensible.inches();
                        Log.d(TAG, "ultrasonic - distanceInInches: " + distanceInInches);
                        ultrasonicViewModel.setDistance(distanceInInches, Distance.DistanceUnit.in);
                    } else if(sensorMode == SensorUltrasonic.Mode.LISTEN_ONLY) {
                        // nothing for now
                        Log.w(TAG, "Unsupported sensor mode: " + sensorMode);
                    } else {
                        throw new RuntimeException("Unknown sensor mode: " + sensorMode);
                    }
                }
                break;
            case INFRA_RED:
                throw new RuntimeException("Operation not implemented"); // todo
            default:
                throw new RuntimeException("Unknown sensor type: " + sensorType);
        }
    }

    public void listenToUltrasonic(final PortSensor portSensor, final UltrasonicViewModel ultrasonicViewModel) {
        Snackbar.make(viewPager, getString(R.string.Listening_to_ultrasonic_at_port, portSensor.getName()), Snackbar.LENGTH_SHORT).show();
        brickService.register(portSensor, Sensor.Type.ULTRASONIC, SensorUltrasonic.Mode.DISTANCE_CENTIMETERS);
        // todo register ultrasonic view model for broadcasts
    }

    public void unlistenToUltrasonic(final PortSensor portSensor, final UltrasonicViewModel ultrasonicViewModel) {
        Snackbar.make(viewPager, R.string.Stopped_listening_to_ultrasonic, Snackbar.LENGTH_SHORT).show();
        brickService.unregister(portSensor, Sensor.Type.ULTRASONIC, SensorUltrasonic.Mode.DISTANCE_CENTIMETERS);
        // todo unregister ultrasonic view model for broadcasts
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "EV3 Service connected");
            // We've bound to EV3Service, cast the IBinder and get EV3Service instance
            final EV3Service.EV3Binder binder = (EV3Service.EV3Binder) service;
            brickService = binder.getService();
            brickInfoViewModel.setConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "EV3 Service disconnected");
            brickInfoViewModel.setDisconnected();
        }
    };

    private void registerToEv3Events() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EV3Service.EV3_ACTION_STARTED);
        intentFilter.addAction(EV3Service.EV3_ACTION_STOPPED);
        intentFilter.addAction(EV3Service.EV3_SENSED_VALUES);
        intentFilter.addAction(EV3Service.EV3_ERROR_DISCONNECTED);
        registerReceiver(ev3BroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver ev3BroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            assert action != null;
            switch (action) {
                case EV3Service.EV3_ACTION_STARTED:
                    brickInfoViewModel.setStarted();
                    break;
                case EV3Service.EV3_ACTION_STOPPED:
                    Toast.makeText(context, R.string.Connection_broken, Toast.LENGTH_SHORT).show();
                    brickInfoViewModel.setStopped();
                    break;
                case EV3Service.EV3_SENSED_VALUES:
                    final ArrayList<SensedValue> sensedValues = intent.getParcelableArrayListExtra(SensedValue.EXTRA_SENSED_VALUES);
//                    Toast.makeText(context, "sensed values: " + sensedValue, Toast.LENGTH_SHORT).show();
Log.d(TAG, "sensed values: " + sensedValues);//todo
                    if(sensedValues != null) {
                        for(final SensedValue sensedValue : sensedValues) {
                            final Sensor.Type sensorType = sensedValue.getSensorType();
                            final Sensor.Mode sensorMode = sensedValue.getSensorMode();
                            final Bundle valuesBundle = sensedValue.getValues();
                            if(SensorUltrasonic.Mode.DISTANCE_CENTIMETERS.name().equalsIgnoreCase(sensorMode.name())) {
                                final int distanceInCentimeters = valuesBundle.getInt(SensorUltrasonic.VALUE_CENTIMETERS);
                                ultrasonicViewModel.setDistance(distanceInCentimeters, Distance.DistanceUnit.cm);
                            } else if(SensorUltrasonic.Mode.DISTANCE_INCHES.name().equalsIgnoreCase(sensorMode.name())) {
                                final int distanceInInches = valuesBundle.getInt(SensorUltrasonic.VALUE_INCHES);
                                ultrasonicViewModel.setDistance(distanceInInches, Distance.DistanceUnit.in);
                            }
                        }
                    }
                    break;
                case EV3Service.EV3_ERROR_DISCONNECTED:
                    Toast.makeText(context, R.string.Could_not_connect, Toast.LENGTH_SHORT).show();
                    brickInfoViewModel.setStopped();
                    finish();
                    break;
                default:
                    // unknown action
            }
        }
    };
}