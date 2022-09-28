package org.apache.batik.gvt.renderer;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.batik.gvt.font.FontFamilyResolver;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTFontFamily;
import org.apache.batik.gvt.font.GVTGlyphMetrics;
import org.apache.batik.gvt.font.GVTLineMetrics;
import org.apache.batik.gvt.text.AttributedCharacterSpanIterator;
import org.apache.batik.gvt.text.BidiAttributedCharacterIterator;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.gvt.text.TextHit;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.batik.gvt.text.TextPath;
import org.apache.batik.gvt.text.TextSpanLayout;

public class StrokingTextPainter extends BasicTextPainter {
   public static final AttributedCharacterIterator.Attribute PAINT_INFO;
   public static final AttributedCharacterIterator.Attribute FLOW_REGIONS;
   public static final AttributedCharacterIterator.Attribute FLOW_PARAGRAPH;
   public static final AttributedCharacterIterator.Attribute TEXT_COMPOUND_ID;
   public static final AttributedCharacterIterator.Attribute GVT_FONT;
   public static final AttributedCharacterIterator.Attribute GVT_FONTS;
   public static final AttributedCharacterIterator.Attribute BIDI_LEVEL;
   public static final AttributedCharacterIterator.Attribute XPOS;
   public static final AttributedCharacterIterator.Attribute YPOS;
   public static final AttributedCharacterIterator.Attribute TEXTPATH;
   public static final AttributedCharacterIterator.Attribute WRITING_MODE;
   public static final Integer WRITING_MODE_TTB;
   public static final Integer WRITING_MODE_RTL;
   public static final AttributedCharacterIterator.Attribute ANCHOR_TYPE;
   public static final Integer ADJUST_SPACING;
   public static final Integer ADJUST_ALL;
   public static final GVTAttributedCharacterIterator.TextAttribute ALT_GLYPH_HANDLER;
   static Set extendedAtts;
   protected static TextPainter singleton;

   public static TextPainter getInstance() {
      return singleton;
   }

   public void paint(TextNode var1, Graphics2D var2) {
      AttributedCharacterIterator var3 = var1.getAttributedCharacterIterator();
      if (var3 != null) {
         List var4 = this.getTextRuns(var1, var3);
         this.paintDecorations(var4, var2, 1);
         this.paintDecorations(var4, var2, 4);
         this.paintTextRuns(var4, var2);
         this.paintDecorations(var4, var2, 2);
      }
   }

   protected void printAttrs(AttributedCharacterIterator var1) {
      var1.first();
      int var2 = var1.getBeginIndex();
      System.out.print("AttrRuns: ");

      while(var1.current() != '\uffff') {
         int var3 = var1.getRunLimit();
         System.out.print("" + (var3 - var2) + ", ");
         var1.setIndex(var3);
         var2 = var3;
      }

      System.out.println("");
   }

   public List getTextRuns(TextNode var1, AttributedCharacterIterator var2) {
      List var3 = var1.getTextRuns();
      if (var3 != null) {
         return var3;
      } else {
         AttributedCharacterIterator[] var4 = this.getTextChunkACIs(var2);
         var3 = this.computeTextRuns(var1, var2, var4);
         var1.setTextRuns(var3);
         return var1.getTextRuns();
      }
   }

   public List computeTextRuns(TextNode var1, AttributedCharacterIterator var2, AttributedCharacterIterator[] var3) {
      int[][] var4 = new int[var3.length][];
      int var5 = var2.getBeginIndex();

      for(int var6 = 0; var6 < var3.length; ++var6) {
         BidiAttributedCharacterIterator var7 = new BidiAttributedCharacterIterator(var3[var6], this.fontRenderContext, var5);
         var3[var6] = var7;
         var4[var6] = var7.getCharMap();
         var3[var6] = createModifiedACIForFontMatching(var3[var6]);
         var5 += var3[var6].getEndIndex() - var3[var6].getBeginIndex();
      }

      ArrayList var11 = new ArrayList();
      TextChunk var8 = null;
      int var9 = 0;
      Point2D var10 = var1.getLocation();

      TextChunk var12;
      do {
         var3[var9].first();
         var12 = this.getTextChunk(var1, var3[var9], var4[var9], var11, var8);
         var3[var9].first();
         if (var12 != null) {
            var10 = this.adjustChunkOffsets(var10, var11, var12);
         }

         var8 = var12;
         ++var9;
      } while(var12 != null && var9 < var3.length);

      return var11;
   }

