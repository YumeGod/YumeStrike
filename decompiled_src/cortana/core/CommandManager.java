package cortana.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Loadable;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class CommandManager {
   protected Map commands = new HashMap();

   protected SleepClosure getCommand(String var1) {
      if (this.commands.containsKey(var1)) {
         SleepClosure var2 = (SleepClosure)this.commands.get(var1);
         if (var2.getOwner().isLoaded()) {
            return var2;
         }

         this.commands.remove(var1);
      }

      return null;
   }

   public List commandList(String var1) {
      Iterator var2 = this.commands.entrySet().iterator();
      LinkedList var3 = new LinkedList();

      while(true) {
         String var5;
         SleepClosure var6;
         do {
            if (!var2.hasNext()) {
               return var3;
            }

            Map.Entry var4 = (Map.Entry)var2.next();
            var5 = var4.getKey() + "";
            var6 = (SleepClosure)var4.getValue();
         } while(var1 != null && !var5.startsWith(var1));

         if (var6.getOwner().isLoaded()) {
            var3.add(var5);
         } else {
            var2.remove();
         }
      }
   }

   public Loadable getBridge() {
      return new Commands(this);
   }

   public void registerCommand(String var1, SleepClosure var2) {
      this.commands.put(var1, var2);
   }

   public boolean fireCommand(String var1, String var2) {
      Stack var3 = new Stack();
      StringBuffer var4 = new StringBuffer();

      for(int var5 = 0; var5 < var2.length(); ++var5) {
         char var6 = var2.charAt(var5);
         if (var6 == ' ') {
            if (var4.length() > 0) {
               var3.add(0, SleepUtils.getScalar(var4.toString()));
            }

            var4 = new StringBuffer();
         } else if (var6 == '"' && var4.length() == 0) {
            ++var5;

            while(var5 < var2.length() && var2.charAt(var5) != '"') {
               var4.append(var2.charAt(var5));
               ++var5;
            }

            var3.add(0, SleepUtils.getScalar(var4.toString()));
            var4 = new StringBuffer();
         } else {
            var4.append(var6);
         }
      }

      if (var4.length() > 0) {
         var3.add(0, SleepUtils.getScalar(var4.toString()));
      }

      var3.pop();
      return this.fireCommand(var1, var2, var3);
   }

   public boolean fireCommand(String var1, String var2, Stack var3) {
      SleepClosure var4 = this.getCommand(var1);
      if (var4 == null) {
         return false;
      } else {
         SleepUtils.runCode((SleepClosure)var4, var2, (ScriptInstance)null, EventManager.shallowCopy(var3));
         return true;
      }
   }
}
