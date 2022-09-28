package org.apache.batik.parser;

import java.util.Iterator;
import java.util.LinkedList;

public class LengthArrayProducer extends DefaultLengthListHandler {
   protected LinkedList vs;
   protected float[] v;
   protected LinkedList us;
   protected short[] u;
   protected int index;
   protected int count;
   protected short currentUnit;

   public short[] getLengthTypeArray() {
      return this.u;
   }

   public float[] getLengthValueArray() {
      return this.v;
   }

   public void startLengthList() throws ParseException {
      this.us = new LinkedList();
      this.u = new short[11];
      this.vs = new LinkedList();
      this.v = new float[11];
      this.count = 0;
      this.index = 0;
   }

   public void numberValue(float var1) throws ParseException {
   }

   public void lengthValue(float var1) throws ParseException {
      if (this.index == this.v.length) {
         this.vs.add(this.v);
         this.v = new float[this.v.length * 2 + 1];
         this.us.add(this.u);
         this.u = new short[this.u.length * 2 + 1];
         this.index = 0;
      }

      this.v[this.index] = var1;
   }

   public void startLength() throws ParseException {
      this.currentUnit = 1;
   }

   public void endLength() throws ParseException {
      this.u[this.index++] = this.currentUnit;
      ++this.count;
   }

   public void em() throws ParseException {
      this.currentUnit = 3;
   }

   public void ex() throws ParseException {
      this.currentUnit = 4;
   }

   public void in() throws ParseException {
      this.currentUnit = 8;
   }

   public void cm() throws ParseException {
      this.currentUnit = 6;
   }

   public void mm() throws ParseException {
      this.currentUnit = 7;
   }

   public void pc() throws ParseException {
      this.currentUnit = 10;
   }

   public void pt() throws ParseException {
      this.currentUnit = 9;
   }

   public void px() throws ParseException {
      this.currentUnit = 5;
   }

   public void percentage() throws ParseException {
      this.currentUnit = 2;
   }

   public void endLengthList() throws ParseException {
      float[] var1 = new float[this.count];
      int var2 = 0;

      Iterator var3;
      float[] var4;
      for(var3 = this.vs.iterator(); var3.hasNext(); var2 += var4.length) {
         var4 = (float[])var3.next();
         System.arraycopy(var4, 0, var1, var2, var4.length);
      }

      System.arraycopy(this.v, 0, var1, var2, this.index);
      this.vs.clear();
      this.v = var1;
      short[] var6 = new short[this.count];
      var2 = 0;

      short[] var5;
      for(var3 = this.us.iterator(); var3.hasNext(); var2 += var5.length) {
         var5 = (short[])var3.next();
         System.arraycopy(var5, 0, var6, var2, var5.length);
      }

      System.arraycopy(this.u, 0, var6, var2, this.index);
      this.us.clear();
      this.u = var6;
   }
}
