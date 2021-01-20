package robutev3.android;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import robutev3.core.Sensor;
import robutev3.core.SensorColor;
import robutev3.core.SensorUltrasonic;

/**
 * @author Nearchos
 * Created: 29-Jan-20
 */
public class SensedValue implements Parcelable {

    public static final String EXTRA_SENSED_VALUES = "EXTRA_SENSED_VALUES";

    private final Sensor.Type sensorType;
    private final Sensor.Mode sensorMode;
    private final Bundle values;

    SensedValue(Sensor.Type sensorType, Sensor.Mode sensorMode, Bundle values) {
        this.sensorType = sensorType;
        this.sensorMode = sensorMode;
        this.values = values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sensorType.name());
        dest.writeString(sensorMode.name());
        dest.writeBundle(values);
    }

    public static final Creator<SensedValue> CREATOR = new Creator<SensedValue>() {

        @Override
        public SensedValue [] newArray(int size) {
            return new SensedValue[size];
        }

        @Override
        public SensedValue createFromParcel(Parcel source) {
            final Sensor.Type sensorType = Sensor.Type.valueOf(source.readString());
            final Sensor.Mode sensorMode;
            switch (sensorType) {
                case TOUCH:
                    // todo
                    throw new UnsupportedOperationException();
                case COLOR:
                    sensorMode = SensorColor.Mode.fromName(source.readString());
                    break;
                case INFRA_RED:
                    // todo
                    throw new UnsupportedOperationException();
                case ULTRASONIC:
                    sensorMode = SensorUltrasonic.Mode.fromName(source.readString());
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
            final Bundle values = source.readBundle(); // source.readBundle(getClass().getClassLoader())
            return new SensedValue(sensorType, sensorMode, values);
        }
    };

    public Sensor.Type getSensorType() {
        return sensorType;
    }

    public Sensor.Mode getSensorMode() {
        return sensorMode;
    }

    public Bundle getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "SensedValue{" +
                "sensorType=" + sensorType +
                ", sensorMode=" + sensorMode +
                ", values=" + values + " (" + values.get("value") + ")" +
                '}';
    }
}