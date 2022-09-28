package sleep.interfaces;

import sleep.runtime.ScriptInstance;

public interface Loadable {
   void scriptLoaded(ScriptInstance var1);

   void scriptUnloaded(ScriptInstance var1);
}
