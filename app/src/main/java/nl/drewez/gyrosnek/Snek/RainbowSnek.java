package nl.drewez.gyrosnek.Snek;

import android.content.Context;
import android.graphics.Point;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.RainbowSnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.SnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.SnekPartType;
import nl.drewez.gyrosnek.SnekFood.CheeseBurger;
import nl.drewez.gyrosnek.SnekFood.HotDog;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;
import nl.drewez.gyrosnek.SnekFood.Pizza;

public class RainbowSnek extends Snek implements ISnek {
    protected double multiplier = 1.2;
    protected Class snekPartType = RainbowSnekPart.class;

    private static int moves = 0;
    private int maxMoves = 50;

    public RainbowSnek(Context context) {
        super(context);
    }

    public RainbowSnek(Context context, ISnekPart[] currentSnekParts, Score currentScore) {
        super(context, currentSnekParts, currentScore);
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
