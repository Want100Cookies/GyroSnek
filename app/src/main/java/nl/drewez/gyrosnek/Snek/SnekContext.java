package nl.drewez.gyrosnek.Snek;

public class SnekContext {
    private ISnek currentSnek;

    public SnekContext(ISnek snek) {
        this.currentSnek = snek;
    }

    public ISnek getSnek() {
        return this.currentSnek;
    }

    public void setSnek(ISnek snek) {
        this.currentSnek = snek;
    }
}
