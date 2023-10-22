import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Subclass for normal notes
 */

public class NormalNote extends Note {

    public NormalNote(String dir, int appearanceFrame) {
        super(appearanceFrame);
        Image image = new Image("res/note" + dir + ".png");
        setImageNote(image);
        setY(100);
    }

    /** Calculates score when normal note is pressed and deactivates the note accordingly,
     * @param input is the first parameter for detecting key presses
     * @param accuracy is the second parameter contains the object that can calculate the score
     * @param targetHeight is the third parameter contains the target stationary point y coordinate
     * @param relevantKey is the fourth parameter that contains the normal note keyboard input
     * @return int returns the score if normal note pressed
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            int score = accuracy.evaluateScore(y, targetHeight, input.wasPressed(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            }
        }
        return 0;
    }


    /** Checks if a bomb note is pressed and deactivates the note accordingly,
     * @param collision is the first parameter is an object that can determine collisions
     * @param enemies is the second parameter containing an array of Enemy objects
     * @param numEnemies is the third parameter is the number of Enemy objects in the enemy array
     */
    public void checkCollision(Collision collision, Enemy[] enemies, int numEnemies) {
        if (numEnemies > 0) {
            for(int i=0; i<numEnemies; i++){
                if(enemies[i].isActive()){
                    if(collision.evaluateNoteEnemyCollision(x,y,enemies[i].getEnemyPosX(), enemies[i].getEnemyPosY())){
                        deactivate();
                    }
                }
            }
        }
    }

}
