package robutev3.core;

/**
 * @author Nearchos Paspallis [http://nearchos.github.io]
 * Created: 09-Dec-19
 */
public class SensorUltrasonic {

    public static final String VALUE_CENTIMETERS = "VALUE_CENTIMETERS";
    public static final String VALUE_INCHES = "VALUE_INCHES";

    public enum Mode implements Sensor.Mode {
        /** Returns the latest distance measured in centimeters */
        DISTANCE_CENTIMETERS,
        /** Returns the latest distance measured in inches */
        DISTANCE_INCHES,
        /** Works in passive mode - i.e. it does not emit any ultrasound, only listens for */
        LISTEN_ONLY;

        public static Mode fromName(final String name) {
            return valueOf(name);
        }
    }

    private final Brick brick;
    private final PortSensor port;

    SensorUltrasonic(final Brick brick, final PortSensor port) {
        this.brick = brick;
        this.port = port;
    }

    public class DistanceSensible {

        /**
         * Returns distance detected by the ultrasound sensor, measured in centimeters
         *
         * @return an int in the range 0..255
         */
        public int centimeters() { // returns distance measured in centimeters
            return brick.senseRaw(port.getCode(), Code.SENSOR_TYPE_ULTRASONIC, Code.SENSOR_MODE_ULTRASONIC_CM);
        }

        public int inches() { // returns whether pressed
            return brick.senseRaw(port.getCode(), Code.SENSOR_TYPE_ULTRASONIC, Code.SENSOR_MODE_ULTRASONIC_INCH);
        }

        public int mode(final String modeName) {
            if(Mode.DISTANCE_CENTIMETERS.name().equalsIgnoreCase(modeName)) return centimeters();
            else if(Mode.DISTANCE_INCHES.name().equalsIgnoreCase(modeName)) return inches();
            else throw new UnsupportedOperationException();
        }
    }
}