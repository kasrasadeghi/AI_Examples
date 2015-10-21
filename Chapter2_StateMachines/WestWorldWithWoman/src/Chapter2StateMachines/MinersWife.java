/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines;

import Chapter2StateMachines.MinersWifeOwnedStates.WifesGlobalState;
import Chapter2StateMachines.MinersWifeOwnedStates.DoHouseWork;
import static common.windows.*;
import static common.misc.ConsoleUtils.*;

public class MinersWife extends BaseGameEntity {

    //an instance of the state machine class
    private StateMachine<MinersWife> m_pStateMachine;
    private location_type m_Location;

    public MinersWife(EntityNames id) {
        super(id);
        m_Location = location_type.shack;
        m_pStateMachine = new StateMachine<MinersWife>(this);
        m_pStateMachine.SetCurrentState(DoHouseWork.Instance());
        m_pStateMachine.SetGlobalState(WifesGlobalState.Instance());
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        m_pStateMachine = null;
    }

    @Override
    public void Update() {
        //set text color to green
        SetTextColor(FOREGROUND_GREEN | FOREGROUND_INTENSITY);

        m_pStateMachine.Update();
    }

    public StateMachine<MinersWife> GetFSM() {
        return m_pStateMachine;
    }

    //----------------------------------------------------accessors
    public location_type Location() {
        return m_Location;
    }

    public void ChangeLocation(location_type loc) {
        m_Location = loc;
    }
}