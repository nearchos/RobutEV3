package robutev3.android;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import robutev3.core.Brick;
import robutev3.core.IBrick;
import robutev3.core.Light;
import robutev3.core.Motor;
import robutev3.core.Sensor;
import robutev3.core.Sound;

public class EV3Service extends Service implements IBrick {

    public static final String TAG = "robutev3:api";

    public static final String EV3_ACTION_STARTED = "EV3_ACTION_STARTED";
    public static final String EV3_ACTION_STOPPED = "EV3_ACTION_STOPPED";

    public EV3Service() { /* empty */ }

    @Override
    public IBinder onBind(Intent intent) {
        final Device device = (Device) intent.getSerializableExtra(Device.EXTRA_DEVICE);
        if(device == null) throw new IllegalArgumentException("Intent must include a device attached to key: " + Device.EXTRA_DEVICE);
        return new EV3Binder(device);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(brick == null) {
            Log.w(TAG, "onUnbind(): Cannot stop a null brick");
        } else {
            try { brick.stop(); } catch (IOException ioe) { Log.e(TAG, ioe.getMessage(), ioe); }
        }
        return super.onUnbind(intent);
    }

    private Brick brick = null;

    public class EV3Binder extends Binder {

        EV3Binder(final Device device) {
            if(device == null) throw new IllegalArgumentException("Argument cannot be null");

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
        new Thread(() -> {
            Log.d(TAG, "Starting brick ...");
            try { brick.start(); } catch (IOException ioe) { Log.e(TAG, ioe.getMessage(), ioe); }
            Log.d(TAG, "Started brick!");
            // use a broadcast to notify that the brick is started
            final Intent intent = new Intent(EV3_ACTION_STARTED);
            sendBroadcast(intent);
        }).start();
    }

    @Override
    public Motor motor() {
        return this.brick.motor();
    }

    @Override
    public Sound sound() {
        return this.brick.sound();
    }

    @Override
    public Light light() {
        return this.brick.light();
    }

    @Override
    public Sensor sensor() {
        return this.brick.sensor();
    }
}