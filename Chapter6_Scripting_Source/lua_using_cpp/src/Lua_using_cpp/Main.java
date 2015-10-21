/**
 * @author Petr (http://www.sallyx.org/)
 */
package Lua_using_cpp;
// Do not forget to include luaj-jse-2.0.2.jar 
// If you also add to Libraries luaj-sources-2.0.2.jar,
// NetBeans will show help for luaj methods and classes.
// You can find both in Common/luaj-2.0.2/lib/

import javax.script.ScriptEngine;
import static common.lua.LuaHelperFunctions.RunLuaScript;
import static common.lua.LuaHelperFunctions.lua_open;
import static common.lua.LuaHelperFunctions.lua_register;
import javax.script.ScriptException;

/**
 *
 * @author Petr
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ScriptException {
        //create a lua state and open lua libs
        ScriptEngine pL = lua_open();

        //register the functions with lua
        lua_register(pL, "cpp_GetAIMove", RockPaperScissors.class);
        lua_register(pL, "cpp_EvaluateTheGuesses", RockPaperScissors.class);

        //run the script
        RunLuaScript(pL, "Rock_Paper_Scissors_Using_C++_Funcs.lua");

        System.out.print("\n\n\n");

    }
}
