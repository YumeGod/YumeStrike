package org.apache.batik.gvt.text;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.Character.UnicodeBlock;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.HashSet;
import java.util.Set;
import org.apache.batik.gvt.font.AWTGVTFont;
import org.apache.batik.gvt.font.AltGlyphHandler;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTGlyphMetrics;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.font.GVTLineMetrics;

public class GlyphLayout implements TextSpanLayout {
   private GVTGlyphVector gv;
   private GVTFont font;
   private GVTLineMetrics metrics;
   private AttributedCharacterIterator aci;
   private Point2D advance;
   private Point2D offset;
   private float xScale = 1.0F;
   private float yScale = 1.0F;
   private TextPath textPath;
   private Point2D textPathAdvance;
   private int[] charMap;
   private boolean vertical;
   private boolean adjSpacing = true;
   private float[] glyphAdvances;
   private boolean isAltGlyph;
   private boolean layoutApplied = false;
   private boolean spacingApplied = false;
   private boolean pathApplied = false;
   public static final AttributedCharacterIterator.Attribute FLOW_LINE_BREAK;
   public static final AttributedCharacterIterator.Attribute FLOW_PARAGRAPH;
   public static final AttributedCharacterIterator.Attribute FLOW_EMPTY_PARAGRAPH;
   public static final AttributedCharacterIterator.Attribute LINE_HEIGHT;
   public static final AttributedCharacterIterator.Attribute VERTICAL_ORIENTATION;
   public static final AttributedCharacterIterator.Attribute VERTICAL_ORIENTATION_ANGLE;
   public static final AttributedCharacterIterator.Attribute HORIZONTAL_ORIENTATION_ANGLE;
   private static final AttributedCharacterIterator.Attribute X;
   private static final AttributedCharacterIterator.Attribute Y;
   private static final AttributedCharacterIterator.Attribute DX;
   private static final AttributedCharacterIterator.Attribute DY;
   private static final AttributedCharacterIterator.Attribute ROTATION;
   private static final AttributedCharacterIterator.Attribute BASELINE_SHIFT;
   private static final AttributedCharacterIterator.Attribute WRITING_MODE;
   private static final Integer WRITING_MODE_TTB;
   private static final Integer ORIENTATION_AUTO;
   public static final AttributedCharacterIterator.Attribute GVT_FONT;
   protected static Set runAtts;
   protected static Set szAtts;
   public static final double eps = 1.0E-5;

   public GlyphLayout(AttributedCharacterIterator var1, int[] var2, Point2D var3, FontRenderContext var4) {
      this.aci = var1;
      this.offset = var3;
      this.font = this.getFont();
      this.charMap = var2;
      this.metrics = this.font.getLineMetrics((CharacterIterator)var1, var1.getBeginIndex(), var1.getEndIndex(), var4);
      this.gv = null;
      this.aci.first();
      this.vertical = var1.getAttribute(WRITING_MODE) == WRITING_MODE_TTB;
      this.textPath = (TextPath)var1.getAttribute(GVTAttributedCharacterIterator.TextAttribute.TEXTPATH);
      AltGlyphHandler var5 = (AltGlyphHandler)this.aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.ALT_GLYPH_HANDLER);
      if (var5 != null) {
         this.gv = var5.createGlyphVector(var4, this.font.getSize(), this.aci);
         if (this.gv != null) {
            this.isAltGlyph = true;
         }
      }

