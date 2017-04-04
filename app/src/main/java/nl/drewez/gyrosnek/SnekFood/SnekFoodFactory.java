package nl.drewez.gyrosnek.SnekFood;

import android.content.Context;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;

import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.ISnek;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;


public class SnekFoodFactory implements ISnekFoodFactory {
    private static final Random random = new Random();
    private static final int MinNoOfFoods = 3;
    private static final int MaxNoOfFoods = 8;

    /**
     * Creates a random food object
     * @param  currentFood ,returns food in the array
     * @param  snek ,checks the posiition of the snek
     * @param  context ,the context of the view to retreive the int of the xml
     * @return return new food object  
     */
    @Override
    public ISnekFood[] createSnekBar(ISnekFood[] currentFood, ISnek snek, Context context) {

        //generate random ammount of food between the limits
        int noOfFoods = random.nextInt(MaxNoOfFoods - MinNoOfFoods) + MinNoOfFoods;


        ISnekFood[] snekBar = new ISnekFood[noOfFoods];

        //checks if the spot is available to spawn food
        ArrayList<Point> unavailablePixels = getUnavailablePixels(currentFood, snek);
        ArrayList<Point> availablePixels = getAvailablePixels(unavailablePixels, context);

        //spawns random types of food and adds these to the array
        for (int i = 0; i < noOfFoods; i++) {
            int snekType = random.nextInt(3);
            Point pixel = availablePixels.get(random.nextInt(availablePixels.size()));

            switch (snekType) {
                case 0:
                    snekBar[i] = new CheeseBurger(pixel.x, pixel.y);
                    break;
                case 1:
                    snekBar[i] = new HotDog(pixel.x, pixel.y);
                    break;
                case 2:
                    snekBar[i] = new Pizza(pixel.x, pixel.y);
                    break;
            }
        }

        return snekBar;
    }

    /**
     * method to return the unavailable grid spots
     * @param  currentFood ,checks spot for food
     * @param  snek ,checks spot for sneks
     * @return return the unavailable grid spots 
     */
    private ArrayList<Point> getUnavailablePixels(ISnekFood[] currentFood, ISnek snek) {
        ArrayList<Point> unavailable = new ArrayList<>();

        for (ISnekFood food : currentFood) {
            Point foodPoint = new Point(food.getX(), food.getY());
            unavailable.add(unavailable.size(), foodPoint);
        }

        for (ISnekPart snekPart : snek.getSnekParts()) {
            Point snekPoint = new Point(snekPart.getX(), snekPart.getY());
            unavailable.add(unavailable.size(), snekPoint);
        }

        return unavailable;
    }

    /**
     * method to return the available grid spots
     * @param  currentFood ,checks spot for food
     * @param  snek ,checks spot for sneks
     * @return return the unavailable grid spots 
     */
    private ArrayList<Point> getAvailablePixels(ArrayList<Point> unavailablePoints, Context context) {
        int rows = context.getResources().getInteger(R.integer.rows);
        int cols = context.getResources().getInteger(R.integer.cols);

        ArrayList<Point> availablePoints = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Point current = new Point(col, row);

                if (!unavailablePoints.contains(current)) {
                    availablePoints.add(current);
                }
            }
        }

        return availablePoints;
    }
}
