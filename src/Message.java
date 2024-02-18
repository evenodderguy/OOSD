import bagel.*;



//

/**
 * Render Messages to the Screen
 * Most printing Message job is done with this class
 *
 */
public class Message {
    // const
    private static final double START_X = 260;  // default position
    private static final double START_Y = 250;
    // Messages
    private static final String INS1 = "PRESS SPACE TO START"; // instruction 1
    private static final String INS2 = "USE ARROW KEYS TO FIND GATE"; // instruction 2
    private static final String WIN_MSG = "CONGRATULATIONS!";
    private static final String LOSE_MSG = "GAME OVER!";
    private static final String FONT_PATH = ShadowDimension.FONT_PATH; //Font
    private final static String GAME_TITLE = "SHADOW DIMENSION";

    private static final String MID1 = "PRESS SPACE TO START";
    private static final String MID2 = "PRESS A TO ATTACK";
    private static final String MID3 = "DEFEAT NAVEC TO WIN";
    private static final double MID_X = 350;  // default position for between level instruction
    private static final double MID_Y = 350;
    private static final double MID_Ydiff = 50;

    private static final String LOG_MSG= "%s inflicts %d damage points on %s. %s's current health: ";

    private static final double INS_X = START_X + 90;
    private static final double INS_Y = START_Y + 190;
    private static final double ROW_DIS= 40;
    private static final int DEFAULT_SIZE = 75;
    private static final Font INS_FONT = new Font(FONT_PATH,40);
    private static final Font DEFAULT_FONT = new Font(FONT_PATH, DEFAULT_SIZE);
    private static final String LEVELCMP= "LEVEL COMPLETE!";

    /**
     * Start page with  Game title and
     * PRESS SPACE TO START
     * USE ARROW KEYS TO FIND GATE
     */
    public static void start_menu(){
        // draw display menu
        DEFAULT_FONT.drawString(GAME_TITLE, START_X, START_Y);
        INS_FONT.drawString(INS1,INS_X,INS_Y);
        INS_FONT.drawString(INS2,INS_X,INS_Y+ROW_DIS);
    }

    /**
     * win page
     * CONGRATULATIONS!
     */
    public static void win(){
        // win page
        defaultMsg(WIN_MSG);
    }

    /**
     * lose page
     * GAME OVER!
     */
    public static void lose(){
        // Game Over page
        defaultMsg(LOSE_MSG);
    }

    /**
     * Print text with default font as per specification
     * @param text to be printed
     */
    private static void defaultMsg(String text){
        // for Win and lose page
        double x = Window.getWidth()/2.0 - DEFAULT_FONT.getWidth(text)/2.0;
        // assuming that size 75 means text height is 75, can't find in doc
        double y = Window.getHeight()/2.0 + DEFAULT_SIZE/2.0;
        DEFAULT_FONT.drawString(text,x,y);
    }

    /**
     * the page between level1 and level 0
     * page 2
     */
    public static void mid(){
        // between level instruction
        INS_FONT.drawString(MID1,MID_X,MID_Y);
        INS_FONT.drawString(MID2,MID_X,MID_Y+MID_Ydiff);
        INS_FONT.drawString(MID3,MID_X,MID_Y+MID_Ydiff*2);

    }

    /**
     * the page between level1 and level 0
     * page 1
     */
    public static void levelComplete(){
        defaultMsg(LEVELCMP);
    }

    /**
     * logging format when an entity deal damage to another entity
     * @param A         Attacker's name
     * @param T         Target's name
     * @param damage    Damage amount
     * @return          returns a String to be printed to the log
     */
    public static String log(String A,String T, int damage){
        return String.format(LOG_MSG,A,damage,T,T);
    }

}