   protected AttributedCharacterIterator[] getTextChunkACIs(AttributedCharacterIterator var1) {
      ArrayList var2 = new ArrayList();
      int var3 = var1.getBeginIndex();
      var1.first();
      Object var4 = var1.getAttribute(WRITING_MODE);

      int var7;
      int var15;
      for(boolean var5 = var4 == WRITING_MODE_TTB; var1.setIndex(var3) != '\uffff'; var3 = var7) {
         TextPath var6 = null;
         var7 = var3;

         for(boolean var8 = false; var1.setIndex(var7) != '\uffff'; var7 = var15) {
            TextPath var9 = (TextPath)var1.getAttribute(TEXTPATH);
            if (var7 != var3) {
               Float var10;
               if (var5) {
                  var10 = (Float)var1.getAttribute(YPOS);
                  if (var10 != null && !var10.isNaN()) {
                     break;
                  }
               } else {
                  var10 = (Float)var1.getAttribute(XPOS);
                  if (var10 != null && !var10.isNaN()) {
                     break;
                  }
               }

               if (var6 == null && var9 != null || var6 != null && var9 == null) {
                  break;
               }
            }

            var6 = var9;
            if (var1.getAttribute(FLOW_PARAGRAPH) != null) {
               var15 = var1.getRunLimit(FLOW_PARAGRAPH);
               var1.setIndex(var15);
               break;
            }

            var15 = var1.getRunLimit(TEXT_COMPOUND_ID);
            if (var7 == var3) {
               TextNode.Anchor var16 = (TextNode.Anchor)var1.getAttribute(ANCHOR_TYPE);
               if (var16 != TextNode.Anchor.START) {
                  Float var11;
                  if (var5) {
                     var11 = (Float)var1.getAttribute(YPOS);
                     if (var11 == null || var11.isNaN()) {
                        continue;
                     }
                  } else {
                     var11 = (Float)var1.getAttribute(XPOS);
                     if (var11 == null || var11.isNaN()) {
                        continue;
                     }
                  }

                  for(int var17 = var7 + 1; var17 < var15; var3 = var17++) {
                     var1.setIndex(var17);
                     Float var12;
                     if (var5) {
                        var12 = (Float)var1.getAttribute(YPOS);
                        if (var12 == null || var12.isNaN()) {
                           break;
                        }
                     } else {
                        var12 = (Float)var1.getAttribute(XPOS);
                        if (var12 == null || var12.isNaN()) {
                           break;
                        }
                     }

                     var2.add(new AttributedCharacterSpanIterator(var1, var17 - 1, var17));
                  }
               }
            }
         }

         var7 = var1.getIndex();
         var2.add(new AttributedCharacterSpanIterator(var1, var3, var7));
      }

      AttributedCharacterIterator[] var13 = new AttributedCharacterIterator[var2.size()];
      Iterator var14 = var2.iterator();

      for(var15 = 0; var14.hasNext(); ++var15) {
         var13[var15] = (AttributedCharacterIterator)var14.next();
      }

      return var13;
   }

   protected static AttributedCharacterIterator createModifiedACIForFontMatching(AttributedCharacterIterator var0) {
      var0.first();
      AttributedString var1 = null;
      int var2 = 0;
      int var3 = var0.getBeginIndex();
      boolean var4 = true;
      int var6 = var0.getRunStart(TEXT_COMPOUND_ID);

      while(var4) {
         int var5 = var6;
         var6 = var0.getRunLimit(TEXT_COMPOUND_ID);
         int var7 = var6 - var5;
         List var8 = (List)var0.getAttribute(GVT_FONTS);
         float var9 = 12.0F;
         Float var10 = (Float)var0.getAttribute(TextAttribute.SIZE);
         if (var10 != null) {
            var9 = var10;
         }

         if (var8.size() == 0) {
            var8.add(FontFamilyResolver.defaultFont.deriveFont(var9, var0));
         }

         boolean[] var11 = new boolean[var7];
         if (var1 == null) {
            var1 = new AttributedString(var0);
         }

         GVTFont var12 = null;
         int var13 = 0;
         int var14 = var5;

         int var16;
         GVTFont var18;
         int var19;
         label117:
         for(var16 = 0; var16 < var8.size(); ++var16) {
            int var17 = var14;
            boolean var15 = false;
            var0.setIndex(var14);
            var18 = (GVTFont)var8.get(var16);
            if (var12 == null) {
               var12 = var18;
            }

            while(true) {
               while(var17 < var6) {
                  var19 = var18.canDisplayUpTo((CharacterIterator)var0, var17, var6);
                  Object var20 = var0.getAttribute(ALT_GLYPH_HANDLER);
                  if (var20 != null) {
                     var19 = -1;
                  }

                  if (var19 == -1) {
                     var19 = var6;
                  }

                  if (var19 <= var17) {
                     if (!var15) {
                        var14 = var17;
                        var15 = true;
                     }

                     ++var17;
                  } else {
                     int var21 = -1;

                     for(int var22 = var17; var22 < var19; ++var22) {
                        if (var11[var22 - var5]) {
                           if (var21 != -1) {
                              var1.addAttribute(GVT_FONT, var18, var21 - var3, var22 - var3);
                              var21 = -1;
                           }
                        } else if (var21 == -1) {
                           var21 = var22;
                        }

                        var11[var22 - var5] = true;
                        ++var13;
                     }

                     if (var21 != -1) {
                        var1.addAttribute(GVT_FONT, var18, var21 - var3, var19 - var3);
                     }

                     var17 = var19 + 1;
                  }
               }

               if (var13 == var7) {
                  break label117;
               }
               break;
            }
         }

         var16 = -1;
         GVTFontFamily var23 = null;
         var18 = var12;

         for(var19 = 0; var19 < var7; ++var19) {
            if (var11[var19]) {
               if (var16 != -1) {
                  var1.addAttribute(GVT_FONT, var18, var16 + var2, var19 + var2);
                  var16 = -1;
                  var18 = null;
                  var23 = null;
               }
            } else {
               char var24 = var0.setIndex(var5 + var19);
               GVTFontFamily var25 = FontFamilyResolver.getFamilyThatCanDisplay(var24);
               if (var16 == -1) {
                  var16 = var19;
                  var23 = var25;
                  if (var25 == null) {
                     var18 = var12;
                  } else {
                     var18 = var25.deriveFont(var9, var0);
                  }
               } else if (var23 != var25) {
                  var1.addAttribute(GVT_FONT, var18, var16 + var2, var19 + var2);
                  var16 = var19;
                  var23 = var25;
                  if (var25 == null) {
                     var18 = var12;
                  } else {
                     var18 = var25.deriveFont(var9, var0);
                  }
               }
            }
         }

         if (var16 != -1) {
            var1.addAttribute(GVT_FONT, var18, var16 + var2, var7 + var2);
         }

         var2 += var7;
         if (var0.setIndex(var6) == '\uffff') {
            var4 = false;
         }
      }

      if (var1 != null) {
         return var1.getIterator();
      } else {
         return var0;
      }
   }

