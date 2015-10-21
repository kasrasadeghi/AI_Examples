/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.WestWorldWithMessaging.MinerOwnedStates;

import common.Messaging.Telegram;
import Chapter2StateMachines.WestWorldWithMessaging.MessageTypes;
import Chapter2StateMachines.WestWorldWithMessaging.EntityNames;
import Chapter2StateMachines.WestWorldWithMessaging.Miner;
import Chapter2StateMachines.WestWorldWithMessaging.State;
import Chapter2StateMachines.WestWorldWithMessaging.location_type;
import static Chapter2StateMachines.WestWorldWithMessaging.EntityNames.GetNameOfEntity;
import static common.misc.ConsoleUtils.*;
import static Chapter2StateMachines.WestWorldWithMessaging.MessageDispatcher.*;
import static common.windows.*;
import static common.Time.CrudeTimer.*;

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

        //let the wife know I'm home
        Dispatch.DispatchMessage(SEND_MSG_IMMEDIATELY, //time delay
                pMiner.ID(), //ID of sender
                EntityNames.ent_Elsa.id, //ID of recipient
                MessageTypes.Msg_HiHoneyImHome, //the message
                NO_ADDITIONAL_INFO);
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
        //cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Leaving the house");
    }

    @Override
    public boolean OnMessage(Miner pMiner, Telegram msg) {
        SetTextColor(BACKGROUND_RED | FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE);

        switch (msg.Msg) {
            case Msg_StewReady:

                cout("\nMessage handled by " + GetNameOfEntity(pMiner.ID())
                        + " at time: " + Clock.GetCurrentTime());

                SetTextColor(FOREGROUND_RED | FOREGROUND_INTENSITY);

                cout("\n" + GetNameOfEntity(pMiner.ID())
                        + ": Okay Hun, ahm a comin'!");

                pMiner.GetFSM().ChangeState(EatStew.Instance());

                return true;

        }//end switch

        return false; //send message to global message handler
    }
}