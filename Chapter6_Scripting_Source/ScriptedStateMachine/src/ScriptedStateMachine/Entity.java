/**
 * @author Petr (http://www.sallyx.org/)
 *
 *  Desc:   Base class for a game object
 */
package ScriptedStateMachine;

public abstract class Entity {

    private int m_ID;
    private String m_Name;
    private static int NextID = 0;
    //used by the constructor to give each entity a unique ID

    private int NextValidID() {
        return NextID++;
    }

    public Entity() {
        this("NoName");
    }

    public Entity(String name) {
        m_ID = NextValidID();
        m_Name = name;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    //all entities must implement an update function
    public abstract void Update();

    //accessors
    public int ID() {
        return m_ID;
    }

    public String Name() {
        return m_Name;
    }
}
