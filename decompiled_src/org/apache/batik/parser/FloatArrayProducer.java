package org.apache.batik.parser;

import java.util.Iterator;
import java.util.LinkedList;

public class FloatArrayProducer extends DefaultNumberListHandler implements PointsHandler {
   protected LinkedList as;
   protected float[] a;
   protected int index;
   protected int count;

   public float[] getFloatArray() {
      return this.a;
   }

   public void startNumberList() throws ParseException {
      this.as = new LinkedList();
      this.a = new float[11];
      this.count = 0;
      this.index = 0;
   }

   public void numberValue(float var1) throws ParseException {
      if (this.index == this.a.length) {
         this.as.add(this.a);
         this.a = new float[this.a.length * 2 + 1];
         this.index = 0;
      }

      this.a[this.index++] = var1;
      ++this.count;
   }

   public void endNumberList() throws ParseException {
      float[] var1 = new float[this.count];
      int var2 = 0;

      float[] var4;
      for(Iterator var3 = this.as.iterator(); var3.hasNext(); var2 += var4.length) {
         var4 = (float[])var3.next();
         System.arraycopy(var4, 0, var1, var2, var4.length);
      }

      System.arraycopy(this.a, 0, var1, var2, this.index);
      this.as.clear();
      this.a = var1;
   }

   public void startPoints() throws ParseException {
      this.startNumberList();
   }

   public void point(float var1, float var2) throws ParseException {
      this.numberValue(var1);
      this.numberValue(var2);
   }

   public void endPoints() throws ParseException {
      this.endNumberList();
   }
}
