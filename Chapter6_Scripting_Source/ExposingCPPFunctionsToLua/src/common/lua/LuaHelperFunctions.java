/**
 * @author Petr (http://www.sallyx.org/)
 */
package common.lua;

import java.io.InputStreamReader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

final public class LuaHelperFunctions {

    public static class LuaExceptionGuard {

        ScriptEngine pLua;

        LuaExceptionGuard(ScriptEngine L) {
            this.pLua = L;
        }

        @Override
        protected void finalize() {
            pLua = null;
        }
    };

    /**
     * runs a script file
     */
    public static void RunLuaScript(ScriptEngine L, String script_name) {
        try {
            L.eval(new InputStreamReader(ClassLoader.getSystemResourceAsStream(script_name)));
        } catch (Exception error) {
            throw new RuntimeException("ERROR(" + (error.getMessage())
                    + "): Problem with lua script file " + script_name);
        }
    }

    /**
     * a function template to retrieve a number from the lua stack
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T PopLuaNumber(ScriptEngine pL, final String name) {
        Object val = pL.get(name);
        if (val instanceof Number) {
            return (T) val;
        } else {
            String err = "<PopLuaNumber> Cannot retrieve: ";
            throw new RuntimeException(err + name);
        }
    }

    /**
     * a function template to retrieve a string from the lua stack
     */
    public static String PopLuaString(ScriptEngine pL, final String name) {
        //check that the variable is the correct type. If it is not throw an
        //exception
        Object val = pL.get(name);
        if (val instanceof String) {
            return (String) val;
        } else {
            String err = "<PopLuaString> Cannot retrieve: ";
            throw new RuntimeException(err + name);
        }
    }

    /**
     * a function template to retrieve a boolean from the lua stack
     */
    public static boolean PopLuaBool(ScriptEngine pL, final String name) {
        //check that the variable is the correct type. If it is not throw an
        //exception
        Object val = pL.get(name);
        if (val instanceof Boolean) {
            //grab the value, cast to the correct type and return
            return (Boolean) val;
        } else {
            String err = "<PopLuaBool> Cannot retrieve: ";
            throw new RuntimeException(err + name);
        }
    }

    public static String LuaPopStringFieldFromTable(LuaTable L, final String key) {
        LuaValue val = L.get(key);

        //check that the variable is the correct type. If it is not throw an
        //exception
        if (!(val instanceof LuaString)) {
            String err = "<LuaPopStringFieldFromTable> Cannot retrieve: ";
            throw new RuntimeException(err + key);
        }

        //grab the data
        return ((LuaString) val).toString();
    }

    public static <T extends Number> T LuaPopNumberFieldFromTable(LuaTable L, final String key) {
        LuaValue val = L.get(key);

        //check that the variable is the correct type. If it is not throw an
        //exception
        if (!(val instanceof LuaString)) {
            String err = "<LuaPopNumberFieldFromTable> Cannot retrieve: ";
            throw new RuntimeException(err + key);
        }

        //grab the data
        return (T) new Double(val.todouble());
    }

    //////////////////////// Lua Interface //////////////////////////////////////////
    public static ScriptEngine lua_open() {
        ScriptEngine pL = new ScriptEngineManager().getEngineByExtension(".lua");
        RunLuaScript(pL, "common/lua/LuaHelperFunctions.lua");
        return pL;
    }

     /**
     * Expose class method as a function in Lua
     * @param pL Lua state
     * @param funName Class method
     * @param aClass  Class
     */
    public static void lua_register(ScriptEngine pL, String funName, Class aClass) {
        String className = aClass.getName();
        try {
            // create Lua function
            String fun = "function " + funName + "(...)\n"
                    + "local cl = luajava.bindClass('" + className + "');\n"
                    + "local data = cl:" + funName + "(...);\n"
                    + "return LuaHelperFunctions:unpackUserData(data);\n"
                    + "end";
            //System.out.println(fun);
            pL.eval(fun);
        } catch (ScriptException ex) {
            throw new RuntimeException("<lua_register> Exception: " + ex.getMessage());
        }
    }
}