   protected TextChunk getTextChunk(TextNode var1, AttributedCharacterIterator var2, int[] var3, List var4, TextChunk var5) {
      int var6 = 0;
      if (var5 != null) {
         var6 = var5.end;
      }

      int var7 = var6;
      int var8 = var2.getIndex();
      if (var2.current() == '\uffff') {
         return null;
      } else {
         Point2D.Float var9 = new Point2D.Float(0.0F, 0.0F);
         Point2D.Float var10 = new Point2D.Float(0.0F, 0.0F);
         boolean var11 = true;
         TextSpanLayout var12 = null;

         while(true) {
            int var13 = var2.getRunStart(extendedAtts);
            int var14 = var2.getRunLimit(extendedAtts);
            AttributedCharacterSpanIterator var15 = new AttributedCharacterSpanIterator(var2, var13, var14);
            int[] var16 = new int[var14 - var13];
            System.arraycopy(var3, var13 - var8, var16, 0, var16.length);
            FontRenderContext var17 = this.fontRenderContext;
            RenderingHints var18 = var1.getRenderingHints();
            if (var18 != null && var18.get(RenderingHints.KEY_TEXT_ANTIALIASING) == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) {
               var17 = this.aaOffFontRenderContext;
            }

            var12 = this.getTextLayoutFactory().createTextLayout(var15, var16, var9, var17);
            var4.add(new TextRun(var12, var15, var11));
            Point2D var19 = var12.getAdvance2D();
            var10.x += (float)var19.getX();
            var10.y += (float)var19.getY();
            ++var7;
            if (var2.setIndex(var14) == '\uffff') {
               return new TextChunk(var6, var7, var10);
            }

            var11 = false;
         }
      }
   }

