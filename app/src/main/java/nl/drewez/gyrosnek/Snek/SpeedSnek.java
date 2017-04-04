package nl.drewez.gyrosnek.Snek;

import android.content.Context;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.SpeedSnekPart;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;


public class SpeedSnek extends Snek implements ISnek {
    private static int moves = 0;
    private int maxMoves = 50;

    /**
     * Returns an Image object that can then be painted on the screen. 
     * @param  context, context of the current Snek  
     * @param  currentSnekParts, current SnekParts retreived from Snek 
     * @param  currentScore, current score used to calculate the multiplier
     * @return move1, which makes the snek move
     */
    public SpeedSnek(Context context, ISnekPart[] currentSnekParts, Score currentScore) {
        super(context, currentSnekParts, currentScore);

        multiplier = 10;
        snekPartType = SpeedSnekPart.class;
    }

    /**
     * Returns an Image object that can then be painted on the screen. 
     * @param  direction, the direction the Snek can move to 
     * @param  snekBar, retreives info of the current foods
     * @param  snekContext, retreives snekContext to alter the current Snek type
     * @return move1, which makes the snek move
     */
    @Override
    public boolean move(Direction direction, ISnekFood[] snekBar, SnekContext snekContext) {
        boolean ded = super.move(direction, snekBar, snekContext);

        if (moves++ > maxMoves) { // Only have speedsnek for <maxMoves> then go back to normal snek
            snekContext.setSnek(new Snek(super.viewContext, super.snekParts, super.score));
        }

        return ded;
    }
}
