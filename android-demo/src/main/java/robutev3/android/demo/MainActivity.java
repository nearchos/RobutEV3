package robutev3.android.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import robutev3.android.Device;

import static android.content.pm.PackageManager.FEATURE_USB_HOST;
import static robutev3.android.BluetoothCommunicationInterface.BLUETOOTH_OUI_LEGO;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "robutev3";

    public static final String ACTION_USB_PERMISSION = "robut.USB_PERMISSION";

    private CheckBox usbHostModeCheckBox;
    private CheckBox ignoreNonEv3Bluetooth;
    private TextView bluetoothNotSupported;
    private TextView bluetoothNotEnabled;
    private DeviceListAdapter deviceListAdapter;

    private BluetoothAdapter mDefaultBluetoothAdapter;
    private UsbManager mUsbManager;

    private final Vector<Device> allDevices = new Vector<>();

    private PendingIntent permissionIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.usbHostModeCheckBox = findViewById(R.id.usbHostModeCheckBox);
        this.ignoreNonEv3Bluetooth = findViewById(R.id.ignoreNonEv3Bluetooth);
        this.ignoreNonEv3Bluetooth.setOnCheckedChangeListener((buttonView, isChecked) -> updateDevices());
        this.bluetoothNotSupported = findViewById(R.id.bluetoothNotSupported);
        this.bluetoothNotEnabled = findViewById(R.id.bluetoothNotEnabled);
        final ListView devicesListView = findViewById(R.id.devicesListView);

        permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter);

        this.deviceListAdapter = new DeviceListAdapter(this, allDevices);
        devicesListView.setAdapter(deviceListAdapter);
        devicesListView.setOnItemClickListener((adapterView, view, i, l) -> {
            final Device device = allDevices.get(i);
            connect(device);
        });

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mDefaultBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        checkBluetooth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final PackageManager packageManager = getPackageManager();
        final boolean usbHost = packageManager.hasSystemFeature(FEATURE_USB_HOST);
        Log.d("USB", "usbHost: " + usbHost);

        updateDevices();
    }

    private void updateDevices() {
        allDevices.clear();

        // Get USB devices
        final HashMap<String, UsbDevice> usbDevices = mUsbManager.getDeviceList();
        for(final Map.Entry<String,UsbDevice> entry : usbDevices.entrySet()) {
            final String name = entry.getKey();
            final UsbDevice usbDevice = entry.getValue();
            allDevices.add(new Device(Device.Type.TYPE_USB, name,
                    String.format("%s version %s - %s : %s (%s) - %b",
                            usbDevice.getManufacturerName(),
                            usbDevice.getVersion(),
                            usbDevice.getProductName(),
                            usbDevice.getDeviceName(),
                            name,
                            mUsbManager.hasPermission(usbDevice))));
            if(!mUsbManager.hasPermission(usbDevice)) {
                mUsbManager.requestPermission(usbDevice, permissionIntent);
            }
        }

        final UsbAccessory [] usbAccessories = mUsbManager.getAccessoryList();
        if(usbAccessories != null) {
            for(final UsbAccessory usbAccessory : usbAccessories) {
                Log.d("USB", String.format("usbAccessory : serial: %4s, model: %4s, manufacturer: %s, hasPermission: %b",
                        usbAccessory.getSerial(),
                        usbAccessory.getModel(),
                        usbAccessory.getManufacturer(),
                        mUsbManager.hasPermission(usbAccessory)));
                allDevices.add(new Device(Device.Type.TYPE_USB, usbAccessory.toString(),
                        String.format("A %s : %s : %s : %s - %b",
                                usbAccessory.getModel(),
                                usbAccessory.getSerial(),
                                usbAccessory.getManufacturer(),
                                usbAccessory.getVersion(),
                                mUsbManager.hasPermission(usbAccessory))));
            }
        }

        // Get currently paired Bluetooth devices
        final Vector<BluetoothDevice> bluetoothDevices = new Vector<>(mDefaultBluetoothAdapter.getBondedDevices());
        for(final BluetoothDevice bluetoothDevice : bluetoothDevices) {
            if(!ignoreNonEv3Bluetooth.isChecked() || bluetoothDevice.getAddress().startsWith(BLUETOOTH_OUI_LEGO)) {
                allDevices.add(new Device(Device.Type.TYPE_BLUETOOTH, bluetoothDevice.getName(), bluetoothDevice.getAddress()));
            }
        }

        deviceListAdapter.notifyDataSetChanged();
    }

    private void checkBluetooth() {
        // check if bluetooth exists
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothNotSupported.setVisibility(bluetoothAdapter == null ? View.VISIBLE : View.GONE);

        // check if bluetooth is enabled
        bluetoothNotEnabled.setVisibility(bluetoothAdapter!= null && bluetoothAdapter.isEnabled() ? View.GONE : View.VISIBLE);
    }

    private void connect(final Device device) {
        final Intent intent = new Intent(this, ControlActivity.class);
        intent.putExtra(Device.EXTRA_DEVICE, device);
        startActivity(intent);
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    final UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){ recreate(); }
                    }
                }
            }
        }
    };
}