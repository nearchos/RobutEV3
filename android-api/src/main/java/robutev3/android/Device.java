package robutev3.android;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Nearchos
 * Created: 25-May-19
 */
public class Device implements Serializable {

    public static final String EXTRA_DEVICE = "extra-device";

    public enum Type { TYPE_BLUETOOTH, TYPE_USB };

    private Type type;
    private String name;
    private String address;

    public Device(final Type type, final String name, final String address) {
        this.type = type;
        this.name = name;
        this.address = address;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final Device otherDevice = (Device) other;
        return type == otherDevice.type && address.equals(otherDevice.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, address);
    }

    @Override
    public String toString() {
        return "Device{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}