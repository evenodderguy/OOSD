import bagel.Input;
import bagel.Keys;

/**
 * TimeScaleControl in level 1
 * L to increase speed, K to decrease speed by 50%
 * max 3 times
 */
public class TimeScaleControl {
    // The Time Scale effects the whole game
    // so choose it to be all static

    // L increase
    // K decrease

    //consts
    private static final int MAX = 3;
    private static final int MIN = -3;
    private static final String SPDUP = "Sped up, Speed: %d ";
    private static final String SPDDW = "Slowed down, Speed: %d ";
    private static int level = 0;



    /** to detect key press from user input and to change ratio
     * will be called once per update()
     * @param input the input from keyboard
     */
    public static void change_detection(Input input){
        if(input.wasPressed(Keys.L)){
            level = Math.min(MAX, level +1);
            System.out.println(String.format(SPDUP,level));
        }
        if (input.wasPressed(Keys.K)) {
            level = Math.max(MIN,level-1);
            System.out.println(String.format(SPDDW,level));
        }

    }

    /**
     * get the ratio of speed that the entity's speed should be changed to
     * @return the ratio
     */
    public static double getRatio() {
        double ratio =1;
        double l = level;
        if (level>=0){
            ratio =  ratio * Math.pow(1.5,l);
        }else {
            ratio =  ratio * Math.pow(2,l);
            // (1/2) ^ abs(l)
        }
        return ratio;
    }
}
