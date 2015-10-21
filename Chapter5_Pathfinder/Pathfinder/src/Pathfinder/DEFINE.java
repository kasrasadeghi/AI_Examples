/**
 * This class substitute C++ preprocessor. 
 * Workks only inside methods.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package Pathfinder;

import java.util.HashMap;

public class DEFINE {
   private static HashMap<Integer,Boolean> defined = new HashMap<Integer,Boolean>();
   
   public static final int DEBUG = 0;
  
   static { 
       //define(DEBUG);
   }
   public static boolean def(Integer D) {
       Boolean def =  defined.get(D);
       if(def == null) return false;
       return def;
   } 
   
   public static void define(Integer D) {
       defined.put(D, Boolean.TRUE);
   }
   
   public static void undef(Integer D) {
       defined.put(D, Boolean.FALSE);
   }
}
