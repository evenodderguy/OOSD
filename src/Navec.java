import java.util.Random;


/**
 * class for Navec include constants for Navec
 * some constants are from Demon class as per specification
 */
public class Navec extends Enemy
 {
    private static final int MAX_HEALTH = 2 * Demon.MAX_HEALTH;
    private static final int DAMAGE = 2 * Demon.DAMAGE;
    private static final int RANGE = 200;
    private static final double MAXSPD = Demon.MAXSPD;
    private static final double MINSPD = Demon.MINSPD;
    private static final String PATH_L = "res/navec/navecLeft.png";
    private static final String PATH_R = "res/navec/navecRight.png";
    private static final String PATH_IL = "res/navec/navecInvincibleLeft.PNG";
    private static final String PATH_IR = "res/navec/navecInvincibleRight.PNG";
    private static final String PATH_FIRE = "res/navec/navecFire.png";
    private static final int NORMAL_COUNT = 3000/1000*60;
    private Random rd = new Random();


     /**
      * constructor determine whether the demon is aggressive and give a random speed
      * @param x     initial x position of the demon
      * @param y     initial y position of the demon
      */
    public Navec(double x, double y) {
        super(PATH_L, PATH_R, x, y, MAX_HEALTH,PATH_IL,PATH_IR,PATH_FIRE,DAMAGE,RANGE);
        // do not move diagonally
        // do not move diagonally
        if(rd.nextBoolean()){
            // move in x dir
            setSpeedX(MINSPD + rd.nextDouble() * (MAXSPD - MINSPD));
        }else {
            // move in y dir
            setSpeedY(MINSPD + rd.nextDouble() * (MAXSPD - MINSPD));
        }

        if(rd.nextBoolean()){
            // randomise moving direction
            setSpeedX(-getSpeedX());
            setSpeedY(-getSpeedY());
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
