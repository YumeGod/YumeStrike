package org.apache.batik.extension.svg;

import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.font.MultiGlyphVector;
import org.apache.batik.gvt.text.GlyphLayout;

public class FlowExtGlyphLayout extends GlyphLayout {
   public FlowExtGlyphLayout(AttributedCharacterIterator var1, int[] var2, Point2D var3, FontRenderContext var4) {
      super(var1, var2, var3, var4);
   }

   public static void textWrapTextChunk(AttributedCharacterIterator[] var0, List var1, List var2) {
      GVTGlyphVector[] var3 = new GVTGlyphVector[var0.length];
      List[] var4 = new List[var0.length];
      GlyphIterator[] var5 = new GlyphIterator[var0.length];
      Iterator var6 = var1.iterator();
      Iterator var7 = var2.iterator();
      RegionInfo var8 = null;
      float var12 = 0.0F;
      if (var7.hasNext()) {
         var8 = (RegionInfo)var7.next();
         var12 = (float)var8.getHeight();
      }

      boolean var13 = true;
      float var14 = 1.0F;
      float var15 = 0.0F;
      float var16 = 0.0F;
      Point2D.Float var17 = new Point2D.Float(0.0F, 0.0F);
      float var18 = 0.0F;

      int var19;
      for(var19 = 0; var6.hasNext(); ++var19) {
         AttributedCharacterIterator var20 = var0[var19];
         if (var8 != null) {
            List var21 = (List)var20.getAttribute(FLOW_EMPTY_PARAGRAPH);
            if (var21 != null) {
               Iterator var22 = var21.iterator();

               label217:
               while(true) {
                  while(true) {
                     if (!var22.hasNext()) {
                        break label217;
                     }

                     MarginInfo var23 = (MarginInfo)var22.next();
                     float var24 = var18 > var23.getTopMargin() ? var18 : var23.getTopMargin();
                     if (var16 + var24 <= var12 && !var23.isFlowRegionBreak()) {
                        var16 += var24;
                        var18 = var23.getBottomMargin();
                     } else {
                        if (!var7.hasNext()) {
                           var8 = null;
                           break label217;
                        }

                        var8 = (RegionInfo)var7.next();
                        var12 = (float)var8.getHeight();
                        var17 = new Point2D.Float(0.0F, 0.0F);
                        var16 = 0.0F;
                        var18 = 0.0F;
                     }
                  }
               }

               if (var8 == null) {
                  break;
               }
            }
         }

         LinkedList var48 = new LinkedList();
         List var50 = (List)var6.next();
         Iterator var52 = var50.iterator();

         while(var52.hasNext()) {
            GlyphLayout var54 = (GlyphLayout)var52.next();
            var48.add(var54.getGlyphVector());
         }

         MultiGlyphVector var55 = new MultiGlyphVector(var48);
         var3[var19] = var55;
         int var25 = var55.getNumGlyphs();
         var20.first();
         MarginInfo var26 = (MarginInfo)var20.getAttribute(FLOW_PARAGRAPH);
         if (var26 != null) {
            if (var8 == null) {
               for(int var27 = 0; var27 < var25; ++var27) {
                  var55.setGlyphVisible(var27, false);
               }
            } else {
               float var58 = var18 > var26.getTopMargin() ? var18 : var26.getTopMargin();
               if (var16 + var58 <= var12) {
                  var16 += var58;
               } else {
                  if (!var7.hasNext()) {
                     var8 = null;
                     break;
                  }

                  var8 = (RegionInfo)var7.next();
                  var12 = (float)var8.getHeight();
                  var17 = new Point2D.Float(0.0F, 0.0F);
                  var16 = var26.getTopMargin();
               }

               var18 = var26.getBottomMargin();
               float var28 = var26.getLeftMargin();
               float var29 = var26.getRightMargin();
               if (((GlyphLayout)var50.get(0)).isLeftToRight()) {
                  var28 += var26.getIndent();
               } else {
                  var29 += var26.getIndent();
               }

               float var10 = (float)var8.getX() + var28;
               float var9 = (float)var8.getY();
               float var11 = (float)(var8.getWidth() - (double)(var28 + var29));
               var12 = (float)var8.getHeight();
               LinkedList var30 = new LinkedList();
               var4[var19] = var30;
               float var31 = 0.0F;
               GlyphIterator var32 = new GlyphIterator(var20, var55);
               var5[var19] = var32;
               GlyphIterator var33 = null;
               GlyphIterator var34 = null;
               if (!var32.done() && !var32.isPrinting()) {
                  updateVerticalAlignOffset(var17, var8, var16);
                  var30.add(var32.newLine(new Point2D.Float(var10, var9 + var16), var11, true, var17));
               }

               GlyphIterator var35 = var32.copy();
               boolean var36 = true;

               label187:
               while(true) {
                  while(true) {
                     while(true) {
                        boolean var37;
                        boolean var38;
                        while(true) {
                           if (var32.done()) {
                              break label187;
                           }

                           var37 = false;
                           var38 = false;
                           if (var32.isPrinting() && var32.getAdv() > var11) {
                              if (var33 == null) {
                                 if (!var7.hasNext()) {
                                    var8 = null;
                                    var32 = var35.copy(var32);
                                    break label187;
                                 }

                                 var8 = (RegionInfo)var7.next();
                                 var10 = (float)var8.getX() + var28;
                                 var9 = (float)var8.getY();
                                 var11 = (float)(var8.getWidth() - (double)(var28 + var29));
                                 var12 = (float)var8.getHeight();
                                 var17 = new Point2D.Float(0.0F, 0.0F);
                                 var16 = var36 ? var26.getTopMargin() : 0.0F;
                                 var31 = 0.0F;
                                 var32 = var35.copy(var32);
                                 continue;
                              }

                              var32 = var33.copy(var32);
                              var15 = 1.0F;
                              var37 = true;
                              var38 = false;
                              break;
                           }

                           if (var32.isLastChar()) {
                              var15 = 1.0F;
                              var37 = true;
                              var38 = true;
                           }
                           break;
                        }

                        int var39 = var32.getLineBreaks();
                        if (var39 != 0) {
                           if (var37) {
                              --var15;
                           }

                           var15 += (float)var39;
                           var37 = true;
                           var38 = true;
                        }

                        if (var37) {
                           float var60 = var32.getMaxAscent() + var32.getMaxDescent();
                           float var41;
                           if (var13) {
                              var41 = var32.getMaxFontSize() * var14;
                           } else {
                              var41 = var14;
                           }

                           float var42 = (var41 - var60) / 2.0F;
                           float var43 = var31 + var42 + var32.getMaxAscent();
                           float var44 = var42 + var32.getMaxDescent();
                           var16 += var43;
                           float var45 = var44;
                           if (var44 < var32.getMaxDescent()) {
                              var45 = var32.getMaxDescent();
                           }

                           if (var16 + var45 > var12) {
                              if (!var7.hasNext()) {
                                 var8 = null;
                                 var32 = var35.copy(var32);
                                 break label187;
                              }

                              float var46 = var11;
                              var8 = (RegionInfo)var7.next();
                              var10 = (float)var8.getX() + var28;
                              var9 = (float)var8.getY();
                              var11 = (float)(var8.getWidth() - (double)(var28 + var29));
                              var12 = (float)var8.getHeight();
                              var17 = new Point2D.Float(0.0F, 0.0F);
                              var16 = var36 ? var26.getTopMargin() : 0.0F;
                              var31 = 0.0F;
                              if (var46 > var11 || var39 != 0) {
                                 var32 = var35.copy(var32);
                              }
                           } else {
                              var31 = var44 + (var15 - 1.0F) * var41;
                              var15 = 0.0F;
                              updateVerticalAlignOffset(var17, var8, var16 + var45);
                              var30.add(var32.newLine(new Point2D.Float(var10, var9 + var16), var11, var38, var17));
                              var10 -= var28;
                              var11 += var28 + var29;
                              var28 = var26.getLeftMargin();
                              var29 = var26.getRightMargin();
                              var10 += var28;
                              var11 -= var28 + var29;
                              var36 = false;
                              var35 = var32.copy(var35);
                              var33 = null;
                           }
                        } else if (!var32.isBreakChar() && var33 != null && var33.isBreakChar()) {
                           var32.nextChar();
                        } else {
                           var34 = var32.copy(var34);
                           var32.nextChar();
                           if (var32.getChar() != 8205) {
                              GlyphIterator var40 = var33;
                              var33 = var34;
                              var34 = var40;
                           }
                        }
                     }
                  }
               }

               var16 += var31;
               int var59 = var32.getGlyphIndex();

               while(var59 < var25) {
                  var55.setGlyphVisible(var59++, false);
               }

               if (var26.isFlowRegionBreak()) {
                  var8 = null;
                  if (var7.hasNext()) {
                     var8 = (RegionInfo)var7.next();
                     var12 = (float)var8.getHeight();
                     var16 = 0.0F;
                     var18 = 0.0F;
                     var17 = new Point2D.Float(0.0F, 0.0F);
                  }
               }
            }
         }
      }

      for(var19 = 0; var19 < var0.length; ++var19) {
         List var47 = var4[var19];
         if (var47 != null) {
            AttributedCharacterIterator var49 = var0[var19];
            var49.first();
            MarginInfo var51 = (MarginInfo)var49.getAttribute(FLOW_PARAGRAPH);
            if (var51 != null) {
               int var53 = var51.getJustification();
               GVTGlyphVector var56 = var3[var19];
               if (var56 == null) {
                  break;
               }

               GlyphIterator var57 = var5[var19];
               layoutChunk(var56, var57.getOrigin(), var53, var47);
            }
         }
      }

   }

