package nl.drewez.gyrosnek.Snek;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;

public interface ISnek {
    /**
     * Move the snek in the given direction (if possible) and do all actions needed
     *
     * @param direction   The direction the snek has to move
     * @param snekBar     The food the snek can eat if he encounters it
     * @param snekContext The context of the current snek (used for state-switching)
     * @return boolean False if the snek can not move due to constraints (off screen or self eating)
     */
    boolean move(Direction direction, ISnekFood[] snekBar, SnekContext snekContext);

    /**
     * @return Score get the score of this snek
     */
    Score getScore();

    /**
     * @return ISnekPart[] get the snekParts associated with this snek
     */
    ISnekPart[] getSnekParts();
}