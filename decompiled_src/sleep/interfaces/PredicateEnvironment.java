package sleep.interfaces;

import sleep.engine.Block;
import sleep.engine.atoms.Check;
import sleep.runtime.ScriptInstance;

public interface PredicateEnvironment {
   void bindPredicate(ScriptInstance var1, String var2, Check var3, Block var4);
}
