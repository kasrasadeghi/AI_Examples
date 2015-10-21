/**
 * @author Petr (http://www.sallyx.org/)
 * Desc:   A simple scripted state machine class. Inherit from this class and 
 *         create some states in Lua to give your agents FSM functionality
 */
package ScriptedStateMachine;

import common.lua.LuaHelperFunctions.LuabindObject;
import org.luaj.vm2.LuaValue;

public class ScriptedStateMachine<entity_type extends Entity> {

    /**
     * pointer to the agent that owns this instance
     */
    private entity_type m_pOwner;
    /**
     * the current state is a lua table of lua functions. A table may be
     * represented in Java using a LuaTable
     */
    private LuabindObject m_CurrentState;

    public ScriptedStateMachine(entity_type owner) {
        m_pOwner = owner;
    }

    /**
     * use these methods to initialize the FSM
     */
    public void SetCurrentState(final LuabindObject s) {
        m_CurrentState = s;
    }

    /**
     * call this to update the FSM
     */
    public void Update() {
        //make sure the state is valid before calling its Execute 'method'
        if (m_CurrentState.is_valid()) {
            m_CurrentState.call("Execute", m_pOwner);
        }
    }

    //change to a new state
    public void ChangeState(final LuaValue new_state) {

        //call the exit method of the existing state
        m_CurrentState.call("Exit",m_pOwner);

	//change state to the new state
	m_CurrentState = new LuabindObject(new_state);

        //call the entry method of the new state
        m_CurrentState.call("Enter",m_pOwner);
  }

  /**
   * retrieve the current state
   */
  public LuabindObject CurrentState() {
        return m_CurrentState;
    }
}