package stagers;

import common.ListenerConfig;
import common.MudgeSanity;
import common.ScListener;

public abstract class GenericStager implements Cloneable {
   protected ScListener listener = null;

   public GenericStager(ScListener var1) {
   }

   public GenericStager create(ScListener var1) {
      try {
         GenericStager var2 = (GenericStager)this.clone();
         var2.listener = var1;
         return var2;
      } catch (CloneNotSupportedException var3) {
         MudgeSanity.logException("can't clone", var3, false);
         return null;
      }
   }

   public ListenerConfig getConfig() {
      return this.listener.getConfig();
   }

   public ScListener getListener() {
      return this.listener;
   }

   public abstract String arch();

   public abstract String payload();

   public abstract byte[] generate();
}
