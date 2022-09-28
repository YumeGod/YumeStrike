package sleep.engine.atoms;

import sleep.runtime.ScriptEnvironment;

public interface Check {
   boolean check(ScriptEnvironment var1);

   String toString(String var1);

   void setInfo(int var1);
}
