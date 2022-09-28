package sleep.interfaces;

import java.io.Serializable;
import java.util.Stack;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;

public interface Function extends Serializable {
   Scalar evaluate(String var1, ScriptInstance var2, Stack var3);
}
