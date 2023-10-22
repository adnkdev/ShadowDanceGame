import bagel.Window;

/**
 * Class for dealing with accuracy of pressing the special notes
 */
public class SpecialNoteAccuracy extends Accuracy {

    /**
     * score messages for special notes
     */
    public static final String DOUBLE = "DOUBLED";
    public static final String SPEED_UP = "SPEED UP";
    public static final String SLOW_DOWN = "SLOW DOWN";
    public static final String BOMB_NOTE = "CLEAR";
    private static final int SPECIAL_DISTANCE = 50;
    private static final int DOUBLE_SCORE_RENDER = 480;

    /**
     * score for speed-up and slow-down notes
     */
    public static final int SPECIAL_SCORED = 15;
    private int specialFrameCount = 0;
    private boolean speedUpActive = false;
    private boolean slowDownActive = false;
    private boolean doubleScoreActive = false;
    private int currentMultiplier = 1;

    /** evaluates if speed-up note is pressed within desired distance and returns relevant score
     * @param height is first parameter containing the current y coordinate of the note
     * @param targetHeight is the second parameter containing the target y coordinate in the lane
     * @param triggered is the third parameter that contains boolean if relevant input key was corrected interacted with
     * @return int returns speed-up note score
     */

    public int evaluateSpeedUpScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= SPECIAL_DISTANCE) {
                setAccuracy(SPEED_UP);
                speedUpActive = true;
                slowDownActive = false;
                return SPECIAL_SCORED;
            }
        }else if (height >= (Window.getHeight())) {
            return MISS_SCORE;
        }
        return NOT_SCORED;
    }

    /** evaluates if slow-down note is pressed within desired distance and returns relevant score
     * @param height is first parameter containing the current y coordinate of the note
     * @param targetHeight is the second parameter containing the target y coordinate in the lane
     * @param triggered is the third parameter that contains boolean if relevant input key was corrected interacted with
     * @return int returns slow-down note score
     */

    public int evaluateSlowDownScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= SPECIAL_DISTANCE) {
                setAccuracy(SLOW_DOWN);
                slowDownActive = true;
                speedUpActive = false;
                return SPECIAL_SCORED;
            }
        }else if (height >= (Window.getHeight())) {
            return MISS_SCORE;
        }

        return NOT_SCORED;
    }

    /**
     * evaluates the current speed of game
     * @return int returns current speed
     */

    public int updateSpeed() {
        if(speedUpActive){
            return 1;
        }else if(slowDownActive){
            return -1;
        }
        return 0;
    }

    /** evaluates if double score note is pressed within desired distance and sets the appropriate multiplier
     * @param height is first parameter containing the current y coordinate of the note
     * @param targetHeight is the second parameter containing the target y coordinate in the lane
     * @param triggered is the third parameter that contains boolean if relevant input key was corrected interacted with
     * @return int returns double score note score
     */
    public int evaluateDoubleScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= SPECIAL_DISTANCE) {
                setAccuracy(DOUBLE);
                doubleScoreActive = true;
                currentMultiplier *= 2;
                specialFrameCount = 0;
                return SPECIAL_SCORED;
            }
        }else if (height >= (Window.getHeight())) {
            return MISS_SCORE;
        }
        return NOT_SCORED;
    }

    /**
     * calculates the appropriate multiplier
     * @return int returns the current multiplier
     */
    public int updateDoubleScore() {
        if (specialFrameCount < DOUBLE_SCORE_RENDER && doubleScoreActive) {
            specialFrameCount++;
            return currentMultiplier;
        }
        doubleScoreActive = false;
        currentMultiplier = 1;

        return currentMultiplier;
    }

    /** evaluates if bomb-note is pressed within desired distance
     * @param height is first parameter containing the current y coordinate of the note
     * @param targetHeight is the second parameter containing the target y coordinate in the lane
     * @param triggered is the third parameter that contains boolean if relevant input key was corrected interacted with
     * @return int returns integer relates to score that should be calculated
     */

    public int evaluateBomb(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= SPECIAL_DISTANCE) {
                setAccuracy(BOMB_NOTE);
                return 1;
            }
        }else if (height >= (Window.getHeight())) {
            return 2;
        }
        return 3;
    }

    /**
     * resets the attributes to original values
     */
    public void resetSpecialAccuracy(){
        specialFrameCount = 0;
        speedUpActive = false;
        slowDownActive = false;
        doubleScoreActive = false;
        currentMultiplier = 1;
    }


}
