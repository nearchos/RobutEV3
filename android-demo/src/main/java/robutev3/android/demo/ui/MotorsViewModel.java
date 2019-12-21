package robutev3.android.demo.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

import robutev3.core.PortMotor;

/**
 * @author Nearchos
 * Created: 19-Dec-19
 */
public class MotorsViewModel extends ViewModel {

    private MotorsInfo mMotorsInfo = new MotorsInfo();

    private final MutableLiveData<MotorsInfo> mMotorsMutableLiveData = new MutableLiveData<>();

    public LiveData<MotorsInfo> getMotorsInfo() {
        return mMotorsMutableLiveData;
    }

    void setPower(final PortMotor portMotor, final int power) {
        if(power < -100 || power > 100) throw new IllegalArgumentException("Power must be in [-100,100]");
        switch (portMotor) {
            case A:
                mMotorsInfo.powerPortA = power;
                break;
            case B:
                mMotorsInfo.powerPortB = power;
                break;
            case C:
                mMotorsInfo.powerPortC = power;
                if(mMotorsInfo.lockedMotorsBandC) {
                    mMotorsInfo.powerPortB = power;
                }
                break;
            case D:
                mMotorsInfo.powerPortD = power;
                break;
            default:
                throw new IllegalArgumentException("Unknown port: " + portMotor);
        }

        mMotorsMutableLiveData.setValue(mMotorsInfo);
    }

    void lockMotorsBandC(final boolean locked) {
        mMotorsInfo.lockedMotorsBandC = locked;
        if(locked) {
            mMotorsInfo.powerPortC = mMotorsInfo.powerPortB;
        }
        mMotorsMutableLiveData.setValue(mMotorsInfo);
    }

    public class MotorsInfo implements Serializable {

        boolean lockedMotorsBandC = false;
        private int powerPortA = 0;
        private int powerPortB = 0;
        private int powerPortC = 0;
        private int powerPortD = 0;

        public boolean isLockedMotorsBandC() {
            return lockedMotorsBandC;
        }

        public int getPowerPortA() {
            return powerPortA;
        }

        public int getPowerPortB() {
            return powerPortB;
        }

        public int getPowerPortC() {
            return powerPortC;
        }

        public int getPowerPortD() {
            return powerPortD;
        }

        public int getPower(final PortMotor portMotor) {
            switch (portMotor) {
                case A: return powerPortA;
                case B: return powerPortB;
                case C: return powerPortC;
                case D: return powerPortD;
                default: throw new IllegalArgumentException("Unknown motor port: " + portMotor);
            }
        }
    }
}