package server;

import common.CommonUtils;
import common.MudgeSanity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistentData implements Runnable {
   protected String model;
   protected Object value = null;
   protected Object lock = null;

   public PersistentData(String var1, Object var2) {
      this.model = var1;
      this.lock = var2;
      (new Thread(this, "save thread for: " + var1)).start();
   }

   public void save(Object var1) {
      synchronized(this.lock) {
         this.value = var1;
      }
   }

   private void _save() {
      try {
         (new File("data")).mkdirs();
         File var1 = CommonUtils.SafeFile("data", this.model + ".bin");
         ObjectOutputStream var2 = new ObjectOutputStream(new FileOutputStream(var1, false));
         var2.writeObject(this.value);
         var2.close();
      } catch (Exception var3) {
         MudgeSanity.logException("save " + this.model, var3, false);
      }

   }

   public void run() {
      while(true) {
         synchronized(this.lock) {
            if (this.value != null) {
               this._save();
               this.value = null;
            }
         }

         CommonUtils.sleep(10000L);
      }
   }

   public Object getValue(Object var1) {
      try {
         File var2 = CommonUtils.SafeFile("data", this.model + ".bin");
         if (var2.exists()) {
            ObjectInputStream var3 = new ObjectInputStream(new FileInputStream(var2));
            Object var4 = var3.readObject();
            var3.close();
            return var4;
         }
      } catch (Exception var5) {
         MudgeSanity.logException("load " + this.model, var5, false);
         CommonUtils.print_error("the " + this.model + " model will start empty [everything is OK]");
      }

      return var1;
   }
}
