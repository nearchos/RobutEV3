package robutev3.android;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import robutev3.core.Brick;
import robutev3.core.PortSensor;
import robutev3.core.Sensor;
import robutev3.core.SensorUltrasonic;

public class EV3Service extends Service implements BrickService {

    public static final String TAG = "robutev3:api";

    public static final String EV3_ACTION_STARTED = "EV3_ACTION_STARTED";
    public static final String EV3_ACTION_STOPPED = "EV3_ACTION_STOPPED";

    public static final String EV3_SENSED_VALUES = "EV3_SENSED_VALUES";
    public static final String EV3_ERROR_DISCONNECTED = "EV3_ERROR_DISCONNECTED";

    public EV3Service() { /* empty */ }

    public static final int NUMBER_OF_THREADS = 1;
    private final ExecutorService workExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final long POLLING_INTERVAL_MILLISECONDS = 1000;
    private final ScheduledExecutorService scheduledWorkExecutor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public IBinder onBind(Intent intent) {
        final Device device = intent.getParcelableExtra(Device.EXTRA_DEVICE);
        if(device == null) throw new IllegalArgumentException("Intent must include a device attached to key: " + Device.EXTRA_DEVICE);
        return new EV3Binder(device);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(brick == null) {
            Log.w(TAG, "onUnbind(): Cannot stop a null brick");
        } else {
            try {
                Log.d(TAG, "Stopping the brick ...");
                brick.stop();
                Log.d(TAG, "Shutting down scheduled tasks executor...");
                scheduledWorkExecutor.awaitTermination(POLLING_INTERVAL_MILLISECONDS, TimeUnit.MILLISECONDS);
            } catch (IOException ioe) {
                Log.e(TAG, "I/O Error while stopping the brick: " + ioe.getMessage());
            } catch (InterruptedException ie) {
                Log.e(TAG, "Error while shutting down scheduled tasks: " + ie.getMessage());
            }
        }
        return super.onUnbind(intent);
    }

    private Brick brick = null;

    public class EV3Binder extends Binder {

        EV3Binder(@NonNull final Device device) {
            Log.d(TAG, "Device: " + device);
            try {
                switch (device.getType()) {
                    case TYPE_BLUETOOTH: {
                        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        final BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(device.getAddress());
                        brick = Brick.create(new BluetoothCommunicationInterface(bluetoothDevice));
                        break;
                    }
                    case TYPE_USB: {
                        final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                        assert usbManager != null;
                        final Map<String,UsbDevice> usbDevices = usbManager.getDeviceList();
                        final UsbDevice usbDevice = usbDevices.get(device.getName());
                        assert usbDevice != null;
                        brick = Brick.create(new USBCommunicationInterface(usbManager, usbDevice));
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Device cannot be of type: " + device.getType());
                }
                Log.d(TAG, "Brick created: " + brick);

                Log.d(TAG, "Brick adding listeners for warning and error messages ...");
                brick.addLogListener(new Brick.LogListener() {
                    @Override public void warning(String message) { Log.w(TAG, message); }
                    @Override public void error(String message) { Log.e(TAG, message); }
                });

                Log.d(TAG, "Brick starting ...");
                start();

            } catch (IOException ioe) {
                Log.e(TAG, ioe.getMessage(), ioe);
                throw new RuntimeException(ioe);
            }
        }

        public EV3Service getService() {
            return EV3Service.this;
        }
    }

