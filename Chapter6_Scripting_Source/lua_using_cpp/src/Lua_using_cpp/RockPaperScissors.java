/**
 * @author Petr (http://www.sallyx.org/)
 */
package Lua_using_cpp;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import common.misc.CppToJava.IntegerRef;
import static common.misc.utils.RandInt;

final public class RockPaperScissors {

    final static int NumPlayStrings = 3;
    final static String[] PossiblePlayStrings = {"scissors", "rock", "paper"};

    /**
     * this is GetAIMove as you would normally use it in Java
     */
    public static String GetAIMove() {
        return PossiblePlayStrings[RandInt(0, 2)];
    }

    /**
     * this is the wrapper written for GetAIMove to expose the function to lua
     * @see com.lua.LuaHelpFunctions.lua
     */
    public static LuaValue[] cpp_GetAIMove(Varargs args) {
        return new LuaValue[]{LuaValue.valueOf(GetAIMove())};
    }

    /**
     * given a play string, this function returns its key in PossiblePlayStrings
     */
    public static int GuessToIndex(final String guess) {
        for (int i = 0; i < NumPlayStrings; ++i) {
            if (guess.equals(PossiblePlayStrings[i])) {
                return i;
            }
        }
        //this value will force an error
        System.err.println("<GuessToIndex> bad guess: " + guess);
        return -1;
    }

    /**
     * Given the computer's play string and the users play string, and references
     *  to the scores, this function decides who has won the round and assigns
     *  points accordingly
     */
    public static void EvaluateTheGuesses(String user_guess,
            String comp_guess,
            IntegerRef user_score,
            IntegerRef comp_score) {

        final int score_table[][] = {
            {0, -1, 1},
            {1, 0, -1},
            {-1, 1, 0}
        };

        System.out.println("\nuser guess..." + user_guess + "  comp guess..." + comp_guess);

        if (score_table[GuessToIndex(user_guess)][GuessToIndex(comp_guess)] == 1) {
            System.out.println("\nYou have won this round!");
            user_score.inc();
        } else if (score_table[GuessToIndex(user_guess)][GuessToIndex(comp_guess)] == -1) {
            System.out.println("\nComputer wins this round.");
            comp_score.inc();
        } else {
            System.out.println("\nIt's a draw!");
        }
    }

    /**
     * the wrapper for EvaluateTheGuesses
     * @see com.lua.LuaHelpFunctions.lua
     */
    public static LuaValue[] cpp_EvaluateTheGuesses(String user_guess, String comp_guess, int user_score, int comp_score) {
        IntegerRef ir1 = new IntegerRef(user_score);
        IntegerRef ir2 = new IntegerRef(comp_score);
        //call the Java function proper
        EvaluateTheGuesses(user_guess, comp_guess, ir1, ir2);
        return new LuaValue[]{
                    LuaValue.valueOf(ir1.toInteger()),
                    LuaValue.valueOf(ir2.toInteger())
                };
    }
}