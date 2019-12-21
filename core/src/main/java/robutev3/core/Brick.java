package robutev3.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * @author Nearchos
 * Created: 06-Dec-19
 */
public class Brick implements IBrick {

    private final CommunicationInterface communicationInterface;

    private Brick(final CommunicationInterface communicationInterface) {
        this.communicationInterface = communicationInterface;
    }

    static public Brick create(final CommunicationInterface communicationInterface) {
        return new Brick(communicationInterface);
    }

    public void start() throws IOException {
        communicationInterface.connect();
    }

    public void stop() throws IOException {
        communicationInterface.disconnect();
    }

    // API

    public Motor motor() {
        return new Motor(this);
    }

    public Sound sound() {
        return new Sound(this);
    }

    public Light light() {
        return new Light(this);
    }

    public Sensor sensor() {
        return new Sensor(this);
    }

    // End of API

    void motorStop(byte ports, boolean brake) {
        try {
            Command c = new Command(Code.DIRECT_COMMAND_NO_REPLY);
            c.motorStopMotor(ports, brake);
            sendCommand(c);
        } catch (IOException ioe) {
            error(ioe);
        }
    }

    void motorTurnAtSpeed(byte ports, int speed) {
        try {
            Command c = new Command(Code.DIRECT_COMMAND_NO_REPLY);
            c.motorTurnAtSpeed(ports, speed);
            c.motorStartMotor(ports);
            sendCommand(c);
        } catch (IOException ioe) {
            error(ioe);
        }
    }

    void motorTurnDegrees(final byte ports, final int speed, final int degrees, final Motor.StopMode stopMode) {
        motorStepAtSpeed(ports, speed, 0, degrees, 0, stopMode == Motor.StopMode.BRAKE);
    }

    private void motorStepAtSpeed(byte ports, int speed, int degreesRampUp, int degreesContinueRun, int degreesRampDown, boolean brake) {
        try {
            Command c = new Command(Code.DIRECT_COMMAND_NO_REPLY);
            c.motorStepAtSpeed(ports, speed, degreesRampUp, degreesContinueRun, degreesRampDown, brake);
            c.motorStartMotor(ports);
            sendCommand(c);
        } catch (IOException ioe) {
            error(ioe);
        }
    }

    void motorTurnTime(final byte ports, final int speed, final int milliseconds, final Motor.StopMode stopMode) {
        motorTurnTimeAtSpeed(ports, speed, 0, milliseconds, 0, stopMode == Motor.StopMode.BRAKE);
    }

    private void motorTurnTimeAtSpeed(byte ports, int speed, int msRampUp, int msContinueRun, int msRampDown, boolean brake) {
        try {
            Command c = new Command(Code.DIRECT_COMMAND_NO_REPLY);
            c.motorTimeAtSpeed(ports, speed, msRampUp, msContinueRun, msRampDown, brake);
            c.motorStartMotor(ports);
            sendCommand(c);
        } catch (IOException ioe) {
            error(ioe);
        }
    }

    void beep(final Sound.Beepable beepable) {
        try {
            Command c = new Command(Code.DIRECT_COMMAND_NO_REPLY, 0, 0);
            c.speakerPlayTone(beepable.getVolume(), beepable.getFrequency(), beepable.getDuration());
            sendCommand(c);
        } catch (IOException ioe) {
            error(ioe);
        }
    }

    void light(final Light light) {
        try {
            Command c = new Command(Code.DIRECT_COMMAND_NO_REPLY);
            c.ledSetPattern(light.getCode());
            sendCommand(c);
        } catch (IOException ioe) {
            error(ioe);
        }
    }

    int senseRaw(final byte port, final int type, final int mode) {
        try {
            final Command command = new Command(Code.DIRECT_COMMAND_REPLY, 4, 0);
            command.sensorReadyRaw(port, type, mode);
            final byte [] valueRaw = sendCommand(command);
            final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] { valueRaw[5], valueRaw[6], valueRaw[7], valueRaw[8] });
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            final int value = byteBuffer.getInt();
//warning(String.format(Locale.US, "%s %s %s %s -- %d",
//        Integer.toBinaryString(valueRaw[5]),
//        Integer.toBinaryString(valueRaw[6]),
//        Integer.toBinaryString(valueRaw[7]),
//        Integer.toBinaryString(valueRaw[8]),
//        value
//        ));//todo
            return value;
        } catch (IOException ioe) {
            error(ioe);
        }
        return 0;
    }

    static int sequence = 1; // todo increase?

    private byte [] sendCommand(Command c) throws IOException { // todo
        synchronized (communicationInterface) {
            communicationInterface.write(c.byteCode());
            if (c.getCommandType() == Code.DIRECT_COMMAND_REPLY || c.getCommandType() == Code.SYSTEM_COMMAND_REPLY) {
                final byte [] reply = waitForReply(c.getCommandType());
                return reply;
            } else {
                return new byte[] { (byte) sequence };
            }
        }
    }

    private byte [] waitForReply(byte commandType) throws IOException { // todo
        while (true) {
            byte[] reply = communicationInterface.read();
//            byte[] reply = communicationInterface.readReply();
            if (commandType == Code.DIRECT_COMMAND_REPLY && reply[4] != Code.DIRECT_REPLY) {
                try {
                    printMessage(reply);
                    throw new Exception(String.format("Direct command: %02X:%02X returned error", reply[2], reply[3]));
                } catch (Exception e) {
                    error(e);
                }
            }
            if (commandType == Code.SYSTEM_COMMAND_REPLY && reply[4] != Code.SYSTEM_REPLY) {
                try {
                    throw new Exception(String.format("System command: %02X:%02X returned error", reply[2], reply[3]));
                } catch (Exception e) {
                    error(e);
                }
            }
            return reply;
        }
    }

    private void printMessage(byte[] cmd) { // todo delete
        System.out.println();
        System.out.print("send" + " 0x|");
        for (int i = 0; i < cmd.length - 1; i++) {
            System.out.printf("%02X:", cmd[i]);
        }
        System.out.printf("%02X|", cmd[cmd.length - 1]);
        System.out.println();
    }

    // Log system

    public interface LogListener {
        void warning(String message);
        void error(String message);
    }

    private Vector<LogListener> logListeners = new Vector<>();

    public boolean addLogListener(final LogListener logListener) {
        return logListeners.add(logListener);
    }

    public boolean removeLogListener(final LogListener logListener) {
        return logListeners.remove(logListener);
    }

    void warning(final String message) {
        final Date date = new Date();
        final String s = String.format(Locale.US, "[%tF %tT] Warning: %s \n", date, date, message);
        for(final LogListener logListener : logListeners) {
            logListener.warning(s);
        }
    }

    void error(final String message) {
        final Date date = new Date();
        final String s = String.format(Locale.US, "[%tF %tT] Warning: %s \n", date, date, message);
        for(final LogListener logListener : logListeners) {
            logListener.error(s);
        }
    }

    private void error(final Exception exception) {
        error(exception.getClass() + " -> " + exception.getMessage());
    }
}