/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines;

import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import static common.misc.ConsoleUtils.*;
import static Chapter2StateMachines.MessageDispatcher.*;
import static Chapter2StateMachines.EntityManager.*;

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

        //register them with the entity manager
        EntityMgr.RegisterEntity(Bob);
        EntityMgr.RegisterEntity(Elsa);
  
        //simply run the miner through a few Update calls
        for (int i = 0; i < 30; ++i) {
            Bob.Update();
            Elsa.Update();
            
            //dispatch any delayed messages
            Dispatch.DispatchDelayedMessages();
            
            Thread.sleep(800);
        }
        Bob = null;
        Elsa = null;
//*/        
        if (location_type.TEXTOUTPUT) {
            System.setOut(out);
        }
        //wait for a keypress (enter) before exiting
        PressAnyKeyToContinue();
    }
}