   protected Point2D adjustChunkOffsets(Point2D var1, List var2, TextChunk var3) {
      TextRun var4 = (TextRun)var2.get(var3.begin);
      int var5 = var4.getAnchorType();
      Float var6 = var4.getLength();
      Integer var7 = var4.getLengthAdjust();
      boolean var8 = true;
      if (var6 == null || var6.isNaN()) {
         var8 = false;
      }

      int var9 = 0;

      for(int var10 = var3.begin; var10 < var3.end; ++var10) {
         var4 = (TextRun)var2.get(var10);
         AttributedCharacterIterator var11 = var4.getACI();
         var9 += var11.getEndIndex() - var11.getBeginIndex();
      }

      if (var7 == GVTAttributedCharacterIterator.TextAttribute.ADJUST_SPACING && var9 == 1) {
         var8 = false;
      }

      float var33 = 1.0F;
      float var34 = 1.0F;
      var4 = (TextRun)var2.get(var3.end - 1);
      TextSpanLayout var12 = var4.getLayout();
      GVTGlyphMetrics var13 = var12.getGlyphMetrics(var12.getGlyphCount() - 1);
      GVTLineMetrics var14 = var12.getLineMetrics();
      Rectangle2D var15 = var13.getBounds2D();
      float var16 = (var13.getVerticalAdvance() - (var14.getAscent() + var14.getDescent())) / 2.0F;
      float var17 = (float)(var15.getWidth() + var15.getX());
      float var18 = (float)((double)(var16 + var14.getAscent()) + var15.getHeight() + var15.getY());
      Point2D.Float var19;
      if (!var8) {
         var19 = new Point2D.Float((float)(var3.advance.getX() + (double)var17 - (double)var13.getHorizontalAdvance()), (float)(var3.advance.getY() - (double)var13.getVerticalAdvance() + (double)var18));
      } else {
         Point2D var20 = var3.advance;
         double var21;
         if (var12.isVertical()) {
            if (var7 == ADJUST_SPACING) {
               var34 = (float)((double)(var6 - var18) / (var20.getY() - (double)var13.getVerticalAdvance()));
            } else {
               var21 = var20.getY() - (double)var13.getVerticalAdvance() + (double)var18;
               var34 = (float)((double)var6 / var21);
            }

            var19 = new Point2D.Float(0.0F, var6);
         } else {
            if (var7 == ADJUST_SPACING) {
               var33 = (float)((double)(var6 - var17) / (var20.getX() - (double)var13.getHorizontalAdvance()));
            } else {
               var21 = var20.getX() + (double)var17 - (double)var13.getHorizontalAdvance();
               var33 = (float)((double)var6 / var21);
            }

            var19 = new Point2D.Float(var6, 0.0F);
         }

         Point2D.Float var36 = new Point2D.Float(0.0F, 0.0F);

         for(int var22 = var3.begin; var22 < var3.end; ++var22) {
            var4 = (TextRun)var2.get(var22);
            var12 = var4.getLayout();
            var12.setScale(var33, var34, var7 == ADJUST_SPACING);
            Point2D var23 = var12.getAdvance2D();
            var36.x += (float)var23.getX();
            var36.y += (float)var23.getY();
         }

         var3.advance = var36;
      }

      float var35 = 0.0F;
      float var37 = 0.0F;
      switch (var5) {
         case 1:
            var35 = (float)(-var19.getX() / 2.0);
            var37 = (float)(-var19.getY() / 2.0);
            break;
         case 2:
            var35 = (float)(-var19.getX());
            var37 = (float)(-var19.getY());
      }

      var4 = (TextRun)var2.get(var3.begin);
      var12 = var4.getLayout();
      AttributedCharacterIterator var38 = var4.getACI();
      var38.first();
      boolean var39 = var12.isVertical();
      Float var24 = (Float)var38.getAttribute(XPOS);
      Float var25 = (Float)var38.getAttribute(YPOS);
      TextPath var26 = (TextPath)var38.getAttribute(TEXTPATH);
      float var27 = (float)var1.getX();
      float var28 = (float)var1.getY();
      float var29 = 0.0F;
      float var30 = 0.0F;
      if (var24 != null && !var24.isNaN()) {
         var27 = var24;
         var29 = var27;
      }

      if (var25 != null && !var25.isNaN()) {
         var28 = var25;
         var30 = var28;
      }

      if (var39) {
         var28 += var37;
         var30 += var37;
         var29 = 0.0F;
      } else {
         var27 += var35;
         var29 += var35;
         var30 = 0.0F;
      }

      for(int var31 = var3.begin; var31 < var3.end; ++var31) {
         var4 = (TextRun)var2.get(var31);
         var12 = var4.getLayout();
         var38 = var4.getACI();
         var38.first();
         var26 = (TextPath)var38.getAttribute(TEXTPATH);
         if (var39) {
            var24 = (Float)var38.getAttribute(XPOS);
            if (var24 != null && !var24.isNaN()) {
               var27 = var24;
            }
         } else {
            var25 = (Float)var38.getAttribute(YPOS);
            if (var25 != null && !var25.isNaN()) {
               var28 = var25;
            }
         }

         Point2D var32;
         if (var26 == null) {
            var12.setOffset(new Point2D.Float(var27, var28));
            var32 = var12.getAdvance2D();
            var27 = (float)((double)var27 + var32.getX());
            var28 = (float)((double)var28 + var32.getY());
         } else {
            var12.setOffset(new Point2D.Float(var29, var30));
            var32 = var12.getAdvance2D();
            var29 += (float)var32.getX();
            var30 += (float)var32.getY();
            var32 = var12.getTextPathAdvance();
            var27 = (float)var32.getX();
            var28 = (float)var32.getY();
         }
      }

      return new Point2D.Float(var27, var28);
   }

   protected void paintDecorations(List var1, Graphics2D var2, int var3) {
      Paint var4 = null;
      Paint var5 = null;
      Stroke var6 = null;
      Rectangle2D.Double var7 = null;
      double var8 = 0.0;
      double var10 = 0.0;

      for(int var12 = 0; var12 < var1.size(); ++var12) {
         TextRun var13 = (TextRun)var1.get(var12);
         AttributedCharacterIterator var14 = var13.getACI();
         var14.first();
         TextPaintInfo var15 = (TextPaintInfo)var14.getAttribute(PAINT_INFO);
         if (var15 != null && var15.composite != null) {
            var2.setComposite(var15.composite);
         }

         Paint var16 = null;
         Stroke var17 = null;
         Paint var18 = null;
         if (var15 != null) {
            switch (var3) {
               case 1:
                  var16 = var15.underlinePaint;
                  var17 = var15.underlineStroke;
                  var18 = var15.underlineStrokePaint;
                  break;
               case 2:
                  var16 = var15.strikethroughPaint;
                  var17 = var15.strikethroughStroke;
                  var18 = var15.strikethroughStrokePaint;
                  break;
               case 3:
               default:
                  return;
               case 4:
                  var16 = var15.overlinePaint;
                  var17 = var15.overlineStroke;
                  var18 = var15.overlineStrokePaint;
            }
         }

         Shape var19;
         Rectangle2D var20;
         if (var13.isFirstRunInChunk()) {
            var19 = var13.getLayout().getDecorationOutline(var3);
            var20 = var19.getBounds2D();
            var8 = var20.getY();
            var10 = var20.getHeight();
         }

         if ((var13.isFirstRunInChunk() || var16 != var4 || var17 != var6 || var18 != var5) && var7 != null) {
            if (var4 != null) {
               var2.setPaint(var4);
               var2.fill(var7);
            }

            if (var6 != null && var5 != null) {
               var2.setPaint(var5);
               var2.setStroke(var6);
               var2.draw(var7);
            }

            var7 = null;
         }

         if ((var16 != null || var18 != null) && !var13.getLayout().isVertical() && !var13.getLayout().isOnATextPath()) {
            var19 = var13.getLayout().getDecorationOutline(var3);
            if (var7 == null) {
               var20 = var19.getBounds2D();
               var7 = new Rectangle2D.Double(var20.getX(), var8, var20.getWidth(), var10);
            } else {
               var20 = var19.getBounds2D();
               double var21 = Math.min(var7.getX(), var20.getX());
               double var23 = Math.max(var7.getMaxX(), var20.getMaxX());
               var7.setRect(var21, var8, var23 - var21, var10);
            }
         }

         var4 = var16;
         var6 = var17;
         var5 = var18;
      }

      if (var7 != null) {
         if (var4 != null) {
            var2.setPaint(var4);
            var2.fill(var7);
         }

         if (var6 != null && var5 != null) {
            var2.setPaint(var5);
            var2.setStroke(var6);
            var2.draw(var7);
         }
      }

   }

