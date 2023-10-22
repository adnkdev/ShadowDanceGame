import bagel.*;


public class Guardian {
    private final Image guardian = new Image("res/guardian.PNG");
    private static final int GUARDIAN_POS_X = 800;
    private static final int GUARDIAN_POS_Y = 600;
    private final Enemy[] enemies = new Enemy[150];
    private final Projectile[] arrows = new Projectile[150];
    private static final int ENEMY_RENDER = 600;
    private int numEnemies = 0;
    private int numArrows = 0;
    private int enemyFrameCount = 0;


    /** Checks if an enemy collides with an arrow and deactivates the enemy accordingly,
     * @param input is the first parameter for detecting key presses
     * @param collision is the second parameter is an object that can determine collisions
     */
    public void update(Input input, Collision collision){

        addEnemy();
        if(input.wasPressed(Keys.LEFT_SHIFT)){
            addArrow();
        }
        checkArrowEnemyCollision(collision,arrows,numArrows);
        draw();

        if(numEnemies>0){
            for(int i=0; i<numEnemies; i++){
                enemies[i].update();
            }
        }

        if(numArrows>0){
            for(int i=0; i<numArrows; i++){
                arrows[i].update();
            }
        }

    }


    /** Checks if an enemy collides with an arrow and deactivates the enemy accordingly,
     * @param collision is the first parameter is an object that can determine collisions
     * @param arrows is the second parameter containing an array of Projectile objects
     * @param numArrows is the third parameter is the number of Projectile objects in the arrows array
     */
    public void checkArrowEnemyCollision(Collision collision, Projectile[] arrows, int numArrows){
        if(numEnemies>0){
            for(int i=0; i<numEnemies; i++){
                if(enemies[i].checkEnemyCollision(collision,arrows,numArrows) && enemies[i].isActive()){
                    enemies[i].destroyEnemy();
                };
            }
        }
    }

    /**
     * draws the guardian,enemies and projectiles
     */
    public void draw() {

        guardian.draw(GUARDIAN_POS_X,GUARDIAN_POS_Y);

        if(numEnemies>0){
            for(int i=0; i<numEnemies; i++){
                enemies[i].draw();
            }
        }

        if(numArrows>0){
            for(int i=0; i<numArrows; i++){
                arrows[i].draw();
            }
        }

    }

    /**
     * adds enemies to an array when
     * frame count reaches enemy render value
     */
    public void addEnemy(){
        enemyFrameCount++;
        if(enemyFrameCount == ENEMY_RENDER){
            Enemy enemy = new Enemy();
            enemies[numEnemies++] = enemy;
            enemy.activateEnemy();
            enemyFrameCount = 0;
        }
    }

    /**
     * adds arrows to an array
     */
    public void addArrow(){
            Projectile arrow = new Projectile(enemies,numEnemies);
            arrows[numArrows++] = arrow;
            arrow.activateArrow();
    }

    /**
     * gets the current enemies array
     */
    public Enemy[] getEnemies(){
        return enemies;
    }

    /**
     * gets the current number of enemies
     */
    public int getNumEnemies() {
        return numEnemies;
    }

    /**
     * resets the enemies and arrows values
     */
    public void reset(){
        numEnemies = 0;
        enemyFrameCount = 0;
        numArrows = 0;
    }
}
