/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.WestWorld1;
import java.io.FileNotFoundException;
import static common.misc.ConsoleUtils.*;

public class main {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        //send output to swing component instead of console
        enableSwing();
        //create a miner
        Miner miner = new Miner(EntityNames.ent_Miner_Bob);
        
        //simply run the miner through a few Update calls
        for (int i = 0; i < 20; ++i) {
            miner.Update();
            Thread.sleep(800);
        }

        //wait for a keypress (enter) before exiting
        PressAnyKeyToContinue();
    }
}
