package robutev3.core;

/**
 * @author Nearchos
 * Created: 13-Feb-20
 */
public class Interval {

    public static final int MIN = 0;

    private final int milliseconds;

    private Interval(final int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public static Interval milliseconds(final int milliseconds) {
        return new Interval(milliseconds);
    }

    public static Interval seconds(final int seconds) {
        return new Interval(1000 * seconds);
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    @Override
    public String toString() {
        return milliseconds + " ms";
    }
}