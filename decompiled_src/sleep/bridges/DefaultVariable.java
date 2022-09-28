package sleep.bridges;

import java.util.Hashtable;
import sleep.interfaces.Loadable;
import sleep.interfaces.Variable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;

public class DefaultVariable implements Variable, Loadable {
   protected Hashtable values = new Hashtable();

   public boolean scalarExists(String var1) {
      return this.values.containsKey(var1);
   }

   public Scalar getScalar(String var1) {
      return (Scalar)this.values.get(var1);
   }

   public Scalar putScalar(String var1, Scalar var2) {
      return (Scalar)this.values.put(var1, var2);
   }

   public void removeScalar(String var1) {
      this.values.remove(var1);
   }

   public Variable createLocalVariableContainer() {
      return new DefaultVariable();
   }

   public Variable createInternalVariableContainer() {
      return new DefaultVariable();
   }

   public void scriptLoaded(ScriptInstance var1) {
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }
}
