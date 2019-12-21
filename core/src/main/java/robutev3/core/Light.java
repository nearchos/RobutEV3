package robutev3.core;

import java.util.Random;

/**
 * @author Nearchos
 * Created: 07-Dec-19
 */
public class Light {

    public enum Color { NONE, GREEN, RED, ORANGE }

    public enum Type { ON, OFF, FLASH, PULSE }

    private Brick brick;
    private Color color = Color.NONE;
    private Type type = Type.OFF;

    Light(final Brick brick) {
        this.brick = brick;
    }

    public void off() {
        new LightTypeSelector().off();
    }

    public void on(final Color color) {
        this.color = color;
        brick.light(this);
    }

    public void on(final Color color, final Type type) {
        this.color = color;
        this.type = type;
        brick.light(this);
    }

    private final Random random = new Random();

    public void randomColor() {
        final Color [] ALL_COLORS = Color.values();
        this.color = ALL_COLORS[random.nextInt(ALL_COLORS.length)];
        this.type = Type.ON;
        brick.light(this);
    }

    public LightTypeSelector green() {
        this.color = Color.GREEN;
        return new LightTypeSelector();
    }

    public LightTypeSelector red() {
        this.color = Color.RED;
        return new LightTypeSelector();
    }

    public LightTypeSelector orange() {
        this.color = Color.ORANGE;
        return new LightTypeSelector();
    }

    public class LightTypeSelector {

        LightTypeSelector() {
        }

        public void on() {
            Light.this.type = Type.ON;
            brick.light(Light.this);
        }

        void off() {
            Light.this.type = Type.OFF;
            brick.light(Light.this);
        }

        public void flash() {
            Light.this.type = Type.FLASH;
            brick.light(Light.this);
        }

        public void pulse() {
            Light.this.type = Type.PULSE;
            brick.light(Light.this);
        }
    }

    byte getCode() {
        switch (type) {
            case OFF:
                return (byte) 0x00;
            case ON:
                switch (color) {
                    case GREEN:     return (byte) 0x01;
                    case RED:       return (byte) 0x02;
                    case ORANGE:    return (byte) 0x03;
                }
            case FLASH:
                switch (color) {
                    case GREEN:     return (byte) 0x04;
                    case RED:       return (byte) 0x05;
                    case ORANGE:    return (byte) 0x06;
                }
            case PULSE:
                switch (color) {
                    case GREEN:     return (byte) 0x07;
                    case RED:       return (byte) 0x08;
                    case ORANGE:    return (byte) 0x09;
                }
            default:
                return (byte) 0x00;
        }
    }
}