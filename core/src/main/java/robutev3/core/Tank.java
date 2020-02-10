package robutev3.core;

/**
 * @author Nearchos
 * Created: 10-Feb-20
 */
public class Tank {

    public static final int DEFAULT_TIME = 400;
    public static final int DEFAULT_SPEED = 50;

    public enum ActionType { FORWARD, BACKWARD, LEFT, RIGHT, STOP }

    private final Brick brick;

    private ActionType actionType;

    private int time = DEFAULT_TIME;
    private int speed = DEFAULT_SPEED;
    private Motor.StopMode stopMode = Motor.StopMode.COAST;

    Tank(Brick brick) { this.brick = brick; }

    public Goable forward() {
        Tank.this.actionType = ActionType.FORWARD;
        return new Goable();
    }

    public Goable forward(int speed) { // speed must be 0..100
        // check bounds
        if(speed < 0) {
            brick.warning("Speed can not be < 0: " + speed);
            speed = 0;
        }
        if(speed > +100) {
            brick.warning("Speed can not be > +100: " + speed);
            speed = +100;
        }
        Tank.this.actionType = ActionType.FORWARD;
        Tank.this.speed = speed;
        return new Goable();
    }

    public Goable backward() {
        Tank.this.actionType = ActionType.BACKWARD;
        return new Goable();
    }

    public Goable backward(int speed) { // speed must be 0..100
        // check bounds
        if(speed < 0) {
            brick.warning("Speed can not be < 0: " + speed);
            speed = 0;
        }
        if(speed > +100) {
            brick.warning("Speed can not be > +100: " + speed);
            speed = +100;
        }
        Tank.this.actionType = ActionType.BACKWARD;
        Tank.this.speed = speed;
        return new Goable();
    }

    public Goable turnLeft() {
        Tank.this.actionType = ActionType.LEFT;
        return new Goable();
    }

    public Goable turnLeft(int time) { // time must be 0..2000
        // check bounds
        if(time < 0) {
            brick.warning("Time can not be < 0: " + time);
            time = 0;
        }
        if(time > +2000) {
            brick.warning("Time can not be > +2000: " + time);
            time = 2000;
        }
        Tank.this.actionType = ActionType.LEFT;
        this.time = time;
        return new Goable();
    }

    public Goable turnRight() {
        Tank.this.actionType = ActionType.RIGHT;
        return new Goable();
    }

    public Goable turnRight(int time) { // time must be 0..2000
        // check bounds
        if(time < 0) {
            brick.warning("Time can not be < 0: " + time);
            time = 0;
        }
        if(time > +2000) {
            brick.warning("Time can not be > +2000: " + time);
            time = 2000;
        }
        Tank.this.actionType = ActionType.RIGHT;
        this.time = time;
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
                case FORWARD:
                    brick.motorTankForward(speed);
                    break;
                case BACKWARD:
                    brick.motorTankBackward(speed);
                    break;
                case LEFT:
                    brick.motorTankLeft(time);
                    break;
                case RIGHT:
                    brick.motorTankRight(time);
                    break;
                case STOP:
                    brick.motorTankStop(stopMode == Motor.StopMode.BRAKE);
                    break;
            }
        }
    }
}