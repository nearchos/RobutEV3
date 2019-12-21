package robutev3.core;

/**
 * @author Nearchos
 * Created: 06-Dec-19
 */
public enum PortSensor implements Port {

    ONE     ("1", (byte) 0x00, "SensorTouch Sensor"),
    TWO     ("2", (byte) 0x01, "No Sensor"),
    THREE   ("3", (byte) 0x02, "SensorColor Sensor"),
    FOUR    ("4", (byte) 0x03, "Infrared Sensor"),
    A       ("A", (byte) 0x10, "Medium Motor"),
    B       ("B", (byte) 0x11, "Large Motor"),
    C       ("C", (byte) 0x12, "Large Motor"),
    D       ("D", (byte) 0x13, "Medium Motor");

    private final String name;
    private final byte code;
    private final String defaultDevice; // source https://www.lego.com/cdn/cs/set/assets/bltbef4d6ce0f40363c/LMSUser_Guide_LEGO_MINDSTORMS_EV3_11_Tablet_ENUS.pdf

    PortSensor(final String name, final byte code, final String defaultDevice) {
        this.name = name;
        this.code = code;
        this.defaultDevice = defaultDevice;
    }

    public String getName() {
        return name;
    }

    public byte getCode() {
        return code;
    }

    public String getDefaultDevice() {
        return defaultDevice;
    }
}