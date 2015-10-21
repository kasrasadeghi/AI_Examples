/**
 * A class defining a goldminer.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines;

import common.Messaging.Telegram;
import Chapter2StateMachines.MinerOwnedStates.GoHomeAndSleepTilRested;
import static common.windows.*;
import static common.misc.ConsoleUtils.*;

public class Miner extends BaseGameEntity {
    //the amount of gold a miner must have before he feels comfortable
    final public static int ComfortLevel = 5;
    //the amount of nuggets a miner can carry
    final public static int MaxNuggets = 3;
    //above this value a miner is thirsty
    final public static int ThirstLevel = 5;
    //above this value a miner is sleepy
    final public static int TirednessThreshold = 5;
    
    private StateMachine<Miner> m_pStateMachine;
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
        
        m_pStateMachine = new StateMachine<Miner>(this);
        m_pStateMachine.SetCurrentState(GoHomeAndSleepTilRested.Instance());
        
        /* NOTE, A GLOBAL STATE HAS NOT BEEN IMPLEMENTED FOR THE MINER */
    }
    
    @Override
    protected void finalize() throws Throwable { 
        super.finalize();
        this.m_pStateMachine = null;
    }
    
    public StateMachine<Miner> GetFSM() {return m_pStateMachine;}
 
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
        SetTextColor(FOREGROUND_RED| FOREGROUND_INTENSITY);
        m_iThirst += 1;
        m_pStateMachine.Update();
    }
    
@Override
public boolean HandleMessage(Telegram msg)
{
  return m_pStateMachine.HandleMessage(msg);
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