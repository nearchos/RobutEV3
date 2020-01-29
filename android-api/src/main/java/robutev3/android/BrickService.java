package robutev3.android;

import robutev3.core.Brick;
import robutev3.core.PortSensor;
import robutev3.core.Sensor;

/**
 * @author Nearchos
 * Created: 29-Jan-20
 */
public interface BrickService {

    Brick brick();

    boolean register(final PortSensor portSensor, final Sensor.Type sensorType, final Sensor.Mode sensorMode);

    boolean unregister(final PortSensor portSensor, final Sensor.Type sensorType, final Sensor.Mode sensorMode);
}