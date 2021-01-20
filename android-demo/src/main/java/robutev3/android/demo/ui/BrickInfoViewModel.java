package robutev3.android.demo.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

/**
 * @author Nearchos
 * Created: 19-Dec-19
 */
public class BrickInfoViewModel extends ViewModel {

    private BrickInfo mBrickInfo = new BrickInfo();

    private MutableLiveData<BrickInfoViewModel.BrickInfo> mBrickInfoMutableLiveData = new MutableLiveData<>();

    private LiveData<BrickInfoViewModel.BrickInfo> mBrickInfoLiveData = Transformations.map(mBrickInfoMutableLiveData, input -> input);

    LiveData<BrickInfoViewModel.BrickInfo> getBrickInfo() {
        return mBrickInfoLiveData;
    }

    public void setConnected() {
        mBrickInfo.connected = true;
        mBrickInfoMutableLiveData.setValue(mBrickInfo);
    }

    public void setDisconnected() {
        mBrickInfo.connected = false;
        mBrickInfoMutableLiveData.setValue(mBrickInfo);
    }

    public void setStarted() {
        mBrickInfo.started = true;
        mBrickInfoMutableLiveData.setValue(mBrickInfo);
    }

    public void setStopped() {
        mBrickInfo.started = false;
        mBrickInfoMutableLiveData.setValue(mBrickInfo);
    }

    public void setFailed() {
        mBrickInfo.failed = true;
        mBrickInfoMutableLiveData.setValue(mBrickInfo);
    }

    public class BrickInfo implements Serializable {

        private boolean connected = false;
        private boolean started = false;
        private boolean failed = false;

        boolean isConnected() {
            return connected;
        }

        boolean isStarted() {
            return started;
        }

        boolean isFailed() {
            return failed;
        }
    }
}