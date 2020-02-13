package robutev3.core;

/**
 * @author Nearchos
 * Created: 10-Feb-20
 */
public class Tank {

    private static final int DEFAULT_TIME = 438;
    private static final int DEFAULT_SPEED = 50;

    public enum ActionType {FORWARD_INDEFINITELY, FORWARD_TIME, BACKWARD_INDEFINITELY, BACKWARD_TIME, LEFT, RIGHT, STOP }

    private final Brick brick;

    private ActionType actionType;

    private int timeInMilliseconds = DEFAULT_TIME;
    private int speed = DEFAULT_SPEED;
    private Motor.StopMode stopMode = Motor.StopMode.COAST;

    Tank(Brick brick) { this.brick = brick; }

    public Goable forward() {
        Tank.this.actionType = ActionType.FORWARD_INDEFINITELY;
        return new Goable();
    }

    public Goable forward(final Interval interval) { // interval must be 0..10000 ms
        return forward(interval, Speed.fromValue(DEFAULT_SPEED));
    }

    public Goable forward(final Interval interval, final Speed speed) { // interval must be 0..10000 ms
        // check bounds
        if(interval.getMilliseconds() < 0) {
            brick.warning("Interval can not be < 0 ms: " + interval);
            timeInMilliseconds = 0;
        }
        if(interval.getMilliseconds() > +10000) {
            brick.warning("Interval can not be > +10000 ms: " + interval);
            timeInMilliseconds = +10000;
        }
        Tank.this.actionType = ActionType.FORWARD_TIME;
        Tank.this.timeInMilliseconds = interval.getMilliseconds();
        Tank.this.speed = speed.getValue();
        return new Goable();
    }

    public Goable forward(final Speed speed) { // speed must be 0..100
        // check bounds
        Tank.this.actionType = ActionType.FORWARD_INDEFINITELY;
        Tank.this.speed = speed.getValue();
        return new Goable();
    }

    public Goable backward() {
        Tank.this.actionType = ActionType.BACKWARD_INDEFINITELY;
        return new Goable();
    }

    public Goable backward(final Interval interval) { // interval must be 0..10000 ms
        return backward(interval, Speed.fromValue(DEFAULT_SPEED));
    }

    public Goable backward(final Interval interval, final Speed speed) { // interval must be 0..10000 ms
        // check bounds
        if(interval.getMilliseconds() < 0) {
            brick.warning("Interval can not be < 0 ms: " + interval);
            timeInMilliseconds = 0;
        }
        if(interval.getMilliseconds() > +10000) {
            brick.warning("Interval can not be > +10000 ms: " + interval);
            timeInMilliseconds = +10000;
        }
        Tank.this.actionType = ActionType.BACKWARD_TIME;
        Tank.this.timeInMilliseconds = interval.getMilliseconds();
        Tank.this.speed = speed.getValue();
        return new Goable();
    }

    public Goable backward(final Speed speed) { // speed must be 0..100
        Tank.this.actionType = ActionType.BACKWARD_INDEFINITELY;
        Tank.this.speed = speed.getValue();
        return new Goable();
    }

    public Goable turnLeft() {
        Tank.this.actionType = ActionType.LEFT;
        return new Goable();
    }

    public Goable turnLeft(final Interval interval) { // timeInMilliseconds must be 0..2000
        // check bounds
        if(interval.getMilliseconds() < 0) {
            brick.warning("Time can not be < 0: " + interval);
            this.timeInMilliseconds = 0;
        }
        if(interval.getMilliseconds() > +2000) {
            brick.warning("Time can not be > +2000: " + interval);
            this.timeInMilliseconds = 2000;
        }
        Tank.this.actionType = ActionType.LEFT;
        this.timeInMilliseconds = interval.getMilliseconds();
        return new Goable();
    }

    public Goable turnRight() {
        Tank.this.actionType = ActionType.RIGHT;
        return new Goable();
    }

    public Goable turnRight(final Interval interval) { // timeInMilliseconds must be 0..2000
        // check bounds
        if(interval.getMilliseconds() < 0) {
            brick.warning("Time can not be < 0: " + interval);
            this.timeInMilliseconds = 0;
        }
        if(interval.getMilliseconds() > +2000) {
            brick.warning("Time can not be > +2000: " + interval);
            this.timeInMilliseconds = 2000;
        }
        Tank.this.actionType = ActionType.RIGHT;
        this.timeInMilliseconds = interval.getMilliseconds();
        return new Goable();
    }

    public StopSelector stop() {
        Tank.this.actionType = ActionType.STOP;
        return new StopSelector();
    }

    public class StopSelector {

        public Goable coast() {
            Tank.this.stopMode = Motor.StopMode.COAST;
            return new Goable();
        }

        public Goable brake() {
            Tank.this.stopMode = Motor.StopMode.BRAKE;
            return new Goable();
        }
    }

    public class Goable {

        public void go() {
            switch (actionType) {
                case FORWARD_INDEFINITELY:
                    brick.motorTankForward(speed);
                    break;
                case FORWARD_TIME:
                    brick.motorTankForward(speed, timeInMilliseconds, stopMode);
                    break;
                case BACKWARD_INDEFINITELY:
                    brick.motorTankBackward(speed);
                    break;
                case BACKWARD_TIME:
                    brick.motorTankBackward(speed, timeInMilliseconds, stopMode);
                    break;
                case LEFT:
                    brick.motorTankLeft(timeInMilliseconds);
                    break;
                case RIGHT:
                    brick.motorTankRight(timeInMilliseconds);
                    break;
                case STOP:
                    brick.motorTankStop(stopMode == Motor.StopMode.BRAKE);
                    break;
            }
        }
    }
}