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
//  In this state the miner will walk to a goldmine and pick up a nugget
//  of gold. If the miner already has a nugget of gold he'll change state
//  to VisitBankAndDepositGold. If he gets thirsty he'll change state
//  to QuenchThirst
//------------------------------------------------------------------------
public class EnterMineAndDigForNugget extends State {

    static final EnterMineAndDigForNugget instance = new EnterMineAndDigForNugget();

    private EnterMineAndDigForNugget() {
    }

    //copy ctor and assignment should be private
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    //this is a singleton
    public static EnterMineAndDigForNugget Instance() {
        return instance;
    }

    @Override
    public void Enter(Miner pMiner) {
        //if the miner is not already located at the goldmine, he must
        //change location to the gold mine
        if (pMiner.Location() != location_type.goldmine) {
            SetTextColor(FOREGROUND_RED | FOREGROUND_INTENSITY);
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Walkin' to the goldmine");

            pMiner.ChangeLocation(location_type.goldmine);
        }
    }

    @Override
    public void Execute(Miner pMiner) {
        //the miner digs for gold until he is carrying in excess of MaxNuggets. 
        //If he gets thirsty during his digging he packs up work for a while and 
        //changes state to go to the saloon for a whiskey.
        pMiner.AddToGoldCarried(1);

        pMiner.IncreaseFatigue();

        SetTextColor(FOREGROUND_RED | FOREGROUND_INTENSITY);
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Pickin' up a nugget");

        //if enough gold mined, go and put it in the bank
        if (pMiner.PocketsFull()) {
            pMiner.ChangeState(VisitBankAndDepositGold.Instance());
        }

        if (pMiner.Thirsty()) {
            pMiner.ChangeState(QuenchThirst.Instance());
        }
    }

    @Override
    public void Exit(Miner pMiner) {
        SetTextColor(FOREGROUND_RED| FOREGROUND_INTENSITY);
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                + "Ah'm leavin' the goldmine with mah pockets full o' sweet gold");
    }
};