   public static void updateVerticalAlignOffset(Point2D.Float var0, RegionInfo var1, float var2) {
      float var3 = (float)var1.getHeight() - var2;
      var0.setLocation(0.0F, var1.getVerticalAlignment() * var3);
   }

   public static void layoutChunk(GVTGlyphVector var0, Point2D var1, int var2, List var3) {
      Iterator var4 = var3.iterator();
      int var5 = var0.getNumGlyphs();
      float[] var6 = var0.getGlyphPositions(0, var5 + 1, (float[])null);
      Point2D.Float var7 = null;
      float var8 = 0.0F;
      float var9 = 0.0F;
      float var10 = (float)var1.getX();
      float var11 = (float)var1.getY();
      float var12 = 1.0F;
      float var13 = 0.0F;
      float var14 = 0.0F;
      float var15 = 0.0F;
      boolean var16 = false;
      float var17 = 0.0F;
      int var18 = 0;
      Point2D.Float var20 = new Point2D.Float();

      int var19;
      for(var19 = 0; var19 < var5; ++var19) {
         if (var19 == var18) {
            var10 += var8;
            if (!var4.hasNext()) {
               break;
            }

            LineInfo var21 = (LineInfo)var4.next();
            var18 = var21.getEndIdx();
            var7 = var21.getLocation();
            var8 = var21.getAdvance();
            var9 = var21.getVisualAdvance();
            var14 = var21.getLastCharWidth();
            var15 = var21.getLineWidth();
            var16 = var21.isPartialLine();
            var17 = var21.getVerticalAlignOffset().y;
            var13 = 0.0F;
            var12 = 1.0F;
            switch (var2) {
               case 0:
               default:
                  break;
               case 1:
                  var13 = (var15 - var9) / 2.0F;
                  break;
               case 2:
                  var13 = var15 - var9;
                  break;
               case 3:
                  if (!var16 && var18 != var19 + 1) {
                     var12 = (var15 - var14) / (var9 - var14);
                  }
            }
         }

         var20.x = var7.x + (var6[2 * var19] - var10) * var12 + var13;
         var20.y = var7.y + var6[2 * var19 + 1] - var11 + var17;
         var0.setGlyphPosition(var19, var20);
      }

      var20.x = var10;
      var20.y = var11;
      if (var7 != null) {
         var20.x = var7.x + (var6[2 * var19] - var10) * var12 + var13;
         var20.y = var7.y + (var6[2 * var19 + 1] - var11) + var17;
      }

      var0.setGlyphPosition(var19, var20);
   }
}
