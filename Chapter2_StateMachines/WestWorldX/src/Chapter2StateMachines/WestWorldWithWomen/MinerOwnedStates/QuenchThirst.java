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
//  miner changes location to the saloon and keeps buying Whiskey until
//  his thirst is quenched. When satisfied he returns to the goldmine
//  and resumes his quest for nuggets.
//------------------------------------------------------------------------
class QuenchThirst extends State<Miner> {

    static final QuenchThirst instance = new QuenchThirst();

    private QuenchThirst() {
    }

    //copy ctor and assignment should be private
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    static public QuenchThirst Instance() {
        return instance;
    }

    @Override
    public void Enter(Miner pMiner) {
        if (pMiner.Location() != location_type.saloon) {
            pMiner.ChangeLocation(location_type.saloon);

            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                    + "Boy, ah sure is thusty! Walking to the saloon");
        }
    }

    @Override
    public void Execute(Miner pMiner) {
        if (pMiner.Thirsty()) {
            pMiner.BuyAndDrinkAWhiskey();

            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                    + "That's mighty fine sippin liquer");

            pMiner.GetFSM().ChangeState(EnterMineAndDigForNugget.Instance());
        } else {
            cout("\nERROR!\nERROR!\nERROR!");
        }
    }

    @Override
    public void Exit(Miner pMiner) {
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                + "Leaving the saloon, feelin' good");
    }
}