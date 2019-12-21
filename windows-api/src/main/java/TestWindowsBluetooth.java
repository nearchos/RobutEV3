import java.io.IOException;

import robutev3.windows.BluetoothCommunicationInterface;
import robutev3.windows.USBCommunicationInterface;

/**
 * Demonstrates the use of {@link USBCommunicationInterface} with RobutEV3.
 *
 * @author Nearchos
 * Created: 18-Dec-19
 */
public class TestWindowsBluetooth {

    // use your own brick's ID which you can find by looking it up under 'Settings' (4th menu) and 'Brick Info'
    private static final String LEGO_BLUETOOTH_MAC_ADDRESS = "0016537f46b1";

    public static void main(String[] args) throws IOException {

        final BluetoothCommunicationInterface bluetoothCommunicationInterface =
                new BluetoothCommunicationInterface(LEGO_BLUETOOTH_MAC_ADDRESS);

        TestCommon.test(bluetoothCommunicationInterface);

        System.exit(0);
    }
}