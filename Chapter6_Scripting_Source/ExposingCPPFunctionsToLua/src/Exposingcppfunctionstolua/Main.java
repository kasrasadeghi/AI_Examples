/**
 * @author Petr (http://www.sallyx.org/)
 */
package Exposingcppfunctionstolua;
// Do not forget to include luaj-jse-2.0.2.jar 
// If you also add to Libraries luaj-sources-2.0.2.jar,
// NetBeans will show help for luaj methods and classes.
// You can find both in Common/luaj-2.0.2/lib/

import javax.script.ScriptEngine;
import static common.lua.LuaHelperFunctions.RunLuaScript;
import static common.lua.LuaHelperFunctions.lua_open;
import static common.lua.LuaHelperFunctions.lua_register;

public class Main {

    //define a couple of simple functions
    public static void HelloWorld() {
        System.out.println("\n[Java]: Hello World!");
    }

    public static int add(int a, int b) {
        return a + b;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //create a lua state and open lua libraries
        ScriptEngine pLua = lua_open();

        lua_register(pLua, "HelloWorld", Main.class);
        lua_register(pLua, "add", Main.class);


        //load and run the script
        RunLuaScript(pLua, "ExposingCPPFunctionsToLua.lua");

    }
}
