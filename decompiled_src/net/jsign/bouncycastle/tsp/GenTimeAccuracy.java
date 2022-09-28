package net.jsign.bouncycastle.tsp;

import net.jsign.bouncycastle.asn1.ASN1Integer;
import net.jsign.bouncycastle.asn1.tsp.Accuracy;

public class GenTimeAccuracy {
   private Accuracy accuracy;

   public GenTimeAccuracy(Accuracy var1) {
      this.accuracy = var1;
   }

   public int getSeconds() {
      return this.getTimeComponent(this.accuracy.getSeconds());
   }

   public int getMillis() {
      return this.getTimeComponent(this.accuracy.getMillis());
   }

   public int getMicros() {
      return this.getTimeComponent(this.accuracy.getMicros());
   }

   private int getTimeComponent(ASN1Integer var1) {
      return var1 != null ? var1.getValue().intValue() : 0;
   }

   public String toString() {
      return this.getSeconds() + "." + this.format(this.getMillis()) + this.format(this.getMicros());
   }

   private String format(int var1) {
      if (var1 < 10) {
         return "00" + var1;
      } else {
         return var1 < 100 ? "0" + var1 : Integer.toString(var1);
      }
   }
}
