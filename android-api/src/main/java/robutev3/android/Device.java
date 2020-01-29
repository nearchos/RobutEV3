package robutev3.android;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * @author Nearchos
 * Created: 25-May-19
 */
public class Device implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type.name());
        dest.writeString(name);
        dest.writeString(address);
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device [] newArray(int size) {
            return new Device[size];
        }

        @Override
        public Device createFromParcel(Parcel source) {
            return new Device(Type.valueOf(source.readString()), source.readString(), source.readString());
        }
    };

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