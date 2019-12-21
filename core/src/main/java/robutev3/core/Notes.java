package robutev3.core;

/**
 * @author Nearchos
 * Created: 20-Dec-19
 */
public enum Notes {

    DO(523),
    RE(587),
    MI(659),
    FA(698),
    SOL(784),
    LA(880),
    TI(988),
    DO_P(1047)
    ;

    private int frequency;

    private Notes(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }
}