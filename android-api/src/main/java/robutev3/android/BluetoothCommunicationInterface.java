package robutev3.android;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import robutev3.core.CommunicationInterface;

public class BluetoothCommunicationInterface implements CommunicationInterface {

	public static final String BLUETOOTH_OUI_LEGO = "00:16:53";

	private final BluetoothSocket bluetoothSocket;

	private InputStream in;
	private OutputStream out;

	private static final String mUUID = "00001101-0000-1000-8000-00805F9B34FB";

	private final String TAG = "robutev3:api";

	@RequiresApi(Build.VERSION_CODES.S)
	@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
	public BluetoothCommunicationInterface(BluetoothDevice bluetoothDevice) throws IOException {
		this.bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(mUUID));
	}

	@RequiresApi(Build.VERSION_CODES.S)
	@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
	@Override
	public void connect() throws IOException {
		Log.d(TAG, "Trying to create connection ...");
		bluetoothSocket.connect();
		Log.d(TAG, "Connection successful");
		in = bluetoothSocket.getInputStream();
		out = bluetoothSocket.getOutputStream();
	}

	@Override
	public void disconnect() throws IOException {
		in.close();
		out.close();
		bluetoothSocket.close();
	}

	@Override
	public void write(byte[] bytes) throws IOException {
		out.write(bytes);
	}

	@Override
	public byte [] read() throws IOException {
		byte[] reply = new byte[1024];
		int numOfBytes = in.read(reply);
		Log.v(TAG, String.format("received %d bytes: 0x|%02X|", numOfBytes, reply[4]));
		return reply;
	}
}