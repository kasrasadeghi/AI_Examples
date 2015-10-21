/**
 * @author Petr (http://www.sallyx.org/)
 */
package starthere;

//Do not forget to include luaj-jse-2.0.2.jar 
//If you also add to Libraries luaj-sources-2.0.2.jar,
//NetBeans will show help for luaj methods and classes.
// You can find both in Common/luaj-2.0.2/lib/
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String script = "starthere/your_first_lua_script.lua";

        // Create a standard set of globals for JSE including all the libraries.
        LuaTable _G = JsePlatform.standardGlobals();
        try {
            //get Lua dofile function 'dofile' and call it with script name as an argument
            _G.get("dofile").call(LuaValue.valueOf(script));
        } catch (LuaError error) {
            System.err.println("\n[Java]: ERROR(" + error.getMessage()
                    + "): Problem with lua script file!\n\n");
        }
    }
}
