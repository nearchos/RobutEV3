package robutev3.core;

/**
 * @author Nearchos Paspallis [http://nearchos.github.io]
 * Created: 09-Dec-19
 */
public class SensorTouch {

    public enum Mode implements Sensor.Mode { TOUCH, BUMP }

    private final Brick brick;
    private final PortSensor port;

    SensorTouch(final Brick brick, final PortSensor port) {
        this.brick = brick;
        this.port = port;
    }

    public class TouchSensible {

        public int sense() { // returns whether pressed
            return brick.senseRaw(port.getCode(), Code.SENSOR_TYPE_TOUCH, Code.SENSOR_MODE_TOUCH_TOUCH);
        }
    }

    public class BumpSensible {

        public int sense() { // returns count of bumps
            return brick.senseRaw(port.getCode(), Code.SENSOR_TYPE_TOUCH, Code.SENSOR_MODE_TOUCH_BUMPS);
        }
    }
}