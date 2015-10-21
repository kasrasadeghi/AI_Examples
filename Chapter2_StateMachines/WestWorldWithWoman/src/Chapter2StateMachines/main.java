/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines;

import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import static common.misc.ConsoleUtils.*;

public class main {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        //define this to send output to a text file (see locations.h)
        PrintStream out = System.out;
        if (location_type.TEXTOUTPUT) {
            System.setOut(new PrintStream(new FileOutputStream(new File("output.txt"))));
        } else {
            //send output to swing component instead of console
            enableSwing();
        }
        //create a miner
        Miner Bob = new Miner(EntityNames.ent_Miner_Bob);

        //create his wife
        MinersWife Elsa = new MinersWife(EntityNames.ent_Elsa);

        //simply run the miner through a few Update calls
        for (int i = 0; i < 20; ++i) {
            Bob.Update();
            Elsa.Update();
            Thread.sleep(800);
        }

        if (location_type.TEXTOUTPUT) {
            System.setOut(out);
        }
        //wait for a keypress (enter) before exiting
        PressAnyKeyToContinue();
    }
}
