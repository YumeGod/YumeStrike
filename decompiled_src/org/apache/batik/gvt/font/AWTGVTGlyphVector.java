package org.apache.batik.gvt.font;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import org.apache.batik.gvt.text.ArabicTextHandler;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.TextPaintInfo;

public class AWTGVTGlyphVector implements GVTGlyphVector {
   public static final AttributedCharacterIterator.Attribute PAINT_INFO;
   private GlyphVector awtGlyphVector;
   private AWTGVTFont gvtFont;
   private CharacterIterator ci;
   private Point2D[] defaultGlyphPositions;
   private Point2D.Float[] glyphPositions;
   private AffineTransform[] glyphTransforms;
   private Shape[] glyphOutlines;
   private Shape[] glyphVisualBounds;
   private Shape[] glyphLogicalBounds;
   private boolean[] glyphVisible;
   private GVTGlyphMetrics[] glyphMetrics;
   private GeneralPath outline;
   private Rectangle2D visualBounds;
   private Rectangle2D logicalBounds;
   private Rectangle2D bounds2D;
   private float scaleFactor;
   private float ascent;
   private float descent;
   private TextPaintInfo cacheTPI;
   private static final boolean outlinesPositioned;
   private static final boolean drawGlyphVectorWorks;
   private static final boolean glyphVectorTransformWorks;

   public AWTGVTGlyphVector(GlyphVector var1, AWTGVTFont var2, float var3, CharacterIterator var4) {
      this.awtGlyphVector = var1;
      this.gvtFont = var2;
      this.scaleFactor = var3;
      this.ci = var4;
      GVTLineMetrics var5 = this.gvtFont.getLineMetrics("By", this.awtGlyphVector.getFontRenderContext());
      this.ascent = var5.getAscent();
      this.descent = var5.getDescent();
      this.outline = null;
      this.visualBounds = null;
      this.logicalBounds = null;
      this.bounds2D = null;
      int var6 = var1.getNumGlyphs();
      this.glyphPositions = new Point2D.Float[var6 + 1];
      this.glyphTransforms = new AffineTransform[var6];
      this.glyphOutlines = new Shape[var6];
      this.glyphVisualBounds = new Shape[var6];
      this.glyphLogicalBounds = new Shape[var6];
      this.glyphVisible = new boolean[var6];
      this.glyphMetrics = new GVTGlyphMetrics[var6];

      for(int var7 = 0; var7 < var6; ++var7) {
         this.glyphVisible[var7] = true;
      }

   }

   public GVTFont getFont() {
      return this.gvtFont;
   }

   public FontRenderContext getFontRenderContext() {
      return this.awtGlyphVector.getFontRenderContext();
   }

   public int getGlyphCode(int var1) {
      return this.awtGlyphVector.getGlyphCode(var1);
   }

   public int[] getGlyphCodes(int var1, int var2, int[] var3) {
      return this.awtGlyphVector.getGlyphCodes(var1, var2, var3);
   }

   public GlyphJustificationInfo getGlyphJustificationInfo(int var1) {
      return this.awtGlyphVector.getGlyphJustificationInfo(var1);
   }

   public Rectangle2D getBounds2D(AttributedCharacterIterator var1) {
      var1.first();
      TextPaintInfo var2 = (TextPaintInfo)var1.getAttribute(PAINT_INFO);
      if (this.bounds2D != null && TextPaintInfo.equivilent(var2, this.cacheTPI)) {
         return this.bounds2D;
      } else if (var2 == null) {
         return null;
      } else if (!var2.visible) {
         return null;
      } else {
         this.cacheTPI = new TextPaintInfo(var2);
         Shape var3 = null;
         if (var2.fillPaint != null) {
            var3 = this.getOutline();
            this.bounds2D = var3.getBounds2D();
         }

         Stroke var4 = var2.strokeStroke;
         Paint var5 = var2.strokePaint;
         if (var4 != null && var5 != null) {
            if (var3 == null) {
               var3 = this.getOutline();
            }

            Rectangle2D var6 = var4.createStrokedShape(var3).getBounds2D();
            if (this.bounds2D == null) {
               this.bounds2D = var6;
            } else {
               this.bounds2D.add(var6);
            }
         }

         if (this.bounds2D == null) {
            return null;
         } else {
            if (this.bounds2D.getWidth() == 0.0 || this.bounds2D.getHeight() == 0.0) {
               this.bounds2D = null;
            }

            return this.bounds2D;
         }
      }
   }

