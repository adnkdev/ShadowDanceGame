import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Subclass for bomb notes
 */
public class BombNote extends Note{
    public BombNote(String dir, int appearanceFrame) {
        super(appearanceFrame);
        Image image = new Image("res/note" + dir + ".PNG");
        setImageNote(image);
        setY(100);
    }

    /** Checks if a bomb note is pressed and deactivates the note accordingly,
     * @param input is the first parameter for detecting key presses
     * @param accuracy is the second parameter contains the object that check for key presses
     * @param targetHeight is the third parameter contains the target stationary point y coordinate
     * @param relevantKey is the fourth parameter that contains the bomb note keyboard input
     * @return boolean returns true if checkBomb is pressed, otherwise false
     */

    public boolean checkBomb(Input input, SpecialNoteAccuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {

            if(accuracy.evaluateBomb(y, targetHeight, input.wasPressed(relevantKey)) == 1){
                deactivate();
                return true;
            }

            if(accuracy.evaluateBomb(y, targetHeight, input.wasPressed(relevantKey)) == 2){
                deactivate();
                return false;
            }

        }
        return false;
    }
}
