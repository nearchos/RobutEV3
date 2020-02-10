package robutev3.core;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nearchos
 * Created: 08-Dec-19
 */
public class Motor {

    public static final int DEFAULT_SPEED = 50;

    public enum ActionType { OFF, TURN_INDEFINITELY, TURN_DEGREES, TURN_TIME }

    public enum StopMode { BRAKE, COAST }

    private final Brick brick;

    private Set<PortMotor> selectedPorts = new HashSet<>();
    private ActionType actionType = ActionType.OFF;
    private StopMode stopMode = StopMode.COAST;
    private int speed = DEFAULT_SPEED;
    private int degrees = 0;
    private int time = 0;

    Motor(final Brick brick) {
        this.brick = brick;
    }

    public ActionSelector port(final PortMotor portMotor) {
        selectedPorts.add(portMotor);
        return new ActionSelector();
    }

    public ActionSelector portA() {
        selectedPorts.add(PortMotor.A);
        return new ActionSelector();
    }

    public ActionSelector portB() {
        selectedPorts.add(PortMotor.B);
        return new ActionSelector();
    }

    public ActionSelector portC() {
        selectedPorts.add(PortMotor.C);
        return new ActionSelector();
    }

    public ActionSelector portD() {
        selectedPorts.add(PortMotor.D);
        return new ActionSelector();
    }

    public ActionSelector portsBandC() {
        selectedPorts.add(PortMotor.B);
        selectedPorts.add(PortMotor.C);
        return new ActionSelector();
    }

    public StopSelector stopAll() {
        selectedPorts.add(PortMotor.A);
        selectedPorts.add(PortMotor.B);
        selectedPorts.add(PortMotor.C);
        selectedPorts.add(PortMotor.D);
        return new ActionSelector().stop();
    }

    private byte getPortsCode() {
        byte code = (byte) 0x00;
        for(final PortMotor portMotor : selectedPorts) {
            code += portMotor.getCode();
        }
        return code;
    }

    public class ActionSelector {

        public StopSelector stop() {
            actionType = ActionType.OFF;
            return new StopSelector();
        }

        public Goable turnIndefinitely() {
            return turnIndefinitely(DEFAULT_SPEED);
        }

        public Goable turnIndefinitely(int speed) {
            actionType = ActionType.TURN_INDEFINITELY;
            // check bounds
            if(speed < -100) {
                brick.warning("Speed can not be < -100: " + speed);
                speed = -100;
            }
            if(speed > +100) {
                brick.warning("Speed can not be > +100: " + speed);
                speed = +100;
            }
            Motor.this.speed = speed;

            return new Goable();
        }

        public StopSelector turnDegrees(final int degrees) {
            return turnDegrees(degrees, DEFAULT_SPEED);
        }

        public StopSelector turnDegrees(int degrees, int speed) {
            actionType = ActionType.TURN_DEGREES;
            // check bounds
            if(degrees < -36000) {
                brick.warning("Warning, degrees cannot exceed -36000. Setting value to -36000.");
                degrees = -36000;
            }
            if(degrees > +36000) {
                brick.warning("Warning, degrees cannot exceed +36000. Setting value to +36000.");
                degrees = +36000;
            }
            Motor.this.degrees = degrees;

            // check bounds
            if(speed < -100) {
                brick.warning("Speed can not be < -100: " + speed);
                speed = -100;
            }
            if(speed > +100) {
                brick.warning("Speed can not be > +100: " + speed);
                speed = +100;
            }
            Motor.this.speed = speed;

            return new StopSelector();
        }

        public StopSelector turnTime(int time) {
            return turnTime(time, DEFAULT_SPEED);
        }

        public StopSelector turnTime(int time, int speed) {
            actionType = ActionType.TURN_TIME;
            // check bounds
            if(time < 0) {
                brick.warning("Warning, time cannot be < 0. Setting value to 0.");
                time = 0;
            }
            Motor.this.time = time;

            // check bounds
            if(speed < -100) {
                brick.warning("Speed can not be < -100: " + speed);
                speed = -100;
            }
            if(speed > +100) {
                brick.warning("Speed can not be > +100: " + speed);
                speed = +100;
            }
            Motor.this.speed = speed;

            return new StopSelector();
        }
    }

    public class StopSelector {

        public Goable coast() {
            Motor.this.stopMode = StopMode.COAST;
            return new Goable();
        }

        public Goable brake() {
            Motor.this.stopMode = StopMode.BRAKE;
            return new Goable();
        }
    }

    public class Goable {

        public void go() {
            switch (actionType) {
                case OFF:
                    brick.motorStop(getPortsCode(), stopMode == StopMode.BRAKE);
                    break;
                case TURN_INDEFINITELY:
                    brick.motorTurnAtSpeed(getPortsCode(), speed);
                    break;
                case TURN_DEGREES:
                    brick.motorTurnDegrees(getPortsCode(), speed, degrees, stopMode);
                    break;
                case TURN_TIME:
                    brick.motorTurnTime(getPortsCode(), speed, time, stopMode);
                    break;
            }
        }
    }
}