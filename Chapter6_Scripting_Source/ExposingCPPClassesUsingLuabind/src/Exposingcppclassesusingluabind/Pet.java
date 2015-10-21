/**
 * @author Petr (http://www.sallyx.org/)
 */
package Exposingcppclassesusingluabind;

public class Pet extends Animal {

    private String m_Name;

    public Pet(String name, String noise, int NumLegs) {
        super(noise, NumLegs);
        m_Name = name;
    }

    public String GetName() {
        return m_Name;
    }
}