   public Rectangle2D getLogicalBounds() {
      if (this.logicalBounds == null) {
         this.computeGlyphLogicalBounds();
      }

      return this.logicalBounds;
   }

   public Shape getGlyphLogicalBounds(int var1) {
      if (this.glyphLogicalBounds[var1] == null && this.glyphVisible[var1]) {
         this.computeGlyphLogicalBounds();
      }

      return this.glyphLogicalBounds[var1];
   }

   private void computeGlyphLogicalBounds() {
      Shape[] var1 = new Shape[this.getNumGlyphs()];
      boolean[] var2 = new boolean[this.getNumGlyphs()];
      double var3 = -1.0;
      double var5 = -1.0;

      for(int var7 = 0; var7 < this.getNumGlyphs(); ++var7) {
         if (!this.glyphVisible[var7]) {
            var1[var7] = null;
         } else {
            AffineTransform var8 = this.getGlyphTransform(var7);
            GVTGlyphMetrics var9 = this.getGlyphMetrics(var7);
            float var10 = 0.0F;
            float var11 = -this.ascent / this.scaleFactor;
            float var12 = var9.getHorizontalAdvance() / this.scaleFactor;
            float var13 = var9.getVerticalAdvance() / this.scaleFactor;
            Rectangle2D.Double var14 = new Rectangle2D.Double((double)var10, (double)var11, (double)var12, (double)var13);
            if (var14.isEmpty()) {
               if (var7 > 0) {
                  var2[var7] = var2[var7 - 1];
               } else {
                  var2[var7] = true;
               }
            } else {
               Point2D.Double var15 = new Point2D.Double(var14.getMinX(), var14.getMinY());
               Point2D.Double var16 = new Point2D.Double(var14.getMaxX(), var14.getMinY());
               Point2D.Double var17 = new Point2D.Double(var14.getMinX(), var14.getMaxY());
               Point2D var18 = this.getGlyphPosition(var7);
               AffineTransform var19 = AffineTransform.getTranslateInstance(var18.getX(), var18.getY());
               if (var8 != null) {
                  var19.concatenate(var8);
               }

               var19.scale((double)this.scaleFactor, (double)this.scaleFactor);
               var1[var7] = var19.createTransformedShape(var14);
               Point2D.Double var20 = new Point2D.Double();
               Point2D.Double var21 = new Point2D.Double();
               Point2D.Double var22 = new Point2D.Double();
               var19.transform(var15, var20);
               var19.transform(var16, var21);
               var19.transform(var17, var22);
               double var23 = var20.getX() - var21.getX();
               double var25 = var20.getX() - var22.getX();
               double var27 = var20.getY() - var21.getY();
               double var29 = var20.getY() - var22.getY();
               if (Math.abs(var23) < 0.001 && Math.abs(var29) < 0.001 || Math.abs(var25) < 0.001 && Math.abs(var27) < 0.001) {
                  var2[var7] = false;
               } else {
                  var2[var7] = true;
               }

               Rectangle2D var31 = var1[var7].getBounds2D();
               if (var31.getWidth() > var3) {
                  var3 = var31.getWidth();
               }

               if (var31.getHeight() > var5) {
                  var5 = var31.getHeight();
               }
            }
         }
      }

      GeneralPath var32 = new GeneralPath();

      int var33;
      for(var33 = 0; var33 < this.getNumGlyphs(); ++var33) {
         if (var1[var33] != null) {
            var32.append(var1[var33], false);
         }
      }

      this.logicalBounds = var32.getBounds2D();
      Rectangle2D var34;
      double var35;
      double var36;
      Rectangle2D var37;
      double var38;
      double var39;
      if (this.logicalBounds.getHeight() < var5 * 1.5) {
         for(var33 = 0; var33 < this.getNumGlyphs(); ++var33) {
            if (!var2[var33] && var1[var33] != null) {
               var34 = var1[var33].getBounds2D();
               var35 = var34.getMinX();
               var36 = var34.getWidth();
               if (var33 < this.getNumGlyphs() - 1 && var1[var33 + 1] != null) {
                  var37 = var1[var33 + 1].getBounds2D();
                  if (var37.getX() > var35) {
                     var38 = var37.getX() - var35;
                     if (var38 < var36 * 1.15 && var38 > var36 * 0.85) {
                        var39 = (var38 - var36) * 0.5;
                        var36 += var39;
                        var37.setRect(var37.getX() - var39, var37.getY(), var37.getWidth() + var39, var37.getHeight());
                     }
                  }
               }

               var1[var33] = new Rectangle2D.Double(var35, this.logicalBounds.getMinY(), var36, this.logicalBounds.getHeight());
            }
         }
      } else if (this.logicalBounds.getWidth() < var3 * 1.5) {
         for(var33 = 0; var33 < this.getNumGlyphs(); ++var33) {
            if (!var2[var33] && var1[var33] != null) {
               var34 = var1[var33].getBounds2D();
               var35 = var34.getMinY();
               var36 = var34.getHeight();
               if (var33 < this.getNumGlyphs() - 1 && var1[var33 + 1] != null) {
                  var37 = var1[var33 + 1].getBounds2D();
                  if (var37.getY() > var35) {
                     var38 = var37.getY() - var35;
                     if (var38 < var36 * 1.15 && var38 > var36 * 0.85) {
                        var39 = (var38 - var36) * 0.5;
                        var36 += var39;
                        var37.setRect(var37.getX(), var37.getY() - var39, var37.getWidth(), var37.getHeight() + var39);
                     }
                  }
               }

               var1[var33] = new Rectangle2D.Double(this.logicalBounds.getMinX(), var35, this.logicalBounds.getWidth(), var36);
            }
         }
      }

      System.arraycopy(var1, 0, this.glyphLogicalBounds, 0, this.getNumGlyphs());
   }

