import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/**
 * Character that are classified as enemy, Navec and demons
 */
public abstract class Enemy extends Character{

    private final Image imgInvLeft;
    private final Image imgInvRight;
    private final Image imgFire;
    private double speedX = 0,speedY = 0;
    private boolean isNormal = true;
    private int normalCount = 0;
    private final int DAMAGE;
    private final int RANGE;

    /**
     * only constructor brings constants to super class
     * @param pathL
     * @param pathR
     * @param x
     * @param y
     * @param maxH
     * @param pathIL
     * @param pathIR
     * @param fire
     * @param damage
     * @param range
     */
    public Enemy(String pathL, String pathR, double x, double y,
                 int maxH, String pathIL, String pathIR, String fire,
                int damage, int range) {
        super(pathL, pathR, x, y, maxH);
        imgInvLeft = new Image(pathIL);
        imgInvRight = new Image(pathIR);
        imgFire = new Image(fire);
        DAMAGE = damage;
        RANGE = range;
    }

    @Override
    public void draw() {
        double x = getX(), y = getY();

        // draw if still alive
        if(isAlive()){

            if(speedX>=0){ //move in right(non-left) direction,

                if (isNormal)
                    imgRight.drawFromTopLeft(x,y);
                else
                    imgInvRight.drawFromTopLeft(x,y);
            }else { //left
                if (isNormal)
                    imgLeft.drawFromTopLeft(x,y);
                else
                    imgInvLeft.drawFromTopLeft(x,y);
            }
            super.drawHealth();
        }
    }


    @Override
    public void move(Input input, ArrayList<Obstacle> obstacles, Point topLeft, Point bottomRight) {
        double x = getX(), y = getY();


        // timer
        if(!isNormal){
            normalCount--;
            if (normalCount == 0){
                isNormal = true;
            }
        }

        // current timescale speed ratio
        double speedRatio = TimeScaleControl.getRatio();


        double newx=x + speedX * speedRatio,newy=y + speedY * speedRatio; // intended move

        // check with map bounding with topleft position of demon
        if (newx < topLeft.x || newy < topLeft.y || newx> bottomRight.x || newy > bottomRight.y){
            // reverse speed direction
            speedY = -speedY;
            speedX = -speedX;
        }

        box.moveTo(new Point(newx,newy));
        // check collision of obstacles
        for(Obstacle obstacle:obstacles){
            if (obstacle.getBox() != null && box.intersects(obstacle.getBox())){
                speedY = -speedY;
                speedX = -speedX;
                newx = x;
                newy = y;
                break;

            }
        }

        this.setX(newx);
        this.setY(newy);
        box.moveTo(new Point(newx,newy));


    }

    @Override
    public void attack(ArrayList<Character> targets) {
        // Only the player Fae is needed
        // to be left like passing whole list for extendability, eg 2 players
        if (died()) return;
        Player fae = (Player) targets.get(ShadowDimension.FAE);
        Point renderPoint;
        DrawOptions rotation = new DrawOptions();
        double fireH = imgFire.getHeight(),
                fireW = imgFire.getWidth() ;

        if (isPlayerInRange(fae)){

            // setting up rotation
            if (fae.getBox().centre().x >= box.centre().x){
                // player is on the right of demon
                if(fae.getBox().centre().y >= box.centre().y){
                    //bottom   -right
                    renderPoint = box.bottomRight();
                    rotation.setRotation(0);

                }else{
                    //top   -right
                    renderPoint = new Point(box.topRight().x,box.topRight().y - fireH) ;
                    rotation.setRotation(Math.PI* 1.5);
                }
            }else{
                // left
                if(fae.getBox().centre().y >= box.centre().y){
                    //bottom   -left
                    renderPoint = new Point(box.bottomLeft().x - fireW,box.bottomLeft().y);
                    rotation.setRotation(Math.PI/2);
                }else {
                    //top   -left
                    renderPoint = new Point(box.topLeft().x - fireW,box.topLeft().y - fireH);
                    rotation.setRotation(Math.PI);
                }
            }

            // draw fire
            imgFire.drawFromTopLeft(renderPoint.x,renderPoint.y,rotation);

            // check if fire hits player
            Rectangle firebox = imgFire.getBoundingBox();
            firebox.moveTo(renderPoint);
            if (fae.canTakeDmg(firebox)){
                fae.takeDamage(DAMAGE);
                System.out.println(Message.log(this.getClass().getName(),fae.getName(),getDamage())+ fae.log());
            }
        }

    }

    /**
     * check if player in range
     * @param p Player
     * @return
     */
    public boolean isPlayerInRange(Player p){
        // first check if they collides
        if (p.getBox().intersects(box.centre()))
            return true;
        else {
            double tempX,tempY;
            Point center = box.centre(),
                    pTL = p.getBox().topLeft(), // player top left
                    pBR = p.getBox().bottomRight(); // player bottom left
            // the center is either on
            // pBR.x/y > pTL.x/y

            /* FIND the CLOSEST point within PLAYER to The ENEMY
             * player have an x range(x1,x2) and a y range(y1,y2)
             * player topleft = x1,y1 bottomRight = x2,y2
             * the center of the Enemy(in single dimension, x or y)
             * can either be
             *      larger than player -- take player's largest point
             *      with in player   -- take Enemy's center point
             *      less than player -- take player's smallest point
             * X and Y are independent
             */
            tempX = Math.min(Math.max(pTL.x, center.x),pBR.x);
            tempY = Math.min(Math.max(pTL.y, center.y),pBR.y);
            return (new Point(tempX,tempY)).distanceTo(center) <= RANGE;
        }
    }

    @Override
    public boolean canTakeDmg(Rectangle box){
        return isNormal && box.intersects(this.box);
    }

    public int getDamage() {
        return DAMAGE;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    /**
     * take damage without checking
     * should be called by child class giving NormalCount
     * @param dmg damage
     * @param normalCount time that this Enemy can take damage again(in frames)
     */
    public void takeDamage(int dmg,int normalCount) {
        // damage taken without check, check should be done by attacker
        super.takeDamage(dmg);
        isNormal = false;
        this.normalCount = normalCount;
    }
}
