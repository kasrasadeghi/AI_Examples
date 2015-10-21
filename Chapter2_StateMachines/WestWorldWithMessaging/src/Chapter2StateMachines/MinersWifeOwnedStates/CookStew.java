/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.MinersWifeOwnedStates;

import common.Messaging.Telegram;
import Chapter2StateMachines.MessageTypes;
import Chapter2StateMachines.MinersWife;
import Chapter2StateMachines.State;
import static Chapter2StateMachines.EntityNames.*;
import static common.misc.ConsoleUtils.*;
import static common.windows.*;
import static Chapter2StateMachines.MessageDispatcher.*;
import static common.Time.CrudeTimer.*;

class CookStew extends State<MinersWife> {

    static final CookStew instance = new CookStew();

    private CookStew() {
    }

    //copy ctor and assignment should be private
    private CookStew(CookStew cs) {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    public static CookStew Instance() {
        return instance;
    }

    @Override
    public void Enter(MinersWife wife) {
        //if not already cooking put the stew in the oven
        if (!wife.Cooking()) {
            cout("\n" + GetNameOfEntity(wife.ID()) + ": Putting the stew in the oven");

            //send a delayed message myself so that I know when to take the stew
            //out of the oven
            Dispatch.DispatchMessage(1.5, //time delay
                    wife.ID(), //sender ID
                    wife.ID(), //receiver ID
                    MessageTypes.Msg_StewReady, //msg
                    NO_ADDITIONAL_INFO);

            wife.SetCooking(true);
        }
    }

    @Override
    public void Execute(MinersWife wife) {
        cout("\n" + GetNameOfEntity(wife.ID()) + ": Fussin' over food");
    }

    @Override
    public void Exit(MinersWife wife) {
        SetTextColor(FOREGROUND_GREEN | FOREGROUND_INTENSITY);

        cout("\n" + GetNameOfEntity(wife.ID()) + ": Puttin' the stew on the table");
    }

    @Override
    public boolean OnMessage(MinersWife wife, Telegram msg) {
        SetTextColor(BACKGROUND_RED | FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE);

        switch (msg.Msg) {
            case Msg_StewReady: {
                cout("\nMessage received by " + GetNameOfEntity(wife.ID())
                        + " at time: " + Clock.GetCurrentTime());

                SetTextColor(FOREGROUND_GREEN | FOREGROUND_INTENSITY);
                cout("\n" + GetNameOfEntity(wife.ID()) + ": StewReady! Lets eat");

                //let hubby know the stew is ready
                Dispatch.DispatchMessage(SEND_MSG_IMMEDIATELY,
                        wife.ID(),
                        ent_Miner_Bob.id,
                        MessageTypes.Msg_StewReady,
                        NO_ADDITIONAL_INFO);

                wife.SetCooking(false);

                wife.GetFSM().ChangeState(DoHouseWork.Instance());
            }

            return true;

        }//end switch

        return false;
    }
}