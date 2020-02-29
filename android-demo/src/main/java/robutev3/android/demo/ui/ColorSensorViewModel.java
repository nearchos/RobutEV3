package robutev3.android.demo.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import robutev3.core.Color;
import robutev3.core.SensorColor;

/**
 * @author Nearchos
 * Created: 29-Feb-20
 */
public class ColorSensorViewModel extends ViewModel {

        private MutableLiveData<ColorValue> mColorMutableLiveData = new MutableLiveData<>();

        private LiveData<ColorValue> mColorLiveData = Transformations.map(mColorMutableLiveData, input -> input);

        LiveData<ColorValue> getColorLiveData() {
            return mColorLiveData;
        }

        public void setColor(final Color color) {
            mColorMutableLiveData.setValue(new ColorValue(color));
        }

        public void setReflectedLightIntensity(final int colorRaw) {
            mColorMutableLiveData.setValue(new ColorValue(SensorColor.Mode.REFLECT, colorRaw));
        }

        public void setAmbientLightIntensity(final int colorRaw) {
            mColorMutableLiveData.setValue(new ColorValue(SensorColor.Mode.AMBIENT, colorRaw));
        }

    static class ColorValue {
        private SensorColor.Mode mode;
        private Color color = Color.UNKNOWN;
        private int lightIntensity = 0;

        private ColorValue(final Color color) {
            this.mode = SensorColor.Mode.COLOR;
            this.color = color;
        }

        private ColorValue(final SensorColor.Mode mode, final int lightIntensity) {
            this.mode = mode;
            this.lightIntensity = lightIntensity;
        }

        public SensorColor.Mode getMode() {
            return mode;
        }

        public Color getColor() {
            return color;
        }

        public int getLightIntensity() {
            return lightIntensity;
        }

        @NonNull
        @Override
        public String toString() {
            switch (mode) {
                case COLOR:
                    return color.toString();
                case REFLECT:
                case AMBIENT:
                    return lightIntensity + "(" + (lightIntensity/255f) + "%)";
                default:
                    return "Unknown value";
            }
        }
    }
}