   public GVTGlyphMetrics getGlyphMetrics(int var1) {
      if (this.glyphMetrics[var1] != null) {
         return this.glyphMetrics[var1];
      } else {
         Point2D var2 = this.defaultGlyphPositions[var1];
         char var3 = this.ci.setIndex(this.ci.getBeginIndex() + var1);
         this.ci.setIndex(this.ci.getBeginIndex());
         AWTGlyphGeometryCache.Value var4 = AWTGVTFont.getGlyphGeometry(this.gvtFont, var3, this.awtGlyphVector, var1, var2);
         Rectangle2D var5 = var4.getBounds2D();
         Rectangle2D.Double var6 = new Rectangle2D.Double(var5.getX() * (double)this.scaleFactor, var5.getY() * (double)this.scaleFactor, var5.getWidth() * (double)this.scaleFactor, var5.getHeight() * (double)this.scaleFactor);
         float var7 = (float)(this.defaultGlyphPositions[var1 + 1].getX() - this.defaultGlyphPositions[var1].getX());
         this.glyphMetrics[var1] = new GVTGlyphMetrics(var7 * this.scaleFactor, this.ascent + this.descent, var6, (byte)0);
         return this.glyphMetrics[var1];
      }
   }

   public Shape getGlyphOutline(int var1) {
      if (this.glyphOutlines[var1] == null) {
         Point2D var2 = this.defaultGlyphPositions[var1];
         char var3 = this.ci.setIndex(this.ci.getBeginIndex() + var1);
         this.ci.setIndex(this.ci.getBeginIndex());
         AWTGlyphGeometryCache.Value var4 = AWTGVTFont.getGlyphGeometry(this.gvtFont, var3, this.awtGlyphVector, var1, var2);
         Shape var5 = var4.getOutline();
         AffineTransform var6 = AffineTransform.getTranslateInstance(this.getGlyphPosition(var1).getX(), this.getGlyphPosition(var1).getY());
         AffineTransform var7 = this.getGlyphTransform(var1);
         if (var7 != null) {
            var6.concatenate(var7);
         }

         var6.scale((double)this.scaleFactor, (double)this.scaleFactor);
         this.glyphOutlines[var1] = var6.createTransformedShape(var5);
      }

      return this.glyphOutlines[var1];
   }

