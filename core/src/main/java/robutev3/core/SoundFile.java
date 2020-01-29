package robutev3.core;

/**
 * @author Nearchos
 * Created: 21-Dec-19
 */
public enum SoundFile {

    ZERO("Zero"),
    ONE("One"),
    TWO("Two"),
    THREE("Three"),
    FOUR("Four"),
    FIVE("Five"),
    SIX("Six"),
    SEVEN("Seven"),
    EIGHT("Eight"),
    NINE("Nine"),
    TEN("Ten"),
    ;

    private final String fileName;

    SoundFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}