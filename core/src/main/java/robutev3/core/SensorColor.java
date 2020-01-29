package robutev3.core;

/**
 * @author Nearchos Paspallis [http://nearchos.github.io]
 * Created: 14-Dec-19
 */
public class SensorColor {

    public enum Mode implements Sensor.Mode {
        /** Measures the amount of light reflected */
        REFLECT,
        /** Measures the amount of ambient light, i.e. with emitting LED off */
        AMBIENT,
        /** Detects the color of the object where the light is reflected */
        COLOR
    }

    private final Brick brick;
    private final PortSensor port;

    SensorColor(final Brick brick, final PortSensor port) {
        this.brick = brick;
        this.port = port;
    }

    public class ReflectSensible {

        public int sense() {
            return brick.senseRaw(port.getCode(), Code.SENSOR_TYPE_COLOR, Code.SENSOR_MODE_COLOR_REFLECT);
        }
    }

    public class AmbientSensible {

        public int sense() {
            return brick.senseRaw(port.getCode(), Code.SENSOR_TYPE_COLOR, Code.SENSOR_MODE_COLOR_AMBIENT);
        }
    }

    public class ColorSensible {

        public int senseRaw() {
            return brick.senseRaw(port.getCode(), Code.SENSOR_TYPE_COLOR, Code.SENSOR_MODE_COLOR_COLOR);
        }

        public Colors sense() {
            final int colorCode = senseRaw();
            return Colors.fromCode(colorCode);
        }
    }
}