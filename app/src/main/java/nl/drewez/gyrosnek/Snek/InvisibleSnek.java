package nl.drewez.gyrosnek.Snek;

import android.content.Context;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;

public class InvisibleSnek extends Snek implements ISnek {
    protected static final double multiplier = 1.2;
    protected static final Class snekPartType = InvisibleSnek.class;

    private static int moves = 0;
    private int maxMoves = 15;

    public InvisibleSnek(Context context) {
        super(context);
    }

    public InvisibleSnek(Context context, ISnekPart[] currentSnekParts, Score currentScore) {
        super(context, currentSnekParts, currentScore);
    }

    @Override
    public boolean move(Direction direction, ISnekFood[] snekBar, SnekContext snekContext) {
        boolean ded = super.move(direction, snekBar, snekContext);

        if (moves++ > maxMoves) { // Only have invisible snek for <maxMoves> then go back to normal snek
            snekContext.setSnek(new Snek(super.viewContext, super.snekParts, super.score));
        }

        return ded;
    }
}
