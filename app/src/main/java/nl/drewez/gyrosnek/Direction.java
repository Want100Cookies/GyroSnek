package nl.drewez.gyrosnek;

import android.support.annotation.NonNull;

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

    public Direction getOpposite() {
        switch (this) {
            case Up:
                return Down;
            case Right:
                return Left;
            case Down:
                return Up;
            case Left:
                return Right;
        }

        return null;
    }
}
