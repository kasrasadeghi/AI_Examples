/**
 * @author Petr (http://www.sallyx.org/)
 */
package Exposingcppclassesusingluabind;

public class Animal {

    private int m_iNumLegs;
    private String m_NoiseEmitted;

    public Animal(String NoiseEmitted, int NumLegs) {
        m_iNumLegs = NumLegs;
        m_NoiseEmitted = NoiseEmitted;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void Speak() {
        System.out.println("\n[Java]: " + m_NoiseEmitted);
    }

    public int NumLegs() {
        return m_iNumLegs;
    }
}
