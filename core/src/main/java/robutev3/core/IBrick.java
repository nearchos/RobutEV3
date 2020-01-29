package robutev3.core;

import java.io.IOException;

/**
 * @author Nearchos
 * Created: 20-Dec-19
 */
public interface IBrick { // todo delete?

    Motor motor();

    Sound sound();

    Light light();

    Sensor sensor();
}
