package cortana;

import console.Console;
import console.GenericTabCompletion;
import java.util.Collection;

public class CortanaTabCompletion extends GenericTabCompletion {
   protected ConsoleInterface myinterface;

   public String transformText(String var1) {
      return var1.replace(" ~", " " + System.getProperty("user.home"));
   }

   public CortanaTabCompletion(Console var1, Cortana var2) {
      super(var1);
      this.myinterface = var2.getConsoleInterface();
   }

   public Collection getOptions(String var1) {
      return this.myinterface.commandList(var1);
   }
}
