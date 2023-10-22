import bagel.*;

/**
 * Class for the lanes which notes fall down
 */
public class Lane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    private final String type;
    private final Image image;
    private final Note[] notes = new Note[120];
    private int numNotes = 0;
    private Keys relevantKey;
    private final int location;
    private int currNote = 0;
    private static final int LEVEL_1_SPEED = 0;

    public Lane(String dir, int location) {
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
            case "Special":
                relevantKey = Keys.SPACE;
                break;
        }
    }

    /**
     * gets the lane type
     * @return String is the name of the lane
     */

    public String getType() {
        return type;
    }

    /** draw and updates normal and hold notes in level 1
     * @param input is the first parameter for detecting key presses
     * @param accuracy is the second parameter contains the object that can calculate the score
     * @return int returns the score
     */
    public int updateLvl1(Input input, Accuracy accuracy) {
        draw();

        for (int i = currNote; i < numNotes; i++) {
            notes[i].update(LEVEL_1_SPEED);
        }

        if (currNote < numNotes) {
            int score = notes[currNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (notes[currNote].isCompleted()) {
                currNote++;
            }
            return score;
        }

        return Accuracy.NOT_SCORED;
    }

    /** draw and updates level1, speed-up, slow-down, bomb and double score notes in level 2
     * @param input is the first parameter for detecting key presses
     * @param accuracy is the second parameter contains the object that can calculate the score
     * @return int returns the score
     */

    public int updateLvl2(Input input, SpecialNoteAccuracy accuracy) {
        draw();
        int multiplier = accuracy.updateDoubleScore();

        for (int i = currNote; i < numNotes; i++) {
            notes[i].update(accuracy.updateSpeed());
        }

        if (currNote < numNotes) {

            int score = 0;

            if(notes[currNote] instanceof NormalNote || notes[currNote] instanceof HoldNote ){
                score = notes[currNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            }

            if(notes[currNote] instanceof SpeedUpNote || notes[currNote] instanceof SlowDownNote){
                score = notes[currNote].checkSpecialScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            }

            if (notes[currNote] instanceof DoubleScoreNote){
                score = notes[currNote].checkSpecialScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            }

            if (notes[currNote] instanceof BombNote){
                if(notes[currNote].checkBomb(input, accuracy, TARGET_HEIGHT, relevantKey)){
                    currNote += deactivateLaneNotes();
                }
            }

            if (notes[currNote].isCompleted()) {
                currNote++;
            }

            return multiplier * score;
        }

        return Accuracy.NOT_SCORED;
    }

    /**
     * deactivates all the notes in the lane
     * return int returns the number to increment currNote value by
     */
    public int deactivateLaneNotes(){
        int incrementCurrNote = 0;
        for (int i = currNote; i < numNotes; i++) {
            if(notes[i].deactivateNote()){
                incrementCurrNote++;
            }
        }
        return incrementCurrNote;
    }

    /** draw and updates level 2 notes and detects enemy collisions in level 3
     * @param input is the first parameter for detecting key presses
     * @param accuracy is the second parameter contains the object that can calculate the score
     * @param collision is the third parameter is an object that can determine collisions
     * @param enemies is the fourth parameter containing an array of Enemy objects
     * @param numEnemies is the fifth parameter is the number of Enemy objects in the enemy array
     * @return int returns the score
     */
    public int updateLvl3(Input input, SpecialNoteAccuracy accuracy,  Collision collision, Enemy[] enemies, int numEnemies) {

        checkNoteEnemyCollision(collision,enemies,numEnemies);
        return updateLvl2(input,accuracy);
    }

    /** Checks if a note collides with an enemy and deactivates the note accordingly,
     * @param collision is the first parameter is an object that can determine collisions
     * @param enemies is the second parameter containing an array of Enemy objects
     * @param numEnemies is the third parameter is the number of Enemy objects in the enemy array
     */
    public void checkNoteEnemyCollision(Collision collision, Enemy[] enemies, int numEnemies){

        for (int i = currNote; i < numNotes; i++) {
            if(notes[i] instanceof NormalNote && notes[i].isActive()){
                ((NormalNote) notes[i]).checkCollision(collision, enemies, numEnemies);
            }
        }
    }

    /**
     * adds notes to notes array
     * @param n is the Note object
     */
    public void addNote(Note n) {
        notes[numNotes++] = n;
    }


    /**
     * Finished when all the notes have been pressed or missed
     */
    public boolean isFinished() {
        for (int i = 0; i < numNotes; i++) {
            if (!notes[i].isCompleted()) {
                return false;
            }
        }
        return true;
    }

    /**
     * draws the lane and the notes
     */
    public void draw() {
        image.draw(location, HEIGHT);

        for (int i = currNote; i < numNotes; i++) {
            notes[i].draw(location);
        }

    }

}