/**
 * @author Petr (http://www.sallyx.org/)
 */
package Exposingcppclassesusingluabind;
// Do not forget to include luaj-jse-2.0.2.jar 
// If you also add to Libraries luaj-sources-2.0.2.jar,
// NetBeans will show help for luaj methods and classes.
// You can find both in Common/luaj-2.0.2/lib/

import javax.script.ScriptEngine;
import static common.lua.LuaHelperFunctions.RunLuaScript;
import static common.lua.LuaHelperFunctions.lua_open;
import static common.lua.LuaHelperFunctions.open;
import static common.lua.LuaHelperFunctions.lua_class;
import javax.script.ScriptException;

/**
 *
 * @author Petr
 */
public class Main {

    public static void RegisterAnimalWithLua(ScriptEngine pLua) {
        lua_class(pLua, Animal.class, "Animal")
                .def("Speak", "Speak")
                .def("NumLegs", "NumLegs");
    }

    public static void RegisterPetWithLua(ScriptEngine pLua) {
        /*
        module(pLua)
        [
        class_<Pet, bases<Animal> >("Pet")
        .def(constructor<string, string, int>())
        .def("GetName", &Pet::GetName)  
        ];
         */

        lua_class(pLua, Pet.class, "Pet")
                .base(Animal.class)
                .def("GetName", "GetName");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ScriptException {
        //create a lua state and open lua libs
        ScriptEngine pLua = lua_open();

        //open luabind symulation
        open(pLua);

        RegisterAnimalWithLua(pLua);
        RegisterPetWithLua(pLua);

        //load and run the script
        RunLuaScript(pLua, "ExposingCPPClassesToLua.lua");
    }
}