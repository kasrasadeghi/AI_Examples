/**
 * @author Petr (http://www.sallyx.org/)
 */
package Cpp_using_lua;

// Do not forget to include luaj-jse-2.0.2.jar 
// If you also add to Libraries luaj-sources-2.0.2.jar,
// NetBeans will show help for luaj methods and classes.
// You can find both in Common/luaj-2.0.2/lib/
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaNumber;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.luaj.vm2.LuaValue;
import static common.lua.LuaHelperFunctions.*;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // get Lua script engine
        ScriptEngine pL = new ScriptEngineManager().getEngineByExtension(".lua");
        RunLuaScript(pL, "cpp_using_lua.lua");

        System.out.println("\n[Java]:  1. Assigning lua string and number types to Java String & int types\n");


        //get the global variables 'age' and 'name' 
        Object g1 = pL.get("age");
        Object g2 = pL.get("name");

        //check that the variables are the correct type.
        if (!(g1 instanceof Number) || !(g2 instanceof String)) {
            System.err.println("\n[Java]: ERROR: Invalid type!");
        }

        //now assign the values to Java variables
        String name = (String) g2;
        int age = (int) ((Number) g1).longValue();

        System.out.println("\n\n[Java]: name = " + name + "\n[Java]: age  = " + age);

        System.out.println("\n\n[Java]:  2. Retrieving simple table");


        //get  the table
        Object table = pL.get("simple_table");

        if (!(table instanceof LuaTable)) {
            System.err.println("[Java]: ERROR: simple_table is not a valid table");
        } else {
            LuaValue vname = ((LuaTable) table).get("name");

            //check that is the correct type
            if (!(vname instanceof LuaString)) {
                System.err.println("\n[Java]: ERROR: invalid type");
            }

            //grab the data
            name = ((LuaString) vname).toString();

            System.out.println("\n\n[Java]: name = " + name);

            /* now to do the same for the age */

            LuaValue vAge = ((LuaTable) table).get("age");

            if (!(vAge instanceof LuaNumber)) {
                System.err.println("\n[Java]: ERROR: invalid type");
            }

            //grab the data
            age = ((LuaNumber) vAge).toint();

            System.out.println("\n[Java]: age  = " + age);

            System.out.println("\n\n[Java]: 3. Calling a simple Lua function: add(a,b)");

            //get the function from the global table
            Object fce = pL.get("add");

            //check that it is there
            if (!(fce instanceof LuaFunction)) {
                System.err.println("\n\n[Java]: Oops! The lua function 'add' has not been defined");
            }

            LuaFunction add = (LuaFunction) fce;

            LuaValue vResult = add.call(LuaValue.valueOf(5), LuaValue.valueOf(8));

            //grab the result
            int result = ((LuaNumber) vResult).toint();

            System.out.println("\n\n[Java]: <lua>add(5,8) = " + result);

            System.out.println("\n\n\n");

        }
    }
}