   static boolean outlinesPositioned() {
      return outlinesPositioned;
   }

   public Rectangle2D getGlyphCellBounds(int var1) {
      return this.getGlyphLogicalBounds(var1).getBounds2D();
   }

   public Point2D getGlyphPosition(int var1) {
      return this.glyphPositions[var1];
   }

   public float[] getGlyphPositions(int var1, int var2, float[] var3) {
      if (var3 == null) {
         var3 = new float[var2 * 2];
      }

      for(int var4 = var1; var4 < var1 + var2; ++var4) {
         Point2D var5 = this.getGlyphPosition(var4);
         var3[(var4 - var1) * 2] = (float)var5.getX();
         var3[(var4 - var1) * 2 + 1] = (float)var5.getY();
      }

      return var3;
   }

   public AffineTransform getGlyphTransform(int var1) {
      return this.glyphTransforms[var1];
   }

   public Shape getGlyphVisualBounds(int var1) {
      if (this.glyphVisualBounds[var1] == null) {
         Point2D var2 = this.defaultGlyphPositions[var1];
         char var3 = this.ci.setIndex(this.ci.getBeginIndex() + var1);
         this.ci.setIndex(this.ci.getBeginIndex());
         AWTGlyphGeometryCache.Value var4 = AWTGVTFont.getGlyphGeometry(this.gvtFont, var3, this.awtGlyphVector, var1, var2);
         Rectangle2D var5 = var4.getOutlineBounds2D();
         AffineTransform var6 = AffineTransform.getTranslateInstance(this.getGlyphPosition(var1).getX(), this.getGlyphPosition(var1).getY());
         AffineTransform var7 = this.getGlyphTransform(var1);
         if (var7 != null) {
            var6.concatenate(var7);
         }

         var6.scale((double)this.scaleFactor, (double)this.scaleFactor);
         this.glyphVisualBounds[var1] = var6.createTransformedShape(var5);
      }

      return this.glyphVisualBounds[var1];
   }

   public int getNumGlyphs() {
      return this.awtGlyphVector.getNumGlyphs();
   }

   public Shape getOutline() {
      if (this.outline != null) {
         return this.outline;
      } else {
         this.outline = new GeneralPath();

         for(int var1 = 0; var1 < this.getNumGlyphs(); ++var1) {
            if (this.glyphVisible[var1]) {
               Shape var2 = this.getGlyphOutline(var1);
               this.outline.append(var2, false);
            }
         }

         return this.outline;
      }
   }

   public Shape getOutline(float var1, float var2) {
      Shape var3 = this.getOutline();
      AffineTransform var4 = AffineTransform.getTranslateInstance((double)var1, (double)var2);
      var3 = var4.createTransformedShape(var3);
      return var3;
   }

   public Rectangle2D getGeometricBounds() {
      if (this.visualBounds == null) {
         Shape var1 = this.getOutline();
         this.visualBounds = var1.getBounds2D();
      }

      return this.visualBounds;
   }

