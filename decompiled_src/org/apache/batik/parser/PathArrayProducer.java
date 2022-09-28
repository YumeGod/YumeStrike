package org.apache.batik.parser;

import java.util.Iterator;
import java.util.LinkedList;

public class PathArrayProducer implements PathHandler {
   protected LinkedList ps;
   protected float[] p;
   protected LinkedList cs;
   protected short[] c;
   protected int cindex;
   protected int pindex;
   protected int ccount;
   protected int pcount;

   public short[] getPathCommands() {
      return this.c;
   }

   public float[] getPathParameters() {
      return this.p;
   }

   public void startPath() throws ParseException {
      this.cs = new LinkedList();
      this.c = new short[11];
      this.ps = new LinkedList();
      this.p = new float[11];
      this.ccount = 0;
      this.pcount = 0;
      this.cindex = 0;
      this.pindex = 0;
   }

   public void movetoRel(float var1, float var2) throws ParseException {
      this.command((short)3);
      this.param(var1);
      this.param(var2);
   }

   public void movetoAbs(float var1, float var2) throws ParseException {
      this.command((short)2);
      this.param(var1);
      this.param(var2);
   }

   public void closePath() throws ParseException {
      this.command((short)1);
   }

   public void linetoRel(float var1, float var2) throws ParseException {
      this.command((short)5);
      this.param(var1);
      this.param(var2);
   }

   public void linetoAbs(float var1, float var2) throws ParseException {
      this.command((short)4);
      this.param(var1);
      this.param(var2);
   }

   public void linetoHorizontalRel(float var1) throws ParseException {
      this.command((short)13);
      this.param(var1);
   }

   public void linetoHorizontalAbs(float var1) throws ParseException {
      this.command((short)12);
      this.param(var1);
   }

   public void linetoVerticalRel(float var1) throws ParseException {
      this.command((short)15);
      this.param(var1);
   }

   public void linetoVerticalAbs(float var1) throws ParseException {
      this.command((short)14);
      this.param(var1);
   }

   public void curvetoCubicRel(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
      this.command((short)7);
      this.param(var1);
      this.param(var2);
      this.param(var3);
      this.param(var4);
      this.param(var5);
      this.param(var6);
   }

   public void curvetoCubicAbs(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
      this.command((short)6);
      this.param(var1);
      this.param(var2);
      this.param(var3);
      this.param(var4);
      this.param(var5);
      this.param(var6);
   }

   public void curvetoCubicSmoothRel(float var1, float var2, float var3, float var4) throws ParseException {
      this.command((short)17);
      this.param(var1);
      this.param(var2);
      this.param(var3);
      this.param(var4);
   }

   public void curvetoCubicSmoothAbs(float var1, float var2, float var3, float var4) throws ParseException {
      this.command((short)16);
      this.param(var1);
      this.param(var2);
      this.param(var3);
      this.param(var4);
   }

   public void curvetoQuadraticRel(float var1, float var2, float var3, float var4) throws ParseException {
      this.command((short)9);
      this.param(var1);
      this.param(var2);
      this.param(var3);
      this.param(var4);
   }

   public void curvetoQuadraticAbs(float var1, float var2, float var3, float var4) throws ParseException {
      this.command((short)8);
      this.param(var1);
      this.param(var2);
      this.param(var3);
      this.param(var4);
   }

   public void curvetoQuadraticSmoothRel(float var1, float var2) throws ParseException {
      this.command((short)19);
      this.param(var1);
      this.param(var2);
   }

   public void curvetoQuadraticSmoothAbs(float var1, float var2) throws ParseException {
      this.command((short)18);
      this.param(var1);
      this.param(var2);
   }

   public void arcRel(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
      this.command((short)11);
      this.param(var1);
      this.param(var2);
      this.param(var3);
      this.param(var4 ? 1.0F : 0.0F);
      this.param(var5 ? 1.0F : 0.0F);
      this.param(var6);
      this.param(var7);
   }

   public void arcAbs(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
      this.command((short)10);
      this.param(var1);
      this.param(var2);
      this.param(var3);
      this.param(var4 ? 1.0F : 0.0F);
      this.param(var5 ? 1.0F : 0.0F);
      this.param(var6);
      this.param(var7);
   }

   protected void command(short var1) throws ParseException {
      if (this.cindex == this.c.length) {
         this.cs.add(this.c);
         this.c = new short[this.c.length * 2 + 1];
         this.cindex = 0;
      }

      this.c[this.cindex++] = var1;
      ++this.ccount;
   }

   protected void param(float var1) throws ParseException {
      if (this.pindex == this.p.length) {
         this.ps.add(this.p);
         this.p = new float[this.p.length * 2 + 1];
         this.pindex = 0;
      }

      this.p[this.pindex++] = var1;
      ++this.pcount;
   }

   public void endPath() throws ParseException {
      short[] var1 = new short[this.ccount];
      int var2 = 0;

      Iterator var3;
      short[] var4;
      for(var3 = this.cs.iterator(); var3.hasNext(); var2 += var4.length) {
         var4 = (short[])var3.next();
         System.arraycopy(var4, 0, var1, var2, var4.length);
      }

      System.arraycopy(this.c, 0, var1, var2, this.cindex);
      this.cs.clear();
      this.c = var1;
      float[] var6 = new float[this.pcount];
      var2 = 0;

      float[] var5;
      for(var3 = this.ps.iterator(); var3.hasNext(); var2 += var5.length) {
         var5 = (float[])var3.next();
         System.arraycopy(var5, 0, var6, var2, var5.length);
      }

      System.arraycopy(this.p, 0, var6, var2, this.pindex);
      this.ps.clear();
      this.p = var6;
   }
}
