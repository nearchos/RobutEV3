import java.io.IOException;

import robutev3.core.Brick;
import robutev3.core.CommunicationInterface;

/**
 * @author Nearchos
 * Created: 18-Dec-19
 */
public class TestCommon {

    static void test(final CommunicationInterface communicationInterface) throws IOException {

        // create brick
        System.out.println("Creating brick ...");
        final Brick brick = Brick.create(communicationInterface);

        // set warning/error listeners
        brick.addLogListener(new Brick.LogListener() {
            @Override
            public void debug(String message) {
                System.err.println("debug -> " + message);
            }

            @Override
            public void warning(String message) {
                System.err.println("warning -> " + message);
            }

            @Override
            public void error(String message) {
                System.err.println("error -> " + message);
            }
        });

        // init/start
        System.out.println("Connecting brick ...");
        brick.start();

        System.out.println("Sending beep");
        brick.sound().beep();

        System.out.println("Waiting for 1 second ...");
        try { Thread.sleep(1000); } catch (InterruptedException ie) {}

        System.out.println("Starting motor at port B");
        brick.motor().portB().turnIndefinitely().go();

        System.out.println("Waiting for 1 second ...");
        try { Thread.sleep(1000); } catch (InterruptedException ie) {}

        System.out.println("Turning on red led in flash mode");
        brick.light().red().flash();

        System.out.println("Waiting for 1 second ...");
        try { Thread.sleep(1000); } catch (InterruptedException ie) {}

        System.out.println("Reading bumps...");
        System.out.println("Bumps -> " + brick.sensor().port1().bump().sense());

        System.out.println("Waiting for 1 second ...");
        try { Thread.sleep(1000); } catch (InterruptedException ie) {}

        System.out.println("Stopping motor at port B");
        brick.motor().portB().stop().coast().go();

        System.out.println("Waiting for 1 second ...");
        try { Thread.sleep(1000); } catch (InterruptedException ie) {}

        System.out.println("Turning off led");
        brick.light().off();

        System.out.println("Waiting for 1 second ...");
        try { Thread.sleep(1000); } catch (InterruptedException ie) {}

        System.out.println("Reading bumps...");
        System.out.println("Bumps -> " + brick.sensor().port1().bump().sense());

        System.out.println("Waiting for 1 second ...");
        try { Thread.sleep(1000); } catch (InterruptedException ie) {}

        System.out.println("Press bump button at port 1 ...");
        int bumps = 0;
        int currentBump = brick.sensor().port1().bump().sense();
        final int maxBumps = 3;
        while (bumps < maxBumps) {
            int bump = brick.sensor().port1().bump().sense();
            if(currentBump != bump) {
                currentBump = bump;
                bumps++;
                brick.motor().portB().turnTime(500).brake().go();
                System.out.println("Bumped! " + bumps + " of " + maxBumps);
            }
            try { Thread.sleep(100); } catch (InterruptedException ie) {}
        }

        // stop/finalize brick
        brick.stop();
    }
}
