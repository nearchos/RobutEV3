package robutev3.android.demo.ui;

import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * @author Nearchos
 * Created: 19-Dec-19
 */
public class UltrasonicViewModel extends ViewModel {

    private MutableLiveData<Distance> mUltrasonicMutableLiveData = new MutableLiveData<>();

    private LiveData<Distance> mBrickInfoLiveData = Transformations.map(mUltrasonicMutableLiveData, input -> input);

    LiveData<Distance> getUltrasonicDistance() {
        return mBrickInfoLiveData;
    }

    public void setDistance(final int distance, final Distance.DistanceUnit distanceUnit) {
        mUltrasonicMutableLiveData.setValue(new Distance(distance, distanceUnit));
    }
}