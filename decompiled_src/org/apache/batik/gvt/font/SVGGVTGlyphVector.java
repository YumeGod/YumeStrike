package org.apache.batik.gvt.font;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import org.apache.batik.gvt.text.ArabicTextHandler;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.TextPaintInfo;

public final class SVGGVTGlyphVector implements GVTGlyphVector {
   public static final AttributedCharacterIterator.Attribute PAINT_INFO;
   private GVTFont font;
   private Glyph[] glyphs;
   private FontRenderContext frc;
   private GeneralPath outline;
   private Rectangle2D logicalBounds;
   private Rectangle2D bounds2D;
   private Shape[] glyphLogicalBounds;
   private boolean[] glyphVisible;
   private Point2D endPos;
   private TextPaintInfo cacheTPI;

   public SVGGVTGlyphVector(GVTFont var1, Glyph[] var2, FontRenderContext var3) {
      this.font = var1;
      this.glyphs = var2;
      this.frc = var3;
      this.outline = null;
      this.bounds2D = null;
      this.logicalBounds = null;
      this.glyphLogicalBounds = new Shape[var2.length];
      this.glyphVisible = new boolean[var2.length];

      for(int var4 = 0; var4 < var2.length; ++var4) {
         this.glyphVisible[var4] = true;
      }

      this.endPos = var2[var2.length - 1].getPosition();
      this.endPos = new Point2D.Float((float)(this.endPos.getX() + (double)var2[var2.length - 1].getHorizAdvX()), (float)this.endPos.getY());
   }

   public GVTFont getFont() {
      return this.font;
   }

   public FontRenderContext getFontRenderContext() {
      return this.frc;
   }

   public int getGlyphCode(int var1) throws IndexOutOfBoundsException {
      if (var1 >= 0 && var1 <= this.glyphs.length - 1) {
         return this.glyphs[var1].getGlyphCode();
      } else {
         throw new IndexOutOfBoundsException("glyphIndex " + var1 + " is out of bounds, should be between 0 and " + (this.glyphs.length - 1));
      }
   }

   public int[] getGlyphCodes(int var1, int var2, int[] var3) throws IndexOutOfBoundsException, IllegalArgumentException {
      if (var2 < 0) {
         throw new IllegalArgumentException("numEntries argument value, " + var2 + ", is illegal. It must be > 0.");
      } else if (var1 < 0) {
         throw new IndexOutOfBoundsException("beginGlyphIndex " + var1 + " is out of bounds, should be between 0 and " + (this.glyphs.length - 1));
      } else if (var1 + var2 > this.glyphs.length) {
         throw new IndexOutOfBoundsException("beginGlyphIndex + numEntries (" + var1 + "+" + var2 + ") exceeds the number of glpyhs in this GlyphVector");
      } else {
         if (var3 == null) {
            var3 = new int[var2];
         }

         for(int var4 = var1; var4 < var1 + var2; ++var4) {
            var3[var4 - var1] = this.glyphs[var4].getGlyphCode();
         }

         return var3;
      }
   }

   public GlyphJustificationInfo getGlyphJustificationInfo(int var1) {
      if (var1 >= 0 && var1 <= this.glyphs.length - 1) {
         return null;
      } else {
         throw new IndexOutOfBoundsException("glyphIndex: " + var1 + ", is out of bounds. Should be between 0 and " + (this.glyphs.length - 1) + ".");
      }
   }

   public Shape getGlyphLogicalBounds(int var1) {
      if (this.glyphLogicalBounds[var1] == null && this.glyphVisible[var1]) {
         this.computeGlyphLogicalBounds();
      }

      return this.glyphLogicalBounds[var1];
   }

