package nl.drewez.gyrosnek.Snek;

import android.content.Context;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.SnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.SnekPartType;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;

public class Snek implements ISnek {
    private int multiplier;
    private ISnekPart[] snekParts;
    private Score score;

    public Snek(Context context) {
        this.multiplier = 1;

        int startX = context.getResources().getInteger(R.integer.init_start_x);
        int endX = context.getResources().getInteger(R.integer.init_end_x);
        int row = context.getResources().getInteger(R.integer.init_y);

        int initLength = Math.abs(startX - endX);
        this.snekParts = new ISnekPart[initLength];



        this.snekParts[0] = new SnekPart(SnekPartType.Head, startX, row);

        for (int i = 1; i < initLength-1; i++) {
            this.snekParts[i] = new SnekPart(SnekPartType.Middle, startX + i, row);
        }

        this.snekParts[initLength - 1] = new SnekPart(SnekPartType.Tail, endX, row);

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
