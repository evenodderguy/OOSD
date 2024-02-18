/**
 * A base class of this project where most entity is based on this class
 *
 */
public abstract class GameObject{

    private double x;
    private double y;

    /**
     * constructor for GamObject
     * @param x     initial value of x coordinate
     * @param y     initial value of y coordinate
     */
    public GameObject(double x,double y){
        // pass in as starting position
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