   private void computeGlyphLogicalBounds() {
      float var1 = 0.0F;
      float var2 = 0.0F;
      if (this.font != null) {
         GVTLineMetrics var3 = this.font.getLineMetrics("By", this.frc);
         var1 = var3.getAscent();
         var2 = var3.getDescent();
         if (var2 < 0.0F) {
            var2 = -var2;
         }
      }

      if (var1 == 0.0F) {
         float var30 = 0.0F;
         float var4 = 0.0F;

         for(int var5 = 0; var5 < this.getNumGlyphs(); ++var5) {
            if (this.glyphVisible[var5]) {
               GVTGlyphMetrics var6 = this.getGlyphMetrics(var5);
               Rectangle2D var7 = var6.getBounds2D();
               var1 = (float)(-var7.getMinY());
               var2 = (float)(var7.getHeight() - (double)var1);
               if (var1 > var30) {
                  var30 = var1;
               }

               if (var2 > var4) {
                  var4 = var2;
               }
            }
         }

         var1 = var30;
         var2 = var4;
      }

      Shape[] var31 = new Shape[this.getNumGlyphs()];
      boolean[] var32 = new boolean[this.getNumGlyphs()];
      double var33 = -1.0;
      double var34 = -1.0;

      for(int var9 = 0; var9 < this.getNumGlyphs(); ++var9) {
         if (!this.glyphVisible[var9]) {
            var31[var9] = null;
         } else {
            AffineTransform var10 = this.getGlyphTransform(var9);
            GVTGlyphMetrics var11 = this.getGlyphMetrics(var9);
            Rectangle2D.Double var12 = new Rectangle2D.Double(0.0, (double)(-var1), (double)var11.getHorizontalAdvance(), (double)(var1 + var2));
            if (var12.isEmpty()) {
               if (var9 > 0) {
                  var32[var9] = var32[var9 - 1];
               } else {
                  var32[var9] = true;
               }
            } else {
               Point2D.Double var13 = new Point2D.Double(var12.getMinX(), var12.getMinY());
               Point2D.Double var14 = new Point2D.Double(var12.getMaxX(), var12.getMinY());
               Point2D.Double var15 = new Point2D.Double(var12.getMinX(), var12.getMaxY());
               Point2D var16 = this.getGlyphPosition(var9);
               AffineTransform var17 = AffineTransform.getTranslateInstance(var16.getX(), var16.getY());
               if (var10 != null) {
                  var17.concatenate(var10);
               }

               var31[var9] = var17.createTransformedShape(var12);
               Point2D.Double var18 = new Point2D.Double();
               Point2D.Double var19 = new Point2D.Double();
               Point2D.Double var20 = new Point2D.Double();
               var17.transform(var13, var18);
               var17.transform(var14, var19);
               var17.transform(var15, var20);
               double var21 = var18.getX() - var19.getX();
               double var23 = var18.getX() - var20.getX();
               double var25 = var18.getY() - var19.getY();
               double var27 = var18.getY() - var20.getY();
               if (Math.abs(var21) < 0.001 && Math.abs(var27) < 0.001) {
                  var32[var9] = false;
               } else if (Math.abs(var23) < 0.001 && Math.abs(var25) < 0.001) {
                  var32[var9] = false;
               } else {
                  var32[var9] = true;
               }

               Rectangle2D var29 = var31[var9].getBounds2D();
               if (var29.getWidth() > var33) {
                  var33 = var29.getWidth();
               }

               if (var29.getHeight() > var34) {
                  var34 = var29.getHeight();
               }
            }
         }
      }

      GeneralPath var35 = new GeneralPath();

      for(int var36 = 0; var36 < this.getNumGlyphs(); ++var36) {
         if (var31[var36] != null) {
            var35.append(var31[var36], false);
         }
      }

      Rectangle2D var37 = var35.getBounds2D();
      int var38;
      Rectangle2D var39;
      double var40;
      double var41;
      Rectangle2D var42;
      double var43;
      double var44;
      if (var37.getHeight() < var34 * 1.5) {
         for(var38 = 0; var38 < this.getNumGlyphs(); ++var38) {
            if (!var32[var38] && var31[var38] != null) {
               var39 = var31[var38].getBounds2D();
               var40 = var39.getMinX();
               var41 = var39.getWidth();
               if (var38 < this.getNumGlyphs() - 1 && var31[var38 + 1] != null) {
                  var42 = var31[var38 + 1].getBounds2D();
                  if (var42.getX() > var40) {
                     var43 = var42.getX() - var40;
                     if (var43 < var41 * 1.15 && var43 > var41 * 0.85) {
                        var44 = (var43 - var41) * 0.5;
                        var41 += var44;
                        var42.setRect(var42.getX() - var44, var42.getY(), var42.getWidth() + var44, var42.getHeight());
                     }
                  }
               }

               var31[var38] = new Rectangle2D.Double(var40, var37.getMinY(), var41, var37.getHeight());
            }
         }
      } else if (var37.getWidth() < var33 * 1.5) {
         for(var38 = 0; var38 < this.getNumGlyphs(); ++var38) {
            if (!var32[var38] && var31[var38] != null) {
               var39 = var31[var38].getBounds2D();
               var40 = var39.getMinY();
               var41 = var39.getHeight();
               if (var38 < this.getNumGlyphs() - 1 && var31[var38 + 1] != null) {
                  var42 = var31[var38 + 1].getBounds2D();
                  if (var42.getY() > var40) {
                     var43 = var42.getY() - var40;
                     if (var43 < var41 * 1.15 && var43 > var41 * 0.85) {
                        var44 = (var43 - var41) * 0.5;
                        var41 += var44;
                        var42.setRect(var42.getX(), var42.getY() - var44, var42.getWidth(), var42.getHeight() + var44);
                     }
                  }
               }

               var31[var38] = new Rectangle2D.Double(var37.getMinX(), var40, var37.getWidth(), var41);
            }
         }
      }

      System.arraycopy(var31, 0, this.glyphLogicalBounds, 0, this.getNumGlyphs());
   }

