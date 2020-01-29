package robutev3.android.demo.ui;

import android.os.Parcel;
import android.os.Parcelable;

import robutev3.android.Device;

/**
 * @author Nearchos
 * Created: 29-Jan-20
 */
public class Distance implements Parcelable {

    public enum DistanceUnit { cm, in }

    private final int distance;
    private final DistanceUnit distanceUnit;

    Distance(int distance, DistanceUnit distanceUnit) {
        this.distance = distance;
        this.distanceUnit = distanceUnit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(distance);
        dest.writeString(distanceUnit.name());
    }

    public static final Creator<Distance> CREATOR = new Creator<Distance>() {
        @Override
        public Distance [] newArray(int size) {
            return new Distance[size];
        }

        @Override
        public Distance createFromParcel(Parcel source) {
            return new Distance(source.readInt(), DistanceUnit.valueOf(source.readString()));
        }
    };

    public int getDistance() {
        return distance;
    }

    public DistanceUnit getDistanceUnit() {
        return distanceUnit;
    }
}