/**
 * Wall class
 */

public class Wall extends Obstacle{
    private static final String PATH = "res/wall.png";
    // path of image

    /**
     * constructor of Wall class sets the wall position
     * @param x initial x position
     * @param y initial y position
     */
    public Wall(double x,double y){
        super(PATH,x,y);
    }

}
