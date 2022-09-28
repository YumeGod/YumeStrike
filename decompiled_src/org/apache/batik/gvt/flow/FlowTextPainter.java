package org.apache.batik.gvt.flow;

import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.font.GVTLineMetrics;
import org.apache.batik.gvt.font.MultiGlyphVector;
import org.apache.batik.gvt.renderer.StrokingTextPainter;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.GlyphLayout;

public class FlowTextPainter extends StrokingTextPainter {
   protected static TextPainter singleton = new FlowTextPainter();
   public static final char SOFT_HYPHEN = '\u00ad';
   public static final char ZERO_WIDTH_SPACE = '\u200b';
   public static final char ZERO_WIDTH_JOINER = '\u200d';
   public static final char SPACE = ' ';
   public static final AttributedCharacterIterator.Attribute WORD_LIMIT;
   public static final AttributedCharacterIterator.Attribute FLOW_REGIONS;
   public static final AttributedCharacterIterator.Attribute FLOW_LINE_BREAK;
   public static final AttributedCharacterIterator.Attribute LINE_HEIGHT;
   public static final AttributedCharacterIterator.Attribute GVT_FONT;
   protected static Set szAtts;

   public static TextPainter getInstance() {
      return singleton;
   }

   public List getTextRuns(TextNode var1, AttributedCharacterIterator var2) {
      List var3 = var1.getTextRuns();
      if (var3 != null) {
         return var3;
      } else {
         AttributedCharacterIterator[] var4 = this.getTextChunkACIs(var2);
         var3 = this.computeTextRuns(var1, var2, var4);
         var2.first();
         List var5 = (List)var2.getAttribute(FLOW_REGIONS);
         if (var5 != null) {
            Iterator var6 = var3.iterator();
            ArrayList var7 = new ArrayList();
            StrokingTextPainter.TextRun var8 = (StrokingTextPainter.TextRun)var6.next();
            ArrayList var9 = new ArrayList();
            var7.add(var9);
            var9.add(var8.getLayout());

            for(; var6.hasNext(); var9.add(var8.getLayout())) {
               var8 = (StrokingTextPainter.TextRun)var6.next();
               if (var8.isFirstRunInChunk()) {
                  var9 = new ArrayList();
                  var7.add(var9);
               }
            }

            textWrap(var4, var7, var5, this.fontRenderContext);
         }

         var1.setTextRuns(var3);
         return var1.getTextRuns();
      }
   }

