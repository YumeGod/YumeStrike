package org.apache.fop.render.java2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.render.intermediate.BorderPainter;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.ColorUtil;

public class Java2DBorderPainter extends BorderPainter {
   private static Log log;
   private Java2DPainter painter;
   private GeneralPath currentPath = null;

   public Java2DBorderPainter(Java2DPainter painter) {
      this.painter = painter;
   }

   private Java2DGraphicsState getG2DState() {
      return this.painter.g2dState;
   }

   private Graphics2D getG2D() {
      return this.getG2DState().getGraph();
   }

   protected void drawBorderLine(int x1, int y1, int x2, int y2, boolean horz, boolean startOrBefore, int style, Color color) {
      float w = (float)(x2 - x1);
      float h = (float)(y2 - y1);
      if (!(w < 0.0F) && !(h < 0.0F)) {
         float unit;
         float ym1;
         float ym;
         int rep;
         BasicStroke s;
         switch (style) {
            case 31:
               this.getG2D().setColor(color);
               if (horz) {
                  unit = Math.abs(2.0F * h);
                  rep = (int)(w / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = w / (float)rep;
                  ym = (float)y1 + h / 2.0F;
                  s = new BasicStroke(h, 0, 0, 10.0F, new float[]{unit}, 0.0F);
                  this.getG2D().setStroke(s);
                  this.getG2D().draw(new Line2D.Float((float)x1, ym, (float)x2, ym));
               } else {
                  unit = Math.abs(2.0F * w);
                  rep = (int)(h / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = h / (float)rep;
                  ym = (float)x1 + w / 2.0F;
                  s = new BasicStroke(w, 0, 0, 10.0F, new float[]{unit}, 0.0F);
                  this.getG2D().setStroke(s);
                  this.getG2D().draw(new Line2D.Float(ym, (float)y1, ym, (float)y2));
               }
               break;
            case 36:
               this.getG2D().setColor(color);
               if (horz) {
                  unit = Math.abs(2.0F * h);
                  rep = (int)(w / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = w / (float)rep;
                  ym = (float)y1 + h / 2.0F;
                  s = new BasicStroke(h, 1, 0, 10.0F, new float[]{0.0F, unit}, 0.0F);
                  this.getG2D().setStroke(s);
                  this.getG2D().draw(new Line2D.Float((float)x1, ym, (float)x2, ym));
               } else {
                  unit = Math.abs(2.0F * w);
                  rep = (int)(h / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = h / (float)rep;
                  ym = (float)x1 + w / 2.0F;
                  s = new BasicStroke(w, 1, 0, 10.0F, new float[]{0.0F, unit}, 0.0F);
                  this.getG2D().setStroke(s);
                  this.getG2D().draw(new Line2D.Float(ym, (float)y1, ym, (float)y2));
               }
               break;
            case 37:
               this.getG2D().setColor(color);
               if (horz) {
                  unit = h / 3.0F;
                  ym1 = (float)y1 + unit / 2.0F;
                  ym = ym1 + unit + unit;
                  s = new BasicStroke(unit);
                  this.getG2D().setStroke(s);
                  this.getG2D().draw(new Line2D.Float((float)x1, ym1, (float)x2, ym1));
                  this.getG2D().draw(new Line2D.Float((float)x1, ym, (float)x2, ym));
               } else {
                  unit = w / 3.0F;
                  ym1 = (float)x1 + unit / 2.0F;
                  ym = ym1 + unit + unit;
                  s = new BasicStroke(unit);
                  this.getG2D().setStroke(s);
                  this.getG2D().draw(new Line2D.Float(ym1, (float)y1, ym1, (float)y2));
                  this.getG2D().draw(new Line2D.Float(ym, (float)y1, ym, (float)y2));
               }
               break;
            case 55:
            case 119:
               unit = style == 55 ? 0.4F : -0.4F;
               Color lowercol;
               float h3;
               float ym1;
               Color uppercol;
               if (horz) {
                  uppercol = ColorUtil.lightenColor(color, -unit);
                  lowercol = ColorUtil.lightenColor(color, unit);
                  h3 = h / 3.0F;
                  ym1 = (float)y1 + h3 / 2.0F;
                  this.getG2D().setStroke(new BasicStroke(h3));
                  this.getG2D().setColor(uppercol);
                  this.getG2D().draw(new Line2D.Float((float)x1, ym1, (float)x2, ym1));
                  this.getG2D().setColor(color);
                  this.getG2D().draw(new Line2D.Float((float)x1, ym1 + h3, (float)x2, ym1 + h3));
                  this.getG2D().setColor(lowercol);
                  this.getG2D().draw(new Line2D.Float((float)x1, ym1 + h3 + h3, (float)x2, ym1 + h3 + h3));
               } else {
                  uppercol = ColorUtil.lightenColor(color, -unit);
                  lowercol = ColorUtil.lightenColor(color, unit);
                  h3 = w / 3.0F;
                  ym1 = (float)x1 + h3 / 2.0F;
                  this.getG2D().setStroke(new BasicStroke(h3));
                  this.getG2D().setColor(uppercol);
                  this.getG2D().draw(new Line2D.Float(ym1, (float)y1, ym1, (float)y2));
                  this.getG2D().setColor(color);
                  this.getG2D().draw(new Line2D.Float(ym1 + h3, (float)y1, ym1 + h3, (float)y2));
                  this.getG2D().setColor(lowercol);
                  this.getG2D().draw(new Line2D.Float(ym1 + h3 + h3, (float)y1, ym1 + h3 + h3, (float)y2));
               }
            case 57:
               break;
            case 67:
            case 101:
               unit = style == 101 ? 0.4F : -0.4F;
               if (horz) {
                  color = ColorUtil.lightenColor(color, (float)(startOrBefore ? 1 : -1) * unit);
                  this.getG2D().setStroke(new BasicStroke(h));
                  ym1 = (float)y1 + h / 2.0F;
                  this.getG2D().setColor(color);
                  this.getG2D().draw(new Line2D.Float((float)x1, ym1, (float)x2, ym1));
               } else {
                  color = ColorUtil.lightenColor(color, (float)(startOrBefore ? 1 : -1) * unit);
                  ym1 = (float)x1 + w / 2.0F;
                  this.getG2D().setStroke(new BasicStroke(w));
                  this.getG2D().setColor(color);
                  this.getG2D().draw(new Line2D.Float(ym1, (float)y1, ym1, (float)y2));
               }
               break;
            default:
               this.getG2D().setColor(color);
               if (horz) {
                  ym1 = (float)y1 + h / 2.0F;
                  this.getG2D().setStroke(new BasicStroke(h));
                  this.getG2D().draw(new Line2D.Float((float)x1, ym1, (float)x2, ym1));
               } else {
                  ym1 = (float)x1 + w / 2.0F;
                  this.getG2D().setStroke(new BasicStroke(w));
                  this.getG2D().draw(new Line2D.Float(ym1, (float)y1, ym1, (float)y2));
               }
         }

      } else {
         log.error("Negative extent received. Border won't be painted.");
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
         this.getG2DState().updateClip(boundingRect);
         switch (style.getEnumValue()) {
            case 31:
            case 37:
            case 133:
               this.drawBorderLine(start.x, start.y - half, end.x, end.y + half, true, true, style.getEnumValue(), color);
               break;
            case 36:
               this.drawBorderLine(start.x + half, start.y - half, end.x + half, end.y + half, true, true, style.getEnumValue(), color);
               break;
            case 55:
            case 119:
               this.getG2DState().updateColor(ColorUtil.lightenColor(color, 0.6F));
               this.moveTo(start.x, starty);
               this.lineTo(end.x, starty);
               this.lineTo(end.x, starty + 2 * half);
               this.lineTo(start.x, starty + 2 * half);
               this.closePath();
               this.getG2D().fill(this.currentPath);
               this.currentPath = null;
               this.getG2DState().updateColor(color);
               if (style.getEnumValue() == 55) {
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
               this.getG2D().fill(this.currentPath);
               this.currentPath = null;
            case 95:
         }

         this.restoreGraphicsState();
      }
   }

   protected void clip() {
      if (this.currentPath == null) {
         throw new IllegalStateException("No current path available!");
      } else {
         this.getG2DState().updateClip(this.currentPath);
         this.currentPath = null;
      }
   }

   protected void closePath() {
      this.currentPath.closePath();
   }

   protected void lineTo(int x, int y) {
      if (this.currentPath == null) {
         this.currentPath = new GeneralPath();
      }

      this.currentPath.lineTo((float)x, (float)y);
   }

   protected void moveTo(int x, int y) {
      if (this.currentPath == null) {
         this.currentPath = new GeneralPath();
      }

      this.currentPath.moveTo((float)x, (float)y);
   }

   protected void saveGraphicsState() {
      this.painter.saveGraphicsState();
   }

   protected void restoreGraphicsState() {
      this.painter.restoreGraphicsState();
      this.currentPath = null;
   }

   static {
      log = LogFactory.getLog(Java2DBorderPainter.class);
   }
}
