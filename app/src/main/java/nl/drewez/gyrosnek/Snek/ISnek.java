package nl.drewez.gyrosnek.Snek;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;

public interface ISnek {
    boolean move(Direction direction, ISnekFood[] snekBar, SnekContext context);
    Score getScore();
    ISnekPart[] getSnekParts();
}