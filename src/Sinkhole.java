/**
 * class of Sinkholes
 * with attributes that describes the sinkhole from other Obstacle
 */

public class Sinkhole extends Obstacle
{
    private static final String PATH = "res/sinkhole.png";
    private static final int DAMAGE = 30;
    private boolean exist = true;

    Sinkhole(double x,double y){
        super(PATH,x,y);
    }

    /**
     * attack method like the attack method is the player method
     * but with narrowed targets,
     * only instance of the Player class
     *
     * @param fae target of the class method
     * @see Character#attack
     */
    public void attack(Player fae){
        fae.takeDamage(DAMAGE);
        // deal damage first and then log with deducted health
        exist = false;
        nullBox();
        System.out.println(Message.log("Sinkhole","Fae", DAMAGE)+fae.log());
    }

    @Override
    public void draw(){
        if (exist){
            super.draw();
        }
    }


}
