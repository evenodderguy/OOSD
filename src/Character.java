import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.Font;
import java.util.ArrayList;

/**
 * Character include properties as such
 * capable of moving, attack and be targeted
 */
public abstract class Character extends GameObject
{
    protected Image imgLeft;
    protected Image imgRight;
    protected Rectangle box;
    private final int maxHealth;
    private int health;
    private boolean alive;

    private static final Font healthFont = new Font(ShadowDimension.FONT_PATH,15);
    private static final DrawOptions GREEN = new DrawOptions().setBlendColour(0,0.8,0.2);
    private static final DrawOptions ORANGE = new DrawOptions().setBlendColour(0.9, 0.6, 0);
    private static final DrawOptions RED = new DrawOptions().setBlendColour(1, 0, 0);
    private static final int HEALTH_Y_DISPLACE = -6;
    private static final int GREEN_HEALTH  = 65; // above
    private static final int ORANGE_HEALTH= 35; // above, below is red


    public Character(String pathL,String pathR,double x,double y,int maxH){
        super(x,y);
        imgLeft = new Image(pathL);
        imgRight = new Image(pathR);
        // Assuming left and right image have the same size
        box = imgLeft.getBoundingBox();
        box.moveTo(new Point(x,y));
        maxHealth = maxH;
        health = maxH;
        alive = true;
    }


    /**
     * check if a character is dead
     * @return if a character is dead
     */
    public boolean died(){
        return !alive;
    }

    /** draw health for characters
     * at their top left
     */
    public void drawHealth(){
        double perc = 100.0 * health/maxHealth;
        healthFont.drawString(String.format("%2.0f",perc)+"%",this.getX(),
                this.getY()+HEALTH_Y_DISPLACE,healthOpt(perc));
    }

    /** draw health at given position and font
     * @param font      font that the health is drawn
     * @param p         top left position that the health is drawn
     */
    public void drawHealth(Font font,Point p) {
        double perc = 100.0 * health / maxHealth;
        font.drawString(String.format("%2.0f", perc) + "%", p.x,p.y, healthOpt(perc));
    }


    /** give colour of the health to be draw based on the percentage of the health
     * @param perc percentage of the health
     * @return a DrawOption object with only the colour is set
     */
    public DrawOptions healthOpt(double perc){
        if (perc>= GREEN_HEALTH){
            return GREEN;
        } else if (perc>= ORANGE_HEALTH) {
            return ORANGE;
        }else {
            return RED;
        }
    }

    /** deduct health without checking, also change alive state is health <0
     * @param val amount of health to be deducted
     */
    public void takeDamage(int val){
        health -= val;
        if (health<=0){
            health = 0;
            alive = false;
        }

    }

    /** health out of its max health for logging purpose
     * @return formatted log of current_health/max_health
     */
    public String log(){
        return String.format("%d/%d",health,maxHealth);
    }


    /** determine if this character can take damage from the attacker
     * @param box attacker's damaging box
     * @return whether this character can take damage with giving condition
     */
    public abstract boolean canTakeDmg(Rectangle box);

    /**
     * render the character in the screen if applicable
     */
    public abstract void draw();

    /**
     * attack other character if they can
     * @param characters list of target that this character is able to attack
     */
    public abstract void attack(ArrayList<Character> characters);


    /**
     * move the character and check commission
     * @param input input from keyboard
     * @param obstacles list of obstacles that can block ot damage the character
     * @param topLeft topLeft of the boundaries
     * @param bottomRight bottomRight of the level boundaries
     */
    public abstract void move(Input input,ArrayList<Obstacle> obstacles,Point topLeft,Point bottomRight);

    /**
     * getter for Alive
     * @return alive
     */
    public boolean isAlive(){ return alive;}

}
