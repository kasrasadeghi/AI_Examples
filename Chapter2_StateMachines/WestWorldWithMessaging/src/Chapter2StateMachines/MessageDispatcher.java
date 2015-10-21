/**
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines;

import java.util.TreeSet;
import common.Messaging.Telegram;
import static common.misc.ConsoleUtils.*;
import static common.windows.*;
import static common.Time.CrudeTimer.*;
import static Chapter2StateMachines.EntityNames.*;
import static Chapter2StateMachines.MessageTypes.*;
import static Chapter2StateMachines.EntityManager.*;

public class MessageDispatcher {
    //to make code easier to read

    final public static double SEND_MSG_IMMEDIATELY = 0.0f;
    final public static Object NO_ADDITIONAL_INFO = null;
    //to make life easier...
    final public static MessageDispatcher Dispatch = new MessageDispatcher();
    //a Set is used as the container for the delayed messages
    //because of the benefit of automatic sorting and avoidance
    //of duplicates. Messages are sorted by their dispatch time.
    private TreeSet<Telegram> PriorityQ = new TreeSet<Telegram>();

    /**
     * this method is utilized by DispatchMessage or DispatchDelayedMessages.
     * This method calls the message handling member function of the receiving
     * entity, pReceiver, with the newly created telegram
     */
    private void Discharge(BaseGameEntity pReceiver, Telegram msg) {
        if (!pReceiver.HandleMessage(msg)) {
            //telegram could not be handled
            cout("\nMessage not handled");
        }
    }

    private MessageDispatcher() {
    }

    //copy ctor and assignment should be private
    private MessageDispatcher(MessageDispatcher d) {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    //this class is a singleton
    public static MessageDispatcher Instance() {
        return Dispatch;
    }

    /**
     *  given a message, a receiver, a sender and any time delay , this function
     *  routes the message to the correct agent (if no delay) or stores
     *  in the message queue to be dispatched at the correct time
     */
    public void DispatchMessage(double delay,
            int sender,
            int receiver,
            MessageTypes msg,
            Object ExtraInfo) {
        SetTextColor(BACKGROUND_RED | FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE);

        //get pointers to the sender and receiver
        BaseGameEntity pSender = EntityMgr.GetEntityFromID(sender);
        BaseGameEntity pReceiver = EntityMgr.GetEntityFromID(receiver);

        //make sure the receiver is valid
        if (pReceiver == null) {
            cout("\nWarning! No Receiver with ID of " + receiver + " found");

            return;
        }
        //create the telegram
        Telegram telegram = new Telegram(0, sender, receiver, msg, ExtraInfo);

        //if there is no delay, route telegram immediately                       
        if (delay <= 0.0f) {
            cout("\nInstant telegram dispatched at time: " + Clock.GetCurrentTime()
                    + " by " + GetNameOfEntity(pSender.ID()) + " for "
                    + GetNameOfEntity(pReceiver.ID())
                    + ". Msg is " + MsgToStr(msg));

            //send the telegram to the recipient
            Discharge(pReceiver, telegram);
        } //else calculate the time when the telegram should be dispatched
        else {
            double CurrentTime = Clock.GetCurrentTime();

            telegram.DispatchTime = CurrentTime + delay;

            //and put it in the queue
            PriorityQ.add(telegram);

            cout("\nDelayed telegram from " + GetNameOfEntity(pSender.ID())
                    + " recorded at time " + Clock.GetCurrentTime() + " for "
                    + GetNameOfEntity(pReceiver.ID()) + ". Msg is " + MsgToStr(msg));
        }
    }

    /**
     * This function dispatches any telegrams with a timestamp that has
     *  expired. Any dispatched telegrams are removed from the queue
     *
     * send out any delayed messages. This method is called each time through   
     * the main game loop.
     */
    public void DispatchDelayedMessages() {
        SetTextColor(BACKGROUND_RED | FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE);

        //get current time
        double CurrentTime = Clock.GetCurrentTime();

        //now peek at the queue to see if any telegrams need dispatching.
        //remove all telegrams from the front of the queue that have gone
        //past their sell by date
        while (!PriorityQ.isEmpty()
                && (PriorityQ.last().DispatchTime < CurrentTime)
                && (PriorityQ.last().DispatchTime > 0)) {
            //read the telegram from the front of the queue

            final Telegram telegram = PriorityQ.last();

            //find the recipient
            BaseGameEntity pReceiver = EntityMgr.GetEntityFromID(telegram.Receiver);

            cout("\nQueued telegram ready for dispatch: Sent to "
                    + GetNameOfEntity(pReceiver.ID()) + ". Msg is " + MsgToStr(telegram.Msg));

            //send the telegram to the recipient
            Discharge(pReceiver, telegram);

            //remove it from the queue
            PriorityQ.remove(PriorityQ.last());
        }
    }
}