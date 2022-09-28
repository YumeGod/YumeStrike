package org.apache.batik.swing.svg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.swing.gvt.Overlay;

public class SVGUpdateOverlay implements Overlay {
   List rects = new LinkedList();
   int size;
   int updateCount;
   int[] counts;

   public SVGUpdateOverlay(int var1, int var2) {
      this.size = var1;
      this.counts = new int[var2];
   }

   public void addRect(Rectangle var1) {
      this.rects.add(var1);
      if (this.rects.size() > this.size) {
         this.rects.remove(0);
      }

      ++this.updateCount;
   }

   public void endUpdate() {
      int var1;
      for(var1 = 0; var1 < this.counts.length - 1; ++var1) {
         this.counts[var1] = this.counts[var1 + 1];
      }

      this.counts[var1] = this.updateCount;
      this.updateCount = 0;
      int var2 = this.rects.size();

      for(var1 = this.counts.length - 1; var1 >= 0; --var1) {
         if (this.counts[var1] > var2) {
            this.counts[var1] = var2;
         }

         var2 -= this.counts[var1];
      }

      int[] var10000 = this.counts;
      var10000[0] += var2;
   }

   public void paint(Graphics var1) {
      Iterator var2 = this.rects.iterator();
      int var3 = 0;
      int var4 = 0;

      int var5;
      for(var5 = 0; var5 < this.counts.length - 1 && var4 == this.counts[var5]; ++var5) {
      }

      int var6 = this.counts.length - 1;

      while(var2.hasNext()) {
         Rectangle var7 = (Rectangle)var2.next();
         Color var8 = new Color(1.0F, (float)(var6 - var5) / (float)var6, 0.0F, ((float)var3 + 1.0F) / (float)this.rects.size());
         var1.setColor(var8);
         var1.drawRect(var7.x, var7.y, var7.width, var7.height);
         ++var3;
         ++var4;

         while(var5 < this.counts.length - 1 && var4 == this.counts[var5]) {
            ++var5;
            var4 = 0;
         }
      }

   }
}
