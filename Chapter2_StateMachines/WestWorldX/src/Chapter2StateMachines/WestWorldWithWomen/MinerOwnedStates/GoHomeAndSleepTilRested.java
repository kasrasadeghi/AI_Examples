/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.WestWorldWithWomen.MinerOwnedStates;

import Chapter2StateMachines.WestWorldWithWomen.Miner;
import Chapter2StateMachines.WestWorldWithWomen.State;
import Chapter2StateMachines.WestWorldWithWomen.location_type;
import static Chapter2StateMachines.WestWorldWithWomen.EntityNames.GetNameOfEntity;
import static common.misc.ConsoleUtils.*;

//------------------------------------------------------------------------
//
//  miner will go home and sleep until his fatigue is decreased
//  sufficiently
//------------------------------------------------------------------------
public class GoHomeAndSleepTilRested extends State<Miner> {

    static final GoHomeAndSleepTilRested instance = new GoHomeAndSleepTilRested();

    private GoHomeAndSleepTilRested() {
    }

    //copy ctor and assignment should be private
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    static public GoHomeAndSleepTilRested Instance() {
        return instance;
    }

    @Override
    public void Enter(Miner pMiner) {
        if (pMiner.Location() != location_type.shack) {
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Walkin' home");
            pMiner.ChangeLocation(location_type.shack);
        }
    }

    @Override
    public void Execute(Miner pMiner) {
        //if miner is not fatigued start to dig for nuggets again.
        if (!pMiner.Fatigued()) {
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                    + "What a God darn fantastic nap! Time to find more gold");

            pMiner.GetFSM().ChangeState(EnterMineAndDigForNugget.Instance());
        } else {
            //sleep
            pMiner.DecreaseFatigue();
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "ZZZZ... ");
        }
    }

    @Override
    public void Exit(Miner pMiner) {
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Leaving the house");
    }
}