   protected void paintTextRuns(List var1, Graphics2D var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         TextRun var4 = (TextRun)var1.get(var3);
         AttributedCharacterIterator var5 = var4.getACI();
         var5.first();
         TextPaintInfo var6 = (TextPaintInfo)var5.getAttribute(PAINT_INFO);
         if (var6 != null && var6.composite != null) {
            var2.setComposite(var6.composite);
         }

         var4.getLayout().draw(var2);
      }

   }

   public Shape getOutline(TextNode var1) {
      GeneralPath var2 = null;
      AttributedCharacterIterator var3 = var1.getAttributedCharacterIterator();
      if (var3 == null) {
         return null;
      } else {
         List var4 = this.getTextRuns(var1, var3);

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            TextRun var6 = (TextRun)var4.get(var5);
            TextSpanLayout var7 = var6.getLayout();
            GeneralPath var8 = new GeneralPath(var7.getOutline());
            if (var2 == null) {
               var2 = var8;
            } else {
               var2.setWindingRule(1);
               var2.append(var8, false);
            }
         }

         Shape var9 = this.getDecorationOutline(var4, 1);
         Shape var10 = this.getDecorationOutline(var4, 2);
         Shape var11 = this.getDecorationOutline(var4, 4);
         if (var9 != null) {
            if (var2 == null) {
               var2 = new GeneralPath(var9);
            } else {
               var2.setWindingRule(1);
               var2.append(var9, false);
            }
         }

         if (var10 != null) {
            if (var2 == null) {
               var2 = new GeneralPath(var10);
            } else {
               var2.setWindingRule(1);
               var2.append(var10, false);
            }
         }

         if (var11 != null) {
            if (var2 == null) {
               var2 = new GeneralPath(var11);
            } else {
               var2.setWindingRule(1);
               var2.append(var11, false);
            }
         }

         return var2;
      }
   }

   public Rectangle2D getBounds2D(TextNode var1) {
      AttributedCharacterIterator var2 = var1.getAttributedCharacterIterator();
      if (var2 == null) {
         return null;
      } else {
         List var3 = this.getTextRuns(var1, var2);
         Rectangle2D var4 = null;

         for(int var5 = 0; var5 < var3.size(); ++var5) {
            TextRun var6 = (TextRun)var3.get(var5);
            TextSpanLayout var7 = var6.getLayout();
            Rectangle2D var8 = var7.getBounds2D();
            if (var8 != null) {
               if (var4 == null) {
                  var4 = var8;
               } else {
                  var4.add(var8);
               }
            }
         }

         Shape var9 = this.getDecorationStrokeOutline(var3, 1);
         if (var9 != null) {
            if (var4 == null) {
               var4 = var9.getBounds2D();
            } else {
               var4.add(var9.getBounds2D());
            }
         }

         Shape var10 = this.getDecorationStrokeOutline(var3, 2);
         if (var10 != null) {
            if (var4 == null) {
               var4 = var10.getBounds2D();
            } else {
               var4.add(var10.getBounds2D());
            }
         }

         Shape var11 = this.getDecorationStrokeOutline(var3, 4);
         if (var11 != null) {
            if (var4 == null) {
               var4 = var11.getBounds2D();
            } else {
               var4.add(var11.getBounds2D());
            }
         }

         return var4;
      }
   }

   protected Shape getDecorationOutline(List var1, int var2) {
      GeneralPath var3 = null;
      Paint var4 = null;
      Paint var5 = null;
      Stroke var6 = null;
      Rectangle2D.Double var7 = null;
      double var8 = 0.0;
      double var10 = 0.0;

      for(int var12 = 0; var12 < var1.size(); ++var12) {
         TextRun var13 = (TextRun)var1.get(var12);
         AttributedCharacterIterator var14 = var13.getACI();
         var14.first();
         Paint var15 = null;
         Stroke var16 = null;
         Paint var17 = null;
         TextPaintInfo var18 = (TextPaintInfo)var14.getAttribute(PAINT_INFO);
         if (var18 != null) {
            switch (var2) {
               case 1:
                  var15 = var18.underlinePaint;
                  var16 = var18.underlineStroke;
                  var17 = var18.underlineStrokePaint;
                  break;
               case 2:
                  var15 = var18.strikethroughPaint;
                  var16 = var18.strikethroughStroke;
                  var17 = var18.strikethroughStrokePaint;
                  break;
               case 3:
               default:
                  return null;
               case 4:
                  var15 = var18.overlinePaint;
                  var16 = var18.overlineStroke;
                  var17 = var18.overlineStrokePaint;
            }
         }

         Shape var19;
         Rectangle2D var20;
         if (var13.isFirstRunInChunk()) {
            var19 = var13.getLayout().getDecorationOutline(var2);
            var20 = var19.getBounds2D();
            var8 = var20.getY();
            var10 = var20.getHeight();
         }

         if ((var13.isFirstRunInChunk() || var15 != var4 || var16 != var6 || var17 != var5) && var7 != null) {
            if (var3 == null) {
               var3 = new GeneralPath(var7);
            } else {
               var3.append(var7, false);
            }

            var7 = null;
         }

         if ((var15 != null || var17 != null) && !var13.getLayout().isVertical() && !var13.getLayout().isOnATextPath()) {
            var19 = var13.getLayout().getDecorationOutline(var2);
            if (var7 == null) {
               var20 = var19.getBounds2D();
               var7 = new Rectangle2D.Double(var20.getX(), var8, var20.getWidth(), var10);
            } else {
               var20 = var19.getBounds2D();
               double var21 = Math.min(var7.getX(), var20.getX());
               double var23 = Math.max(var7.getMaxX(), var20.getMaxX());
               var7.setRect(var21, var8, var23 - var21, var10);
            }
         }

         var4 = var15;
         var6 = var16;
         var5 = var17;
      }

      if (var7 != null) {
         if (var3 == null) {
            var3 = new GeneralPath(var7);
         } else {
            var3.append(var7, false);
         }
      }

      return var3;
   }

   protected Shape getDecorationStrokeOutline(List var1, int var2) {
      GeneralPath var3 = null;
      Paint var4 = null;
      Paint var5 = null;
      Stroke var6 = null;
      Rectangle2D.Double var7 = null;
      double var8 = 0.0;
      double var10 = 0.0;

      for(int var12 = 0; var12 < var1.size(); ++var12) {
         TextRun var13 = (TextRun)var1.get(var12);
         AttributedCharacterIterator var14 = var13.getACI();
         var14.first();
         Paint var15 = null;
         Stroke var16 = null;
         Paint var17 = null;
         TextPaintInfo var18 = (TextPaintInfo)var14.getAttribute(PAINT_INFO);
         if (var18 != null) {
            switch (var2) {
               case 1:
                  var15 = var18.underlinePaint;
                  var16 = var18.underlineStroke;
                  var17 = var18.underlineStrokePaint;
                  break;
               case 2:
                  var15 = var18.strikethroughPaint;
                  var16 = var18.strikethroughStroke;
                  var17 = var18.strikethroughStrokePaint;
                  break;
               case 3:
               default:
                  return null;
               case 4:
                  var15 = var18.overlinePaint;
                  var16 = var18.overlineStroke;
                  var17 = var18.overlineStrokePaint;
            }
         }

         Shape var19;
         Rectangle2D var20;
         if (var13.isFirstRunInChunk()) {
            var19 = var13.getLayout().getDecorationOutline(var2);
            var20 = var19.getBounds2D();
            var8 = var20.getY();
            var10 = var20.getHeight();
         }

         if ((var13.isFirstRunInChunk() || var15 != var4 || var16 != var6 || var17 != var5) && var7 != null) {
            Object var26 = null;
            if (var6 != null && var5 != null) {
               var26 = var6.createStrokedShape(var7);
            } else if (var4 != null) {
               var26 = var7;
            }

            if (var26 != null) {
               if (var3 == null) {
                  var3 = new GeneralPath((Shape)var26);
               } else {
                  var3.append((Shape)var26, false);
               }
            }

            var7 = null;
         }

         if ((var15 != null || var17 != null) && !var13.getLayout().isVertical() && !var13.getLayout().isOnATextPath()) {
            var19 = var13.getLayout().getDecorationOutline(var2);
            if (var7 == null) {
               var20 = var19.getBounds2D();
               var7 = new Rectangle2D.Double(var20.getX(), var8, var20.getWidth(), var10);
            } else {
               var20 = var19.getBounds2D();
               double var21 = Math.min(var7.getX(), var20.getX());
               double var23 = Math.max(var7.getMaxX(), var20.getMaxX());
               var7.setRect(var21, var8, var23 - var21, var10);
            }
         }

         var4 = var15;
         var6 = var16;
         var5 = var17;
      }

      if (var7 != null) {
         Object var25 = null;
         if (var6 != null && var5 != null) {
            var25 = var6.createStrokedShape(var7);
         } else if (var4 != null) {
            var25 = var7;
         }

         if (var25 != null) {
            if (var3 == null) {
               var3 = new GeneralPath((Shape)var25);
            } else {
               var3.append((Shape)var25, false);
            }
         }
      }

      return var3;
   }

   public Mark getMark(TextNode var1, int var2, boolean var3) {
      AttributedCharacterIterator var4 = var1.getAttributedCharacterIterator();
      if (var4 == null) {
         return null;
      } else if (var2 >= var4.getBeginIndex() && var2 <= var4.getEndIndex()) {
         TextHit var5 = new TextHit(var2, var3);
         return new BasicTextPainter.BasicMark(var1, var5);
      } else {
         return null;
      }
   }

   protected Mark hitTest(double var1, double var3, TextNode var5) {
      AttributedCharacterIterator var6 = var5.getAttributedCharacterIterator();
      if (var6 == null) {
         return null;
      } else {
         List var7 = this.getTextRuns(var5, var6);

         for(int var8 = 0; var8 < var7.size(); ++var8) {
            TextRun var9 = (TextRun)var7.get(var8);
            TextSpanLayout var10 = var9.getLayout();
            TextHit var11 = var10.hitTestChar((float)var1, (float)var3);
            if (var11 != null && var10.getBounds2D().contains(var1, var3)) {
               return new BasicTextPainter.BasicMark(var5, var11);
            }
         }

         return null;
      }
   }

   public Mark selectFirst(TextNode var1) {
      AttributedCharacterIterator var2 = var1.getAttributedCharacterIterator();
      if (var2 == null) {
         return null;
      } else {
         TextHit var3 = new TextHit(var2.getBeginIndex(), false);
         return new BasicTextPainter.BasicMark(var1, var3);
      }
   }

   public Mark selectLast(TextNode var1) {
      AttributedCharacterIterator var2 = var1.getAttributedCharacterIterator();
      if (var2 == null) {
         return null;
      } else {
         TextHit var3 = new TextHit(var2.getEndIndex() - 1, false);
         return new BasicTextPainter.BasicMark(var1, var3);
      }
   }

   public int[] getSelected(Mark var1, Mark var2) {
      if (var1 != null && var2 != null) {
         BasicTextPainter.BasicMark var3;
         BasicTextPainter.BasicMark var4;
         try {
            var3 = (BasicTextPainter.BasicMark)var1;
            var4 = (BasicTextPainter.BasicMark)var2;
         } catch (ClassCastException var16) {
            throw new Error("This Mark was not instantiated by this TextPainter class!");
         }

         TextNode var5 = var3.getTextNode();
         if (var5 == null) {
            return null;
         } else if (var5 != var4.getTextNode()) {
            throw new Error("Markers are from different TextNodes!");
         } else {
            AttributedCharacterIterator var6 = var5.getAttributedCharacterIterator();
            if (var6 == null) {
               return null;
            } else {
               int[] var7 = new int[]{var3.getHit().getCharIndex(), var4.getHit().getCharIndex()};
               List var8 = this.getTextRuns(var5, var6);
               Iterator var9 = var8.iterator();
               int var10 = -1;
               int var11 = -1;
               TextSpanLayout var12 = null;
               TextSpanLayout var13 = null;

               while(var9.hasNext()) {
                  TextRun var14 = (TextRun)var9.next();
                  TextSpanLayout var15 = var14.getLayout();
                  if (var10 == -1) {
                     var10 = var15.getGlyphIndex(var7[0]);
                     if (var10 != -1) {
                        var12 = var15;
                     }
                  }

                  if (var11 == -1) {
                     var11 = var15.getGlyphIndex(var7[1]);
                     if (var11 != -1) {
                        var13 = var15;
                     }
                  }

                  if (var10 != -1 && var11 != -1) {
                     break;
                  }
               }

               if (var12 != null && var13 != null) {
                  int var17 = var12.getCharacterCount(var10, var10);
                  int var18 = var13.getCharacterCount(var11, var11);
                  if (var17 > 1) {
                     if (var7[0] > var7[1] && var12.isLeftToRight()) {
                        var7[0] += var17 - 1;
                     } else if (var7[1] > var7[0] && !var12.isLeftToRight()) {
                        var7[0] -= var17 - 1;
                     }
                  }

                  if (var18 > 1) {
                     if (var7[1] > var7[0] && var13.isLeftToRight()) {
                        var7[1] += var18 - 1;
                     } else if (var7[0] > var7[1] && !var13.isLeftToRight()) {
                        var7[1] -= var18 - 1;
                     }
                  }

                  return var7;
               } else {
                  return null;
               }
            }
         }
      } else {
         return null;
      }
   }

   public Shape getHighlightShape(Mark var1, Mark var2) {
      if (var1 != null && var2 != null) {
         BasicTextPainter.BasicMark var3;
         BasicTextPainter.BasicMark var4;
         try {
            var3 = (BasicTextPainter.BasicMark)var1;
            var4 = (BasicTextPainter.BasicMark)var2;
         } catch (ClassCastException var15) {
            throw new Error("This Mark was not instantiated by this TextPainter class!");
         }

         TextNode var5 = var3.getTextNode();
         if (var5 == null) {
            return null;
         } else if (var5 != var4.getTextNode()) {
            throw new Error("Markers are from different TextNodes!");
         } else {
            AttributedCharacterIterator var6 = var5.getAttributedCharacterIterator();
            if (var6 == null) {
               return null;
            } else {
               int var7 = var3.getHit().getCharIndex();
               int var8 = var4.getHit().getCharIndex();
               if (var7 > var8) {
                  int var10 = var7;
                  var7 = var8;
                  var8 = var10;
               }

               List var9 = this.getTextRuns(var5, var6);
               GeneralPath var16 = new GeneralPath();

               for(int var11 = 0; var11 < var9.size(); ++var11) {
                  TextRun var12 = (TextRun)var9.get(var11);
                  TextSpanLayout var13 = var12.getLayout();
                  Shape var14 = var13.getHighlightShape(var7, var8);
                  if (var14 != null && !var14.getBounds().isEmpty()) {
                     var16.append(var14, false);
                  }
               }

               return var16;
            }
         }
      } else {
         return null;
      }
   }

   static {
      PAINT_INFO = GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO;
      FLOW_REGIONS = GVTAttributedCharacterIterator.TextAttribute.FLOW_REGIONS;
      FLOW_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_PARAGRAPH;
      TEXT_COMPOUND_ID = GVTAttributedCharacterIterator.TextAttribute.TEXT_COMPOUND_ID;
      GVT_FONT = GVTAttributedCharacterIterator.TextAttribute.GVT_FONT;
      GVT_FONTS = GVTAttributedCharacterIterator.TextAttribute.GVT_FONTS;
      BIDI_LEVEL = GVTAttributedCharacterIterator.TextAttribute.BIDI_LEVEL;
      XPOS = GVTAttributedCharacterIterator.TextAttribute.X;
      YPOS = GVTAttributedCharacterIterator.TextAttribute.Y;
      TEXTPATH = GVTAttributedCharacterIterator.TextAttribute.TEXTPATH;
      WRITING_MODE = GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE;
      WRITING_MODE_TTB = GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE_TTB;
      WRITING_MODE_RTL = GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE_RTL;
      ANCHOR_TYPE = GVTAttributedCharacterIterator.TextAttribute.ANCHOR_TYPE;
      ADJUST_SPACING = GVTAttributedCharacterIterator.TextAttribute.ADJUST_SPACING;
      ADJUST_ALL = GVTAttributedCharacterIterator.TextAttribute.ADJUST_ALL;
      ALT_GLYPH_HANDLER = GVTAttributedCharacterIterator.TextAttribute.ALT_GLYPH_HANDLER;
      extendedAtts = new HashSet();
      extendedAtts.add(FLOW_PARAGRAPH);
      extendedAtts.add(TEXT_COMPOUND_ID);
      extendedAtts.add(GVT_FONT);
      singleton = new StrokingTextPainter();
   }

   public class TextRun {
      protected AttributedCharacterIterator aci;
      protected TextSpanLayout layout;
      protected int anchorType;
      protected boolean firstRunInChunk;
      protected Float length;
      protected Integer lengthAdjust;

      public TextRun(TextSpanLayout var2, AttributedCharacterIterator var3, boolean var4) {
         this.layout = var2;
         this.aci = var3;
         this.aci.first();
         this.firstRunInChunk = var4;
         this.anchorType = 0;
         TextNode.Anchor var5 = (TextNode.Anchor)var3.getAttribute(GVTAttributedCharacterIterator.TextAttribute.ANCHOR_TYPE);
         if (var5 != null) {
            this.anchorType = var5.getType();
         }

         if (var3.getAttribute(StrokingTextPainter.WRITING_MODE) == StrokingTextPainter.WRITING_MODE_RTL) {
            if (this.anchorType == 0) {
               this.anchorType = 2;
            } else if (this.anchorType == 2) {
               this.anchorType = 0;
            }
         }

         this.length = (Float)var3.getAttribute(GVTAttributedCharacterIterator.TextAttribute.BBOX_WIDTH);
         this.lengthAdjust = (Integer)var3.getAttribute(GVTAttributedCharacterIterator.TextAttribute.LENGTH_ADJUST);
      }

      public AttributedCharacterIterator getACI() {
         return this.aci;
      }

      public TextSpanLayout getLayout() {
         return this.layout;
      }

      public int getAnchorType() {
         return this.anchorType;
      }

      public Float getLength() {
         return this.length;
      }

      public Integer getLengthAdjust() {
         return this.lengthAdjust;
      }

      public boolean isFirstRunInChunk() {
         return this.firstRunInChunk;
      }
   }

   class TextChunk {
      public int begin;
      public int end;
      public Point2D advance;

      public TextChunk(int var2, int var3, Point2D var4) {
         this.begin = var2;
         this.end = var3;
         this.advance = new Point2D.Float((float)var4.getX(), (float)var4.getY());
      }
   }
}
