/**
 * @author Petr (http://www.sallyx.org/)
 */
package Creatingclassesusingluabind;
// Do not forget to include luaj-jse-2.0.2.jar 
// If you also add to Libraries luaj-sources-2.0.2.jar,
// NetBeans will show help for luaj methods and classes.
// You can find both in Common/luaj-2.0.2/lib/

import javax.script.ScriptEngine;
import static common.lua.LuaHelperFunctions.RunLuaScript;
import static common.lua.LuaHelperFunctions.lua_open;
import static common.lua.LuaHelperFunctions.open;
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
        ScriptEngine pLua = lua_open();
        
        //open luabind
        open(pLua);

        //load and run the script
        RunLuaScript(pLua, "classes_in_lua.lua");
    }
}