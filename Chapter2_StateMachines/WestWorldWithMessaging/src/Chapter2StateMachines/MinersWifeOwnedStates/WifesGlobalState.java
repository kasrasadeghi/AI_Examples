/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.MinersWifeOwnedStates;

import common.Messaging.Telegram;
import Chapter2StateMachines.MinersWife;
import Chapter2StateMachines.State;
import static common.misc.utils.*;
import static common.misc.ConsoleUtils.*;
import static common.Time.CrudeTimer.*;
import static common.windows.*;
import static Chapter2StateMachines.MessageTypes.*;
import static Chapter2StateMachines.EntityNames.*;

public class WifesGlobalState extends State<MinersWife> {

    static final WifesGlobalState instance = new WifesGlobalState();

    private WifesGlobalState() {
    }

    //copy ctor and assignment should be private
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    public static WifesGlobalState Instance() {
        return instance;
    }

    @Override
    public void Enter(MinersWife wife) {
    }

    @Override
    public void Execute(MinersWife wife) {
        //1 in 10 chance of needing the bathroom(provided she is not already
        //in the bathroom)
        if ((RandFloat() < 0.1)
                && !wife.GetFSM().isInState(VisitBathroom.Instance())) {
            wife.GetFSM().ChangeState(VisitBathroom.Instance());
        }
    }

    @Override
    public void Exit(MinersWife wife) {
    }

    @Override
    public boolean OnMessage(MinersWife wife, Telegram msg) {
        SetTextColor(BACKGROUND_RED | FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE);

        switch (msg.Msg) {
            case Msg_HiHoneyImHome: {
                cout("\nMessage handled by " + GetNameOfEntity(wife.ID()) + " at time: "
                        + Clock.GetCurrentTime());

                SetTextColor(FOREGROUND_GREEN | FOREGROUND_INTENSITY);

                cout("\n" + GetNameOfEntity(wife.ID())
                        + ": Hi honey. Let me make you some of mah fine country stew");

                wife.GetFSM().ChangeState(CookStew.Instance());
            }

            return true;

        }//end switch

        return false;
    }
}