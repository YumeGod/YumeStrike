package aggressor.bridges;

import common.ScriptUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Loadable;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class AliasManager {
   protected Map commands = new HashMap();

   protected SleepClosure getCommand(String var1) {
      synchronized(this) {
         if (this.commands.containsKey(var1)) {
            SleepClosure var3 = (SleepClosure)this.commands.get(var1);
            if (var3.getOwner().isLoaded()) {
               return var3;
            }

            this.commands.remove(var1);
         }

         return null;
      }
   }

   public List commands() {
      synchronized(this) {
         return new LinkedList(this.commands.keySet());
      }
   }

   public Loadable getBridge() {
      return new Aliases(this);
   }

   public void registerCommand(String var1, SleepClosure var2) {
      synchronized(this) {
         this.commands.put(var1, var2);
      }
   }

   public void clearCommand(String var1) {
      synchronized(this) {
         this.commands.remove(var1);
      }
   }

   public boolean isAlias(String var1) {
      return this.getCommand(var1) != null;
   }

   public boolean fireCommand(String var1, String var2, String var3) {
      SleepClosure var4 = this.getCommand(var2);
      if (var4 == null) {
         return false;
      } else {
         Stack var5 = ScriptUtils.StringToArguments(var2 + " " + var3);
         var5.push(SleepUtils.getScalar(var1));
         SleepUtils.runCode((SleepClosure)var4, var2 + " " + var3, (ScriptInstance)null, var5);
         return true;
      }
   }
}
