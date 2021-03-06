package nl.drewez.gyrosnek.Snek;

import android.content.Context;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.InvisibleSnekPart;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;

public class InvisibleSnek extends Snek implements ISnek {
    private static int moves = 0;
    private int maxMoves = 15;

    /**
     * Setup default snek parameters and change multiplier and snekPartType defaults
     *
     * @param context          the GameView context used in the Snek constructor
     * @param currentSnekParts the snekParts used for the previous snek. These are used to init this snek
     * @param currentScore     the current score of the game
     */
    public InvisibleSnek(Context context, ISnekPart[] currentSnekParts, Score currentScore) {
        super(context, currentSnekParts, currentScore);

        multiplier = 2;
        snekPartType = InvisibleSnekPart.class;
    }

    /**
     * Move InvisibleSnek for maxMoves
     *
     * @param direction   The direction the snek has to move
     * @param snekBar     The food the snek can eat if he encounters it
     * @param snekContext The context of the current snek (used for state-switching)
     * @return boolean Is false if this snek can not move.
     */
    @Override
    public boolean move(Direction direction, ISnekFood[] snekBar, SnekContext snekContext) {
        boolean ded = super.move(direction, snekBar, snekContext);

        if (moves++ > maxMoves) { // Only have invisible snek for <maxMoves> then go back to normal snek
            snekContext.setSnek(new Snek(super.viewContext, super.snekParts, super.score));
        }

        return ded;
    }
}
