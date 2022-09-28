package cortana;

import common.CommonUtils;
import common.MudgeSanity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CortanaPipe implements Runnable {
   protected PipedInputStream readme;
   protected PipedOutputStream writeme;
   protected boolean run = true;
   protected List listeners = new LinkedList();

   public OutputStream getOutput() {
      return this.writeme;
   }

   public CortanaPipe() {
      try {
         this.readme = new PipedInputStream(1048576);
         this.writeme = new PipedOutputStream(this.readme);
      } catch (IOException var2) {
         MudgeSanity.logException("create cortana pipe", var2, false);
      }

   }

   public void addCortanaPipeListener(CortanaPipeListener var1) {
      synchronized(this) {
         this.listeners.add(var1);
      }

      if (this.listeners.size() == 1) {
         (new Thread(this, "cortana pipe reader")).start();
      }

   }

   public void close() {
      try {
         this.run = false;
         this.writeme.close();
      } catch (IOException var2) {
         MudgeSanity.logException("close cortana pipe", var2, false);
      }

   }

   public void run() {
      BufferedReader var1 = new BufferedReader(new InputStreamReader(this.readme));

      while(this.run) {
         try {
            String var2 = var1.readLine();
            if (var2 != null) {
               synchronized(this) {
                  Iterator var4 = this.listeners.iterator();

                  while(var4.hasNext()) {
                     CortanaPipeListener var5 = (CortanaPipeListener)var4.next();
                     var5.read(var2);
                  }
               }
            }
         } catch (IOException var9) {
            CommonUtils.sleep(500L);
         }
      }

      try {
         var1.close();
      } catch (IOException var7) {
         MudgeSanity.logException("cortana pipe cleanup", var7, false);
      }

   }

   public interface CortanaPipeListener {
      void read(String var1);
   }
}