      if (this.gv == null) {
         this.gv = this.font.createGlyphVector(var4, (CharacterIterator)this.aci);
      }

   }

   public GVTGlyphVector getGlyphVector() {
      return this.gv;
   }

   public Point2D getOffset() {
      return this.offset;
   }

   public void setScale(float var1, float var2, boolean var3) {
      if (this.vertical) {
         var1 = 1.0F;
      } else {
         var2 = 1.0F;
      }

      if (var1 != this.xScale || var2 != this.yScale || var3 != this.adjSpacing) {
         this.xScale = var1;
         this.yScale = var2;
         this.adjSpacing = var3;
         this.spacingApplied = false;
         this.glyphAdvances = null;
         this.pathApplied = false;
      }

   }

   public void setOffset(Point2D var1) {
      if (var1.getX() != this.offset.getX() || var1.getY() != this.offset.getY()) {
         if (this.layoutApplied || this.spacingApplied) {
            float var2 = (float)(var1.getX() - this.offset.getX());
            float var3 = (float)(var1.getY() - this.offset.getY());
            int var4 = this.gv.getNumGlyphs();
            float[] var5 = this.gv.getGlyphPositions(0, var4 + 1, (float[])null);
            Point2D.Float var6 = new Point2D.Float();

            for(int var7 = 0; var7 <= var4; ++var7) {
               var6.x = var5[2 * var7] + var2;
               var6.y = var5[2 * var7 + 1] + var3;
               this.gv.setGlyphPosition(var7, var6);
            }
         }

         this.offset = var1;
         this.pathApplied = false;
      }

   }

   public GVTGlyphMetrics getGlyphMetrics(int var1) {
      return this.gv.getGlyphMetrics(var1);
   }

   public GVTLineMetrics getLineMetrics() {
      return this.metrics;
   }

   public boolean isVertical() {
      return this.vertical;
   }

   public boolean isOnATextPath() {
      return this.textPath != null;
   }

   public int getGlyphCount() {
      return this.gv.getNumGlyphs();
   }

   public int getCharacterCount(int var1, int var2) {
      return this.gv.getCharacterCount(var1, var2);
   }

   public boolean isLeftToRight() {
      this.aci.first();
      int var1 = (Integer)this.aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.BIDI_LEVEL);
      return (var1 & 1) == 0;
   }

   private final void syncLayout() {
      if (!this.pathApplied) {
         this.doPathLayout();
      }

   }

   public void draw(Graphics2D var1) {
      this.syncLayout();
      this.gv.draw(var1, this.aci);
   }

   public Point2D getAdvance2D() {
      this.adjustTextSpacing();
      return this.advance;
   }

   public Shape getOutline() {
      this.syncLayout();
      return this.gv.getOutline();
   }

   public float[] getGlyphAdvances() {
      if (this.glyphAdvances != null) {
         return this.glyphAdvances;
      } else {
         if (!this.spacingApplied) {
            this.adjustTextSpacing();
         }

         int var1 = this.gv.getNumGlyphs();
         float[] var2 = this.gv.getGlyphPositions(0, var1 + 1, (float[])null);
         this.glyphAdvances = new float[var1 + 1];
         byte var3 = 0;
         if (this.isVertical()) {
            var3 = 1;
         }

         float var4 = var2[var3];

         for(int var5 = 0; var5 < var1 + 1; ++var5) {
            this.glyphAdvances[var5] = var2[2 * var5 + var3] - var4;
         }

         return this.glyphAdvances;
      }
   }

   public Shape getDecorationOutline(int var1) {
      this.syncLayout();
      GeneralPath var2 = new GeneralPath();
      if ((var1 & 1) != 0) {
         ((GeneralPath)var2).append(this.getUnderlineShape(), false);
      }

      if ((var1 & 2) != 0) {
         ((GeneralPath)var2).append(this.getStrikethroughShape(), false);
      }

      if ((var1 & 4) != 0) {
         ((GeneralPath)var2).append(this.getOverlineShape(), false);
      }

      return var2;
   }

   public Rectangle2D getBounds2D() {
      this.syncLayout();
      return this.gv.getBounds2D(this.aci);
   }

   public Rectangle2D getGeometricBounds() {
      this.syncLayout();
      Rectangle2D var1 = this.gv.getGeometricBounds();
      Rectangle2D var2 = this.getDecorationOutline(7).getBounds2D();
      return var1.createUnion(var2);
   }

   public Point2D getTextPathAdvance() {
      this.syncLayout();
      return this.textPath != null ? this.textPathAdvance : this.getAdvance2D();
   }

   public int getGlyphIndex(int var1) {
      int var2 = this.getGlyphCount();
      int var3 = 0;

      for(int var4 = 0; var4 < var2; ++var4) {
         int var5 = this.getCharacterCount(var4, var4);

         for(int var6 = 0; var6 < var5; ++var6) {
            int var7 = this.charMap[var3++];
            if (var1 == var7) {
               return var4;
            }

            if (var3 >= this.charMap.length) {
               return -1;
            }
         }
      }

      return -1;
   }

   public int getLastGlyphIndex(int var1) {
      int var2 = this.getGlyphCount();
      int var3 = this.charMap.length - 1;

      for(int var4 = var2 - 1; var4 >= 0; --var4) {
         int var5 = this.getCharacterCount(var4, var4);

         for(int var6 = 0; var6 < var5; ++var6) {
            int var7 = this.charMap[var3--];
            if (var1 == var7) {
               return var4;
            }

            if (var3 < 0) {
               return -1;
            }
         }
      }

      return -1;
   }

   public double getComputedOrientationAngle(int var1) {
      if (this.isGlyphOrientationAuto()) {
         if (this.isVertical()) {
            char var2 = this.aci.setIndex(var1);
            return this.isLatinChar(var2) ? 90.0 : 0.0;
         } else {
            return 0.0;
         }
      } else {
         return (double)this.getGlyphOrientationAngle();
      }
   }

   public Shape getHighlightShape(int var1, int var2) {
      this.syncLayout();
      if (var1 > var2) {
         int var3 = var1;
         var1 = var2;
         var2 = var3;
      }

      GeneralPath var18 = null;
      int var4 = this.getGlyphCount();
      Point2D.Float[] var5 = new Point2D.Float[2 * var4];
      Point2D.Float[] var6 = new Point2D.Float[2 * var4];
      int var7 = 0;
      int var8 = 0;

      for(int var9 = 0; var9 < var4; ++var9) {
         int var10 = this.charMap[var8];
         if (var10 >= var1 && var10 <= var2 && this.gv.isGlyphVisible(var9)) {
            Shape var11 = this.gv.getGlyphLogicalBounds(var9);
            if (var11 != null) {
               if (var18 == null) {
                  var18 = new GeneralPath();
               }

               float[] var12 = new float[6];
               int var13 = 0;
               boolean var14 = true;
               PathIterator var15 = var11.getPathIterator((AffineTransform)null);
               Point2D.Float var16 = null;

               while(!var15.isDone()) {
                  int var19 = var15.currentSegment(var12);
                  if (var19 != 0 && var19 != 1) {
                     if (var19 != 4 || var13 < 4 || var13 > 5) {
                        break;
                     }
                  } else {
                     if (var13 > 4) {
                        break;
                     }

                     if (var13 == 4) {
                        if (var16 == null || var16.x != var12[0] || var16.y != var12[1]) {
                           break;
                        }
                     } else {
                        Point2D.Float var17 = new Point2D.Float(var12[0], var12[1]);
                        if (var13 == 0) {
                           var16 = var17;
                        }

                        switch (var13) {
                           case 0:
                              var6[var7] = var17;
                              break;
                           case 1:
                              var5[var7] = var17;
                              break;
                           case 2:
                              var5[var7 + 1] = var17;
                              break;
                           case 3:
                              var6[var7 + 1] = var17;
                        }
                     }
                  }

                  ++var13;
                  var15.next();
               }

               if (var15.isDone()) {
                  if (var6[var7] != null && (var5[var7].x != var5[var7 + 1].x || var5[var7].y != var5[var7 + 1].y)) {
                     var7 += 2;
                  }
               } else {
                  addPtsToPath(var18, var5, var6, var7);
                  var7 = 0;
                  var18.append(var11, false);
               }
            }
         }

         var8 += this.getCharacterCount(var9, var9);
         if (var8 >= this.charMap.length) {
            var8 = this.charMap.length - 1;
         }
      }

      addPtsToPath(var18, var5, var6, var7);
      return var18;
   }

   public static boolean epsEQ(double var0, double var2) {
      return var0 + 1.0E-5 > var2 && var0 - 1.0E-5 < var2;
   }

   public static int makeConvexHull(Point2D.Float[] var0, int var1) {
      for(int var3 = 1; var3 < var1; ++var3) {
         if (var0[var3].x < var0[var3 - 1].x || var0[var3].x == var0[var3 - 1].x && var0[var3].y < var0[var3 - 1].y) {
            Point2D.Float var2 = var0[var3];
            var0[var3] = var0[var3 - 1];
            var0[var3 - 1] = var2;
            var3 = 0;
         }
      }

      Point2D.Float var17 = var0[0];
      Point2D.Float var4 = var0[var1 - 1];
      Point2D.Float var5 = new Point2D.Float(var4.x - var17.x, var4.y - var17.y);
      float var7 = var5.y * var17.x - var5.x * var17.y;
      Point2D.Float[] var8 = new Point2D.Float[var1];
      Point2D.Float[] var9 = new Point2D.Float[var1];
      var9[0] = var8[0] = var0[0];
      int var10 = 1;
      int var11 = 1;

      float var6;
      float var14;
      float var15;
      for(int var12 = 1; var12 < var1 - 1; ++var12) {
         Point2D.Float var13 = var0[var12];
         var6 = var5.x * var13.y - var5.y * var13.x + var7;
         float var16;
         if (var6 < 0.0F) {
            while(var11 >= 2) {
               var17 = var9[var11 - 2];
               var4 = var9[var11 - 1];
               var14 = var4.x - var17.x;
               var15 = var4.y - var17.y;
               var16 = var15 * var17.x - var14 * var17.y;
               var6 = var14 * var13.y - var15 * var13.x + var16;
               if ((double)var6 > 1.0E-5) {
                  break;
               }

               if ((double)var6 > -1.0E-5) {
                  if (var4.y < var13.y) {
                     var13 = var4;
                  }

                  --var11;
                  break;
               }

               --var11;
            }

            var9[var11++] = var13;
         } else {
            while(var10 >= 2) {
               var17 = var8[var10 - 2];
               var4 = var8[var10 - 1];
               var14 = var4.x - var17.x;
               var15 = var4.y - var17.y;
               var16 = var15 * var17.x - var14 * var17.y;
               var6 = var14 * var13.y - var15 * var13.x + var16;
               if ((double)var6 < -1.0E-5) {
                  break;
               }

               if ((double)var6 < 1.0E-5) {
                  if (var4.y > var13.y) {
                     var13 = var4;
                  }

                  --var10;
                  break;
               }

               --var10;
            }

            var8[var10++] = var13;
         }
      }

      Point2D.Float var18;
      float var19;
      for(var18 = var0[var1 - 1]; var11 >= 2; --var11) {
         var17 = var9[var11 - 2];
         var4 = var9[var11 - 1];
         var19 = var4.x - var17.x;
         var14 = var4.y - var17.y;
         var15 = var14 * var17.x - var19 * var17.y;
         var6 = var19 * var18.y - var14 * var18.x + var15;
         if ((double)var6 > 1.0E-5) {
            break;
         }

         if ((double)var6 > -1.0E-5) {
            if (var4.y >= var18.y) {
               --var11;
            }
            break;
         }
      }

      while(var10 >= 2) {
         var17 = var8[var10 - 2];
         var4 = var8[var10 - 1];
         var19 = var4.x - var17.x;
         var14 = var4.y - var17.y;
         var15 = var14 * var17.x - var19 * var17.y;
         var6 = var19 * var18.y - var14 * var18.x + var15;
         if ((double)var6 < -1.0E-5) {
            break;
         }

         if ((double)var6 < 1.0E-5) {
            if (var4.y <= var18.y) {
               --var10;
            }
            break;
         }

         --var10;
      }

      System.arraycopy(var8, 0, var0, 0, var10);
      int var20 = var10 + 1;
      var0[var10] = var0[var1 - 1];

      for(int var21 = var11 - 1; var21 > 0; ++var20) {
         var0[var20] = var9[var21];
         --var21;
      }

      return var20;
   }

   public static void addPtsToPath(GeneralPath var0, Point2D.Float[] var1, Point2D.Float[] var2, int var3) {
      if (var3 >= 2) {
         if (var3 == 2) {
            var0.moveTo(var1[0].x, var1[0].y);
            var0.lineTo(var1[1].x, var1[1].y);
            var0.lineTo(var2[1].x, var2[1].y);
            var0.lineTo(var2[0].x, var2[0].y);
            var0.lineTo(var1[0].x, var1[0].y);
         } else {
            Point2D.Float[] var4 = new Point2D.Float[8];
            Point2D.Float[] var5 = new Point2D.Float[8];
            var4[4] = var1[0];
            var4[5] = var1[1];
            var4[6] = var2[1];
            var4[7] = var2[0];
            Area[] var6 = new Area[var3 / 2];
            int var7 = 0;

            for(int var8 = 2; var8 < var3; var8 += 2) {
               var4[0] = var4[4];
               var4[1] = var4[5];
               var4[2] = var4[6];
               var4[3] = var4[7];
               var4[4] = var1[var8];
               var4[5] = var1[var8 + 1];
               var4[6] = var2[var8 + 1];
               var4[7] = var2[var8];
               float var9 = var4[2].x - var4[0].x;
               float var11 = var9 * var9;
               var9 = var4[2].y - var4[0].y;
               var11 += var9 * var9;
               float var10 = (float)Math.sqrt((double)var11);
               var9 = var4[6].x - var4[4].x;
               var11 = var9 * var9;
               var9 = var4[6].y - var4[4].y;
               var11 += var9 * var9;
               var10 += (float)Math.sqrt((double)var11);
               var9 = (var4[0].x + var4[1].x + var4[2].x + var4[3].x - (var4[4].x + var4[5].x + var4[6].x + var4[7].x)) / 4.0F;
               var11 = var9 * var9;
               var9 = (var4[0].y + var4[1].y + var4[2].y + var4[3].y - (var4[4].y + var4[5].y + var4[6].y + var4[7].y)) / 4.0F;
               var11 += var9 * var9;
               var11 = (float)Math.sqrt((double)var11);
               GeneralPath var12 = new GeneralPath();
               if (!(var11 < var10)) {
                  mergeAreas(var0, var6, var7);
                  var7 = 0;
                  if (var8 == 2) {
                     var12.moveTo(var4[0].x, var4[0].y);
                     var12.lineTo(var4[1].x, var4[1].y);
                     var12.lineTo(var4[2].x, var4[2].y);
                     var12.lineTo(var4[3].x, var4[3].y);
                     var12.closePath();
                     var0.append(var12, false);
                     var12.reset();
                  }

                  var12.moveTo(var4[4].x, var4[4].y);
                  var12.lineTo(var4[5].x, var4[5].y);
                  var12.lineTo(var4[6].x, var4[6].y);
                  var12.lineTo(var4[7].x, var4[7].y);
                  var12.closePath();
               } else {
                  System.arraycopy(var4, 0, var5, 0, 8);
                  int var13 = makeConvexHull(var5, 8);
                  var12.moveTo(var5[0].x, var5[0].y);

                  for(int var14 = 1; var14 < var13; ++var14) {
                     var12.lineTo(var5[var14].x, var5[var14].y);
                  }

                  var12.closePath();
               }

               var6[var7++] = new Area(var12);
            }

            mergeAreas(var0, var6, var7);
         }
      }
   }

   public static void mergeAreas(GeneralPath var0, Area[] var1, int var2) {
      for(; var2 > 1; var2 /= 2) {
         int var3 = 0;

         for(int var4 = 1; var4 < var2; var4 += 2) {
            var1[var4 - 1].add(var1[var4]);
            var1[var3++] = var1[var4 - 1];
            var1[var4] = null;
         }

         if ((var2 & 1) == 1) {
            var1[var3 - 1].add(var1[var2 - 1]);
         }
      }

      if (var2 == 1) {
         var0.append(var1[0], false);
      }

   }

   public TextHit hitTestChar(float var1, float var2) {
      this.syncLayout();
      TextHit var3 = null;
      int var4 = 0;

      for(int var5 = 0; var5 < this.gv.getNumGlyphs(); ++var5) {
         Shape var6 = this.gv.getGlyphLogicalBounds(var5);
         if (var6 != null) {
            Rectangle2D var7 = var6.getBounds2D();
            if (var6.contains((double)var1, (double)var2)) {
               boolean var8 = (double)var1 > var7.getX() + var7.getWidth() / 2.0;
               boolean var9 = !var8;
               int var10 = this.charMap[var4];
               var3 = new TextHit(var10, var9);
               return var3;
            }
         }

         var4 += this.getCharacterCount(var5, var5);
         if (var4 >= this.charMap.length) {
            var4 = this.charMap.length - 1;
         }
      }

      return var3;
   }

   protected GVTFont getFont() {
      this.aci.first();
      GVTFont var1 = (GVTFont)this.aci.getAttribute(GVT_FONT);
      return (GVTFont)(var1 != null ? var1 : new AWTGVTFont(this.aci.getAttributes()));
   }

   protected Shape getOverlineShape() {
      double var1 = (double)this.metrics.getOverlineOffset();
      float var3 = this.metrics.getOverlineThickness();
      var1 += (double)var3;
      this.aci.first();
      Float var4 = (Float)this.aci.getAttribute(DY);
      if (var4 != null) {
         var1 += (double)var4;
      }

      BasicStroke var5 = new BasicStroke(var3);
      Rectangle2D var6 = this.gv.getLogicalBounds();
      return var5.createStrokedShape(new Line2D.Double(var6.getMinX() + (double)var3 / 2.0, this.offset.getY() + var1, var6.getMaxX() - (double)var3 / 2.0, this.offset.getY() + var1));
   }

   protected Shape getUnderlineShape() {
      double var1 = (double)this.metrics.getUnderlineOffset();
      float var3 = this.metrics.getUnderlineThickness();
      var1 += (double)var3 * 1.5;
      BasicStroke var4 = new BasicStroke(var3);
      this.aci.first();
      Float var5 = (Float)this.aci.getAttribute(DY);
      if (var5 != null) {
         var1 += (double)var5;
      }

      Rectangle2D var6 = this.gv.getLogicalBounds();
      return var4.createStrokedShape(new Line2D.Double(var6.getMinX() + (double)var3 / 2.0, this.offset.getY() + var1, var6.getMaxX() - (double)var3 / 2.0, this.offset.getY() + var1));
   }

   protected Shape getStrikethroughShape() {
      double var1 = (double)this.metrics.getStrikethroughOffset();
      float var3 = this.metrics.getStrikethroughThickness();
      BasicStroke var4 = new BasicStroke(var3);
      this.aci.first();
      Float var5 = (Float)this.aci.getAttribute(DY);
      if (var5 != null) {
         var1 += (double)var5;
      }

      Rectangle2D var6 = this.gv.getLogicalBounds();
      return var4.createStrokedShape(new Line2D.Double(var6.getMinX() + (double)var3 / 2.0, this.offset.getY() + var1, var6.getMaxX() - (double)var3 / 2.0, this.offset.getY() + var1));
   }

   protected void doExplicitGlyphLayout() {
      this.gv.performDefaultLayout();
      float var1 = this.vertical ? (float)this.gv.getLogicalBounds().getWidth() : this.metrics.getAscent() + Math.abs(this.metrics.getDescent());
      int var2 = this.gv.getNumGlyphs();
      float[] var3 = this.gv.getGlyphPositions(0, var2 + 1, (float[])null);
      float var4 = 0.0F;
      float var5 = 0.0F;
      boolean var6 = this.isGlyphOrientationAuto();
      int var7 = 0;
      if (!var6) {
         var7 = this.getGlyphOrientationAngle();
      }

      int var8 = 0;
      int var9 = this.aci.getBeginIndex();
      int var10 = 0;
      char var11 = this.aci.first();
      int var12 = var10 + var9;
      Float var13 = null;
      Float var14 = null;
      Float var15 = null;
      Float var16 = null;
      Float var17 = null;
      Object var18 = null;
      float var19 = 0.0F;
      float var20 = 0.0F;
      float var21 = (float)this.offset.getX();
      float var22 = (float)this.offset.getY();
      Point2D.Float var23 = new Point2D.Float();

      boolean var24;
      float var30;
      for(var24 = false; var8 < var2; ++var8) {
         if (var10 + var9 >= var12) {
            var12 = this.aci.getRunLimit(runAtts);
            var13 = (Float)this.aci.getAttribute(X);
            var14 = (Float)this.aci.getAttribute(Y);
            var15 = (Float)this.aci.getAttribute(DX);
            var16 = (Float)this.aci.getAttribute(DY);
            var17 = (Float)this.aci.getAttribute(ROTATION);
            var18 = this.aci.getAttribute(BASELINE_SHIFT);
         }

         GVTGlyphMetrics var25 = this.gv.getGlyphMetrics(var8);
         float var26;
         float var27;
         float var28;
         if (var8 == 0) {
            if (this.isVertical()) {
               if (var6) {
                  if (this.isLatinChar(var11)) {
                     var4 = 0.0F;
                  } else {
                     var26 = var25.getVerticalAdvance();
                     var27 = this.metrics.getAscent();
                     var28 = this.metrics.getDescent();
                     var4 = var27 + (var26 - (var27 + var28)) / 2.0F;
                  }
               } else if (var7 == 0) {
                  var26 = var25.getVerticalAdvance();
                  var27 = this.metrics.getAscent();
                  var28 = this.metrics.getDescent();
                  var4 = var27 + (var26 - (var27 + var28)) / 2.0F;
               } else {
                  var4 = 0.0F;
               }
            } else if (var7 == 270) {
               var5 = (float)var25.getBounds2D().getHeight();
            } else {
               var5 = 0.0F;
            }
         } else if (var6 && var4 == 0.0F && !this.isLatinChar(var11)) {
            var26 = var25.getVerticalAdvance();
            var27 = this.metrics.getAscent();
            var28 = this.metrics.getDescent();
            var4 = var27 + (var26 - (var27 + var28)) / 2.0F;
         }

         var26 = 0.0F;
         var27 = 0.0F;
         var28 = 0.0F;
         float var29 = 0.0F;
         if (var11 != '\uffff') {
            if (this.vertical) {
               if (var6) {
                  if (this.isLatinChar(var11)) {
                     var28 = 1.5707964F;
                  } else {
                     var28 = 0.0F;
                  }
               } else {
                  var28 = (float)Math.toRadians((double)var7);
               }

               if (this.textPath != null) {
                  var13 = null;
               }
            } else {
               var28 = (float)Math.toRadians((double)var7);
               if (this.textPath != null) {
                  var14 = null;
               }
            }

            if (var17 != null && !var17.isNaN()) {
               var29 = var17 + var28;
            } else {
               var29 = var28;
            }

            if (var13 != null && !var13.isNaN()) {
               if (var8 == 0) {
                  var19 = (float)((double)var13 - this.offset.getX());
               }

               var21 = var13 - var19;
            }

            if (var15 != null && !var15.isNaN()) {
               var21 += var15;
            }

            if (var14 != null && !var14.isNaN()) {
               if (var8 == 0) {
                  var20 = (float)((double)var14 - this.offset.getY());
               }

               var22 = var14 - var20;
            }

            if (var16 != null && !var16.isNaN()) {
               var22 += var16;
            } else if (var8 > 0) {
               var22 += var3[var8 * 2 + 1] - var3[var8 * 2 - 1];
            }

            var30 = 0.0F;
            if (var18 != null) {
               if (var18 instanceof Integer) {
                  if (var18 == TextAttribute.SUPERSCRIPT_SUPER) {
                     var30 = var1 * 0.5F;
                  } else if (var18 == TextAttribute.SUPERSCRIPT_SUB) {
                     var30 = -var1 * 0.5F;
                  }
               } else if (var18 instanceof Float) {
                  var30 = (Float)var18;
               }

               if (this.vertical) {
                  var26 = var30;
               } else {
                  var27 = -var30;
               }
            }

            if (this.vertical) {
               var27 += var4;
               Rectangle2D var31;
               if (var6) {
                  if (this.isLatinChar(var11)) {
                     var26 += this.metrics.getStrikethroughOffset();
                  } else {
                     var31 = this.gv.getGlyphVisualBounds(var8).getBounds2D();
                     var26 -= (float)(var31.getMaxX() - (double)var3[2 * var8] - var31.getWidth() / 2.0);
                  }
               } else {
                  var31 = this.gv.getGlyphVisualBounds(var8).getBounds2D();
                  if (var7 == 0) {
                     var26 -= (float)(var31.getMaxX() - (double)var3[2 * var8] - var31.getWidth() / 2.0);
                  } else if (var7 == 180) {
                     var26 += (float)(var31.getMaxX() - (double)var3[2 * var8] - var31.getWidth() / 2.0);
                  } else if (var7 == 90) {
                     var26 += this.metrics.getStrikethroughOffset();
                  } else {
                     var26 -= this.metrics.getStrikethroughOffset();
                  }
               }
            } else {
               var26 += var5;
               if (var7 == 90) {
                  var27 -= var25.getHorizontalAdvance();
               } else if (var7 == 180) {
                  var27 -= this.metrics.getAscent();
               }
            }
         }

         var23.x = var21 + var26;
         var23.y = var22 + var27;
         this.gv.setGlyphPosition(var8, var23);
         if (ArabicTextHandler.arabicCharTransparent(var11)) {
            var24 = true;
         } else if (!this.vertical) {
            var30 = 0.0F;
            if (var7 == 0) {
               var30 = var25.getHorizontalAdvance();
            } else if (var7 == 180) {
               var30 = var25.getHorizontalAdvance();
               this.gv.setGlyphTransform(var8, AffineTransform.getTranslateInstance((double)var30, 0.0));
            } else {
               var30 = var25.getVerticalAdvance();
            }

            var21 += var30;
         } else {
            var30 = 0.0F;
            if (var6) {
               if (this.isLatinChar(var11)) {
                  var30 = var25.getHorizontalAdvance();
               } else {
                  var30 = var25.getVerticalAdvance();
               }
            } else if (var7 != 0 && var7 != 180) {
               if (var7 == 90) {
                  var30 = var25.getHorizontalAdvance();
               } else {
                  var30 = var25.getHorizontalAdvance();
                  this.gv.setGlyphTransform(var8, AffineTransform.getTranslateInstance(0.0, (double)var30));
               }
            } else {
               var30 = var25.getVerticalAdvance();
            }

            var22 += var30;
         }

         if (!epsEQ((double)var29, 0.0)) {
            AffineTransform var51 = this.gv.getGlyphTransform(var8);
            if (var51 == null) {
               var51 = new AffineTransform();
            }

            AffineTransform var47;
            if (epsEQ((double)var29, 1.5707963267948966)) {
               var47 = new AffineTransform(0.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.0F);
            } else if (epsEQ((double)var29, Math.PI)) {
               var47 = new AffineTransform(-1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F);
            } else if (epsEQ((double)var29, 4.71238898038469)) {
               var47 = new AffineTransform(0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
            } else {
               var47 = AffineTransform.getRotateInstance((double)var29);
            }

            var51.concatenate(var47);
            this.gv.setGlyphTransform(var8, var51);
         }

         var10 += this.gv.getCharacterCount(var8, var8);
         if (var10 >= this.charMap.length) {
            var10 = this.charMap.length - 1;
         }

         var11 = this.aci.setIndex(var10 + var9);
      }

      var23.x = var21;
      var23.y = var22;
      this.gv.setGlyphPosition(var8, var23);
      this.advance = new Point2D.Float((float)((double)var21 - this.offset.getX()), (float)((double)var22 - this.offset.getY()));
      if (var24) {
         var11 = this.aci.first();
         var10 = 0;
         var8 = 0;

         for(int var43 = -1; var8 < var2; ++var8) {
            if (ArabicTextHandler.arabicCharTransparent(var11)) {
               if (var43 == -1) {
                  var43 = var8;
               }
            } else if (var43 != -1) {
               Point2D var48 = this.gv.getGlyphPosition(var8);
               GVTGlyphMetrics var45 = this.gv.getGlyphMetrics(var8);
               boolean var46 = false;
               boolean var44 = false;
               var30 = 0.0F;
               float var49 = 0.0F;
               if (this.vertical) {
                  if (!var6 && var7 != 90) {
                     if (var7 == 270) {
                        var49 = 0.0F;
                     } else if (var7 == 0) {
                        var30 = var45.getHorizontalAdvance();
                     } else {
                        var30 = -var45.getHorizontalAdvance();
                     }
                  } else {
                     var49 = var45.getHorizontalAdvance();
                  }
               } else if (var7 == 0) {
                  var30 = var45.getHorizontalAdvance();
               } else if (var7 == 90) {
                  var49 = var45.getHorizontalAdvance();
               } else if (var7 == 180) {
                  var30 = 0.0F;
               } else {
                  var49 = -var45.getHorizontalAdvance();
               }

               float var32 = (float)(var48.getX() + (double)var30);
               float var33 = (float)(var48.getY() + (double)var49);
               int var34 = var43;

               while(true) {
                  if (var34 >= var8) {
                     var43 = -1;
                     break;
                  }

                  Point2D var35 = this.gv.getGlyphPosition(var34);
                  GVTGlyphMetrics var36 = this.gv.getGlyphMetrics(var34);
                  float var37 = (float)var35.getX();
                  float var38 = (float)var35.getY();
                  float var39 = 0.0F;
                  float var40 = 0.0F;
                  float var41 = var36.getHorizontalAdvance();
                  if (this.vertical) {
                     if (!var6 && var7 != 90) {
                        if (var7 == 270) {
                           var38 = var33 + var41;
                        } else if (var7 == 0) {
                           var37 = var32 - var41;
                        } else {
                           var37 = var32 + var41;
                        }
                     } else {
                        var38 = var33 - var41;
                     }
                  } else if (var7 == 0) {
                     var37 = var32 - var41;
                  } else if (var7 == 90) {
                     var38 = var33 - var41;
                  } else if (var7 == 180) {
                     var37 = var32 + var41;
                  } else {
                     var38 = var33 + var41;
                  }

                  Point2D.Double var50 = new Point2D.Double((double)var37, (double)var38);
                  this.gv.setGlyphPosition(var34, var50);
                  if (var44 || var46) {
                     AffineTransform var42 = AffineTransform.getTranslateInstance((double)var39, (double)var40);
                     var42.concatenate(this.gv.getGlyphTransform(var8));
                     this.gv.setGlyphTransform(var8, var42);
                  }

                  ++var34;
               }
            }

            var10 += this.gv.getCharacterCount(var8, var8);
            if (var10 >= this.charMap.length) {
               var10 = this.charMap.length - 1;
            }

            var11 = this.aci.setIndex(var10 + var9);
         }
      }

      this.layoutApplied = true;
      this.spacingApplied = false;
      this.glyphAdvances = null;
      this.pathApplied = false;
   }

   protected void adjustTextSpacing() {
      if (!this.spacingApplied) {
         if (!this.layoutApplied) {
            this.doExplicitGlyphLayout();
         }

         this.aci.first();
         Boolean var1 = (Boolean)this.aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.CUSTOM_SPACING);
         if (var1 != null && var1) {
            this.advance = this.doSpacing((Float)this.aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.KERNING), (Float)this.aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.LETTER_SPACING), (Float)this.aci.getAttribute(GVTAttributedCharacterIterator.TextAttribute.WORD_SPACING));
            this.layoutApplied = false;
         }

         this.applyStretchTransform(!this.adjSpacing);
         this.spacingApplied = true;
         this.pathApplied = false;
      }
   }

   protected Point2D doSpacing(Float var1, Float var2, Float var3) {
      boolean var4 = true;
      boolean var5 = false;
      boolean var6 = false;
      float var7 = 0.0F;
      float var8 = 0.0F;
      if (var1 != null && !var1.isNaN()) {
         var7 = var1;
         var4 = false;
      }

      if (var2 != null && !var2.isNaN()) {
         var8 = var2;
         var6 = true;
      }

      if (var3 != null && !var3.isNaN()) {
         var5 = true;
      }

      int var9 = this.gv.getNumGlyphs();
      float var10 = 0.0F;
      float var11 = 0.0F;
      Point2D[] var12 = new Point2D[var9 + 1];
      Point2D var13 = this.gv.getGlyphPosition(0);
      int var14 = this.gv.getGlyphCode(0);
      float var15 = (float)var13.getX();
      float var16 = (float)var13.getY();
      Point2D.Double var17 = new Point2D.Double(this.advance.getX() - (this.gv.getGlyphPosition(var9 - 1).getX() - (double)var15), this.advance.getY() - (this.gv.getGlyphPosition(var9 - 1).getY() - (double)var16));

      try {
         GVTFont var18 = this.gv.getFont();
         int var19;
         Point2D var20;
         if (var9 > 1 && (var6 || !var4)) {
            for(var19 = 1; var19 <= var9; ++var19) {
               var20 = this.gv.getGlyphPosition(var19);
               int var21 = var19 == var9 ? -1 : this.gv.getGlyphCode(var19);
               var10 = (float)var20.getX() - (float)var13.getX();
               var11 = (float)var20.getY() - (float)var13.getY();
               if (var4) {
                  if (this.vertical) {
                     var11 += var8;
                  } else {
                     var10 += var8;
                  }
               } else {
                  float var22;
                  if (this.vertical) {
                     var22 = 0.0F;
                     if (var21 != -1) {
                        var22 = var18.getVKern(var14, var21);
                     }

                     var11 += var7 - var22 + var8;
                  } else {
                     var22 = 0.0F;
                     if (var21 != -1) {
                        var22 = var18.getHKern(var14, var21);
                     }

                     var10 += var7 - var22 + var8;
                  }
               }

               var15 += var10;
               var16 += var11;
               var12[var19] = new Point2D.Float(var15, var16);
               var13 = var20;
               var14 = var21;
            }

            for(var19 = 1; var19 <= var9; ++var19) {
               if (var12[var19] != null) {
                  this.gv.setGlyphPosition(var19, var12[var19]);
               }
            }
         }

         if (this.vertical) {
            var17.setLocation(var17.getX(), var17.getY() + (double)var7 + (double)var8);
         } else {
            var17.setLocation(var17.getX() + (double)var7 + (double)var8, var17.getY());
         }

         var10 = 0.0F;
         var11 = 0.0F;
         var13 = this.gv.getGlyphPosition(0);
         var15 = (float)var13.getX();
         var16 = (float)var13.getY();
         if (var9 > 1 && var5) {
            for(var19 = 1; var19 < var9; ++var19) {
               var20 = this.gv.getGlyphPosition(var19);
               var10 = (float)var20.getX() - (float)var13.getX();
               var11 = (float)var20.getY() - (float)var13.getY();
               boolean var33 = false;
               int var23 = var19;

               for(GVTGlyphMetrics var24 = this.gv.getGlyphMetrics(var19); var24.getBounds2D().getWidth() < 0.01 || var24.isWhitespace(); var24 = this.gv.getGlyphMetrics(var19)) {
                  if (!var33) {
                     var33 = true;
                  }

                  if (var19 == var9 - 1) {
                     break;
                  }

                  ++var19;
                  ++var23;
                  var20 = this.gv.getGlyphPosition(var19);
               }

               if (var33) {
                  int var25 = var23 - var19;
                  float var26 = (float)var13.getX();
                  float var27 = (float)var13.getY();
                  var10 = (float)(var20.getX() - (double)var26) / (float)(var25 + 1);
                  var11 = (float)(var20.getY() - (double)var27) / (float)(var25 + 1);
                  if (this.vertical) {
                     var11 += var3 / (float)(var25 + 1);
                  } else {
                     var10 += var3 / (float)(var25 + 1);
                  }

                  for(int var28 = var19; var28 <= var23; ++var28) {
                     var15 += var10;
                     var16 += var11;
                     var12[var28] = new Point2D.Float(var15, var16);
                  }
               } else {
                  var10 = (float)(var20.getX() - var13.getX());
                  var11 = (float)(var20.getY() - var13.getY());
                  var15 += var10;
                  var16 += var11;
                  var12[var19] = new Point2D.Float(var15, var16);
               }

               var13 = var20;
            }

            Point2D var32 = this.gv.getGlyphPosition(var9);
            var15 += (float)(var32.getX() - var13.getX());
            var16 += (float)(var32.getY() - var13.getY());
            var12[var9] = new Point2D.Float(var15, var16);

            for(int var31 = 1; var31 <= var9; ++var31) {
               if (var12[var31] != null) {
                  this.gv.setGlyphPosition(var31, var12[var31]);
               }
            }
         }
      } catch (Exception var29) {
         var29.printStackTrace();
      }

      double var30 = this.gv.getGlyphPosition(var9 - 1).getX() - this.gv.getGlyphPosition(0).getX();
      double var34 = this.gv.getGlyphPosition(var9 - 1).getY() - this.gv.getGlyphPosition(0).getY();
      Point2D.Double var35 = new Point2D.Double(var30 + var17.getX(), var34 + var17.getY());
      return var35;
   }

   protected void applyStretchTransform(boolean var1) {
      if (this.xScale != 1.0F || this.yScale != 1.0F) {
         AffineTransform var2 = AffineTransform.getScaleInstance((double)this.xScale, (double)this.yScale);
         int var3 = this.gv.getNumGlyphs();
         float[] var4 = this.gv.getGlyphPositions(0, var3 + 1, (float[])null);
         float var5 = var4[0];
         float var6 = var4[1];
         Point2D.Float var7 = new Point2D.Float();

         for(int var8 = 0; var8 <= var3; ++var8) {
            float var9 = var4[2 * var8] - var5;
            float var10 = var4[2 * var8 + 1] - var6;
            var7.x = var5 + var9 * this.xScale;
            var7.y = var6 + var10 * this.yScale;
            this.gv.setGlyphPosition(var8, var7);
            if (var1 && var8 != var3) {
               AffineTransform var11 = this.gv.getGlyphTransform(var8);
               if (var11 != null) {
                  var11.preConcatenate(var2);
                  this.gv.setGlyphTransform(var8, var11);
               } else {
                  this.gv.setGlyphTransform(var8, var2);
               }
            }
         }

         this.advance = new Point2D.Float((float)(this.advance.getX() * (double)this.xScale), (float)(this.advance.getY() * (double)this.yScale));
         this.layoutApplied = false;
      }
   }

   protected void doPathLayout() {
      if (!this.pathApplied) {
         if (!this.spacingApplied) {
            this.adjustTextSpacing();
         }

         this.getGlyphAdvances();
         if (this.textPath == null) {
            this.pathApplied = true;
         } else {
            boolean var1 = !this.isVertical();
            boolean var2 = this.isGlyphOrientationAuto();
            boolean var3 = false;
            if (!var2) {
               int var31 = this.getGlyphOrientationAngle();
            }

            float var4 = this.textPath.lengthOfPath();
            float var5 = this.textPath.getStartOffset();
            int var6 = this.gv.getNumGlyphs();

            for(int var7 = 0; var7 < var6; ++var7) {
               this.gv.setGlyphVisible(var7, true);
            }

            float var32;
            if (var1) {
               var32 = (float)this.gv.getLogicalBounds().getWidth();
            } else {
               var32 = (float)this.gv.getLogicalBounds().getHeight();
            }

            if (var4 != 0.0F && var32 != 0.0F) {
               Point2D var8 = this.gv.getGlyphPosition(0);
               float var9 = 0.0F;
               float var10;
               if (var1) {
                  var9 = (float)var8.getY();
                  var10 = (float)(var8.getX() + (double)var5);
               } else {
                  var9 = (float)var8.getX();
                  var10 = (float)(var8.getY() + (double)var5);
               }

               char var11 = this.aci.first();
               int var12 = this.aci.getBeginIndex();
               int var13 = 0;
               int var14 = -1;
               float var15 = 0.0F;

               for(int var16 = 0; var16 < var6; ++var16) {
                  Point2D var17 = this.gv.getGlyphPosition(var16);
                  float var18 = 0.0F;
                  float var19 = 0.0F;
                  Point2D var20 = this.gv.getGlyphPosition(var16 + 1);
                  if (var1) {
                     var18 = (float)(var20.getX() - var17.getX());
                     var19 = (float)(var20.getY() - var17.getY());
                  } else {
                     var18 = (float)(var20.getY() - var17.getY());
                     var19 = (float)(var20.getX() - var17.getX());
                  }

                  Rectangle2D var21 = this.gv.getGlyphOutline(var16).getBounds2D();
                  float var22 = (float)var21.getWidth();
                  float var23 = (float)var21.getHeight();
                  float var24 = 0.0F;
                  if (var22 > 0.0F) {
                     var24 = (float)(var21.getX() + (double)(var22 / 2.0F));
                     var24 -= (float)var17.getX();
                  }

                  float var25 = 0.0F;
                  if (var23 > 0.0F) {
                     var25 = (float)(var21.getY() + (double)(var23 / 2.0F));
                     var25 -= (float)var17.getY();
                  }

                  float var26;
                  if (var1) {
                     var26 = var10 + var24;
                  } else {
                     var26 = var10 + var25;
                  }

                  Point2D var27 = this.textPath.pointAtLength(var26);
                  if (var27 != null) {
                     float var28 = this.textPath.angleAtLength(var26);
                     AffineTransform var29 = new AffineTransform();
                     if (var1) {
                        var29.rotate((double)var28);
                     } else {
                        var29.rotate((double)var28 - 1.5707963267948966);
                     }

                     if (var1) {
                        var29.translate(0.0, (double)var9);
                     } else {
                        var29.translate((double)var9, 0.0);
                     }

                     if (var1) {
                        var29.translate((double)(-var24), 0.0);
                     } else {
                        var29.translate(0.0, (double)(-var25));
                     }

                     AffineTransform var30 = this.gv.getGlyphTransform(var16);
                     if (var30 != null) {
                        var29.concatenate(var30);
                     }

                     this.gv.setGlyphTransform(var16, var29);
                     this.gv.setGlyphPosition(var16, var27);
                     var14 = var16;
                     var15 = var18;
                  } else {
                     this.gv.setGlyphVisible(var16, false);
                  }

                  var10 += var18;
                  var9 += var19;
                  var13 += this.gv.getCharacterCount(var16, var16);
                  if (var13 >= this.charMap.length) {
                     var13 = this.charMap.length - 1;
                  }

                  this.aci.setIndex(var13 + var12);
               }

               if (var14 > -1) {
                  Point2D var33 = this.gv.getGlyphPosition(var14);
                  if (var1) {
                     this.textPathAdvance = new Point2D.Double(var33.getX() + (double)var15, var33.getY());
                  } else {
                     this.textPathAdvance = new Point2D.Double(var33.getX(), var33.getY() + (double)var15);
                  }
               } else {
                  this.textPathAdvance = new Point2D.Double(0.0, 0.0);
               }

               this.layoutApplied = false;
               this.spacingApplied = false;
               this.pathApplied = true;
            } else {
               this.pathApplied = true;
               this.textPathAdvance = this.advance;
            }
         }
      }
   }

   protected boolean isLatinChar(char var1) {
      if (var1 < 255 && Character.isLetterOrDigit(var1)) {
         return true;
      } else {
         Character.UnicodeBlock var2 = UnicodeBlock.of(var1);
         return var2 == UnicodeBlock.BASIC_LATIN || var2 == UnicodeBlock.LATIN_1_SUPPLEMENT || var2 == UnicodeBlock.LATIN_EXTENDED_ADDITIONAL || var2 == UnicodeBlock.LATIN_EXTENDED_A || var2 == UnicodeBlock.LATIN_EXTENDED_B || var2 == UnicodeBlock.ARABIC || var2 == UnicodeBlock.ARABIC_PRESENTATION_FORMS_A || var2 == UnicodeBlock.ARABIC_PRESENTATION_FORMS_B;
      }
   }

   protected boolean isGlyphOrientationAuto() {
      if (!this.isVertical()) {
         return false;
      } else {
         this.aci.first();
         Integer var1 = (Integer)this.aci.getAttribute(VERTICAL_ORIENTATION);
         if (var1 != null) {
            return var1 == ORIENTATION_AUTO;
         } else {
            return true;
         }
      }
   }

   protected int getGlyphOrientationAngle() {
      int var1 = 0;
      this.aci.first();
      Float var2;
      if (this.isVertical()) {
         var2 = (Float)this.aci.getAttribute(VERTICAL_ORIENTATION_ANGLE);
      } else {
         var2 = (Float)this.aci.getAttribute(HORIZONTAL_ORIENTATION_ANGLE);
      }

      if (var2 != null) {
         var1 = (int)var2;
      }

      if (var1 != 0 || var1 != 90 || var1 != 180 || var1 != 270) {
         while(var1 < 0) {
            var1 += 360;
         }

         while(var1 >= 360) {
            var1 -= 360;
         }

         if (var1 > 45 && var1 <= 315) {
            if (var1 > 45 && var1 <= 135) {
               var1 = 90;
            } else if (var1 > 135 && var1 <= 225) {
               var1 = 180;
            } else {
               var1 = 270;
            }
         } else {
            var1 = 0;
         }
      }

      return var1;
   }

   public boolean hasCharacterIndex(int var1) {
      for(int var2 = 0; var2 < this.charMap.length; ++var2) {
         if (var1 == this.charMap[var2]) {
            return true;
         }
      }

      return false;
   }

   public boolean isAltGlyph() {
      return this.isAltGlyph;
   }

   static {
      FLOW_LINE_BREAK = GVTAttributedCharacterIterator.TextAttribute.FLOW_LINE_BREAK;
      FLOW_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_PARAGRAPH;
      FLOW_EMPTY_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_EMPTY_PARAGRAPH;
      LINE_HEIGHT = GVTAttributedCharacterIterator.TextAttribute.LINE_HEIGHT;
      VERTICAL_ORIENTATION = GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION;
      VERTICAL_ORIENTATION_ANGLE = GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION_ANGLE;
      HORIZONTAL_ORIENTATION_ANGLE = GVTAttributedCharacterIterator.TextAttribute.HORIZONTAL_ORIENTATION_ANGLE;
      X = GVTAttributedCharacterIterator.TextAttribute.X;
      Y = GVTAttributedCharacterIterator.TextAttribute.Y;
      DX = GVTAttributedCharacterIterator.TextAttribute.DX;
      DY = GVTAttributedCharacterIterator.TextAttribute.DY;
      ROTATION = GVTAttributedCharacterIterator.TextAttribute.ROTATION;
      BASELINE_SHIFT = GVTAttributedCharacterIterator.TextAttribute.BASELINE_SHIFT;
      WRITING_MODE = GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE;
      WRITING_MODE_TTB = GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE_TTB;
      ORIENTATION_AUTO = GVTAttributedCharacterIterator.TextAttribute.ORIENTATION_AUTO;
      GVT_FONT = GVTAttributedCharacterIterator.TextAttribute.GVT_FONT;
      runAtts = new HashSet();
      runAtts.add(X);
      runAtts.add(Y);
      runAtts.add(DX);
      runAtts.add(DY);
      runAtts.add(ROTATION);
      runAtts.add(BASELINE_SHIFT);
      szAtts = new HashSet();
      szAtts.add(TextAttribute.SIZE);
      szAtts.add(GVT_FONT);
      szAtts.add(LINE_HEIGHT);
   }
}
