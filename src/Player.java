import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;


/**
 * the player fae class
 */
public class Player extends Character
{
    // statics
    private static final String PATH_L = "res/fae/faeLeft.png";
    private static final String PATH_R = "res/fae/faeRight.png";
    private static final Image IMG_A_LEFT = new Image("res/fae/faeAttackLeft.png");
    private static final Image IMG_A_RIGHT = new Image("res/fae/faeAttackRight.png");
    private static final int MAX_HEALTH = 100;
    private static final double DEFAULT_SPD = 2;
    private static final Point HEALTH_POSITION = new Point(20,25);
    private static final Font HEALTH_FONT = new Font(ShadowDimension.FONT_PATH,30);
    private static final int DAMAGE = 20;
    private static final int NORMAL = 0;
    private static final int ATTACK = 1;
    private static final int IDLE = 3;
    private static final int IDLE_FRAME = 2000/1000 * ShadowDimension.FPS;
    private static final int ATTACK_FRAME = 1000/1000 * ShadowDimension.FPS;
    private static final int INV_FRAME = 3000/1000 * ShadowDimension.FPS;
    private int status_count = 0;
    private int status = NORMAL;
    private static final String NAME= "Fae";

    private boolean isRight = true;
    private int invCount = 0;
    // not invincible at count =0;
    private final double speed = DEFAULT_SPD;

    /**
     * constructor requiring initial positions
     * @param x initial x coordinate
     * @param y initial y coordinate
     */
    public Player(double x,double y) {
        super(PATH_L,PATH_R,x,y,MAX_HEALTH);
    }

    @Override
    public void move(Input input, ArrayList<Obstacle> obstacles, Point topLeft, Point bottomRight) {
        double x = getX(), y = getY();

        // move with collision detection
        double newx=x,newy=y;

        if (input.isDown(Keys.LEFT)) {
            newx = x- speed;
            //change direction, doesn't matter of collision
            isRight = false;
        }
        if (input.isDown(Keys.RIGHT)) {
            newx = x + speed;
            isRight = true;
        }
        if (input.isDown(Keys.UP)) {
            newy = y - speed;
        }
        if (input.isDown(Keys.DOWN)) {
            newy = y + speed;
        }


        // collision detection
        // boundaries check with topleft of Fae
        // leave
        if (newx < topLeft.x){
            newx = topLeft.x;
        }
        if (newy < topLeft.y){
            newy = topLeft.y;
        }
        if (newx> bottomRight.x){
            newx = bottomRight.x;
        }
        if (newy > bottomRight.y){
            newy = bottomRight.y;
        }

        // add situation for multiple keys pressed together
        if(newx != x && newy != y) {
            // could have just divide by sqrt(2)
            double sumsquare = (newx - x) * (newx - x) + (newy - y) * (newy - y);
            newx = x + (newx - x) / Math.sqrt(sumsquare)*speed;
            newy = y + (newy - y) / Math.sqrt(sumsquare)*speed;
        }

        box.moveTo(new Point(newx,newy));

        if(collision(obstacles,new Point(newx,newy))){
            // by checking single directional movement,
            // allows both key pressed together to move with the wall/tree
            if(!collision(obstacles,new Point(newx,y))){
                newy  = y;
            }else if(!collision(obstacles,new Point(x,newy))){
                newx = x;
            }else{
                newx = x;
                newy = y;
            }
        }

        this.setX(newx);
        this.setY(newy);
        box.moveTo(new Point(x,y));

        // Moving end
        // also checking and counting player attack
        if (status==NORMAL){
            if(input.isDown(Keys.A)){
                status = ATTACK;
                status_count = ATTACK_FRAME;
            }
        } else if (status == ATTACK) {
            status_count--;
            if(status_count == 0){
                status = IDLE;
                status_count = IDLE_FRAME;

            }
        } else if (status == IDLE) {
            status_count--;
            if (status_count == 0) {
                status = NORMAL;
            }
        }

        // counting invincible
        // at 0 then not invincible
        if(invCount>0){
            invCount--;
        }


    }


    /**
     * check if player at a given point collides with Wall or Tree
     * sinkholes will not be count as collide, but deal damage to player
     * note that this function do not put the player box back to the original position
     * this should be done by the caller
     *
     * @param obstacles list of Obstacles, include wall, tree and holes
     * @param new_point topleft point of the player that it will move to
     * @return boolean saying collide or not
     */
    private boolean collision(ArrayList<Obstacle> obstacles,Point new_point){
        box.moveTo(new_point);
        for(Obstacle obstacle:obstacles){
            if (obstacle.getBox() != null && box.intersects(obstacle.getBox())){
                if (obstacle instanceof Sinkhole){
                    ((Sinkhole) obstacle).attack(this);
                }else{
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void draw() {
        double x = getX(), y = getY();
        if (isRight){
            if (status == ATTACK){
                IMG_A_RIGHT.drawFromTopLeft(x,y);
            }else{
                imgRight.drawFromTopLeft(x,y);
            }

        }else {
            if (status == ATTACK){
                IMG_A_LEFT.drawFromTopLeft(x,y);
            }else{
                imgLeft.drawFromTopLeft(x,y);
            }
        }
        drawHealth();
    }




    @Override
    public void drawHealth() {
        super.drawHealth(HEALTH_FONT,HEALTH_POSITION);
    }


    @Override
    public void attack(ArrayList<Character> targets) {
        if(status == ATTACK){
            for (Character t: targets) {
                if (t == this){
                    // do not attack itself
                    continue;
                }
                if(!t.died() && t.canTakeDmg(box)){
                    //
                    t.takeDamage(DAMAGE);
                    System.out.println(Message.log(NAME,t.getClass().getName(),DAMAGE) + t.log());
                }

            }
        }

    }


    @Override
    public boolean canTakeDmg(Rectangle box){
        // collides and not invincible
        return box.intersects(this.box) && invCount <= 0;
    }


    @Override
    public void takeDamage(int val) {
        // check if invincible
        if (invCount <= 0) {
            super.takeDamage(val);
            invCount = INV_FRAME;
        }
    }

    public Rectangle getBox(){
        return box;
    }

    /**
     * return player's name Fae
     * @return "Fae"
     */
    public String getName(){
        return NAME;
    }
}
