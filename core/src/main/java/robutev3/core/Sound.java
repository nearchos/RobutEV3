package robutev3.core;

/**
 * @author Nearchos
 * Created: 08-Dec-19
 */
public class Sound {

    private final Brick brick;

    Sound(final Brick brick) {
        this.brick = brick;
    }

    public void beep() {
        beep(Beepable.DEFAULT_FREQUENCY, Beepable.DEFAULT_DURATION, Beepable.DEFAULT_VOLUME);
    }

    public void beep(final int frequency, final int duration, final int volume) {
        new Beepable(frequency, duration, volume).play();
    }

    public void play(final SoundFile soundFile, final int volume) {
        brick.play(volume, soundFile.getFileName());
    }

    public void stop() {
        brick.playStop();
    }

    class Beepable {

        public static final int DEFAULT_FREQUENCY = 500;
        public static final int DEFAULT_DURATION = 100;
        public static final int DEFAULT_VOLUME = 30;

        private final int frequency; // Hz
        private final int duration; // milliseconds
        private final int volume; // percentage

        Beepable(int frequency, int duration, int volume) {
            // check bounds
            if(frequency < 250) {
                frequency = 250;
                brick.warning("Frequency cannot be < 250: " + frequency);
            }
            if(frequency > 10000) {
                frequency = 10000;
                brick.warning("Frequency cannot be > 10000: " + frequency);
            }
            this.frequency = frequency;

            // check bounds
            if(duration < 0) {
                duration = 0;
                brick.warning("Duration cannot be < 0: " + duration);
            }
            this.duration = duration;

            // check bounds
            if(volume < 0) {
                volume = 0;
                brick.warning("Volume cannot be < 0: " + volume);
            }
            if(volume > 100) {
                volume = 100;
                brick.warning("Volume cannot be > 100: " + volume);
            }
            this.volume = volume;
        }

        int getFrequency() {
            return frequency;
        }

        int getDuration() {
            return duration;
        }

        int getVolume() {
            return volume;
        }

        void play() {
            brick.beep(this);
        }
    }
}