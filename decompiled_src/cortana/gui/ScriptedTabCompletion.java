package cortana.gui;

import console.Console;
import console.GenericTabCompletion;
import java.util.Collection;

public class ScriptedTabCompletion extends GenericTabCompletion {
   protected Completer completer;

   public ScriptedTabCompletion(Console var1, Completer var2) {
      super(var1);
      this.completer = var2;
   }

   public Collection getOptions(String var1) {
      return this.completer.getOptions(var1);
   }

   public interface Completer {
      Collection getOptions(String var1);
   }
}
