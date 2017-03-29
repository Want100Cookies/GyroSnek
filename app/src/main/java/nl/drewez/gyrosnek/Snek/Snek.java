package nl.drewez.gyrosnek.Snek;

import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;

import java.lang.reflect.InvocationTargetException;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.SnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.SnekPartType;
import nl.drewez.gyrosnek.SnekFood.CheeseBurger;
import nl.drewez.gyrosnek.SnekFood.HotDog;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;
import nl.drewez.gyrosnek.SnekFood.Pizza;

public class Snek implements ISnek {
    protected double multiplier = 1;
    protected ISnekPart[] snekParts;
    protected Score score = new Score();
    protected Context viewContext;
    protected Class snekPartType = SnekPart.class;

    public Snek(Context context) {
        this.score.setScore(0);

        this.viewContext = context;

        int startX = context.getResources().getInteger(R.integer.init_start_x);
        int endX = context.getResources().getInteger(R.integer.init_end_x);
        int row = context.getResources().getInteger(R.integer.init_y);

        int initLength = Math.abs(startX - endX);
        this.snekParts = new ISnekPart[initLength];


        this.snekParts[0] = this.createSnekPart(SnekPartType.Head, startX, row);

        for (int i = 1; i < initLength - 1; i++) {
            this.snekParts[i] = this.createSnekPart(SnekPartType.Middle, startX + i, row);
        }

        this.snekParts[initLength - 1] = this.createSnekPart(SnekPartType.Tail, endX, row);
    }

    public Snek(Context context, ISnekPart[] currentSnekParts, Score currentScore) {
        this.score = currentScore;
        this.viewContext = context;
        this.snekParts = new ISnekPart[currentSnekParts.length];

        this.snekParts[0] = this.createSnekPart(
                SnekPartType.Head,
                currentSnekParts[0].getX(),
                currentSnekParts[0].getY());

        for (int i = 1; i < currentSnekParts.length - 1; i++) {
            this.snekParts[i] = this.createSnekPart(
                    SnekPartType.Middle,
                    currentSnekParts[i].getX(),
                    currentSnekParts[i].getY());
        }

        this.snekParts[currentSnekParts.length - 1] = this.createSnekPart(
                SnekPartType.Head,
                currentSnekParts[currentSnekParts.length - 1].getX(),
                currentSnekParts[currentSnekParts.length - 1].getY());
    }

    @Override
    public boolean move(Direction direction, ISnekFood[] snekBar, SnekContext snekContext) {
        Point currentPixel = new Point(this.snekParts[0].getX(), this.snekParts[0].getY());
        int maxY = this.viewContext.getResources().getInteger(R.integer.rows);
        int maxX = this.viewContext.getResources().getInteger(R.integer.cols);

        switch (direction) {
            case Up:
                if ((currentPixel.y--) < 0) {
                    // Can't move outside grid
                    return false;
                }
                break;
            case Right:
                if ((currentPixel.x++) > maxX) {
                    return false;
                }
                break;
            case Down:
                if ((currentPixel.y++) > maxY) {
                    return false;
                }
                break;
            case Left:
                if ((currentPixel.x--) < 0) {
                    return false;
                }
                break;
        }


        // Remove last snekPart
        ISnekPart[] newParts = new ISnekPart[this.snekParts.length];

        for (int i = 0; i < this.snekParts.length; i++) {
            // Todo: test this, not sure it works
            newParts[(i + 1) % (this.snekParts.length - 1)] = this.snekParts[i];
        }

        newParts[0] = this.createSnekPart(
                SnekPartType.Head,
                currentPixel.x,
                currentPixel.y
        );
        newParts[1] = this.createSnekPart(
                SnekPartType.Middle,
                newParts[1].getX(),
                newParts[1].getY()
        );
        newParts[this.snekParts.length - 1] = this.createSnekPart(
                SnekPartType.Tail,
                newParts[this.snekParts.length - 1].getX(),
                newParts[this.snekParts.length - 1].getY()
        );

        this.snekParts = newParts;

        for (int i = 1; i < this.snekParts.length; i++) {
            if (this.snekParts[i].getX() == this.snekParts[0].getX()
                    && this.snekParts[i].getY() == this.snekParts[0].getY()) {
                return false;
            }
        }

        // Calculate score
        ISnekFood snekFood = getSnekFood(snekBar, currentPixel.x, currentPixel.y);

        if (snekFood != null) {
            this.score.setScore(this.score.getScore() + (int) ((double) snekFood.getScore() * this.multiplier));

            if (snekContext.getSnek() instanceof Snek) { // Only change state if current state is Snek
                if (snekFood instanceof HotDog) {
                    // Todo: give current snekParts and score to next snek
                    snekContext.setSnek(new InvisibleSnek(this.viewContext, this.snekParts, this.score));
                }

                if (snekFood instanceof Pizza) {
                    snekContext.setSnek(new RainbowSnek(this.viewContext, this.snekParts, this.score));
                }

                if (snekFood instanceof CheeseBurger) {
                    snekContext.setSnek(new SpeedSnek(this.viewContext, this.snekParts, this.score)); // Kek, cheezburger makes fast snek
                }
            }
        }


        return true;
    }

    @Override
    public Score getScore() {
        return this.score;
    }

    @Override
    public ISnekPart[] getSnekParts() {
        return this.snekParts;
    }

    protected ISnekFood getSnekFood(ISnekFood[] snekBar, int x, int y) {
        for (ISnekFood snekFood : snekBar) {
            if (snekFood.getX() == x && snekFood.getY() == y) {
                return snekFood;
            }
        }

        return null;
    }

    protected ISnekPart createSnekPart(SnekPartType type, int x, int y) {
        try {
            return (ISnekPart) this.snekPartType
                    .getConstructor(SnekPartType.class, int.class, int.class)
                    .newInstance(type, x, y);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }
}