   public static boolean textWrap(AttributedCharacterIterator[] var0, List var1, List var2, FontRenderContext var3) {
      GVTGlyphVector[] var4 = new GVTGlyphVector[var0.length];
      WordInfo[][] var5 = new WordInfo[var0.length][];
      Iterator var6 = var1.iterator();
      float var7 = 0.0F;
      int var8 = 0;
      BlockInfo[] var9 = new BlockInfo[var0.length];
      float[] var10 = new float[var0.length];

      AttributedCharacterIterator var12;
      for(int var11 = 0; var6.hasNext(); ++var11) {
         var12 = var0[var11];
         LinkedList var13 = new LinkedList();
         List var14 = (List)var6.next();
         Iterator var15 = var14.iterator();

         while(var15.hasNext()) {
            GlyphLayout var16 = (GlyphLayout)var15.next();
            var13.add(var16.getGlyphVector());
         }

         MultiGlyphVector var31 = new MultiGlyphVector(var13);
         var4[var11] = var31;
         var5[var11] = doWordAnalysis(var31, var12, var8, var3);
         var12.first();
         BlockInfo var17 = (BlockInfo)var12.getAttribute(FLOW_PARAGRAPH);
         var17.initLineInfo(var3);
         var9[var11] = var17;
         if (var7 > var17.getTopMargin()) {
            var10[var11] = var7;
         } else {
            var10[var11] = var17.getTopMargin();
         }

         var7 = var17.getBottomMargin();
         var8 += var5[var11].length;
      }

      Iterator var26 = var2.iterator();
      var12 = null;
      int var28 = 0;
      int var29 = 0;
      LinkedList var30 = new LinkedList();

      WordInfo[] var34;
      while(var26.hasNext()) {
         RegionInfo var27 = (RegionInfo)var26.next();
         FlowRegions var32 = new FlowRegions(var27.getShape());

         while(var29 < var5.length) {
            var34 = var5[var29];
            BlockInfo var18 = var9[var29];
            WordInfo var19 = var34[var28];
            Object var20 = var19.getFlowLine();
            double var21 = (double)Math.max(var19.getLineHeight(), var18.getLineHeight());
            LineInfo var23 = new LineInfo(var32, var18, true);
            double var24 = var23.getCurrentY() + (double)var10[var29];
            var10[var29] = 0.0F;
            if (var23.gotoY(var24)) {
               break;
            }

            while(!var23.addWord(var19)) {
               var24 = var23.getCurrentY() + var21 * 0.1;
               if (var23.gotoY(var24)) {
                  break;
               }
            }

            if (var32.done()) {
               break;
            }

            ++var28;

            for(; var28 < var34.length; ++var28) {
               var19 = var34[var28];
               if (var19.getFlowLine() != var20 || !var23.addWord(var19)) {
                  var23.layout();
                  var30.add(var23);
                  var23 = null;
                  var20 = var19.getFlowLine();
                  var21 = (double)Math.max(var19.getLineHeight(), var18.getLineHeight());
                  if (!var32.newLine(var21)) {
                     break;
                  }

                  var23 = new LineInfo(var32, var18, false);

                  while(!var23.addWord(var19)) {
                     var24 = var23.getCurrentY() + var21 * 0.1;
                     if (var23.gotoY(var24)) {
                        break;
                     }
                  }

                  if (var32.done()) {
                     break;
                  }
               }
            }

            if (var23 != null) {
               var23.setParaEnd(true);
               var23.layout();
            }

            if (var32.done()) {
               break;
            }

            ++var29;
            var28 = 0;
            if (var18.isFlowRegionBreak() || !var32.newLine(var21)) {
               break;
            }
         }

         if (var29 == var5.length) {
            break;
         }
      }

      boolean var33;
      for(var33 = var29 < var5.length; var29 < var5.length; var28 = 0) {
         for(var34 = var5[var29]; var28 < var34.length; ++var28) {
            WordInfo var35 = var34[var28];
            int var36 = var35.getNumGlyphGroups();

            for(int var37 = 0; var37 < var36; ++var37) {
               GlyphGroupInfo var38 = var35.getGlyphGroup(var37);
               GVTGlyphVector var22 = var38.getGlyphVector();
               int var39 = var38.getEnd();

               for(int var40 = var38.getStart(); var40 <= var39; ++var40) {
                  var22.setGlyphVisible(var40, false);
               }
            }
         }

         ++var29;
      }

      return var33;
   }

   static int[] allocWordMap(int[] var0, int var1) {
      if (var0 != null) {
         if (var1 <= var0.length) {
            return var0;
         }

         if (var1 < var0.length * 2) {
            var1 = var0.length * 2;
         }
      }

      int[] var2 = new int[var1];
      int var3 = var0 != null ? var0.length : 0;
      if (var1 < var3) {
         var3 = var1;
      }

      if (var3 != 0) {
         System.arraycopy(var0, 0, var2, 0, var3);
      }

      Arrays.fill(var2, var3, var1, -1);
      return var2;
   }

