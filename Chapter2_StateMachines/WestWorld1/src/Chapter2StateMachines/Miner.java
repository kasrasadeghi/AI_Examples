/**
 * A class defining a goldminer.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines;

import Chapter2StateMachines.MinerOwnedStates.GoHomeAndSleepTilRested;

public class Miner extends BaseGameEntity {
    //the amount of gold a miner must have before he feels comfortable
    final public static int ComfortLevel = 5;
    //the amount of nuggets a miner can carry
    final public static int MaxNuggets = 3;
    //above this value a miner is thirsty
    final public static int ThirstLevel = 5;
    //above this value a miner is sleepy
    final public static int TirednessThreshold = 5;
    
    private State m_pCurrentState;
    private location_type m_Location;
    //how many nuggets the miner has in his pockets
    private int m_iGoldCarried;
    private int m_iMoneyInBank;
    //the higher the value, the thirstier the miner
    private int m_iThirst;
    //the higher the value, the more tired the miner
    private int m_iFatigue;

    public Miner(EntityNames id) {
        super(id);
        m_Location = location_type.shack;
        m_iGoldCarried = 0;
        m_iMoneyInBank = 0;
        m_iThirst = 0;
        m_iFatigue = 0;
        m_pCurrentState = GoHomeAndSleepTilRested.Instance();
    }

//--------------------------- ChangeState -------------------------------------
    //this method changes the current state to the new state. It first
    //calls the Exit() method of the current state, then assigns the
    //new state to m_pCurrentState and finally calls the Entry()
    //method of the new state.
//-----------------------------------------------------------------------------
    public void ChangeState(State pNewState) {
        //make sure both states are both valid before attempting to 
        //call their methods
        assert m_pCurrentState != null : pNewState;

        //call the exit method of the existing state
        m_pCurrentState.Exit(this);

        //change state to the new state
        m_pCurrentState = pNewState;

        //call the entry method of the new state
        m_pCurrentState.Enter(this);
    }

//-----------------------------------------------------------------------------
    public void AddToGoldCarried(int val) {
        m_iGoldCarried += val;

        if (m_iGoldCarried < 0) {
            m_iGoldCarried = 0;
        }
    }

//-----------------------------------------------------------------------------
    public void AddToWealth(int val) {
        m_iMoneyInBank += val;

        if (m_iMoneyInBank < 0) {
            m_iMoneyInBank = 0;
        }
    }

//-----------------------------------------------------------------------------
    public boolean Thirsty() {
        if (m_iThirst >= ThirstLevel) {
            return true;
        }

        return false;
    }

//-----------------------------------------------------------------------------
    @Override
    public void Update() {
        m_iThirst += 1;
        
        if (m_pCurrentState != null) {
            m_pCurrentState.Execute(this);
        }
    }

//-----------------------------------------------------------------------------
   public boolean Fatigued() {
        if (m_iFatigue > TirednessThreshold) {
            return true;
        }

        return false;
    }

    public location_type Location() {
        return m_Location;
    }

    public void ChangeLocation(location_type loc) {
        m_Location = loc;
    }

    public int GoldCarried() {
        return m_iGoldCarried;
    }

    public void SetGoldCarried(int val) {
        m_iGoldCarried = val;
    }

    public boolean PocketsFull() {
        return m_iGoldCarried >= MaxNuggets;
    }

    public void DecreaseFatigue() {
        m_iFatigue -= 1;
    }

    public void IncreaseFatigue() {
        m_iFatigue += 1;
    }

    public int Wealth() {
        return m_iMoneyInBank;
    }

    public void SetWealth(int val) {
        m_iMoneyInBank = val;
    }

    public void BuyAndDrinkAWhiskey() {
        m_iThirst = 0;
        m_iMoneyInBank -= 2;
    }
};