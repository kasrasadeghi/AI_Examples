/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.MinerOwnedStates;

import common.Messaging.Telegram;
import Chapter2StateMachines.Miner;
import Chapter2StateMachines.State;
import Chapter2StateMachines.location_type;
import static Chapter2StateMachines.EntityNames.GetNameOfEntity;
import static common.misc.ConsoleUtils.*;

//------------------------------------------------------------------------
//
//  Entity will go to a bank and deposit any nuggets he is carrying. If the 
//  miner is subsequently wealthy enough he'll walk home, otherwise he'll
//  keep going to get more gold
//------------------------------------------------------------------------
public class VisitBankAndDepositGold extends State<Miner> {

    static final VisitBankAndDepositGold instance = new VisitBankAndDepositGold();

    private VisitBankAndDepositGold() {
    }

    //copy ctor and assignment should be private
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    //this is a singleton
    static public VisitBankAndDepositGold Instance() {
        return instance;
    }

    @Override
    public void Enter(Miner pMiner) {
        //on entry the miner makes sure he is located at the bank
        if (pMiner.Location() != location_type.bank) {
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Goin' to the bank. Yes siree");

            pMiner.ChangeLocation(location_type.bank);
        }
    }

    @Override
    public void Execute(Miner pMiner) {

        //deposit the gold
        pMiner.AddToWealth(pMiner.GoldCarried());

        pMiner.SetGoldCarried(0);

        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                + "Depositing gold. Total savings now: " + pMiner.Wealth());

        //wealthy enough to have a well earned rest?
        if (pMiner.Wealth() >= Miner.ComfortLevel) {
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                    + "WooHoo! Rich enough for now. Back home to mah li'lle lady");

            pMiner.GetFSM().ChangeState(GoHomeAndSleepTilRested.Instance());
        } //otherwise get more gold
        else {
            pMiner.GetFSM().ChangeState(EnterMineAndDigForNugget.Instance());
        }
    }

    @Override
    public void Exit(Miner pMiner) {
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Leavin' the bank");
    }

    @Override
    public boolean OnMessage(Miner pMiner, Telegram msg) {
        //send msg to global message handler
        return false;
    }
}
