import bagel.*;

/**
 * Subclass for hold Notes
 */
public class HoldNote extends Note{
    private static final int HEIGHT_OFFSET = 82;
    private boolean holdStarted = false;

    public HoldNote(String dir, int appearanceFrame) {
        super(appearanceFrame);
        Image image = new Image("res/holdNote" + dir + ".PNG");
        setImageNote(image);
        setY(24);
    }

    /**
     * set the attribute holdStarted to true
     */
    public void startHold() {
        holdStarted = true;
    }


    /** Calculates score twice, once at the start of the hold and once at the end
     *  when hold note is pressed and released and deactivates the note accordingly,
     * @param input is the first parameter for detecting key presses
     * @param accuracy is the second parameter contains the object that can calculate the score
     * @param targetHeight is the third parameter contains the target stationary point y coordinate
     * @param relevantKey is the fourth parameter that contains the hold note keyboard input
     * @return int returns the score if hold note pressed
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive() && !holdStarted) {
            int score = accuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(relevantKey));

            if (score == Accuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != Accuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {

            int score = accuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                accuracy.setAccuracy(Accuracy.MISS);
                return Accuracy.MISS_SCORE;
            }
        }

        return 0;
    }


    /**
     * gets the location of the start of the note
     */
    private int getBottomHeight() {
        return y + HEIGHT_OFFSET;
    }

    /**
     * gets the location of the end of the note
     */
    private int getTopHeight() {
        return y - HEIGHT_OFFSET;
    }
}