   public GVTGlyphMetrics getGlyphMetrics(int var1) {
      if (var1 >= 0 && var1 <= this.glyphs.length - 1) {
         if (var1 < this.glyphs.length - 1 && this.font != null) {
            float var2 = this.font.getHKern(this.glyphs[var1].getGlyphCode(), this.glyphs[var1 + 1].getGlyphCode());
            float var3 = this.font.getVKern(this.glyphs[var1].getGlyphCode(), this.glyphs[var1 + 1].getGlyphCode());
            return this.glyphs[var1].getGlyphMetrics(var2, var3);
         } else {
            return this.glyphs[var1].getGlyphMetrics();
         }
      } else {
         throw new IndexOutOfBoundsException("idx: " + var1 + ", is out of bounds. Should be between 0 and " + (this.glyphs.length - 1) + '.');
      }
   }

   public Shape getGlyphOutline(int var1) {
      if (var1 >= 0 && var1 <= this.glyphs.length - 1) {
         return this.glyphs[var1].getOutline();
      } else {
         throw new IndexOutOfBoundsException("glyphIndex: " + var1 + ", is out of bounds. Should be between 0 and " + (this.glyphs.length - 1) + ".");
      }
   }

   public Rectangle2D getGlyphCellBounds(int var1) {
      return this.getGlyphLogicalBounds(var1).getBounds2D();
   }

   public Point2D getGlyphPosition(int var1) {
      if (var1 == this.glyphs.length) {
         return this.endPos;
      } else if (var1 >= 0 && var1 <= this.glyphs.length - 1) {
         return this.glyphs[var1].getPosition();
      } else {
         throw new IndexOutOfBoundsException("glyphIndex: " + var1 + ", is out of bounds. Should be between 0 and " + (this.glyphs.length - 1) + '.');
      }
   }

