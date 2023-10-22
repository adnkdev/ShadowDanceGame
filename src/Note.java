import bagel.*;

/**
 * Parent class for notes
 */
public class Note {
    protected Image image;
    protected final int appearanceFrame;
    protected final int speed = 2;
    protected int y;
    protected int x;
    protected boolean active = false;
    protected boolean completed = false;

    public Note(int appearanceFrame) {
        this.appearanceFrame = appearanceFrame;
    }

    /**
     * checks if note is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * checks if note is complete
     */
    public boolean isCompleted() {return completed;}

    /**
     * deactivates the note
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /** updates notes coordinates and speed and activates note when
     * corresponding frame number is reached
     * @param alterSpeed is the current speed of the game
     */
    public void update(int alterSpeed) {
        if (active) {
            y += speed + alterSpeed;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /**
     * draws the notes
     * @param x is the notes x coordinate
     */

    public void draw(int x) {
        this.x = x;
        if (active) {
            image.draw(x, y);
        }
    }

    /**
     * check normal and hold note scores
     * @return int returns the score for relevant note
     */

    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey){
        return 0;
    };

    /**
     * check speed up and slow down note scores
     * @return int returns the score for relevant note
     */

    public int checkSpecialScore(Input input, SpecialNoteAccuracy accuracy, int targetHeight, Keys relevantKey){
        return 0;
    };

    /**
     * check if bomb notes has been pressed
     * @return boolean returns if bomb note has been pressed
     */

    public boolean checkBomb(Input input, SpecialNoteAccuracy accuracy, int targetHeight, Keys relevantKey){
        return false;
    }

    /**
     * deactivates the note if active
     * @return boolean return true if note has be deactivated,
     * otherwise it returns false
     */

    public boolean deactivateNote(){
        if(active){
            deactivate();
            return true;
        }
        return false;
    }

    /**
     * sets the initial note drop height
     * @param x is the drop height value
     */
    public void setY(int x){
        this.y = x;
    }

    /**
     * sets the relevant note Image
     * @param noteType is the corresponding note image
     */
    public void setImageNote(Image noteType){
        image = noteType;
    }

}
