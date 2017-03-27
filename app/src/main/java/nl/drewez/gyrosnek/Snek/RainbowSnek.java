package nl.drewez.gyrosnek.Snek;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;

public class RainbowSnek implements ISnek {
    private int multiplier;
    private ISnekPart[] snekParts;
    private Score score;

    public RainbowSnek() {

    }

    @Override
    public boolean move(Direction direction, ISnekFood[] snekBar, SnekContext context) {
        return false;
    }

    @Override
    public Score getScore() {
        return null;
    }

    @Override
    public ISnekPart[] getSnekParts() {
        return new ISnekPart[0];
    }
}
