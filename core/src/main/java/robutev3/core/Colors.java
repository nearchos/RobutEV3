package robutev3.core;

/**
 * @author Nearchos
 * Created: 14-Dec-19
 */
public enum Colors {

    UNKNOWN (0),
    BLACK   (1),
    BLUE    (2),
    GREEN   (3),
    YELLOW  (4),
    RED     (5),
    WHITE   (6),
    BROWN   (7);

    private int code;

    Colors(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Colors fromCode(final int code) {
        switch (code) {
            case 0: return UNKNOWN;
            case 1: return BLACK;
            case 2: return BLUE;
            case 3: return GREEN;
            case 4: return YELLOW;
            case 5: return RED;
            case 6: return WHITE;
            case 7: return BROWN;
            default: return BLACK;
        }
    }
}