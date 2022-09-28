package org.apache.batik.gvt.text;

import java.awt.Composite;
import java.awt.Paint;
import java.awt.Stroke;

public class TextPaintInfo {
   public boolean visible;
   public Paint fillPaint;
   public Paint strokePaint;
   public Stroke strokeStroke;
   public Composite composite;
   public Paint underlinePaint;
   public Paint underlineStrokePaint;
   public Stroke underlineStroke;
   public Paint overlinePaint;
   public Paint overlineStrokePaint;
   public Stroke overlineStroke;
   public Paint strikethroughPaint;
   public Paint strikethroughStrokePaint;
   public Stroke strikethroughStroke;
   public int startChar;
   public int endChar;

   public TextPaintInfo() {
   }

   public TextPaintInfo(TextPaintInfo var1) {
      this.set(var1);
   }

   public void set(TextPaintInfo var1) {
      if (var1 == null) {
         this.fillPaint = null;
         this.strokePaint = null;
         this.strokeStroke = null;
         this.composite = null;
         this.underlinePaint = null;
         this.underlineStrokePaint = null;
         this.underlineStroke = null;
         this.overlinePaint = null;
         this.overlineStrokePaint = null;
         this.overlineStroke = null;
         this.strikethroughPaint = null;
         this.strikethroughStrokePaint = null;
         this.strikethroughStroke = null;
         this.visible = false;
      } else {
         this.fillPaint = var1.fillPaint;
         this.strokePaint = var1.strokePaint;
         this.strokeStroke = var1.strokeStroke;
         this.composite = var1.composite;
         this.underlinePaint = var1.underlinePaint;
         this.underlineStrokePaint = var1.underlineStrokePaint;
         this.underlineStroke = var1.underlineStroke;
         this.overlinePaint = var1.overlinePaint;
         this.overlineStrokePaint = var1.overlineStrokePaint;
         this.overlineStroke = var1.overlineStroke;
         this.strikethroughPaint = var1.strikethroughPaint;
         this.strikethroughStrokePaint = var1.strikethroughStrokePaint;
         this.strikethroughStroke = var1.strikethroughStroke;
         this.visible = var1.visible;
      }

   }

   public static boolean equivilent(TextPaintInfo var0, TextPaintInfo var1) {
      if (var0 == null) {
         return var1 == null;
      } else if (var1 == null) {
         return false;
      } else if (var0.fillPaint == null != (var1.fillPaint == null)) {
         return false;
      } else if (var0.visible != var1.visible) {
         return false;
      } else {
         boolean var2 = var0.strokePaint != null && var0.strokeStroke != null;
         boolean var3 = var1.strokePaint != null && var1.strokeStroke != null;
         return var2 == var3;
      }
   }
}
