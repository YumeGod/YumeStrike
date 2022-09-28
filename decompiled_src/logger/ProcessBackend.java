package logger;

import common.CommonUtils;
import common.MudgeSanity;
import java.util.LinkedList;

public abstract class ProcessBackend implements Runnable {
   protected LinkedList tasks = new LinkedList();

   public void start(String var1) {
      (new Thread(this, var1)).start();
   }

   public void act(Object var1) {
      synchronized(this) {
         this.tasks.add(var1);
      }
   }

   protected Object grabTask() {
      synchronized(this) {
         return this.tasks.pollFirst();
      }
   }

   public abstract void process(Object var1);

   public void run() {
      while(true) {
         Object var1 = this.grabTask();
         if (var1 != null) {
            try {
               this.process(var1);
            } catch (Exception var3) {
               MudgeSanity.logException("ProcessBackend: " + var1.getClass(), var3, false);
            }

            Thread.yield();
         } else {
            CommonUtils.sleep(10000L);
         }
      }
   }
}
