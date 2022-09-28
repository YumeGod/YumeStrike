package org.apache.batik.svggen.font;

public class Point {
   public int x = 0;
   public int y = 0;
   public boolean onCurve = true;
   public boolean endOfContour = false;
   public boolean touched = false;

   public Point(int var1, int var2, boolean var3, boolean var4) {
      this.x = var1;
      this.y = var2;
      this.onCurve = var3;
      this.endOfContour = var4;
   }
}
