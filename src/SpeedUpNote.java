import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Subclass for speed up notes
 */

public class SpeedUpNote extends Note{

    public SpeedUpNote (String dir, int appearanceFrame) {
        super(appearanceFrame);
        Image image = new Image("res/note" + dir + ".PNG");
        setImageNote(image);
        setY(100);
    }

    /** Calculates score when speed-up note is pressed and deactivates the note accordingly,
     * @param input is the first parameter for detecting key presses
     * @param accuracy is the second parameter contains the object that can calculate the score
     * @param targetHeight is the third parameter contains the target stationary point y coordinate
     * @param relevantKey is the fourth parameter that contains the speed-up note keyboard input
     * @return int returns the score if speed-up note pressed
     */

    public int checkSpecialScore(Input input, SpecialNoteAccuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateSpeedUpScore(y, targetHeight, input.wasPressed(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                if(score == Accuracy.MISS_SCORE){
                    score = Accuracy.NOT_SCORED;
                }
                deactivate();
                return score;
            }
        }
        return 0;
    }



}