   public void performDefaultLayout() {
      if (this.defaultGlyphPositions == null) {
         this.awtGlyphVector.performDefaultLayout();
         this.defaultGlyphPositions = new Point2D.Float[this.getNumGlyphs() + 1];

         for(int var1 = 0; var1 <= this.getNumGlyphs(); ++var1) {
            this.defaultGlyphPositions[var1] = this.awtGlyphVector.getGlyphPosition(var1);
         }
      }

      this.outline = null;
      this.visualBounds = null;
      this.logicalBounds = null;
      this.bounds2D = null;
      float var6 = 0.0F;

      int var2;
      Point2D var3;
      for(var2 = 0; var2 < this.getNumGlyphs(); ++var2) {
         this.glyphTransforms[var2] = null;
         this.glyphVisualBounds[var2] = null;
         this.glyphLogicalBounds[var2] = null;
         this.glyphOutlines[var2] = null;
         this.glyphMetrics[var2] = null;
         var3 = this.defaultGlyphPositions[var2];
         float var4 = (float)(var3.getX() * (double)this.scaleFactor - (double)var6);
         float var5 = (float)(var3.getY() * (double)this.scaleFactor);
         this.ci.setIndex(var2 + this.ci.getBeginIndex());
         if (this.glyphPositions[var2] == null) {
            this.glyphPositions[var2] = new Point2D.Float(var4, var5);
         } else {
            this.glyphPositions[var2].x = var4;
            this.glyphPositions[var2].y = var5;
         }
      }

      var3 = this.defaultGlyphPositions[var2];
      this.glyphPositions[var2] = new Point2D.Float((float)(var3.getX() * (double)this.scaleFactor - (double)var6), (float)(var3.getY() * (double)this.scaleFactor));
   }

   public void setGlyphPosition(int var1, Point2D var2) {
      this.glyphPositions[var1].x = (float)var2.getX();
      this.glyphPositions[var1].y = (float)var2.getY();
      this.outline = null;
      this.visualBounds = null;
      this.logicalBounds = null;
      this.bounds2D = null;
      if (var1 != this.getNumGlyphs()) {
         this.glyphVisualBounds[var1] = null;
         this.glyphLogicalBounds[var1] = null;
         this.glyphOutlines[var1] = null;
         this.glyphMetrics[var1] = null;
      }

   }

   public void setGlyphTransform(int var1, AffineTransform var2) {
      this.glyphTransforms[var1] = var2;
      this.outline = null;
      this.visualBounds = null;
      this.logicalBounds = null;
      this.bounds2D = null;
      this.glyphVisualBounds[var1] = null;
      this.glyphLogicalBounds[var1] = null;
      this.glyphOutlines[var1] = null;
      this.glyphMetrics[var1] = null;
   }

   public void setGlyphVisible(int var1, boolean var2) {
      if (var2 != this.glyphVisible[var1]) {
         this.glyphVisible[var1] = var2;
         this.outline = null;
         this.visualBounds = null;
         this.logicalBounds = null;
         this.bounds2D = null;
         this.glyphVisualBounds[var1] = null;
         this.glyphLogicalBounds[var1] = null;
         this.glyphOutlines[var1] = null;
         this.glyphMetrics[var1] = null;
      }
   }

   public boolean isGlyphVisible(int var1) {
      return this.glyphVisible[var1];
   }

   public int getCharacterCount(int var1, int var2) {
      if (var1 < 0) {
         var1 = 0;
      }

      if (var2 >= this.getNumGlyphs()) {
         var2 = this.getNumGlyphs() - 1;
      }

      int var3 = 0;
      int var4 = var1 + this.ci.getBeginIndex();
      int var5 = var2 + this.ci.getBeginIndex();

      for(char var6 = this.ci.setIndex(var4); this.ci.getIndex() <= var5; var6 = this.ci.next()) {
         var3 += ArabicTextHandler.getNumChars(var6);
      }

      return var3;
   }

