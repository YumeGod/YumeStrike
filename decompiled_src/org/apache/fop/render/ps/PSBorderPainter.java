package org.apache.fop.render.ps;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.render.intermediate.BorderPainter;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.ColorUtil;
import org.apache.xmlgraphics.ps.PSGenerator;

public class PSBorderPainter extends BorderPainter {
   private static Log log;
   private PSGenerator generator;

   public PSBorderPainter(PSGenerator generator) {
      this.generator = generator;
   }

   protected void drawBorderLine(int x1, int y1, int x2, int y2, boolean horz, boolean startOrBefore, int style, Color col) throws IOException {
      drawBorderLine(this.generator, toPoints(x1), toPoints(y1), toPoints(x2), toPoints(y2), horz, startOrBefore, style, col);
   }

   private static void drawLine(PSGenerator gen, float startx, float starty, float endx, float endy) throws IOException {
      gen.writeln(gen.formatDouble((double)startx) + " " + gen.formatDouble((double)starty) + " M " + gen.formatDouble((double)endx) + " " + gen.formatDouble((double)endy) + " lineto stroke newpath");
   }

   public static void drawBorderLine(PSGenerator gen, float x1, float y1, float x2, float y2, boolean horz, boolean startOrBefore, int style, Color col) throws IOException {
      float w = x2 - x1;
      float h = y2 - y1;
      if (!(w < 0.0F) && !(h < 0.0F)) {
         float unit;
         Color c;
         float ym;
         float ym1;
         int rep;
         switch (style) {
            case 31:
               gen.useColor(col);
               if (horz) {
                  unit = Math.abs(2.0F * h);
                  rep = (int)(w / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = w / (float)rep;
                  gen.useDash("[" + unit + "] 0");
                  gen.useLineCap(0);
                  gen.useLineWidth((double)h);
                  ym = y1 + h / 2.0F;
                  drawLine(gen, x1, ym, x2, ym);
               } else {
                  unit = Math.abs(2.0F * w);
                  rep = (int)(h / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = h / (float)rep;
                  gen.useDash("[" + unit + "] 0");
                  gen.useLineCap(0);
                  gen.useLineWidth((double)w);
                  ym = x1 + w / 2.0F;
                  drawLine(gen, ym, y1, ym, y2);
               }
               break;
            case 36:
               gen.useColor(col);
               gen.useLineCap(1);
               if (horz) {
                  unit = Math.abs(2.0F * h);
                  rep = (int)(w / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = w / (float)rep;
                  gen.useDash("[0 " + unit + "] 0");
                  gen.useLineWidth((double)h);
                  ym = y1 + h / 2.0F;
                  drawLine(gen, x1, ym, x2, ym);
               } else {
                  unit = Math.abs(2.0F * w);
                  rep = (int)(h / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = h / (float)rep;
                  gen.useDash("[0 " + unit + "] 0");
                  gen.useLineWidth((double)w);
                  ym = x1 + w / 2.0F;
                  drawLine(gen, ym, y1, ym, y2);
               }
               break;
            case 37:
               gen.useColor(col);
               gen.useDash((String)null);
               if (horz) {
                  unit = h / 3.0F;
                  gen.useLineWidth((double)unit);
                  ym1 = y1 + unit / 2.0F;
                  ym = ym1 + unit + unit;
                  drawLine(gen, x1, ym1, x2, ym1);
                  drawLine(gen, x1, ym, x2, ym);
               } else {
                  unit = w / 3.0F;
                  gen.useLineWidth((double)unit);
                  ym1 = x1 + unit / 2.0F;
                  ym = ym1 + unit + unit;
                  drawLine(gen, ym1, y1, ym1, y2);
                  drawLine(gen, ym, y1, ym, y2);
               }
               break;
            case 55:
            case 119:
               unit = style == 55 ? 0.4F : -0.4F;
               gen.useDash((String)null);
               float h3;
               float ym1;
               Color lowercol;
               if (horz) {
                  c = ColorUtil.lightenColor(col, -unit);
                  lowercol = ColorUtil.lightenColor(col, unit);
                  h3 = h / 3.0F;
                  gen.useLineWidth((double)h3);
                  ym1 = y1 + h3 / 2.0F;
                  gen.useColor(c);
                  drawLine(gen, x1, ym1, x2, ym1);
                  gen.useColor(col);
                  drawLine(gen, x1, ym1 + h3, x2, ym1 + h3);
                  gen.useColor(lowercol);
                  drawLine(gen, x1, ym1 + h3 + h3, x2, ym1 + h3 + h3);
               } else {
                  c = ColorUtil.lightenColor(col, -unit);
                  lowercol = ColorUtil.lightenColor(col, unit);
                  h3 = w / 3.0F;
                  gen.useLineWidth((double)h3);
                  ym1 = x1 + h3 / 2.0F;
                  gen.useColor(c);
                  drawLine(gen, ym1, y1, ym1, y2);
                  gen.useColor(col);
                  drawLine(gen, ym1 + h3, y1, ym1 + h3, y2);
                  gen.useColor(lowercol);
                  drawLine(gen, ym1 + h3 + h3, y1, ym1 + h3 + h3, y2);
               }
            case 57:
               break;
            case 67:
            case 101:
               unit = style == 101 ? 0.4F : -0.4F;
               gen.useDash((String)null);
               if (horz) {
                  c = ColorUtil.lightenColor(col, (float)(startOrBefore ? 1 : -1) * unit);
                  gen.useLineWidth((double)h);
                  ym = y1 + h / 2.0F;
                  gen.useColor(c);
                  drawLine(gen, x1, ym, x2, ym);
               } else {
                  c = ColorUtil.lightenColor(col, (float)(startOrBefore ? 1 : -1) * unit);
                  gen.useLineWidth((double)w);
                  ym = x1 + w / 2.0F;
                  gen.useColor(c);
                  drawLine(gen, ym, y1, ym, y2);
               }
               break;
            default:
               gen.useColor(col);
               gen.useDash((String)null);
               gen.useLineCap(0);
               if (horz) {
                  gen.useLineWidth((double)h);
                  ym1 = y1 + h / 2.0F;
                  drawLine(gen, x1, ym1, x2, ym1);
               } else {
                  gen.useLineWidth((double)w);
                  ym1 = x1 + w / 2.0F;
                  drawLine(gen, ym1, y1, ym1, y2);
               }
         }

      } else {
         log.error("Negative extent received. Border won't be painted.");
      }
   }

   public void drawLine(Point start, Point end, int width, Color color, RuleStyle style) throws IOException {
      if (start.y != end.y) {
         throw new UnsupportedOperationException("Can only deal with horizontal lines right now");
      } else {
         this.saveGraphicsState();
         int half = width / 2;
         int starty = start.y - half;
         switch (style.getEnumValue()) {
            case 31:
            case 37:
            case 133:
               this.drawBorderLine(start.x, starty, end.x, starty + width, true, true, style.getEnumValue(), color);
               break;
            case 36:
               this.clipRect(start.x, starty, end.x - start.x, width);
               this.generator.concatMatrix(1.0, 0.0, 0.0, 1.0, (double)toPoints(half), 0.0);
               this.drawBorderLine(start.x, starty, end.x, starty + width, true, true, style.getEnumValue(), color);
               break;
            case 55:
            case 119:
               this.generator.useColor(ColorUtil.lightenColor(color, 0.6F));
               this.moveTo(start.x, starty);
               this.lineTo(end.x, starty);
               this.lineTo(end.x, starty + 2 * half);
               this.lineTo(start.x, starty + 2 * half);
               this.closePath();
               this.generator.writeln(" fill newpath");
               this.generator.useColor(color);
               if (style == RuleStyle.GROOVE) {
                  this.moveTo(start.x, starty);
                  this.lineTo(end.x, starty);
                  this.lineTo(end.x, starty + half);
                  this.lineTo(start.x + half, starty + half);
                  this.lineTo(start.x, starty + 2 * half);
               } else {
                  this.moveTo(end.x, starty);
                  this.lineTo(end.x, starty + 2 * half);
                  this.lineTo(start.x, starty + 2 * half);
                  this.lineTo(start.x, starty + half);
                  this.lineTo(end.x - half, starty + half);
               }

               this.closePath();
               this.generator.writeln(" fill newpath");
               break;
            default:
               throw new UnsupportedOperationException("rule style not supported");
         }

         this.restoreGraphicsState();
      }
   }

   private static float toPoints(int mpt) {
      return (float)mpt / 1000.0F;
   }

   protected void moveTo(int x, int y) throws IOException {
      this.generator.writeln(this.generator.formatDouble((double)toPoints(x)) + " " + this.generator.formatDouble((double)toPoints(y)) + " M");
   }

   protected void lineTo(int x, int y) throws IOException {
      this.generator.writeln(this.generator.formatDouble((double)toPoints(x)) + " " + this.generator.formatDouble((double)toPoints(y)) + " lineto");
   }

   protected void closePath() throws IOException {
      this.generator.writeln("cp");
   }

   private void clipRect(int x, int y, int width, int height) throws IOException {
      this.generator.defineRect((double)toPoints(x), (double)toPoints(y), (double)toPoints(width), (double)toPoints(height));
      this.clip();
   }

   protected void clip() throws IOException {
      this.generator.writeln("clip newpath");
   }

   protected void saveGraphicsState() throws IOException {
      this.generator.saveGraphicsState();
   }

   protected void restoreGraphicsState() throws IOException {
      this.generator.restoreGraphicsState();
   }

   static {
      log = LogFactory.getLog(PSBorderPainter.class);
   }
}
