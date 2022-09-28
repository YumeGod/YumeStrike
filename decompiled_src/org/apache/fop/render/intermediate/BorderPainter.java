package org.apache.fop.render.intermediate;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;

public abstract class BorderPainter {
   public void drawBorders(Rectangle borderRect, BorderProps bpsBefore, BorderProps bpsAfter, BorderProps bpsStart, BorderProps bpsEnd) throws IOException {
      int startx = borderRect.x;
      int starty = borderRect.y;
      int width = borderRect.width;
      int height = borderRect.height;
      boolean[] b = new boolean[]{bpsBefore != null, bpsEnd != null, bpsAfter != null, bpsStart != null};
      if (b[0] || b[1] || b[2] || b[3]) {
         int[] bw = new int[]{b[0] ? bpsBefore.width : 0, b[1] ? bpsEnd.width : 0, b[2] ? bpsAfter.width : 0, b[3] ? bpsStart.width : 0};
         int[] clipw = new int[]{BorderProps.getClippedWidth(bpsBefore), BorderProps.getClippedWidth(bpsEnd), BorderProps.getClippedWidth(bpsAfter), BorderProps.getClippedWidth(bpsStart)};
         starty += clipw[0];
         height -= clipw[0];
         height -= clipw[2];
         startx += clipw[3];
         width -= clipw[3];
         width -= clipw[1];
         boolean[] slant = new boolean[]{b[3] && b[0], b[0] && b[1], b[1] && b[2], b[2] && b[3]};
         int sy2;
         int ey1;
         int ey2;
         int outerx;
         int clipx;
         int innerx;
         int sy1a;
         int ey1a;
         if (bpsBefore != null) {
            sy2 = slant[0] ? startx + bw[3] - clipw[3] : startx;
            ey1 = startx + width;
            ey2 = slant[1] ? ey1 - bw[1] + clipw[1] : ey1;
            outerx = starty - clipw[0];
            clipx = outerx + clipw[0];
            innerx = outerx + bw[0];
            this.saveGraphicsState();
            this.moveTo(startx, clipx);
            sy1a = startx;
            ey1a = ey1;
            if (bpsBefore.mode == 2) {
               if (bpsStart != null && bpsStart.mode == 2) {
                  sy1a = startx - clipw[3];
               }

               if (bpsEnd != null && bpsEnd.mode == 2) {
                  ey1a = ey1 + clipw[1];
               }

               this.lineTo(sy1a, outerx);
               this.lineTo(ey1a, outerx);
            }

            this.lineTo(ey1, clipx);
            this.lineTo(ey2, innerx);
            this.lineTo(sy2, innerx);
            this.closePath();
            this.clip();
            this.drawBorderLine(sy1a, outerx, ey1a, innerx, true, true, bpsBefore.style, bpsBefore.color);
            this.restoreGraphicsState();
         }

         if (bpsEnd != null) {
            sy2 = slant[1] ? starty + bw[0] - clipw[0] : starty;
            ey1 = starty + height;
            ey2 = slant[2] ? ey1 - bw[2] + clipw[2] : ey1;
            outerx = startx + width + clipw[1];
            clipx = outerx - clipw[1];
            innerx = outerx - bw[1];
            this.saveGraphicsState();
            this.moveTo(clipx, starty);
            sy1a = starty;
            ey1a = ey1;
            if (bpsEnd.mode == 2) {
               if (bpsBefore != null && bpsBefore.mode == 2) {
                  sy1a = starty - clipw[0];
               }

               if (bpsAfter != null && bpsAfter.mode == 2) {
                  ey1a = ey1 + clipw[2];
               }

               this.lineTo(outerx, sy1a);
               this.lineTo(outerx, ey1a);
            }

            this.lineTo(clipx, ey1);
            this.lineTo(innerx, ey2);
            this.lineTo(innerx, sy2);
            this.closePath();
            this.clip();
            this.drawBorderLine(innerx, sy1a, outerx, ey1a, false, false, bpsEnd.style, bpsEnd.color);
            this.restoreGraphicsState();
         }

         if (bpsAfter != null) {
            sy2 = slant[3] ? startx + bw[3] - clipw[3] : startx;
            ey1 = startx + width;
            ey2 = slant[2] ? ey1 - bw[1] + clipw[1] : ey1;
            outerx = starty + height + clipw[2];
            clipx = outerx - clipw[2];
            innerx = outerx - bw[2];
            this.saveGraphicsState();
            this.moveTo(ey1, clipx);
            sy1a = startx;
            ey1a = ey1;
            if (bpsAfter.mode == 2) {
               if (bpsStart != null && bpsStart.mode == 2) {
                  sy1a = startx - clipw[3];
               }

               if (bpsEnd != null && bpsEnd.mode == 2) {
                  ey1a = ey1 + clipw[1];
               }

               this.lineTo(ey1a, outerx);
               this.lineTo(sy1a, outerx);
            }

            this.lineTo(startx, clipx);
            this.lineTo(sy2, innerx);
            this.lineTo(ey2, innerx);
            this.closePath();
            this.clip();
            this.drawBorderLine(sy1a, innerx, ey1a, outerx, true, false, bpsAfter.style, bpsAfter.color);
            this.restoreGraphicsState();
         }

         if (bpsStart != null) {
            sy2 = slant[0] ? starty + bw[0] - clipw[0] : starty;
            ey1 = starty + height;
            ey2 = slant[3] ? ey1 - bw[2] + clipw[2] : ey1;
            outerx = startx - clipw[3];
            clipx = outerx + clipw[3];
            innerx = outerx + bw[3];
            this.saveGraphicsState();
            this.moveTo(clipx, ey1);
            sy1a = starty;
            ey1a = ey1;
            if (bpsStart.mode == 2) {
               if (bpsBefore != null && bpsBefore.mode == 2) {
                  sy1a = starty - clipw[0];
               }

               if (bpsAfter != null && bpsAfter.mode == 2) {
                  ey1a = ey1 + clipw[2];
               }

               this.lineTo(outerx, ey1a);
               this.lineTo(outerx, sy1a);
            }

            this.lineTo(clipx, starty);
            this.lineTo(innerx, sy2);
            this.lineTo(innerx, ey2);
            this.closePath();
            this.clip();
            this.drawBorderLine(outerx, sy1a, innerx, ey1a, false, true, bpsStart.style, bpsStart.color);
            this.restoreGraphicsState();
         }

      }
   }

   protected abstract void drawBorderLine(int var1, int var2, int var3, int var4, boolean var5, boolean var6, int var7, Color var8) throws IOException;

   public abstract void drawLine(Point var1, Point var2, int var3, Color var4, RuleStyle var5) throws IOException;

   protected abstract void moveTo(int var1, int var2) throws IOException;

   protected abstract void lineTo(int var1, int var2) throws IOException;

   protected abstract void closePath() throws IOException;

   protected abstract void clip() throws IOException;

   protected abstract void saveGraphicsState() throws IOException;

   protected abstract void restoreGraphicsState() throws IOException;
}
