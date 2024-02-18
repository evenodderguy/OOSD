/**
 * Tree class
 */

public class Tree extends Obstacle{
    private static final String PATH = "res/tree.png";
    // path of image

    /**
     * constructor of Tree that sets Tree position
     * @param x initial x position
     * @param y initial y position
     */
    public Tree(double x,double y){
        super(PATH,x,y);
    }


}
