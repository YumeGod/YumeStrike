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

public class EventManager {
   protected Map listeners = new HashMap();
   protected EventQueue queue = new EventQueue(this);
   protected boolean wildcards;

   protected List getListener(String var1) {
      synchronized(this) {
         if (this.listeners.containsKey(var1)) {
            return (List)this.listeners.get(var1);
         } else {
            this.listeners.put(var1, new LinkedList());
            return (List)this.listeners.get(var1);
         }
      }
   }

   public Loadable getBridge() {
      return new Events(this);
   }

   public void stop() {
      this.queue.stop();
   }

   public boolean hasWildcardListener() {
      return this.wildcards;
   }

   public boolean isLiveEvent(String var1) {
      return this.hasWildcardListener() || this.hasListener(var1);
   }

   public void addListener(String var1, SleepClosure var2, boolean var3) {
      synchronized(this) {
         if ("*".equals(var1)) {
            this.wildcards = true;
         }

         this.getListener(var1).add(new Listener(var2, var3));
      }
   }

   public static Stack shallowCopy(Stack var0) {
      Stack var1 = new Stack();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         var1.push(var2.next());
      }

      return var1;
   }

   public void fireEvent(String var1, Stack var2) {
      this.queue.add(var1, var2);
   }

   public boolean hasListener(String var1) {
      synchronized(this) {
         return this.getListener(var1).size() != 0;
      }
   }

   protected List getListeners(String var1, ScriptInstance var2) {
      Object var3 = null;
      if (var2 != null) {
         var3 = var2.getMetadata().get("%scriptid%");
      }

      synchronized(this) {
         LinkedList var5 = new LinkedList();
         Iterator var6 = this.getListener(var1).iterator();

         while(true) {
            while(var6.hasNext()) {
               Listener var7 = (Listener)var6.next();
               if (!var7.getClosure().getOwner().isLoaded()) {
                  var6.remove();
               } else if (var3 == null || var3.equals(var7.getClosure().getOwner().getMetadata().get("%scriptid%"))) {
                  var5.add(var7.getClosure());
                  if (var7.isTemporary()) {
                     var6.remove();
                  }
               }
            }

            return var5;
         }
      }
   }

   public void fireEventNoQueue(String var1, Stack var2, ScriptInstance var3) {
      if (this.hasListener(var1)) {
         Iterator var4 = this.getListeners(var1, var3).iterator();

         while(var4.hasNext()) {
            SleepClosure var5 = (SleepClosure)var4.next();
            SleepUtils.runCode((SleepClosure)var5, var1, (ScriptInstance)null, shallowCopy(var2));
         }
      }

   }

   private static class Listener {
      protected SleepClosure listener;
      protected boolean temporary;

      public Listener(SleepClosure var1, boolean var2) {
         this.listener = var1;
         this.temporary = var2;
      }

      public SleepClosure getClosure() {
         return this.listener;
      }

      public boolean isTemporary() {
         return this.temporary;
      }
   }
}
