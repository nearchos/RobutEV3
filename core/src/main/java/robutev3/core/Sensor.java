package robutev3.core;

/**
 * @author Nearchos
 * Created: 09-Dec-19
 */
public class Sensor {

    public enum Type { TOUCH, COLOR, ULTRASONIC, INFRA_RED }

    public interface Mode {
        String name();
    }

    private final Brick brick;
    private PortSensor port;

    Sensor(final Brick brick) {
        this.brick = brick;
    }

    public SensorSelector port1() {
        this.port = PortSensor.ONE;
        return new SensorSelector();
    }

    public SensorSelector port2() {
        this.port = PortSensor.TWO;
        return new SensorSelector();
    }

    public SensorSelector port3() {
        this.port = PortSensor.THREE;
        return new SensorSelector();
    }

    public SensorSelector port4() {
        this.port = PortSensor.FOUR;
        return new SensorSelector();
    }

    public SensorSelector port(PortSensor portSensor) {
        this.port = portSensor;
        return new SensorSelector();
    }

    public MotorSensorSelector portA() {
        this.port = PortSensor.A;
        return new MotorSensorSelector();
    }

    public MotorSensorSelector portB() {
        this.port = PortSensor.B;
        return new MotorSensorSelector();
    }

    public MotorSensorSelector portC() {
        this.port = PortSensor.C;
        return new MotorSensorSelector();
    }

    public MotorSensorSelector portD() {
        this.port = PortSensor.D;
        return new MotorSensorSelector();
    }

    public class SensorSelector {

        public SensorTouch.TouchSensible touch() {
            return new SensorTouch(brick, port).new TouchSensible();
        }

        public SensorTouch.BumpSensible bump() {
            return new SensorTouch(brick, port).new BumpSensible();
        }

        public SensorColor.ReflectSensible reflect() { return new SensorColor(brick, port).new ReflectSensible(); }

        public SensorColor.AmbientSensible ambient() { return new SensorColor(brick, port).new AmbientSensible(); }

        public SensorColor.ColorSensible color() { return new SensorColor(brick, port).new ColorSensible(); }

        public SensorUltrasonic.DistanceSensible ultrasonic() { return new SensorUltrasonic(brick, port).new DistanceSensible(); }
    }

    public class MotorSensorSelector {

        // todo
    }

    interface Listener {
    }
}