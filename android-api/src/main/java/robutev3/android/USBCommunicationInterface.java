package robutev3.android;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.IOException;

import robutev3.core.CommunicationInterface;

/**
 * Tested with a Samsung Tab S2 tablet.
 *
 * @author Nearchos
 * Created: 16-Dec-19
 */
public class USBCommunicationInterface implements CommunicationInterface {

    /** The communication timeout in milliseconds. */
    private static final int TIMEOUT = 1000;

    /** The interface number of the LEGO EV3 */
    private static final int INTERFACE_NUMBER = (byte) 0x00;

    /** The input endpoint of LEGO EV3 */
    private static final int IN_ENDPOINT_INDEX = 0;

    /** The output endpoint of LEGO EV3 */
    private static final int OUT_ENDPOINT_INDEX = 1;

    private final UsbInterface usbInterface;
    private final UsbEndpoint inEndpoint;
    private final UsbEndpoint outEndpoint;
    private final UsbDeviceConnection usbDeviceConnection;

    public USBCommunicationInterface(final UsbManager usbManager, final UsbDevice usbDevice) {
        this.usbInterface = usbDevice.getInterface(INTERFACE_NUMBER);
        this.inEndpoint = usbInterface.getEndpoint(IN_ENDPOINT_INDEX);
        this.outEndpoint = usbInterface.getEndpoint(OUT_ENDPOINT_INDEX);
        this.usbDeviceConnection = usbManager.openDevice(usbDevice);
        Log.d("robut-ev3", "inEndpoint: " + inEndpoint.getAddress() + ", outEndpoint: " + outEndpoint);
    }

    @Override
    public void connect() throws IOException {
        usbDeviceConnection.claimInterface(usbInterface, true); // todo
    }

    @Override
    public void disconnect() throws IOException {
        usbDeviceConnection.releaseInterface(usbInterface);
    }

    @Override
    public void write(byte[] message) throws IOException {
        usbDeviceConnection.bulkTransfer(outEndpoint, message, message.length, TIMEOUT);
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    @Override
    public byte[] read() throws IOException {
        final byte [] buffer = new byte[DEFAULT_BUFFER_SIZE];
        final int numOfBytes = usbDeviceConnection.bulkTransfer(inEndpoint, buffer, DEFAULT_BUFFER_SIZE, TIMEOUT);
        final byte [] reply = new byte[numOfBytes];
        System.arraycopy(buffer, 0, reply, 0, reply.length);
        return reply;
    }
}