import bagel.*;
import bagel.util.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * main class of the game
 * includes level control and the start the game
 *
 * Please enter your name below
 * @author Yifeng Guo
 */


public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final Image BACKGROUND_IMAGE_LEVEL0 = new Image("res/background0.png");
    private final Image BACKGROUND_IMAGE_LEVEL1 = new Image("res/background1.png");
    private static final Point L0Portal = new Point(950,670);
    private static final String L0file = "res/level0.csv";
    private static final String L1file = "res/level1.csv";

    public static final String FONT_PATH = "res/frostbite.ttf";
    // make public because this constant is needed for multiple classes


    // Constants relate to gameplay status
    private static final int START = 0;
    private static final int LEVEL0 = 1;
    private static final int LEVEL1 = 3;
    private static final int BETWEEN_LEVEL = 2;
    private static final int WIN = 4;
    private static final int LOSE = 5;

    // Public Constants
    public static final int FAE = 0;
    public static final int NAVEC = 1;
    public static final int FPS = 60;
    //

    private static final int THREE_SECOND = 3*FPS;
    private int counter;


    //var
    private int gameStatus = START;
    private Point topLeft;
    private Point bottomRight;
    private final ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    private final ArrayList<Character> characters = new ArrayList<Character>();
    private Player Fae;


    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Method used to read file and create objects (You can change this
     * method as you wish).
     */
    private void readCSV(String worldFile){
        // no other input check is implemented.
        try (BufferedReader br = new BufferedReader(new FileReader(worldFile))){
            characters.clear();
            characters.add(null);
            characters.add(null);
            obstacles.clear();
            String line = null;
            // line format <name>,<coor_x>,<coor_y>
            String[] ele = new String[3];
            double v1,v2;
            while ((line = br.readLine()) != null){
                ele = line.split(",");
                v1 = Double.parseDouble(ele[1]);
                v2 = Double.parseDouble(ele[2]);

                switch (ele[0]){
                    // checking for different input
                    case "Fae":
                        //assume only one player in the csv file
                        // otherwise previous one is ignored
                        characters.set(0,new Player(v1,v2));
                        break;

                    case "Navec":
                        characters.set(1,new Navec(v1,v2));
                        break;

                    case "Demon":
                        characters.add(new Demon(v1,v2));
                        break;

                    case "Wall":
                        obstacles.add(new Wall(v1,v2));
                        break;

                    case "Sinkhole":
                        obstacles.add(new Sinkhole(v1,v2));
                        break;

                    case "Tree":
                        obstacles.add(new Tree(v1,v2));
                        break;

                    case "TopLeft":
                        topLeft = new Point(v1,v2);
                        break;
                    case "BottomRight":
                        bottomRight = new Point(v1,v2);
                        break;

                    default:
                        // no match
                        System.out.println("error: can't understand --" + line);

                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            // always check esc key press
            Window.close();
        }
        if(checkLose()){
            // always check lose
            gameStatus = LOSE;
        }


        if (gameStatus == START){
            //within start menu
            Message.start_menu();

            //condition to next level: level0
            if (input.isDown(Keys.SPACE)){
                gameStatus = LEVEL0;
                readCSV(L0file);
            }

        } else if (gameStatus == LEVEL0) {
            //within level 0
            level0(input);

            //conditions to next level
            if (checkWin0() || input.isDown(Keys.W)){
                // win L1  ||  enter W to cheat
                gameStatus = BETWEEN_LEVEL;
                counter = THREE_SECOND;
            }


        } else if (gameStatus == BETWEEN_LEVEL) {
            if (counter>=0){
                Message.levelComplete();
                counter--;
            }else {
                // within between level page
                Message.mid();
                //condition to next level
                if (input.isDown(Keys.SPACE)){
                    gameStatus = LEVEL1;
                    readCSV(L1file);
                    // Player Object is recreated in next level
                }
            }



        } else if (gameStatus == LEVEL1) {
            level1(input);

            // WIN condition
            if (checkWin1()){
                gameStatus = WIN;
            }

        } else if (gameStatus == WIN) {
            Message.win();

        }else if (gameStatus == LOSE) {
            //check lose
            Message.lose();

        }

    }

    /**
     * perform level 0 operations
     * @param input the Input
     */
    private void level0(Input input){
        BACKGROUND_IMAGE_LEVEL0.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        // Moving player
        characters.get(FAE).move(input,obstacles,topLeft,bottomRight);

        // drawing player and all obstacles
        characters.get(FAE).draw();
        for(Obstacle obs: obstacles){
            obs.draw();
        }

    }

    /**
     * perform level 1 operations
     * @param input the Input
     */
    private void level1(Input input){
        BACKGROUND_IMAGE_LEVEL1.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        //Time scale change
        TimeScaleControl.change_detection(input);


        for(Character ch:characters){
            if (ch == null) break;
            ch.move(input,obstacles,topLeft,bottomRight);
            ch.attack(characters);
        }

        // all drawing
        for(Character character:characters){
            character.draw();
        }
        for(Obstacle obstacle:obstacles){
            obstacle.draw();
        }
        characters.get(FAE).draw();

    }

    /**
     * level 0 win check
     * @return boolean
     */
    private boolean checkWin0(){
        return characters.get(FAE).getX() >= L0Portal.x && characters.get(FAE).getY() >= L0Portal.y;
    }

    /**
     * all level lose check ie is player dead
     * @return booleana
     */
    private boolean checkLose(){
        if (gameStatus == LEVEL0 || gameStatus == LEVEL1){
            return characters.get(FAE).died();
        }
        // game not started yet
        return false;
    }

    /**
     * check win of level 1
     * @return boolean
     */
    private boolean checkWin1(){
        return characters.get(NAVEC).died();
    }

}
