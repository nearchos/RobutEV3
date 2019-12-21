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

import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import robutev3.android.Device;
import robutev3.android.EV3Service;
import robutev3.android.demo.ui.BrickInfoViewModel;
import robutev3.android.demo.ui.MotorsViewModel;
import robutev3.android.demo.ui.SectionsPagerAdapter;
import robutev3.core.IBrick;
import robutev3.core.PortMotor;

public class ControlActivity extends AppCompatActivity {

    public static final String TAG = "robutev3:demo";

    private BrickInfoViewModel brickInfoViewModel;
    private MotorsViewModel motorsViewModel;

    private IBrick brick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        brickInfoViewModel = ViewModelProviders.of(this).get(BrickInfoViewModel.class);
        motorsViewModel = ViewModelProviders.of(this).get(MotorsViewModel.class);

        registerToEv3Events();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Get Device parameter
        final Device device = (Device) getIntent().getSerializableExtra(Device.EXTRA_DEVICE);
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
                    brick.motor().portA().stop().coast().go();
                } else {
                    brick.motor().portA().turnIndefinitely(powerPortA).go();
                }
            }

            if(motorsInfo.isLockedMotorsBandC()) {
                if(motorsInfo.getPowerPortB() != powerPortB) {
                    powerPortB = powerPortC = motorsInfo.getPowerPortB();
                    if(powerPortB == 0) {
                        brick.motor().portsBandC().stop().coast().go();
                    } else {
                        brick.motor().portsBandC().turnIndefinitely(powerPortB);
                    }
                }
            } else {
                if(motorsInfo.getPowerPortB() != powerPortB) {
                    powerPortB = motorsInfo.getPowerPortB();
                    if(powerPortB == 0) {
                        brick.motor().portB().stop().coast().go();
                    } else {
                        brick.motor().portB().turnIndefinitely(powerPortB).go();
                    }
                }

                if(motorsInfo.getPowerPortC() != powerPortC) {
                    powerPortC = motorsInfo.getPowerPortC();
                    if(powerPortC == 0) {
                        brick.motor().portC().stop().coast().go();
                    } else {
                        brick.motor().portC().turnIndefinitely(powerPortC).go();
                    }
                }
            }

            if(motorsInfo.getPowerPortD() != powerPortD) {
                powerPortD = motorsInfo.getPowerPortD();
                if(powerPortD == 0) {
                    brick.motor().portD().stop().coast().go();
                } else {
                    brick.motor().portD().turnIndefinitely(powerPortD).go();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Stop motors is running
        if(brick != null) {
            Log.d(TAG, "Stopping all motors");
            brick.motor().stopAll().coast().go();
        }
        // Unbind from EV3Service
        unbindService(connection);
        brickInfoViewModel.setDisconnected();
    }

    public void beep() {
        brick.sound().beep();
    }

    public void play(final int frequency, final int duration, final int volume) {
        brick.sound().beep(frequency, duration, volume);
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "EV3 Service connected");
            // We've bound to EV3Service, cast the IBinder and get EV3Service instance
            final EV3Service.EV3Binder binder = (EV3Service.EV3Binder) service;
            brick = binder.getService();
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
                    brickInfoViewModel.setStopped();
                    break;
                default:
                    // unknown action
            }
        }
    };
}