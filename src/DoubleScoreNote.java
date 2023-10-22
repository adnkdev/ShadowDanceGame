import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Subclass for double score notes
 */
public class DoubleScoreNote extends Note{

    public DoubleScoreNote(String dir, int appearanceFrame) {
        super(appearanceFrame);
        Image image = new Image("res/note" + dir + ".PNG");
        setImageNote(image);
        setY(100);
    }

    /** Calculates score when double score note is pressed and deactivates the note accordingly,
     * @param input is the first parameter for detecting key presses
     * @param accuracy is the second parameter contains the object that can calculate the score
     * @param targetHeight is the third parameter contains the target stationary point y coordinate
     * @param relevantKey is the fourth parameter that contains the double score note keyboard input
     * @return int returns the score if double score note pressed
     */

    public int checkSpecialScore(Input input, SpecialNoteAccuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateDoubleScore(y, targetHeight, input.wasPressed(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return 0;
            }
        }
        return 0;
    }
}
