/**
 * abstract base class to define an interface for a state
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package Chapter2StateMachines.WestWorld1;

public abstract class State {

  @Override
  public void finalize() throws Throwable{ super.finalize();}

  //this will execute when the state is entered
  abstract public void Enter(Miner miner);

  //this is the state's normal update function
  abstract public void Execute(Miner miner);

  //this will execute when the state is exited. (My word, isn't
  //life full of surprises... ;o))
  abstract public void Exit(Miner miner);
}
