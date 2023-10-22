public class Collision {
    private static final int ENEMY_ARROW_DISTANCE = 62;
    private static final int ENEMY_NOTE_DISTANCE = 104;

    /** evaluates if note and enemy collide
     * @param Object1_X is the first parameter of the first object x value
     * @param Object1_Y is the second parameter of the first object y value
     * @param Object2_X is the third parameter of the second object x value
     * @param Object2_Y is the four parameter of the second object y value
     * @return boolean if enemy and note collision occur
     */
    public boolean evaluateNoteEnemyCollision(int Object1_X, int Object1_Y,int Object2_X, int Object2_Y){
        double distance = (Math.abs(Math.sqrt(Math.pow(Object1_X - Object2_X,2)+Math.pow(Object1_Y-Object2_Y,2))));
        if (distance <= ENEMY_NOTE_DISTANCE){
            return true;
        }
        return false;
    }

    /** evaluates if arrow and enemy collide
     * @param Object1_X is the first parameter of the first object x value
     * @param Object1_Y is the second parameter of the first object y value
     * @param Object2_X is the third parameter of the second object x value
     * @param Object2_Y is the four parameter of the second object y value
     * @return boolean if enemy and arrow collision occurs
     */
    public boolean evaluateArrowEnemyCollision(int Object1_X, int Object1_Y,double Object2_X,  double Object2_Y){
        double distance = (Math.abs(Math.sqrt(Math.pow(Object1_X - Object2_X,2)+Math.pow(Object1_Y-Object2_Y,2))));
        if (distance <= ENEMY_ARROW_DISTANCE){
            return true;
        }
        return false;
    }
}