    private void start() {
        workExecutor.execute(() -> {
            Log.d(TAG, "Starting brick ...");
            try {
                brick.start();
                Log.d(TAG, "Started brick!");
                // use a broadcast to notify that the brick is started
                final Intent intent = new Intent(EV3_ACTION_STARTED);
                sendBroadcast(intent);
            } catch (IOException ioe) {
                Log.e(TAG, ioe.getMessage(), ioe);
                // use a broadcast to notify that the brick is not available
                final Intent intent = new Intent(EV3_ERROR_DISCONNECTED);
                sendBroadcast(intent);
            }
        });

        // create periodic handler
        scheduledWorkExecutor.scheduleAtFixedRate(pollingTaskRunnable, POLLING_INTERVAL_MILLISECONDS, POLLING_INTERVAL_MILLISECONDS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scheduledWorkExecutor.shutdown();
    }

    private Set<PollingTask> synchronizedPollingTaskSet = Collections.synchronizedSet(new HashSet<>());

    private class PollingTask {
        private final PortSensor portSensor;
        private final Sensor.Type sensorType;
        private final Sensor.Mode sensorMode;

        public PollingTask(final PortSensor portSensor, final Sensor.Type sensorType, final Sensor.Mode sensorMode) {
            this.portSensor = portSensor;
            this.sensorType = sensorType;
            this.sensorMode = sensorMode;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;
            PollingTask that = (PollingTask) other;
            return this.portSensor == that.portSensor &&
                    this.sensorType == that.sensorType &&
                    Objects.equals(this.sensorMode, that.sensorMode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portSensor, sensorType, sensorMode);
        }

        @NonNull
        @Override
        public String toString() {
            return "PollingTask{ portSensor=" + portSensor + ", sensorType=" + sensorType + ", sensorMode=" + sensorMode + '}';
        }
    }

    @Override
    public Brick brick() {
Log.d(TAG, "synchronizedPollingTaskSet: " + synchronizedPollingTaskSet);//todo delete
        return brick;
    }

    @Override
    public boolean register(final PortSensor portSensor, final Sensor.Type sensorType, final Sensor.Mode sensorMode) {
        return synchronizedPollingTaskSet.add(new PollingTask(portSensor, sensorType, sensorMode));
    }

    @Override
    public boolean unregister(final PortSensor portSensor, final Sensor.Type sensorType, final Sensor.Mode sensorMode) {
        return synchronizedPollingTaskSet.remove(new PollingTask(portSensor, sensorType, sensorMode));
    }

    private Runnable pollingTaskRunnable = () -> {
        Log.d(TAG, "Started polling ...");
        final ArrayList<SensedValue> sensedValues = new ArrayList<>();

        for(final PollingTask pollingTask : synchronizedPollingTaskSet) {
            final Sensor.Type sensorType = pollingTask.sensorType;
            final Sensor.Mode sensorMode = pollingTask.sensorMode;
            switch (sensorType) {
                case TOUCH:
                    //todo
                    break;
                case COLOR:
                    //todo
                    break;
                case INFRA_RED:
                    //todo
                    break;
                case ULTRASONIC:
                    final int polledValue = brick.sensor().port(pollingTask.portSensor).ultrasonic().mode(pollingTask.sensorMode.name());
                    final Bundle bundle = new Bundle(1);
                    if(SensorUltrasonic.Mode.DISTANCE_CENTIMETERS.name().equalsIgnoreCase(sensorMode.name())) {
                        bundle.putInt(SensorUltrasonic.VALUE_CENTIMETERS, polledValue);
                    } else if(SensorUltrasonic.Mode.DISTANCE_INCHES.name().equalsIgnoreCase(sensorMode.name())) {
                        bundle.putInt(SensorUltrasonic.VALUE_INCHES, polledValue);
                    } else if(SensorUltrasonic.Mode.LISTEN_ONLY.name().equalsIgnoreCase(sensorMode.name())) {
                        // todo implement - skip for now
                        Log.i(TAG, "Not implemented for MODE: " + SensorUltrasonic.Mode.LISTEN_ONLY);
                    } else {
                        Log.w(TAG, "Unknown MODE for sensor: " + Sensor.Type.ULTRASONIC);
                    }
                    bundle.putInt("value", polledValue);
                    sensedValues.add(new SensedValue(Sensor.Type.ULTRASONIC, pollingTask.sensorMode, bundle));
                    break;
                default: throw new UnsupportedOperationException();
            }
        }

        if(!sensedValues.isEmpty()) {
            final Intent intent = new Intent(EV3_SENSED_VALUES);
            intent.putParcelableArrayListExtra(SensedValue.EXTRA_SENSED_VALUES, sensedValues);
            sendBroadcast(intent);
            Log.d(TAG, "Broadcasting sensed values: " + sensedValues);
        }
    };
}