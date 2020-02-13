package robutev3.core;

import java.util.Objects;

/**
 * @author Nearchos
 * Created: 13-Feb-20
 */
public class Speed implements Comparable<Speed> {

    public static final int MIN = -100;
    public static final int MAX = 100;

    private final int speed;

    private Speed(final int speed) {
        if(speed < MIN) {
            this.speed = MIN;
        } else if(speed > MAX) {
            this.speed = MAX;
        } else {
            this.speed = speed;
        }
    }

    public static Speed zero() {
        return new Speed(0);
    }

    public static Speed min() {
        return new Speed(MIN);
    }

    public static Speed max() {
        return new Speed(MAX);
    }

    public static Speed fromValue(final int speed) {
        return new Speed(speed);
    }

    public int getValue() {
        return speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Speed speed1 = (Speed) o;
        return speed == speed1.speed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed);
    }

    @Override
    public int compareTo(Speed other) {
        return this.speed - other.speed;
    }

    @Override
    public String toString() {
        return Integer.toString(speed);
    }
}