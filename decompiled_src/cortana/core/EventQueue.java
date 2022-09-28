package cortana.core;

import common.MudgeSanity;
import java.util.LinkedList;
import java.util.Stack;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class EventQueue implements Runnable {
   protected EventManager manager;
   protected LinkedList queue = new LinkedList();
   protected boolean run = true;

   public EventQueue(EventManager var1) {
      this.manager = var1;
      (new Thread(this, "Aggressor Script Event Queue")).start();
   }

   public void add(String var1, Stack var2) {
      Event var3 = new Event();
      var3.name = var1;
      var3.args = var2;
      synchronized(this) {
         if (this.manager.hasWildcardListener()) {
            this.queue.add(var3.wildcard());
         }

         this.queue.add(var3);
      }
   }

   protected Event grabEvent() {
      synchronized(this) {
         return (Event)this.queue.pollFirst();
      }
   }

   public void stop() {
      this.run = false;
   }

   public void run() {
      while(this.run) {
         Event var1 = this.grabEvent();

         try {
            if (var1 != null) {
               this.manager.fireEventNoQueue(var1.name, var1.args, (ScriptInstance)null);
            } else {
               Thread.sleep(25L);
            }
         } catch (Exception var3) {
            if (var1 != null) {
               MudgeSanity.logException("event: " + var1.name + "/" + SleepUtils.describe(var1.args), var3, false);
            } else {
               MudgeSanity.logException("event (none)", var3, false);
            }
         }
      }

   }

   private static class Event {
      public String name;
      public Stack args;

      private Event() {
      }

      public Event wildcard() {
         Event var1 = new Event();
         var1.name = "*";
         var1.args = new Stack();
         var1.args.addAll(this.args);
         var1.args.push(SleepUtils.getScalar(this.name));
         return var1;
      }

      // $FF: synthetic method
      Event(Object var1) {
         this();
      }
   }
}
