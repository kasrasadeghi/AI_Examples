/**
 * @author Petr (http://www.sallyx.org/)
 */
package ScriptedStateMachine;
// Do not forget to include luaj-jse-2.0.2.jar 
// If you also add to Libraries luaj-sources-2.0.2.jar,
// NetBeans will show help for luaj methods and classes.
// You can find both in Common/luaj-2.0.2/lib/

import static common.lua.LuaHelperFunctions.LuaExceptionGuard;
import common.lua.LuaHelperFunctions.LuabindObject;
import javax.script.ScriptEngine;
import static common.lua.LuaHelperFunctions.RunLuaScript;
import static common.lua.LuaHelperFunctions.lua_open;
import static common.lua.LuaHelperFunctions.open;
import static common.lua.LuaHelperFunctions.lua_class;
import static common.lua.LuaHelperFunctions.globals;
import javax.script.ScriptException;
import org.luaj.vm2.LuaValue;

public class Main {

	static void RegisterScriptedStateMachineWithLua(ScriptEngine pLua) {
		/*
		module(pLua)
		[
		class_<ScriptedStateMachine<Miner> >("ScriptedStateMachine")

		.def("ChangeState", &ScriptedStateMachine<Miner>::ChangeState)
		.def("CurrentState", &ScriptedStateMachine<Miner>::CurrentState)
		.def("SetCurrentState", &ScriptedStateMachine<Miner>::SetCurrentState)
		];  */

		lua_class(pLua, ScriptedStateMachine.class, "ScriptedStateMachine")
			.def("ChangeState", "ChangeState")
			.def("CurrentState", "CurrentState")
			.def("SetCurrentState", "SetCurrentState");
	}

	static void RegisterEntityWithLua(ScriptEngine pLua) {
		lua_class(pLua, Entity.class, "Entity")
			.def("Name", "Name")
			.def("ID", "ID");
	}

	static void RegisterMinerWithLua(ScriptEngine pLua) {
		lua_class(pLua, Miner.class, "Miner")
			.base(Entity.class)
			.def("GoldCarried", "GoldCarried")
			.def("SetGoldCarried", "SetGoldCarried")
			.def("AddToGoldCarried", "AddToGoldCarried")
			.def("Fatigued", "Fatigued")
			.def("DecreaseFatigue", "DecreaseFatigue")
			.def("IncreaseFatigue", "IncreaseFatigue")
			.def("GetFSM", "GetFSM");
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws ScriptException {
		// create a lua state and open lua librarires
		ScriptEngine pLua = lua_open();

		LuaExceptionGuard guard = new LuaExceptionGuard(pLua);

		//open luabind
		open(pLua);

		//bind the relevant classes to Lua
		RegisterEntityWithLua(pLua);
		RegisterMinerWithLua(pLua);
		RegisterScriptedStateMachineWithLua(pLua);


		//load and run the script
		RunLuaScript(pLua, "StateMachineScript.lua");

		//create a miner
		Miner bob = new Miner("bob");

		//grab the global table from the lua state. This will inlclude
		//all the functions and variables defined in the scripts run so far
		//(StateMachineScript.lua in this example)
		LuabindObject states = globals(pLua);

		//ensure states is a table
		if (states.type() == LuaValue.TTABLE) {
			//make sure Bob's CurrentState object is set to a valid state.
			bob.GetFSM().SetCurrentState(new LuabindObject(pLua,(LuaValue)pLua.get("State_GoHome")));

			//run him through a few update cycles
			for (int i = 0; i < 10; ++i) {
				bob.Update();
			}
		}

	}
}
