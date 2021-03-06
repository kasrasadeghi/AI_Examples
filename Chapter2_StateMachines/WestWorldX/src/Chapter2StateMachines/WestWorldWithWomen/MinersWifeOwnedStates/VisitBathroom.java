/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.WestWorldWithWomen.MinersWifeOwnedStates;

import Chapter2StateMachines.WestWorldWithWomen.MinersWife;
import Chapter2StateMachines.WestWorldWithWomen.State;
import static Chapter2StateMachines.WestWorldWithWomen.EntityNames.*;
import static common.misc.ConsoleUtils.*;

public class VisitBathroom extends State<MinersWife> {

    static final VisitBathroom instance = new VisitBathroom();

    private VisitBathroom() {
    }

    //copy ctor and assignment should be private
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    public static VisitBathroom Instance() {
        return instance;
    }

    @Override
    public void Enter(MinersWife wife) {
        cout("\n" + GetNameOfEntity(wife.ID())
                + ": Walkin' to the can. Need to powda mah pretty li'lle nose");
    }

    @Override
    public void Execute(MinersWife wife) {
        cout("\n" + GetNameOfEntity(wife.ID()) + ": Ahhhhhh! Sweet relief!");
        wife.GetFSM().RevertToPreviousState();
    }

    @Override
    public void Exit(MinersWife wife) {
        cout("\n" + GetNameOfEntity(wife.ID()) + ": Leavin' the Jon");
    }
}
