import java.util.Random;

/**
 * Demon class includes constants that are used for demon
 */
public class Demon extends Enemy
{
    /**
     * public constants for the Navec class
     */
    public static final int MAX_HEALTH = 40;
    public static final int DAMAGE = 10;
    public static final double MAXSPD = 0.7;
    public static final double MINSPD = 0.2;
    private static final String PATH_L = "res/demon/demonLeft.png";
    private static final String PATH_R = "res/demon/demonRight.png";
    private static final String PATH_IL = "res/demon/demonInvincibleLeft.PNG";
    private static final String PATH_IR = "res/demon/demonInvincibleRight.PNG";
    private static final String PATH_FIRE = "res/demon/demonFire.png";
    private static final int NORMAL_COUNT = 3000/1000*60;
    private static final int RANGE = 150;
    private Random rd = new Random();


    /**
     * constructor determine whether the demon is aggressive and give a random speed
     * @param x     initial x position of the demon
     * @param y     initial y position of the demon
     */
    public Demon(double x, double y) {
        super(PATH_L, PATH_R, x, y, MAX_HEALTH,PATH_IL,PATH_IR,PATH_FIRE,DAMAGE,RANGE);
        if(rd.nextBoolean()){  // is aggressive

            // do not move diagonally, randomly pick one direction
            if(rd.nextBoolean()){
                // move in x dir
                setSpeedX(MINSPD + rd.nextDouble() * (MAXSPD - MINSPD));
            }else {
                // move in y dir
                setSpeedY(MINSPD + rd.nextDouble() * (MAXSPD - MINSPD));
            }

            // randomise moving direction
            if(rd.nextBoolean()){
                setSpeedX(-getSpeedX());
                setSpeedY(-getSpeedY());
            }
        }
    }

    /**
     * call the takeDamage method with the Invincible time passed to the Enemy Class
     * @param dmg amount of health to be deducted
     */
    public void takeDamage(int dmg) {
        super.takeDamage(dmg,NORMAL_COUNT);
    }


}
