/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.MinersWifeOwnedStates;

import Chapter2StateMachines.MinersWife;
import Chapter2StateMachines.State;
import static Chapter2StateMachines.EntityNames.*;
import static common.misc.ConsoleUtils.*;
import static common.misc.utils.*;

public class DoHouseWork extends State<MinersWife> {

    static final DoHouseWork instance = new DoHouseWork();

    private DoHouseWork() {
    }

    //copy ctor and assignment should be private
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    public static DoHouseWork Instance() {
        return instance;
    }

    @Override
    public void Enter(MinersWife wife) {
    }

    @Override
    public void Execute(MinersWife wife) {
        int r = RandInt(0,2);
        switch (r) {
            case 0:
                cout("\n" + GetNameOfEntity(wife.ID()) + ": Moppin' the floor");
                break;
            case 1:
                cout("\n" + GetNameOfEntity(wife.ID()) + ": Washin' the dishes");
                break;
            case 2:
                cout("\n" + GetNameOfEntity(wife.ID()) + ": Makin' the bed");
                break;
        }
    }

    @Override
    public void Exit(MinersWife wife) {
    }
}