   public void draw(Graphics2D var1, AttributedCharacterIterator var2) {
      int var3 = this.getNumGlyphs();
      var2.first();
      TextPaintInfo var4 = (TextPaintInfo)var2.getAttribute(GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO);
      if (var4 != null) {
         if (var4.visible) {
            Paint var5 = var4.fillPaint;
            Stroke var6 = var4.strokeStroke;
            Paint var7 = var4.strokePaint;
            if (var5 != null || var7 != null && var6 != null) {
               boolean var8 = drawGlyphVectorWorks;
               if (var8 && var6 != null && var7 != null) {
                  var8 = false;
               }

               if (var8 && var5 != null && !(var5 instanceof Color)) {
                  var8 = false;
               }

               if (var8) {
                  Object var9 = var1.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
                  Object var10 = var1.getRenderingHint(RenderingHints.KEY_STROKE_CONTROL);
                  if (var9 == RenderingHints.VALUE_TEXT_ANTIALIAS_ON && var10 == RenderingHints.VALUE_STROKE_PURE) {
                     var8 = false;
                  }
               }

               if (var8) {
                  AffineTransform var11 = var1.getTransform();
                  int var12 = var11.getType();
                  if ((var12 & 32) != 0 || (var12 & 16) != 0) {
                     var8 = false;
                  }
               }

               if (var8) {
                  for(int var21 = 0; var21 < var3; ++var21) {
                     if (!this.glyphVisible[var21]) {
                        var8 = false;
                        break;
                     }

                     AffineTransform var24 = this.glyphTransforms[var21];
                     if (var24 != null) {
                        int var13 = var24.getType();
                        if ((var13 & -2) != 0 && (!glyphVectorTransformWorks || (var13 & 32) != 0 || (var13 & 16) != 0)) {
                           var8 = false;
                           break;
                        }
                     }
                  }
               }

               if (var8) {
                  double var22 = (double)this.scaleFactor;
                  double[] var25 = new double[6];

                  int var14;
                  for(var14 = 0; var14 < var3; ++var14) {
                     Point2D.Float var15 = this.glyphPositions[var14];
                     double var16 = var15.getX();
                     double var18 = var15.getY();
                     AffineTransform var20 = this.glyphTransforms[var14];
                     if (var20 != null) {
                        var20.getMatrix(var25);
                        var16 += var25[4];
                        var18 += var25[5];
                        if (var25[0] == 1.0 && var25[1] == 0.0 && var25[2] == 0.0 && var25[3] == 1.0) {
                           var20 = null;
                        } else {
                           var25[4] = 0.0;
                           var25[5] = 0.0;
                           var20 = new AffineTransform(var25);
                        }
                     }

                     Point2D.Double var26 = new Point2D.Double(var16 / var22, var18 / var22);
                     this.awtGlyphVector.setGlyphPosition(var14, var26);
                     this.awtGlyphVector.setGlyphTransform(var14, var20);
                  }

                  var1.scale(var22, var22);
                  var1.setPaint(var5);
                  var1.drawGlyphVector(this.awtGlyphVector, 0.0F, 0.0F);
                  var1.scale(1.0 / var22, 1.0 / var22);

                  for(var14 = 0; var14 < var3; ++var14) {
                     Point2D var27 = this.defaultGlyphPositions[var14];
                     this.awtGlyphVector.setGlyphPosition(var14, var27);
                     this.awtGlyphVector.setGlyphTransform(var14, (AffineTransform)null);
                  }
               } else {
                  Shape var23 = this.getOutline();
                  if (var5 != null) {
                     var1.setPaint(var5);
                     var1.fill(var23);
                  }

                  if (var6 != null && var7 != null) {
                     var1.setStroke(var6);
                     var1.setPaint(var7);
                     var1.draw(var23);
                  }
               }

            }
         }
      }
   }

   static {
      PAINT_INFO = GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO;
      String var0 = System.getProperty("java.specification.version");
      if ("1.4".compareTo(var0) <= 0) {
         outlinesPositioned = true;
         drawGlyphVectorWorks = true;
         glyphVectorTransformWorks = true;
      } else if ("Mac OS X".equals(System.getProperty("os.name"))) {
         outlinesPositioned = true;
         drawGlyphVectorWorks = false;
         glyphVectorTransformWorks = false;
      } else {
         outlinesPositioned = false;
         drawGlyphVectorWorks = true;
         glyphVectorTransformWorks = false;
      }

   }
}
