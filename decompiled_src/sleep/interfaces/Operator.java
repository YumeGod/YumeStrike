package sleep.interfaces;

import java.util.Stack;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;

public interface Operator {
   Scalar operate(String var1, ScriptInstance var2, Stack var3);
}
