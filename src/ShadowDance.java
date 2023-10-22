import bagel.*;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Assignment 2
 *
 * @author Adnan khan Mohammed
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final static int SCORE_LOCATION = 35;
    private final static int SELECT_AGAIN_LOCATION = 500;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private static final String INSTRUCTIONS =  "SELECT LEVELS WITH\n     NUMBER KEYS\n     1     2     3";
    private static int CLEAR_SCORE;
    private static final int CLEAR_SCORE_LEVEL1 = 150;
    private static final int CLEAR_SCORE_LEVEL2 = 400;
    private static final int CLEAR_SCORE_LEVEL3 = 350;
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private static final String SELECT_AGAIN_MESSAGE = "PRESS SPACE TO RETURN\nTO LEVEL SELECTION";
    private final Accuracy accuracy = new Accuracy();
    private final SpecialNoteAccuracy specialAccuracy = new SpecialNoteAccuracy();
    private Lane[] lanes = new Lane[4];
    private final Guardian guardian = new Guardian();
    private final Collision collision = new Collision();
    private int numLanes = 0;
    private int score = 0;
    private static int currFrame = 0;
    private boolean started = false;
    private boolean finished = false;
    private boolean paused = false;
    private boolean read = false;
    private boolean playLvl1 = false;
    private boolean playLvl2 = false;
    private boolean playLvl3 = false;

    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /** reads the csv and generates the lanes containing notes
     * @param x is the number associated with the level
     * @return Lane[] is the newly generated lanes
     */
    private Lane[] readCsv(int x) {

        Lane[] lanes = new Lane[4];

        try (BufferedReader br = new BufferedReader(new FileReader("res/level"+x+".csv"))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane = new Lane(laneType, pos);
                    lanes[numLanes++] = lane;
                } else {
                    // reading notes
                    String dir = splitText[0];
                    Lane lane = null;
                    for (int i = 0; i < numLanes; i++) {
                        if (lanes[i].getType().equals(dir)) {
                            lane = lanes[i];
                        }
                    }

                    if (lane != null) {
                        switch (splitText[1]) {
                            case "Normal":
                                Note note = new NormalNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(note);
                                break;
                            case "Hold":
                                Note holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(holdNote);
                                break;
                            case "SlowDown":
                                Note slowDownNote = new SlowDownNote("SlowDown", Integer.parseInt(splitText[2]));
                                lane.addNote(slowDownNote);
                                break;
                            case "SpeedUp":
                                Note speedUpNote = new SpeedUpNote("SpeedUp", Integer.parseInt(splitText[2]));
                                lane.addNote(speedUpNote);
                                break;
                            case "Bomb":
                                Note bombNote = new BombNote("Bomb", Integer.parseInt(splitText[2]));
                                lane.addNote(bombNote);
                                break;
                            case "DoubleScore":
                                Note doubleScoreNote = new DoubleScoreNote("DoubleScore", Integer.parseInt(splitText[2]));
                                lane.addNote(doubleScoreNote);
                                break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return lanes;
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            // starting screen
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTIONS,
                    TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);

            // selection for levels
            if (input.wasPressed(Keys.NUM_1)) {
                playLvl1 = true;
                started = true;
                CLEAR_SCORE = CLEAR_SCORE_LEVEL1;
            }

            if (input.wasPressed(Keys.NUM_2)) {
                playLvl2 = true;
                started = true;
                CLEAR_SCORE = CLEAR_SCORE_LEVEL2;
            }

            if (input.wasPressed(Keys.NUM_3)) {
                playLvl3 = true;
                started = true;
                CLEAR_SCORE = CLEAR_SCORE_LEVEL3;
            }
        } else if (finished) {

            // end screen
            if (score >= CLEAR_SCORE) {
                TITLE_FONT.drawString(CLEAR_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                        WINDOW_HEIGHT/2);

                INSTRUCTION_FONT.drawString(SELECT_AGAIN_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                        SELECT_AGAIN_LOCATION);

                if (input.wasPressed(Keys.SPACE)) {
                    finished = false;
                    started = false;
                    playLvl1 = false;
                    playLvl2 = false;
                    playLvl3 = false;
                    read = false;
                    numLanes = 0;
                    currFrame = 0;
                    score = 0;
                    //track.start();
                }


            } else {
                TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                        WINDOW_HEIGHT/2);

                INSTRUCTION_FONT.drawString(SELECT_AGAIN_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                        SELECT_AGAIN_LOCATION);

                if (input.wasPressed(Keys.SPACE)) {
                    finished = false;
                    started = false;
                    playLvl1 = false;
                    playLvl2 = false;
                    playLvl3 = false;
                    read = false;
                    numLanes = 0;
                    currFrame = 0;
                    score = 0;
                    //track.start();
                }
            }
        } else if (playLvl1) {

            // reads level 1 csv
            if(!read){
                setLanes(readCsv(1));
                read = true;
            }

            //gameplay lvl 1

            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                }

                for (int i = 0; i < numLanes; i++) {
                    lanes[i].draw();
                }

            } else {

                currFrame++;
                for (int i = 0; i < numLanes; i++) {
                    score += lanes[i].updateLvl1(input, accuracy);
                }

                accuracy.update();
                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                    //track.pause();
                }
            }
        }else if(playLvl2){

            //read level 2 csv
            if(!read){
                setLanes(readCsv(2));
                read = true;
            }
            // gameplay lvl 2
            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                }

                for (int i = 0; i < numLanes; i++) {
                    lanes[i].draw();
                }

            } else {

                currFrame++;
                for (int i = 0; i < numLanes; i++) {
                    score += lanes[i].updateLvl2(input, specialAccuracy);
                }

                specialAccuracy.update();
                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;

                }
            }

        }else if(playLvl3){

            //read level 3 gameplay
            if(!read){
                setLanes(readCsv(3));
                read = true;
            }

            // gameplay lvl 3
            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                    //track.run();
                }

                for (int i = 0; i < numLanes; i++) {
                    lanes[i].draw();
                }

                guardian.draw();

            } else {

                currFrame++;
                guardian.update(input, collision);
                for (int i = 0; i < numLanes; i++) {
                    score += lanes[i].updateLvl3(input, specialAccuracy, collision, guardian.getEnemies(),guardian.getNumEnemies());
                }

                specialAccuracy.update();
                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                }
            }

        }

    }

    /**
     * gets the current frame number
     */
    public static int getCurrFrame() {
        return currFrame;
    }

    /**
     * sets the shadow dance lanes
     */
    public void setLanes(Lane[] lane){
        this.lanes = lane;
    }


    /**
     * checks if all notes in the lanes are completed
     * @return boolean return true if notes are finished, otherwise return false
     */
    private boolean checkFinished() {
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }
        //resets game for additional play
        guardian.reset();
        specialAccuracy.resetSpecialAccuracy();
        accuracy.resetAccuracy();
        return true;
    }
}