   static WordInfo[] doWordAnalysis(GVTGlyphVector var0, AttributedCharacterIterator var1, int var2, FontRenderContext var3) {
      int var4 = var0.getNumGlyphs();
      int[] var5 = new int[var4];
      int[] var6 = allocWordMap((int[])null, 10);
      int var7 = 0;
      int var8 = var1.getBeginIndex();

      int var9;
      int var12;
      int var13;
      int var14;
      for(var9 = 0; var9 < var4; ++var9) {
         int var10 = var0.getCharacterCount(var9, var9);
         var1.setIndex(var8);
         Integer var11 = (Integer)var1.getAttribute(WORD_LIMIT);
         var12 = var11 - var2;
         if (var12 > var7) {
            var7 = var12;
            var6 = allocWordMap(var6, var12 + 1);
         }

         ++var8;

         for(var13 = 1; var13 < var10; ++var13) {
            var1.setIndex(var8);
            var11 = (Integer)var1.getAttribute(WORD_LIMIT);
            var14 = var11 - var2;
            if (var14 > var7) {
               var7 = var14;
               var6 = allocWordMap(var6, var14 + 1);
            }

            if (var14 < var12) {
               var6[var12] = var14;
               var12 = var14;
            } else if (var14 > var12) {
               var6[var14] = var12;
            }

            ++var8;
         }

         var5[var9] = var12;
      }

      var9 = 0;
      WordInfo[] var40 = new WordInfo[var7 + 1];

      for(int var41 = 0; var41 <= var7; ++var41) {
         var12 = var6[var41];
         if (var12 == -1) {
            var40[var41] = new WordInfo(var9++);
         } else {
            var13 = var12;

            for(var12 = var6[var41]; var12 != -1; var12 = var6[var12]) {
               var13 = var12;
            }

            var6[var41] = var13;
            var40[var41] = var40[var13];
         }
      }

      Object var39 = null;
      WordInfo[] var42 = new WordInfo[var9];

      for(var12 = 0; var12 <= var7; ++var12) {
         WordInfo var43 = var40[var12];
         var42[var43.getIndex()] = var40[var12];
      }

      var8 = var1.getBeginIndex();
      var12 = var1.getEndIndex();
      char var44 = var1.setIndex(var8);
      var14 = var8;
      GVTFont var15 = (GVTFont)var1.getAttribute(GVT_FONT);
      float var16 = 1.0F;
      Float var17 = (Float)var1.getAttribute(LINE_HEIGHT);
      if (var17 != null) {
         var16 = var17;
      }

      int var18 = var1.getRunLimit(szAtts);
      WordInfo var19 = null;
      float[] var20 = new float[var4];
      float[] var21 = new float[var4];
      boolean[] var22 = new boolean[var4];
      boolean[] var23 = new boolean[var4];
      boolean[] var24 = new boolean[var4];
      float[] var25 = var0.getGlyphPositions(0, var4 + 1, (float[])null);

      int var31;
      int var51;
      for(int var26 = 0; var26 < var4; ++var26) {
         char var27 = var44;
         var44 = var1.setIndex(var8);
         Integer var28 = (Integer)var1.getAttribute(WORD_LIMIT);
         WordInfo var29 = var40[var28 - var2];
         if (var29.getFlowLine() == null) {
            var29.setFlowLine(var1.getAttribute(FLOW_LINE_BREAK));
         }

         if (var19 == null) {
            var19 = var29;
         } else if (var19 != var29) {
            GVTLineMetrics var30 = var15.getLineMetrics((CharacterIterator)var1, var14, var8, var3);
            var19.addLineMetrics(var15, var30);
            var19.addLineHeight(var16);
            var14 = var8;
            var19 = var29;
         }

         var51 = var0.getCharacterCount(var26, var26);
         if (var51 == 1) {
            float var32;
            switch (var44) {
               case ' ':
                  var24[var26] = true;
                  var31 = var1.next();
                  var1.previous();
                  var32 = var15.getHKern(var27, var31);
                  var20[var26] = -(var25[2 * var26 + 2] - var25[2 * var26] + var32);
                  break;
               case '\u00ad':
                  var22[var26] = true;
                  var31 = var1.next();
                  var1.previous();
                  var32 = var15.getHKern(var27, var31);
                  var21[var26] = -(var25[2 * var26 + 2] - var25[2 * var26] + var32);
                  break;
               case '\u200b':
                  var23[var26] = true;
                  break;
               case '\u200d':
                  var23[var26] = true;
            }
         }

         var8 += var51;
         if (var8 > var18 && var8 < var12) {
            GVTLineMetrics var52 = var15.getLineMetrics((CharacterIterator)var1, var14, var18, var3);
            var19.addLineMetrics(var15, var52);
            var19.addLineHeight(var16);
            var19 = null;
            var14 = var8;
            var1.setIndex(var8);
            var15 = (GVTFont)var1.getAttribute(GVT_FONT);
            Float var53 = (Float)var1.getAttribute(LINE_HEIGHT);
            var16 = var53;
            var18 = var1.getRunLimit(szAtts);
         }
      }

      GVTLineMetrics var45 = var15.getLineMetrics((CharacterIterator)var1, var14, var18, var3);
      var19.addLineMetrics(var15, var45);
      var19.addLineHeight(var16);
      int[] var46 = new int[var9];

      int var10002;
      for(int var47 = 0; var47 < var4; ++var47) {
         int var49 = var5[var47];
         var51 = var40[var49].getIndex();
         var5[var47] = var51;
         var10002 = var46[var51]++;
      }

      var40 = null;
      int[][] var48 = new int[var9][];
      int[] var50 = new int[var9];

      int var33;
      for(var51 = 0; var51 < var4; ++var51) {
         var31 = var5[var51];
         int[] var54 = var48[var31];
         if (var54 == null) {
            var54 = var48[var31] = new int[var46[var31]];
            var46[var31] = 0;
         }

         var33 = var46[var31];
         var54[var33] = var51;
         if (var33 == 0) {
            var10002 = var50[var31]++;
         } else if (var54[var33 - 1] != var51 - 1) {
            var10002 = var50[var31]++;
         }

         var10002 = var46[var31]++;
      }

      for(var51 = 0; var51 < var9; ++var51) {
         var31 = var50[var51];
         GlyphGroupInfo[] var55 = new GlyphGroupInfo[var31];
         int var35;
         if (var31 == 1) {
            int[] var56 = var48[var51];
            int var57 = var56[0];
            var35 = var56[var56.length - 1];
            var55[0] = new GlyphGroupInfo(var0, var57, var35, var23, var22[var35], var25, var21, var20, var24);
         } else {
            var33 = 0;
            int[] var34 = var48[var51];
            var35 = var34[0];
            int var36 = var35;

            int var37;
            for(var37 = 1; var37 < var34.length; ++var37) {
               if (var35 + 1 != var34[var37]) {
                  int var38 = var34[var37 - 1];
                  var55[var33] = new GlyphGroupInfo(var0, var36, var38, var23, var22[var38], var25, var21, var20, var24);
                  var36 = var34[var37];
                  ++var33;
               }

               var35 = var34[var37];
            }

            var37 = var34[var34.length - 1];
            var55[var33] = new GlyphGroupInfo(var0, var36, var37, var23, var22[var37], var25, var21, var20, var24);
         }

         var42[var51].setGlyphGroups(var55);
      }

      return var42;
   }

   static {
      WORD_LIMIT = TextLineBreaks.WORD_LIMIT;
      FLOW_REGIONS = GVTAttributedCharacterIterator.TextAttribute.FLOW_REGIONS;
      FLOW_LINE_BREAK = GVTAttributedCharacterIterator.TextAttribute.FLOW_LINE_BREAK;
      LINE_HEIGHT = GVTAttributedCharacterIterator.TextAttribute.LINE_HEIGHT;
      GVT_FONT = GVTAttributedCharacterIterator.TextAttribute.GVT_FONT;
      szAtts = new HashSet();
      szAtts.add(TextAttribute.SIZE);
      szAtts.add(GVT_FONT);
      szAtts.add(LINE_HEIGHT);
   }
}
