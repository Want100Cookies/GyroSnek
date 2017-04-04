package nl.drewez.gyrosnek.Snek;

public class SnekContext {
    private ISnek currentSnek;

    /**
     * Initialize the snekContext with snek
     *
     * @param snek The snek used for initialization
     */
    public SnekContext(ISnek snek) {
        this.currentSnek = snek;
    }

    /**
     * @return the current snek
     */
    public ISnek getSnek() {
        return this.currentSnek;
    }

    /**
     * @param snek the snek to be set to currentSnek
     */
    public void setSnek(ISnek snek) {
        this.currentSnek = snek;
    }
}
