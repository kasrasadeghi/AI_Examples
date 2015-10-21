/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.WestWorld1.MinerOwnedStates;

import Chapter2StateMachines.WestWorld1.Miner;
import Chapter2StateMachines.WestWorld1.State;
import Chapter2StateMachines.WestWorld1.location_type;
import static Chapter2StateMachines.WestWorld1.EntityNames.GetNameOfEntity;
import static common.misc.ConsoleUtils.*;
import static common.windows.*;

//------------------------------------------------------------------------
//
//  Entity will go to a bank and deposit any nuggets he is carrying. If the 
//  miner is subsequently wealthy enough he'll walk home, otherwise he'll
//  keep going to get more gold
//------------------------------------------------------------------------
public class VisitBankAndDepositGold extends State {

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
            SetTextColor(FOREGROUND_RED | FOREGROUND_INTENSITY);
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Goin' to the bank. Yes siree");

            pMiner.ChangeLocation(location_type.bank);
        }
    }

    @Override
    public void Execute(Miner pMiner) {

        //deposit the gold
        pMiner.AddToWealth(pMiner.GoldCarried());

        pMiner.SetGoldCarried(0);

        SetTextColor(FOREGROUND_RED | FOREGROUND_INTENSITY);
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                + "Depositing gold. Total savings now: " + pMiner.Wealth());

        //wealthy enough to have a well earned rest?
        if (pMiner.Wealth() >= Miner.ComfortLevel) {
            SetTextColor(FOREGROUND_RED | FOREGROUND_INTENSITY);
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                    + "WooHoo! Rich enough for now. Back home to mah li'lle lady");

            pMiner.ChangeState(GoHomeAndSleepTilRested.Instance());
        } //otherwise get more gold
        else {
            pMiner.ChangeState(EnterMineAndDigForNugget.Instance());
        }
    }

    @Override
    public void Exit(Miner pMiner) {
        SetTextColor(FOREGROUND_RED | FOREGROUND_INTENSITY);
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Leavin' the bank");
    }
}
