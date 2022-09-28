package common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class GenericEvent implements Serializable, Scriptable {
   protected String name;
   protected List args;

   public GenericEvent(String var1, String var2) {
      this.name = var1;
      this.args = new LinkedList();
      this.args.add(var2);
   }

   public String eventName() {
      return this.name;
   }

   public Stack eventArguments() {
      Stack var1 = new Stack();
      Iterator var2 = this.args.iterator();

      while(var2.hasNext()) {
         var1.push(ScriptUtils.convertAll(var2.next()));
      }

      return var1;
   }
}
