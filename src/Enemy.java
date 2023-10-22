import bagel.*;
import java.util.Random;

public class Enemy {

    private final Image enemy = new Image("res/enemy.PNG");
    private static final int X_MIN = 100;
    private static final int X_MAX = 900;
    private static final int Y_MIN = 100;
    private static final int Y_MAX = 500;
    private static final int DIRECTION_MIN = 0;
    private static final int DIRECTION_MAX = 1;
    private int enemyPosX;
    private final int enemyPosY;
    private int enemyDirection;
    private boolean active = false;

    public Enemy(){
        this.enemyPosX = generateRandomPosition(X_MIN,X_MAX);
        this.enemyPosY = generateRandomPosition(Y_MIN,Y_MAX);
        this.enemyDirection = generateDirection(DIRECTION_MIN,DIRECTION_MAX);
    }

    /**
     * generates the random enemy position between two values
     * @param min is the first parameter containing the lower bound value
     * @param max is the second parameter containing the upper bound value
     * @return int returns the new random position value
     */
    public int generateRandomPosition(int min, int max){
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * generates the enemy direction
     * @param min is the first parameter containing the first choice
     * @param max is the second parameter containing the second choice
     * @return int returns the new random direction value
     */
    public int generateDirection(int min,int max){
        Random random = new Random();
        if(random.nextInt((max - min) + 1) + min == 0){
            return -1;
        }
        return 1;
    }

    /**
     * update the enemy x position and changes direction
     * if it reaches the min and max values
     */

    public void update() {

        if (active) {
            if(enemyPosX >= X_MAX || enemyPosX <= X_MIN){
                enemyDirection *= -1;
            }
            enemyPosX += enemyDirection;
        }
    }

    /**
     * draws the enemy
     */
    public void draw(){

        if (active) {
            enemy.draw(enemyPosX, enemyPosY);
        }
    }

    /**
     * activates the enemy
     */
    public void activateEnemy(){
        this.active = true;
    }

    /**
     * deactivates the enemy
     */
    public void destroyEnemy(){
        this.active = false;
    }

    /**
     * returns the enemy's current active status
     */
    public boolean isActive() {
        return active;
    }

    /**
     * gets the enemy's current x value
     */
    public int getEnemyPosX(){
        return this.enemyPosX;
    }

    /**
     * gets the enemy's current y value
     */
    public int getEnemyPosY(){
        return this.enemyPosY;
    }

    /** Checks if an arrow collides with an enemy and deactivates the arrow accordingly
     * @param collision is the first parameter is an object that can determine collisions
     * @param arrows is the second parameter containing an array of Projectile objects
     * @param numArrows is the third parameter is the number of Projectile objects in the arrows array
     * @return boolean if enemy and arrow successfully collide
     */
    public boolean checkEnemyCollision(Collision collision, Projectile[] arrows, int numArrows){

        if(numArrows >0){
            for (int i = 0; i < numArrows; i++) {
                if(collision.evaluateArrowEnemyCollision(enemyPosX, enemyPosY,arrows[i].getArrowPosX(), arrows[i].getArrowPosY())){
                    arrows[i].destroyArrow();
                    return true;
                }
            }
        }
        return false;
    }

}
