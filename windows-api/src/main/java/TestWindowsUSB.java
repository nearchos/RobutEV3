import java.io.IOException;

import robutev3.windows.USBCommunicationInterface;

/**
 * Demonstrates the use of {@link USBCommunicationInterface} with RobutEV3.
 *
 * @author Nearchos
 * Created: 16-Dec-19
 */
public class TestWindowsUSB {

    public static void main(String[] args) throws IOException {

        // For Windows, make sure you install a proper libusb driver for EV3 before using the code
        // see https://github.com/libusb/libusb/wiki/Windows#How_to_use_libusb_on_Windows
        final USBCommunicationInterface USBCommunicationInterface = new USBCommunicationInterface();

//        TestCommon.test(USBCommunicationInterface);
        TestCommon3.test(USBCommunicationInterface);

        System.exit(0);
    }
}
