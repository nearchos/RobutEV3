package robutev3.windows;

import org.usb4java.BufferUtils;
import org.usb4java.Context;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import robutev3.core.CommunicationInterface;

/**
 * Implements a USB-based communication interface for RobutEV3 for Windows.
 *
 * Partly based on the examples at https://github.com/usb4java/usb4java-examples/
 *
 * @author Nearchos
 * Created: 15-Dec-19
 */
public class USBCommunicationInterface implements CommunicationInterface {

    /** The USB vendor ID of the LEGO */
    private static final short LEGO_GROUP_VENDOR_ID = 0x0694;

    /** The USB product ID of the LEGO EV3 */
    private static final short EV3_PRODUCT_ID = 0x0005;

    /** The interface number of the LEGO EV3 */
    private static final int INTERFACE_NUMBER = (byte) 0x00;

    /** The input endpoint of LEGO EV3 */
    private static final byte IN_ENDPOINT = (byte) 0x81;

    /** The output endpoint of LEGO EV3 */
    private static final byte OUT_ENDPOINT = (byte) 0x01;

    /** The communication timeout in milliseconds. */
    private static final int TIMEOUT = 1000;

    private final Context context;

    public USBCommunicationInterface() throws LibUsbException {
        // Initialize the libusb context
        this.context = new Context();
        final int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to initialize libusb", result);
        }
    }

    private DeviceHandle deviceHandle = null;

    @Override
    public void connect() throws IOException {
        // Open device (LEGO EV3)
        deviceHandle = LibUsb.openDeviceWithVidPid(context, LEGO_GROUP_VENDOR_ID, EV3_PRODUCT_ID);
        if (deviceHandle == null) {
            System.err.println("EV3 device not found.");
            System.exit(1);
        }

        // Claim the interface
        final int result = LibUsb.claimInterface(deviceHandle, INTERFACE_NUMBER);
        if (result != LibUsb.SUCCESS) {
            throw new IOException(new LibUsbException("Unable to claim interface", result));
        }
    }

    @Override
    public void disconnect() throws IOException {
        // Release the interface
        final int result = LibUsb.releaseInterface(deviceHandle, INTERFACE_NUMBER);
        if (result != LibUsb.SUCCESS) {
            throw new IOException(new LibUsbException("Unable to release interface", result));
        }

        // Close the device
        LibUsb.close(deviceHandle);

        // De-initialize the libusb context
        LibUsb.exit(context);
    }

    @Override
    public void write(byte [] message) throws IOException {
        // write data to connected device
        final ByteBuffer buffer = BufferUtils.allocateByteBuffer(message.length);
        buffer.put(message);
        final IntBuffer transferred = BufferUtils.allocateIntBuffer();
        int result = LibUsb.interruptTransfer(deviceHandle, OUT_ENDPOINT, buffer, transferred, TIMEOUT);
        if (result != LibUsb.SUCCESS) {
            throw new IOException(new LibUsbException("Unable to send data", result));
        }
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    @Override
    public byte [] read() throws IOException {
        // read data from connected device
        final ByteBuffer buffer = BufferUtils.allocateByteBuffer(DEFAULT_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        final IntBuffer transferred = BufferUtils.allocateIntBuffer();
        int result = LibUsb.bulkTransfer(deviceHandle, IN_ENDPOINT, buffer, transferred, TIMEOUT);
        if (result != LibUsb.SUCCESS) {
            throw new IOException(new LibUsbException("Unable to read data", result));
        }
        final byte [] reply = new byte[buffer.limit()];
        for(int i = 0; i < reply.length; i++) reply[i] = buffer.get(i);
        return reply;
    }
}