   public float[] getGlyphPositions(int var1, int var2, float[] var3) {
      if (var2 < 0) {
         throw new IllegalArgumentException("numEntries argument value, " + var2 + ", is illegal. It must be > 0.");
      } else if (var1 < 0) {
         throw new IndexOutOfBoundsException("beginGlyphIndex " + var1 + " is out of bounds, should be between 0 and " + (this.glyphs.length - 1));
      } else if (var1 + var2 > this.glyphs.length + 1) {
         throw new IndexOutOfBoundsException("beginGlyphIndex + numEntries (" + var1 + '+' + var2 + ") exceeds the number of glpyhs in this GlyphVector");
      } else {
         if (var3 == null) {
            var3 = new float[var2 * 2];
         }

         if (var1 + var2 == this.glyphs.length + 1) {
            --var2;
            var3[var2 * 2] = (float)this.endPos.getX();
            var3[var2 * 2 + 1] = (float)this.endPos.getY();
         }

         for(int var4 = var1; var4 < var1 + var2; ++var4) {
            Point2D var5 = this.glyphs[var4].getPosition();
            var3[(var4 - var1) * 2] = (float)var5.getX();
            var3[(var4 - var1) * 2 + 1] = (float)var5.getY();
         }

         return var3;
      }
   }

   public AffineTransform getGlyphTransform(int var1) {
      if (var1 >= 0 && var1 <= this.glyphs.length - 1) {
         return this.glyphs[var1].getTransform();
      } else {
         throw new IndexOutOfBoundsException("glyphIndex: " + var1 + ", is out of bounds. Should be between 0 and " + (this.glyphs.length - 1) + '.');
      }
   }

   public Shape getGlyphVisualBounds(int var1) {
      if (var1 >= 0 && var1 <= this.glyphs.length - 1) {
         return this.glyphs[var1].getOutline();
      } else {
         throw new IndexOutOfBoundsException("glyphIndex: " + var1 + ", is out of bounds. Should be between 0 and " + (this.glyphs.length - 1) + '.');
      }
   }

   public Rectangle2D getBounds2D(AttributedCharacterIterator var1) {
      var1.first();
      TextPaintInfo var2 = (TextPaintInfo)var1.getAttribute(PAINT_INFO);
      if (this.bounds2D != null && TextPaintInfo.equivilent(var2, this.cacheTPI)) {
         return this.bounds2D;
      } else {
         Rectangle2D var3 = null;
         if (var2.visible) {
            for(int var4 = 0; var4 < this.getNumGlyphs(); ++var4) {
               if (this.glyphVisible[var4]) {
                  Rectangle2D var5 = this.glyphs[var4].getBounds2D();
                  if (var5 != null) {
                     if (var3 == null) {
                        var3 = var5;
                     } else {
                        var3.add(var5);
                     }
                  }
               }
            }
         }

         this.bounds2D = var3;
         if (this.bounds2D == null) {
            this.bounds2D = new Rectangle2D.Float();
         }

         this.cacheTPI = new TextPaintInfo(var2);
         return this.bounds2D;
      }
   }

   public Rectangle2D getLogicalBounds() {
      if (this.logicalBounds == null) {
         GeneralPath var1 = new GeneralPath();

         for(int var2 = 0; var2 < this.getNumGlyphs(); ++var2) {
            Shape var3 = this.getGlyphLogicalBounds(var2);
            if (var3 != null) {
               var1.append(var3, false);
            }
         }

         this.logicalBounds = var1.getBounds2D();
      }

      return this.logicalBounds;
   }

   public int getNumGlyphs() {
      return this.glyphs != null ? this.glyphs.length : 0;
   }

   public Shape getOutline() {
      if (this.outline == null) {
         this.outline = new GeneralPath();

         for(int var1 = 0; var1 < this.glyphs.length; ++var1) {
            if (this.glyphVisible[var1]) {
               Shape var2 = this.glyphs[var1].getOutline();
               if (var2 != null) {
                  this.outline.append(var2, false);
               }
            }
         }
      }

      return this.outline;
   }

   public Shape getOutline(float var1, float var2) {
      Shape var3 = this.getOutline();
      AffineTransform var4 = AffineTransform.getTranslateInstance((double)var1, (double)var2);
      Shape var5 = var4.createTransformedShape(var3);
      return var5;
   }

   public Rectangle2D getGeometricBounds() {
      return this.getOutline().getBounds2D();
   }

