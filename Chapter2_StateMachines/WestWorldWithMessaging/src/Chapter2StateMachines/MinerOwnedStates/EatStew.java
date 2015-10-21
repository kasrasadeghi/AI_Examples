/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.MinerOwnedStates;

//------------------------------------------------------------------------
import common.Messaging.Telegram;
import Chapter2StateMachines.State;
import Chapter2StateMachines.Miner;
import static common.misc.ConsoleUtils.*;
import static Chapter2StateMachines.EntityNames.*;

//
//  this is implemented as a state blip. The miner eats the stew, gives
//  Elsa some compliments and then returns to his previous state
//------------------------------------------------------------------------
public class EatStew extends State<Miner> {

    static final EatStew instance = new EatStew();

    private EatStew() {
    }

    //copy ctor and assignment should be private
    private EatStew(EatStew e) {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    public static EatStew Instance() {
        return instance;
    }

    @Override
    public void Enter(Miner pMiner) {
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Smells Reaaal goood Elsa!");
    }

    @Override
    public void Execute(Miner pMiner) {
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Tastes real good too!");

        pMiner.GetFSM().RevertToPreviousState();
    }

    @Override
    public void Exit(Miner pMiner) {
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                + "Thankya li'lle lady. Ah better get back to whatever ah wuz doin'");
    }

    @Override
    public boolean OnMessage(Miner pMiner, Telegram msg) {
        //send msg to global message handler
        return false;
    }
}