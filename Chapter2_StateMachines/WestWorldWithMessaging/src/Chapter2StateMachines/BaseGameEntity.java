/**
 * Base class for a game object
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines;

import common.Messaging.Telegram;


public abstract class BaseGameEntity {

    //every entity must have a unique identifying number
    private int m_ID = 0;
    //this is the next valid ID. Each time a BaseGameEntity is instantiated
    //this value is updated
    static private int m_iNextValidID = 0;

    public BaseGameEntity(EntityNames id) {
        SetID(id.id);
    }

//----------------------------- SetID -----------------------------------------
//
//  this must be called within each constructor to make sure the ID is set
//  correctly. It verifies that the value passed to the method is greater
//  or equal to the next valid ID, before setting the ID and incrementing
//  the next valid ID
//-----------------------------------------------------------------------------
    public void SetID(int val) {
        //make sure the val is equal to or greater than the next available ID
        assert (val >= m_iNextValidID) : "<BaseGameEntity::SetID>: invalid ID";

        m_ID = val;

        m_iNextValidID = m_ID + 1;
    }

    @Override
    protected void finalize() throws Throwable{super.finalize();}

    //all entities must implement an update function
    abstract public void Update();

  //all entities can communicate using messages. They are sent
  //using the MessageDispatcher singleton class
  abstract public boolean  HandleMessage(Telegram msg);
  
    public int ID() {
        return m_ID;
    }
};