package org.apache.fop.render.pdf;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.render.intermediate.BorderPainter;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.ColorUtil;

public class PDFBorderPainter extends BorderPainter {
   private static Log log;
   private PDFContentGenerator generator;

   public PDFBorderPainter(PDFContentGenerator generator) {
      this.generator = generator;
   }

   protected void drawBorderLine(int x1, int y1, int x2, int y2, boolean horz, boolean startOrBefore, int style, Color col) {
      drawBorderLine(this.generator, (float)x1 / 1000.0F, (float)y1 / 1000.0F, (float)x2 / 1000.0F, (float)y2 / 1000.0F, horz, startOrBefore, style, col);
   }

   public static void drawBorderLine(PDFContentGenerator generator, float x1, float y1, float x2, float y2, boolean horz, boolean startOrBefore, int style, Color col) {
      float w = x2 - x1;
      float h = y2 - y1;
      if (!(w < 0.0F) && !(h < 0.0F)) {
         float unit;
         Color c;
         float ym;
         int rep;
         switch (style) {
            case 31:
               generator.setColor(col, false);
               if (horz) {
                  unit = Math.abs(2.0F * h);
                  rep = (int)(w / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = w / (float)rep;
                  generator.add("[" + format(unit) + "] 0 d ");
                  generator.add(format(h) + " w\n");
                  ym = y1 + h / 2.0F;
                  generator.add(format(x1) + " " + format(ym) + " m " + format(x2) + " " + format(ym) + " l S\n");
               } else {
                  unit = Math.abs(2.0F * w);
                  rep = (int)(h / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = h / (float)rep;
                  generator.add("[" + format(unit) + "] 0 d ");
                  generator.add(format(w) + " w\n");
                  ym = x1 + w / 2.0F;
                  generator.add(format(ym) + " " + format(y1) + " m " + format(ym) + " " + format(y2) + " l S\n");
               }
               break;
            case 36:
               generator.setColor(col, false);
               generator.add("1 J ");
               if (horz) {
                  unit = Math.abs(2.0F * h);
                  rep = (int)(w / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = w / (float)rep;
                  generator.add("[0 " + format(unit) + "] 0 d ");
                  generator.add(format(h) + " w\n");
                  ym = y1 + h / 2.0F;
                  generator.add(format(x1) + " " + format(ym) + " m " + format(x2) + " " + format(ym) + " l S\n");
               } else {
                  unit = Math.abs(2.0F * w);
                  rep = (int)(h / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = h / (float)rep;
                  generator.add("[0 " + format(unit) + " ] 0 d ");
                  generator.add(format(w) + " w\n");
                  ym = x1 + w / 2.0F;
                  generator.add(format(ym) + " " + format(y1) + " m " + format(ym) + " " + format(y2) + " l S\n");
               }
               break;
            case 37:
               generator.setColor(col, false);
               generator.add("[] 0 d ");
               float ym1;
               if (horz) {
                  unit = h / 3.0F;
                  generator.add(format(unit) + " w\n");
                  ym1 = y1 + unit / 2.0F;
                  ym = ym1 + unit + unit;
                  generator.add(format(x1) + " " + format(ym1) + " m " + format(x2) + " " + format(ym1) + " l S\n");
                  generator.add(format(x1) + " " + format(ym) + " m " + format(x2) + " " + format(ym) + " l S\n");
               } else {
                  unit = w / 3.0F;
                  generator.add(format(unit) + " w\n");
                  ym1 = x1 + unit / 2.0F;
                  ym = ym1 + unit + unit;
                  generator.add(format(ym1) + " " + format(y1) + " m " + format(ym1) + " " + format(y2) + " l S\n");
                  generator.add(format(ym) + " " + format(y1) + " m " + format(ym) + " " + format(y2) + " l S\n");
               }
               break;
            case 55:
            case 119:
               unit = style == 55 ? 0.4F : -0.4F;
               generator.add("[] 0 d ");
               float h3;
               float ym1;
               Color lowercol;
               if (horz) {
                  c = ColorUtil.lightenColor(col, -unit);
                  lowercol = ColorUtil.lightenColor(col, unit);
                  h3 = h / 3.0F;
                  generator.add(format(h3) + " w\n");
                  ym1 = y1 + h3 / 2.0F;
                  generator.setColor(c, false);
                  generator.add(format(x1) + " " + format(ym1) + " m " + format(x2) + " " + format(ym1) + " l S\n");
                  generator.setColor(col, false);
                  generator.add(format(x1) + " " + format(ym1 + h3) + " m " + format(x2) + " " + format(ym1 + h3) + " l S\n");
                  generator.setColor(lowercol, false);
                  generator.add(format(x1) + " " + format(ym1 + h3 + h3) + " m " + format(x2) + " " + format(ym1 + h3 + h3) + " l S\n");
               } else {
                  c = ColorUtil.lightenColor(col, -unit);
                  lowercol = ColorUtil.lightenColor(col, unit);
                  h3 = w / 3.0F;
                  generator.add(format(h3) + " w\n");
                  ym1 = x1 + h3 / 2.0F;
                  generator.setColor(c, false);
                  generator.add(format(ym1) + " " + format(y1) + " m " + format(ym1) + " " + format(y2) + " l S\n");
                  generator.setColor(col, false);
                  generator.add(format(ym1 + h3) + " " + format(y1) + " m " + format(ym1 + h3) + " " + format(y2) + " l S\n");
                  generator.setColor(lowercol, false);
                  generator.add(format(ym1 + h3 + h3) + " " + format(y1) + " m " + format(ym1 + h3 + h3) + " " + format(y2) + " l S\n");
               }
            case 57:
               break;
            case 67:
            case 101:
               unit = style == 101 ? 0.4F : -0.4F;
               generator.add("[] 0 d ");
               if (horz) {
                  c = ColorUtil.lightenColor(col, (float)(startOrBefore ? 1 : -1) * unit);
                  generator.add(format(h) + " w\n");
                  ym = y1 + h / 2.0F;
                  generator.setColor(c, false);
                  generator.add(format(x1) + " " + format(ym) + " m " + format(x2) + " " + format(ym) + " l S\n");
               } else {
                  c = ColorUtil.lightenColor(col, (float)(startOrBefore ? 1 : -1) * unit);
                  generator.add(format(w) + " w\n");
                  ym = x1 + w / 2.0F;
                  generator.setColor(c, false);
                  generator.add(format(ym) + " " + format(y1) + " m " + format(ym) + " " + format(y2) + " l S\n");
               }
               break;
            default:
               generator.setColor(col, false);
               generator.add("[] 0 d ");
               if (horz) {
                  generator.add(format(h) + " w\n");
                  unit = y1 + h / 2.0F;
                  generator.add(format(x1) + " " + format(unit) + " m " + format(x2) + " " + format(unit) + " l S\n");
               } else {
                  generator.add(format(w) + " w\n");
                  unit = x1 + w / 2.0F;
                  generator.add(format(unit) + " " + format(y1) + " m " + format(unit) + " " + format(y2) + " l S\n");
               }
         }

      } else {
         log.error("Negative extent received (w=" + w + ", h=" + h + "). Border won't be painted.");
      }
   }

   public void drawLine(Point start, Point end, int width, Color color, RuleStyle style) {
      if (start.y != end.y) {
         throw new UnsupportedOperationException("Can only deal with horizontal lines right now");
      } else {
         this.saveGraphicsState();
         int half = width / 2;
         int starty = start.y - half;
         Rectangle boundingRect = new Rectangle(start.x, start.y - half, end.x - start.x, width);
         switch (style.getEnumValue()) {
            case 31:
            case 37:
            case 133:
               this.drawBorderLine(start.x, start.y - half, end.x, end.y + half, true, true, style.getEnumValue(), color);
               break;
            case 36:
               this.generator.clipRect(boundingRect);
               this.generator.add("1 0 0 1 " + format(half) + " 0 cm\n");
               this.drawBorderLine(start.x, start.y - half, end.x, end.y + half, true, true, style.getEnumValue(), color);
               break;
            case 55:
            case 119:
               this.generator.setColor(ColorUtil.lightenColor(color, 0.6F), true);
               this.generator.add(format(start.x) + " " + format(starty) + " m\n");
               this.generator.add(format(end.x) + " " + format(starty) + " l\n");
               this.generator.add(format(end.x) + " " + format(starty + 2 * half) + " l\n");
               this.generator.add(format(start.x) + " " + format(starty + 2 * half) + " l\n");
               this.generator.add("h\n");
               this.generator.add("f\n");
               this.generator.setColor(color, true);
               if (style == RuleStyle.GROOVE) {
                  this.generator.add(format(start.x) + " " + format(starty) + " m\n");
                  this.generator.add(format(end.x) + " " + format(starty) + " l\n");
                  this.generator.add(format(end.x) + " " + format(starty + half) + " l\n");
                  this.generator.add(format(start.x + half) + " " + format(starty + half) + " l\n");
                  this.generator.add(format(start.x) + " " + format(starty + 2 * half) + " l\n");
               } else {
                  this.generator.add(format(end.x) + " " + format(starty) + " m\n");
                  this.generator.add(format(end.x) + " " + format(starty + 2 * half) + " l\n");
                  this.generator.add(format(start.x) + " " + format(starty + 2 * half) + " l\n");
                  this.generator.add(format(start.x) + " " + format(starty + half) + " l\n");
                  this.generator.add(format(end.x - half) + " " + format(starty + half) + " l\n");
               }

               this.generator.add("h\n");
               this.generator.add("f\n");
               break;
            default:
               throw new UnsupportedOperationException("rule style not supported");
         }

         this.restoreGraphicsState();
      }
   }

   static final String format(int coordinate) {
      return format((float)coordinate / 1000.0F);
   }

   static final String format(float coordinate) {
      return PDFContentGenerator.format(coordinate);
   }

   protected void moveTo(int x, int y) {
      this.generator.add(format(x) + " " + format(y) + " m ");
   }

   protected void lineTo(int x, int y) {
      this.generator.add(format(x) + " " + format(y) + " l ");
   }

   protected void closePath() {
      this.generator.add("h ");
   }

   protected void clip() {
      this.generator.add("W\nn\n");
   }

   protected void saveGraphicsState() {
      this.generator.add("q\n");
   }

   protected void restoreGraphicsState() {
      this.generator.add("Q\n");
   }

   static {
      log = LogFactory.getLog(PDFBorderPainter.class);
   }
}
