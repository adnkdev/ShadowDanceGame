import bagel.*;
import java.util.*;

public class Projectile {
    private final Image arrow = new Image("res/arrow.PNG");
    private static final double ARROW_X = 800;
    private static final double ARROW_Y = 600;
    private static final int ARROW_SPEED = -6;
    private double arrowPosX;
    private double arrowPosY;
    private boolean active = false;
    private final double ARROW_ANGLE;
    private final static double DEFAULT_ANGLE = 4.71239;

    public Projectile(Enemy[] enemies, int numEnemies){
        this.arrowPosX = ARROW_X;
        this.arrowPosY = ARROW_Y;
        this.ARROW_ANGLE = closestEnemyAngle(enemies,numEnemies);

    }

    /**
     * updates the movement of the arrow
     */
    public void update() {

        if(arrowPosX <= 0 || arrowPosY <= 0|| arrowPosX >= Window.getWidth() || arrowPosY >= Window.getHeight()){
            destroyArrow();
        }

        if (active) {
            arrowPosX += (ARROW_SPEED * Math.cos(ARROW_ANGLE));
            arrowPosY += (ARROW_SPEED * Math.sin(ARROW_ANGLE));

        }
    }

    /** determines the closest enemy and return the radian angle
     * @param enemies is the first parameter containing an array of Enemy objects
     * @param numEnemies is the second parameter is the number of Enemy objects in the enemy array
     * @return double returns the radian angle
     */
    public double closestEnemyAngle(Enemy[] enemies, int numEnemies){

        if(numEnemies>0){

            double closestDistance = 0;
            int closestEnemyIndex = 0;

            for(int i=0; i<numEnemies; i++){
                double currentDistance = distance(ARROW_X,ARROW_Y,enemies[i].getEnemyPosX(),enemies[i].getEnemyPosY());
                if(enemies[i].isActive() && currentDistance <= closestDistance){
                        closestDistance = currentDistance;
                        closestEnemyIndex = i;

                };
            }
            return calculateRadian(ARROW_X,ARROW_Y,enemies[closestEnemyIndex].getEnemyPosX(),enemies[closestEnemyIndex].getEnemyPosY());
        }
        return DEFAULT_ANGLE;
    }

    /** calculates the distance between two objects
     * @param Object1_X is the first parameter of the first object x value
     * @param Object1_Y is the second parameter of the first object y value
     * @param Object2_X is the third parameter of the second object x value
     * @param Object2_Y is the four parameter of the second object y value
     * @return double returns the distance
     */
    public double distance(double Object1_X, double Object1_Y,double Object2_X,  double Object2_Y){
        return (Math.abs(Math.sqrt(Math.pow(Object1_X - Object2_X,2)+Math.pow(Object1_Y-Object2_Y,2))));
    }

    /** calculates the radian angle between two objects
     * @param x1 is the first parameter of the first object x value
     * @param y1 is the second parameter of the first object y value
     * @param x2 is the third parameter of the second object x value
     * @param y2 is the four parameter of the second object y value
     * @return double returns the radians
     */
    public double calculateRadian(double x1, double y1, double x2, double y2) {
        return Math.atan2(Math.abs((y2-y1)),Math.abs(((x2-x1))));
    }

    /**
     * draws the arrow at the set rotation
     */
    public void draw(){
        DrawOptions angle = new DrawOptions();
        if (active) {
            arrow.draw(arrowPosX, arrowPosY, angle.setRotation(ARROW_ANGLE));
        }
    }

    /**
     * make arrow active
     */
    public void activateArrow(){
        active = true;
    }

    /**
     * check if arrow active status
     */
    public boolean isActive() {
        return active;
    }

    /**
     * deactivates the arrow
     */
    public void destroyArrow(){ active = false;}

    /**
     * gets the arrow current x coordinate
     */
    public double getArrowPosX(){
        return arrowPosX;
    }

    /**
     * gets the arrow current y coordinate
     */
    public double getArrowPosY(){
        return arrowPosY;
    }

}
