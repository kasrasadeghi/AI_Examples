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
//  In this state the miner will walk to a goldmine and pick up a nugget
//  of gold. If the miner already has a nugget of gold he'll change state
//  to VisitBankAndDepositGold. If he gets thirsty he'll change state
//  to QuenchThirst
//------------------------------------------------------------------------
public class EnterMineAndDigForNugget extends State<Miner> {

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
            cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Walkin' to the goldmine");
            pMiner.ChangeLocation(location_type.goldmine);
        }
    }

    @Override
    public void Execute(Miner pMiner) {
        //if the miner is at the goldmine he digs for gold until he
        //is carrying in excess of MaxNuggets. If he gets thirsty during
        //his digging he packs up work for a while and changes state to
        //gp to the saloon for a whiskey.
        pMiner.AddToGoldCarried(1);

        pMiner.IncreaseFatigue();

        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": " + "Pickin' up a nugget");

        //if enough gold mined, go and put it in the bank
        if (pMiner.PocketsFull()) {
            pMiner.GetFSM().ChangeState(VisitBankAndDepositGold.Instance());
        }

        if (pMiner.Thirsty()) {
            pMiner.GetFSM().ChangeState(QuenchThirst.Instance());
        }
    }

    @Override
    public void Exit(Miner pMiner) {
        cout("\n" + GetNameOfEntity(pMiner.ID()) + ": "
                + "Ah'm leavin' the goldmine with mah pockets full o' sweet gold");
    }

    @Override
    public boolean OnMessage(Miner pMiner, Telegram msg) {
        //send msg to global message handler
        return false;
    }
};