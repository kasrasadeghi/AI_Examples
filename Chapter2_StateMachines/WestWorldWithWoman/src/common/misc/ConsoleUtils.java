/**
 * Just a few handy utilities for dealing with consoles
 * @author Petr (http://www.sallyx.org/)
 */
package common.misc;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import static common.windows.*;

public class ConsoleUtils {
    /* Use swing component or console for output */
    private static boolean useSwing = false;
    private static final JConsole frame = new JConsole();
    public static void enableSwing() {
            useSwing = true;
            frame.setVisible(true);
    }

    // dummy metod
    static public void SetTextColor(int colors) {
        // try jansi.fusesource.org for colored text
        if (!useSwing) {
            return;
        }
        if ((colors & FOREGROUND_RED) != 0) {
            frame.setColor(Color.RED);
        }
        if ((colors & FOREGROUND_GREEN) != 0) {
            frame.setColor(Color.GREEN);
        }
        if ((colors & FOREGROUND_BLUE) != 0) {
            frame.setColor(Color.BLUE);
        }
        if ((colors & FOREGROUND_BLUE) != 0
                && (colors & FOREGROUND_RED) != 0
                && (colors & FOREGROUND_GREEN) != 0) {
            frame.setColor(Color.WHITE);
        }
        if ((colors & common.windows.FOREGROUND_INTENSITY) != 0) {
            frame.setFont(new Font("Serif", Font.BOLD, 14));
        }
        if((colors & common.windows.BACKGROUND_RED) != 0) {
            frame.setBGColor(Color.RED);
        }
        else {
            frame.setBGColor(Color.BLACK);
        }
    }

    synchronized static public void cout(String s) {
        if (!useSwing) {
            System.out.print(s);
            return;
        }
        frame.addString(s);
    }

    static public void PressAnyKeyToContinue() {
        //change text color to white
        SetTextColor(FOREGROUND_BLUE | FOREGROUND_RED | FOREGROUND_GREEN);
        if (!useSwing) {
            cout("\n\nPress ENTER to continue" + "\n");
            try {
                System.in.read();
            } catch (IOException ex) {
            }
            return;
        }
        cout("\n\nPress any key to continue\n");
        frame.closeOnType();
    }
}