package org.apache.batik.gvt.font;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.text.TextPaintInfo;

public class Glyph {
   private String unicode;
   private Vector names;
   private String orientation;
   private String arabicForm;
   private String lang;
   private Point2D horizOrigin;
   private Point2D vertOrigin;
   private float horizAdvX;
   private float vertAdvY;
   private int glyphCode;
   private AffineTransform transform;
   private Point2D.Float position;
   private GVTGlyphMetrics metrics;
   private Shape outline;
   private Rectangle2D bounds;
   private TextPaintInfo tpi;
   private TextPaintInfo cacheTPI;
   private Shape dShape;
   private GraphicsNode glyphChildrenNode;

   public Glyph(String var1, List var2, String var3, String var4, String var5, Point2D var6, Point2D var7, float var8, float var9, int var10, TextPaintInfo var11, Shape var12, GraphicsNode var13) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else if (var6 == null) {
         throw new IllegalArgumentException();
      } else if (var7 == null) {
         throw new IllegalArgumentException();
      } else {
         this.unicode = var1;
         this.names = new Vector(var2);
         this.orientation = var3;
         this.arabicForm = var4;
         this.lang = var5;
         this.horizOrigin = var6;
         this.vertOrigin = var7;
         this.horizAdvX = var8;
         this.vertAdvY = var9;
         this.glyphCode = var10;
         this.position = new Point2D.Float(0.0F, 0.0F);
         this.outline = null;
         this.bounds = null;
         this.tpi = var11;
         this.dShape = var12;
         this.glyphChildrenNode = var13;
      }
   }

   public String getUnicode() {
      return this.unicode;
   }

   public Vector getNames() {
      return this.names;
   }

   public String getOrientation() {
      return this.orientation;
   }

   public String getArabicForm() {
      return this.arabicForm;
   }

   public String getLang() {
      return this.lang;
   }

   public Point2D getHorizOrigin() {
      return this.horizOrigin;
   }

   public Point2D getVertOrigin() {
      return this.vertOrigin;
   }

   public float getHorizAdvX() {
      return this.horizAdvX;
   }

   public float getVertAdvY() {
      return this.vertAdvY;
   }

   public int getGlyphCode() {
      return this.glyphCode;
   }

   public AffineTransform getTransform() {
      return this.transform;
   }

   public void setTransform(AffineTransform var1) {
      this.transform = var1;
      this.outline = null;
      this.bounds = null;
   }

   public Point2D getPosition() {
      return this.position;
   }

   public void setPosition(Point2D var1) {
      this.position.x = (float)var1.getX();
      this.position.y = (float)var1.getY();
      this.outline = null;
      this.bounds = null;
   }

   public GVTGlyphMetrics getGlyphMetrics() {
      if (this.metrics == null) {
         Rectangle2D var1 = this.getGeometryBounds();
         this.metrics = new GVTGlyphMetrics(this.getHorizAdvX(), this.getVertAdvY(), new Rectangle2D.Double(var1.getX() - this.position.getX(), var1.getY() - this.position.getY(), var1.getWidth(), var1.getHeight()), (byte)3);
      }

      return this.metrics;
   }

   public GVTGlyphMetrics getGlyphMetrics(float var1, float var2) {
      return new GVTGlyphMetrics(this.getHorizAdvX() - var1, this.getVertAdvY() - var2, this.getGeometryBounds(), (byte)3);
   }

   public Rectangle2D getGeometryBounds() {
      return this.getOutline().getBounds2D();
   }

   public Rectangle2D getBounds2D() {
      if (this.bounds != null && TextPaintInfo.equivilent(this.tpi, this.cacheTPI)) {
         return this.bounds;
      } else {
         AffineTransform var1 = AffineTransform.getTranslateInstance(this.position.getX(), this.position.getY());
         if (this.transform != null) {
            var1.concatenate(this.transform);
         }

         Object var2 = null;
         if (this.dShape != null && this.tpi != null) {
            if (this.tpi.fillPaint != null) {
               var2 = var1.createTransformedShape(this.dShape).getBounds2D();
            }

            if (this.tpi.strokeStroke != null && this.tpi.strokePaint != null) {
               Shape var3 = this.tpi.strokeStroke.createStrokedShape(this.dShape);
               Rectangle2D var4 = var1.createTransformedShape(var3).getBounds2D();
               if (var2 == null) {
                  var2 = var4;
               } else {
                  ((Rectangle2D)var2).add(var4);
               }
            }
         }

         if (this.glyphChildrenNode != null) {
            Rectangle2D var5 = this.glyphChildrenNode.getTransformedBounds(var1);
            if (var2 == null) {
               var2 = var5;
            } else {
               ((Rectangle2D)var2).add(var5);
            }
         }

         if (var2 == null) {
            var2 = new Rectangle2D.Double(this.position.getX(), this.position.getY(), 0.0, 0.0);
         }

         this.cacheTPI = new TextPaintInfo(this.tpi);
         return (Rectangle2D)var2;
      }
   }

   public Shape getOutline() {
      if (this.outline == null) {
         AffineTransform var1 = AffineTransform.getTranslateInstance(this.position.getX(), this.position.getY());
         if (this.transform != null) {
            var1.concatenate(this.transform);
         }

         Shape var2 = null;
         if (this.glyphChildrenNode != null) {
            var2 = this.glyphChildrenNode.getOutline();
         }

         GeneralPath var3 = null;
         if (this.dShape != null && var2 != null) {
            var3 = new GeneralPath(this.dShape);
            var3.append(var2, false);
         } else if (this.dShape != null && var2 == null) {
            var3 = new GeneralPath(this.dShape);
         } else if (this.dShape == null && var2 != null) {
            var3 = new GeneralPath(var2);
         } else {
            var3 = new GeneralPath();
         }

         this.outline = var1.createTransformedShape(var3);
      }

      return this.outline;
   }

   public void draw(Graphics2D var1) {
      AffineTransform var2 = AffineTransform.getTranslateInstance(this.position.getX(), this.position.getY());
      if (this.transform != null) {
         var2.concatenate(this.transform);
      }

      if (this.dShape != null && this.tpi != null) {
         Shape var3 = var2.createTransformedShape(this.dShape);
         if (this.tpi.fillPaint != null) {
            var1.setPaint(this.tpi.fillPaint);
            var1.fill(var3);
         }

         if (this.tpi.strokeStroke != null && this.tpi.strokePaint != null) {
            var1.setStroke(this.tpi.strokeStroke);
            var1.setPaint(this.tpi.strokePaint);
            var1.draw(var3);
         }
      }

      if (this.glyphChildrenNode != null) {
         this.glyphChildrenNode.setTransform(var2);
         this.glyphChildrenNode.paint(var1);
      }

   }
}
