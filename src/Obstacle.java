import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * none movable GameObject, but can block or damage Chatacters
 */
public abstract class Obstacle extends GameObject{
    private final Image img;
    private Rectangle box;
    // box is left non-final to allow to make Obstacle disappear.

    /**
     * constructor of the class
     * @param path file path which the #Image is created from
     * @param x initial x position
     * @param y initial y position
     */
    public Obstacle(String path,double x,double y){
        super(x,y);
        img = new Image(path);
        box = img.getBoundingBox();
        box.moveTo(new Point(x,y));
    }

    /**
     * renders the object on the screen
     */
    public void draw() {
        img.drawFromTopLeft(getX(),getY());
    }

    public Rectangle getBox(){
        return box;
    }

    /**
     * make the box attribute null
     */
    public void nullBox(){box = null;}

}
