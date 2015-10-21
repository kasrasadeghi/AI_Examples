/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines;

import common.Messaging.Telegram;
import Chapter2StateMachines.MinersWifeOwnedStates.WifesGlobalState;
import Chapter2StateMachines.MinersWifeOwnedStates.DoHouseWork;
import static common.windows.*;
import static common.misc.ConsoleUtils.*;

public class MinersWife extends BaseGameEntity {

    //an instance of the state machine class
    private StateMachine<MinersWife> m_pStateMachine;
    private location_type m_Location;

  //is she presently cooking?
  boolean            m_bCooking;
  
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
      
    @Override
  public boolean  HandleMessage(Telegram msg) {      
  return m_pStateMachine.HandleMessage(msg);
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
    public boolean          Cooking() {return m_bCooking;}
    public void  SetCooking(boolean val){m_bCooking = val;}
}