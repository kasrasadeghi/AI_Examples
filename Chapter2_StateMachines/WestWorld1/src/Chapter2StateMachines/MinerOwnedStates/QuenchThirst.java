/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.MinerOwnedStates;

import Chapter2StateMachines.Miner;
import Chapter2StateMachines.State;
import Chapter2StateMachines.location_type;
import static Chapter2StateMachines.EntityNames.GetNameOfEntity;
import static common.misc.ConsoleUtils.*;
import static common.windows.*;

//------------------------------------------------------------------------
//
//  miner changes location to the saloon and keeps buying Whiskey until
//  his thirst is quenched. When satisfied he returns to the goldmine
//  and resumes his quest for nuggets.
//------------------------------------------------------------------------
class QuenchThirst extends State {

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

            SetTextColor(FOREGROUND_RED| FOREGROUND_INTENSITY);
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                    + "Boy, ah sure is thusty! Walking to the saloon");
        }
    }

    @Override
    public void Execute(Miner pMiner) {
        if (pMiner.Thirsty()) {
            pMiner.BuyAndDrinkAWhiskey();

            SetTextColor(FOREGROUND_RED| FOREGROUND_INTENSITY);
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                    + "That's mighty fine sippin liquer");

            pMiner.ChangeState(EnterMineAndDigForNugget.Instance());
        } else {
            SetTextColor(FOREGROUND_RED | FOREGROUND_INTENSITY);
            cout("\nERROR!\nERROR!\nERROR!");
        }
    }

    @Override
    public void Exit(Miner pMiner) {
        SetTextColor(FOREGROUND_RED| FOREGROUND_INTENSITY);
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                + "Leaving the saloon, feelin' good");
    }
}