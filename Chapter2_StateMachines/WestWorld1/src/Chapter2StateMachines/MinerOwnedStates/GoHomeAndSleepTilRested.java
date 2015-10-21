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
//  miner will go home and sleep until his fatigue is decreased
//  sufficiently
//------------------------------------------------------------------------
public class GoHomeAndSleepTilRested extends State {

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
            SetTextColor(FOREGROUND_RED| FOREGROUND_INTENSITY);
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Walkin' home");
            pMiner.ChangeLocation(location_type.shack);
        }
    }

    @Override
    public void Execute(Miner pMiner) {
        //if miner is not fatigued start to dig for nuggets again.
        if (!pMiner.Fatigued()) {
            SetTextColor(FOREGROUND_RED| FOREGROUND_INTENSITY); 
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                    + "What a God darn fantastic nap! Time to find more gold");

            pMiner.ChangeState(EnterMineAndDigForNugget.Instance());
        } else {
            //sleep
            pMiner.DecreaseFatigue();
            SetTextColor(FOREGROUND_RED| FOREGROUND_INTENSITY);
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "ZZZZ... ");
        }
    }

    @Override
    public void Exit(Miner pMiner) {
        SetTextColor(FOREGROUND_RED| FOREGROUND_INTENSITY);
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Leaving the house");
    }
}
