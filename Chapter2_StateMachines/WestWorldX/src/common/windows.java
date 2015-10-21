/**
 * @author Petr (http://www.sallyx.org/)
 */
package common;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author Petr
 */
final public class windows {
    static public int FOREGROUND_BLUE = 0x0001;
    static public int FOREGROUND_GREEN = 0x0002;
    static public int FOREGROUND_RED = 0x0004;
    static public int FOREGROUND_INTENSITY = 0x0008;
    
    static public int BACKGROUND_RED = 0x0040;
    
    static public class POINT extends Point2D.Double {

        @Override
        public void setLocation(double x, double y) {
            super.setLocation(Math.round(x), Math.round(y));
        }
    }

    static public class POINTS extends Point {
    }
}
