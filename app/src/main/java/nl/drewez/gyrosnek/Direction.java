package nl.drewez.gyrosnek;

import android.support.annotation.NonNull;

public enum Direction {
    Up,
    Right,
    Down,
    Left;

    /**
    * returns direction in string
    * @return string of direction
    */
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

    /**
    * returns opposite direction
    * @return opposite direction
    */
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
