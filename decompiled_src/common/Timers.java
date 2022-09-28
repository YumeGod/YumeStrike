package common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Timers implements Runnable {
   private static Timers mytimer = null;
   protected List timers = new LinkedList();

   public static synchronized Timers getTimers() {
      if (mytimer == null) {
         mytimer = new Timers();
      }

      return mytimer;
   }

   public void every(long var1, String var3, Do var4) {
      synchronized(this) {
         this.timers.add(new ActionItem(var4, var3, var1));
      }
   }

   private Timers() {
      (new Thread(this, "global timer")).start();
   }

   public void fire(ActionItem var1) {
      var1.moment();
   }

   public void run() {
      LinkedList var1 = null;

      while(true) {
         synchronized(this) {
            var1 = new LinkedList(this.timers);
         }

         long var2 = System.currentTimeMillis();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            ActionItem var5 = (ActionItem)var4.next();
            if (var5.isDue(var2)) {
               this.fire(var5);
            }
         }

         synchronized(this) {
            Iterator var6 = this.timers.iterator();

            while(true) {
               if (!var6.hasNext()) {
                  break;
               }

               ActionItem var7 = (ActionItem)var6.next();
               if (!var7.shouldKeep()) {
                  var6.remove();
               }
            }
         }

         CommonUtils.sleep(1000L);
      }
   }

   private static class ActionItem {
      public Do action;
      public long every;
      public long last;
      public boolean keep = true;
      public String msg;

      public ActionItem(Do var1, String var2, long var3) {
         this.action = var1;
         this.every = var3;
         this.last = 0L;
         this.msg = var2;
      }

      public boolean isDue(long var1) {
         return var1 - this.last >= this.every;
      }

      public void moment() {
         try {
            this.last = System.currentTimeMillis();
            this.keep = this.action.moment(this.msg);
         } catch (Exception var2) {
            MudgeSanity.logException("timer to " + this.action.getClass() + "/" + this.msg + " every " + this.last + "ms", var2, false);
            this.keep = false;
         }

      }

      public boolean shouldKeep() {
         return this.keep;
      }
   }
}
