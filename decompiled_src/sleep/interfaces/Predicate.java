package sleep.interfaces;

import java.util.Stack;
import sleep.runtime.ScriptInstance;

public interface Predicate {
   boolean decide(String var1, ScriptInstance var2, Stack var3);
}
