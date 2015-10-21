/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.MinersWifeOwnedStates;

import Chapter2StateMachines.MinersWife;
import Chapter2StateMachines.State;
import static common.misc.utils.*;

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
        //1 in 10 chance of needing the bathroom
        if (RandFloat() < 0.1) {
            wife.GetFSM().ChangeState(VisitBathroom.Instance());
        }
    }

    @Override
    public void Exit(MinersWife wife) {
    }
}