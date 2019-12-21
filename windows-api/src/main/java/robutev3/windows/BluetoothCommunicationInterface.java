package robutev3.windows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import robutev3.core.CommunicationInterface;

/**
 * Implements a Bluetooth-based communication interface for RobutEV3 for Windows.
 *
 * Uses http://www.bluecove.org/. Partly based on
 * https://github.com/LLeddy/J4EV3/blob/master/J4EV3/src/com/j4ev3/desktop/BluetoothComm.java.
 *
 * @author Nearchos
 * Created: 18-Dec-19
 */
public class BluetoothCommunicationInterface implements CommunicationInterface {

    private String macAddress;

    private InputStream inputStream;
    private OutputStream outputStream;

    public BluetoothCommunicationInterface(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public void connect() throws IOException {
        final String uri = "btspp://" + macAddress + ":1";
        final StreamConnection streamConnection = (StreamConnection) Connector.open(uri);
        inputStream = streamConnection.openInputStream();
        outputStream = streamConnection.openOutputStream();
    }

    @Override
    public void disconnect() throws IOException {
        inputStream.close();
        outputStream.close();
    }

    @Override
    public void write(byte[] message) throws IOException {
        outputStream.write(message);
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    @Override
    public byte[] read() throws IOException {
        final byte [] reply = new byte[DEFAULT_BUFFER_SIZE];
        inputStream.read(reply);
        return reply;
    }
}