package nl.drewez.gyrosnek;

public enum Direction {
    Up,
    Right,
    Down,
    Left;

    @Override
    public String toString() {
        switch (this) {
            case Up:
                return "Up";
            case Right:
                return "Right";
            case Down:
                return "Down";
            case Left:
                return "Left";
            default:
                throw new IllegalArgumentException();
        }
    }
}