   public void performDefaultLayout() {
      this.logicalBounds = null;
      this.outline = null;
      this.bounds2D = null;
      float var1 = 0.0F;
      float var2 = 0.0F;

      for(int var3 = 0; var3 < this.glyphs.length; ++var3) {
         Glyph var4 = this.glyphs[var3];
         var4.setTransform((AffineTransform)null);
         this.glyphLogicalBounds[var3] = null;
         String var5 = var4.getUnicode();
         if (var5 != null && var5.length() != 0 && ArabicTextHandler.arabicCharTransparent(var5.charAt(0))) {
            int var6;
            for(var6 = var3 + 1; var6 < this.glyphs.length; ++var6) {
               var5 = this.glyphs[var6].getUnicode();
               if (var5 == null || var5.length() == 0) {
                  break;
               }

               char var7 = var5.charAt(0);
               if (!ArabicTextHandler.arabicCharTransparent(var7)) {
                  break;
               }
            }

            if (var6 != this.glyphs.length) {
               Glyph var10 = this.glyphs[var6];
               float var8 = var1 + var10.getHorizAdvX();

               for(int var9 = var3; var9 < var6; ++var9) {
                  var4 = this.glyphs[var9];
                  var4.setTransform((AffineTransform)null);
                  this.glyphLogicalBounds[var3] = null;
                  var4.setPosition(new Point2D.Float(var8 - var4.getHorizAdvX(), var2));
               }

               var3 = var6;
               var4 = var10;
            }
         }

         var4.setPosition(new Point2D.Float(var1, var2));
         var1 += var4.getHorizAdvX();
      }

      this.endPos = new Point2D.Float(var1, var2);
   }

   public void setGlyphPosition(int var1, Point2D var2) throws IndexOutOfBoundsException {
      if (var1 == this.glyphs.length) {
         this.endPos = (Point2D)var2.clone();
      } else if (var1 >= 0 && var1 <= this.glyphs.length - 1) {
         this.glyphs[var1].setPosition(var2);
         this.glyphLogicalBounds[var1] = null;
         this.outline = null;
         this.bounds2D = null;
         this.logicalBounds = null;
      } else {
         throw new IndexOutOfBoundsException("glyphIndex: " + var1 + ", is out of bounds. Should be between 0 and " + (this.glyphs.length - 1) + '.');
      }
   }

   public void setGlyphTransform(int var1, AffineTransform var2) {
      if (var1 >= 0 && var1 <= this.glyphs.length - 1) {
         this.glyphs[var1].setTransform(var2);
         this.glyphLogicalBounds[var1] = null;
         this.outline = null;
         this.bounds2D = null;
         this.logicalBounds = null;
      } else {
         throw new IndexOutOfBoundsException("glyphIndex: " + var1 + ", is out of bounds. Should be between 0 and " + (this.glyphs.length - 1) + '.');
      }
   }

   public void setGlyphVisible(int var1, boolean var2) {
      if (var2 != this.glyphVisible[var1]) {
         this.glyphVisible[var1] = var2;
         this.outline = null;
         this.bounds2D = null;
         this.logicalBounds = null;
         this.glyphLogicalBounds[var1] = null;
      }
   }

   public boolean isGlyphVisible(int var1) {
      return this.glyphVisible[var1];
   }

   public int getCharacterCount(int var1, int var2) {
      int var3 = 0;
      if (var1 < 0) {
         var1 = 0;
      }

      if (var2 > this.glyphs.length - 1) {
         var2 = this.glyphs.length - 1;
      }

      for(int var4 = var1; var4 <= var2; ++var4) {
         Glyph var5 = this.glyphs[var4];
         if (var5.getGlyphCode() == -1) {
            ++var3;
         } else {
            String var6 = var5.getUnicode();
            var3 += var6.length();
         }
      }

      return var3;
   }

   public void draw(Graphics2D var1, AttributedCharacterIterator var2) {
      var2.first();
      TextPaintInfo var3 = (TextPaintInfo)var2.getAttribute(PAINT_INFO);
      if (var3.visible) {
         for(int var4 = 0; var4 < this.glyphs.length; ++var4) {
            if (this.glyphVisible[var4]) {
               this.glyphs[var4].draw(var1);
            }
         }

      }
   }

   static {
      PAINT_INFO = GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO;
   }
}
