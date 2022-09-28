package sleep.interfaces;

import sleep.engine.Block;
import sleep.runtime.ScriptInstance;

public interface Environment {
   void bindFunction(ScriptInstance var1, String var2, String var3, Block var4);
}
