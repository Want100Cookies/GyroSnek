package nl.drewez.gyrosnek.Snek;

import android.content.Context;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.RainbowSnekPart;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;

public class RainbowSnek extends Snek implements ISnek {
    private static int moves = 0;
    private int maxMoves = 50;

    public RainbowSnek(Context context, ISnekPart[] currentSnekParts, Score currentScore) {
        super(context, currentSnekParts, currentScore);

        multiplier = 5;
        snekPartType = RainbowSnekPart.class;
    }

    @Override
    public boolean move(Direction direction, ISnekFood[] snekBar, SnekContext snekContext) {
        boolean ded = super.move(direction, snekBar, snekContext);

        if (moves++ > maxMoves) { // Only have rainbow snek for <maxMoves> then go back to normal snek
            snekContext.setSnek(new Snek(super.viewContext, super.snekParts, super.score));
        }

        return ded;
    }
}
