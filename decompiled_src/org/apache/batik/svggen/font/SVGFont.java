package org.apache.batik.svggen.font;

import java.io.FileOutputStream;
import java.io.PrintStream;
import org.apache.batik.svggen.font.table.CmapFormat;
import org.apache.batik.svggen.font.table.Feature;
import org.apache.batik.svggen.font.table.FeatureTags;
import org.apache.batik.svggen.font.table.GsubTable;
import org.apache.batik.svggen.font.table.KernSubtable;
import org.apache.batik.svggen.font.table.KernTable;
import org.apache.batik.svggen.font.table.KerningPair;
import org.apache.batik.svggen.font.table.LangSys;
import org.apache.batik.svggen.font.table.PostTable;
import org.apache.batik.svggen.font.table.Script;
import org.apache.batik.svggen.font.table.ScriptTags;
import org.apache.batik.svggen.font.table.SingleSubst;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLConstants;

public class SVGFont implements XMLConstants, SVGConstants, ScriptTags, FeatureTags {
   static final String EOL;
   static final String PROPERTY_LINE_SEPARATOR = "line.separator";
   static final String PROPERTY_LINE_SEPARATOR_DEFAULT = "\n";
   static final int DEFAULT_FIRST = 32;
   static final int DEFAULT_LAST = 128;
   private static String QUOT_EOL;
   private static String CONFIG_USAGE;
   private static String CONFIG_SVG_BEGIN;
   private static String CONFIG_SVG_TEST_CARD_START;
   private static String CONFIG_SVG_TEST_CARD_END;
   public static final char ARG_KEY_START_CHAR = '-';
   public static final String ARG_KEY_CHAR_RANGE_LOW = "-l";
   public static final String ARG_KEY_CHAR_RANGE_HIGH = "-h";
   public static final String ARG_KEY_ID = "-id";
   public static final String ARG_KEY_ASCII = "-ascii";
   public static final String ARG_KEY_TESTCARD = "-testcard";
   public static final String ARG_KEY_AUTO_RANGE = "-autorange";
   public static final String ARG_KEY_OUTPUT_PATH = "-o";

   protected static String encodeEntities(String var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         if (var0.charAt(var2) == '<') {
            var1.append("&lt;");
         } else if (var0.charAt(var2) == '>') {
            var1.append("&gt;");
         } else if (var0.charAt(var2) == '&') {
            var1.append("&amp;");
         } else if (var0.charAt(var2) == '\'') {
            var1.append("&apos;");
         } else if (var0.charAt(var2) == '"') {
            var1.append("&quot;");
         } else {
            var1.append(var0.charAt(var2));
         }
      }

      return var1.toString();
   }

   protected static String getContourAsSVGPathData(Glyph var0, int var1, int var2) {
      if (var0.getPoint(var1).endOfContour) {
         return "";
      } else {
         StringBuffer var3 = new StringBuffer();
         int var4 = 0;

         while(var4 < var2) {
            Point var5 = var0.getPoint(var1 + var4 % var2);
            Point var6 = var0.getPoint(var1 + (var4 + 1) % var2);
            Point var7 = var0.getPoint(var1 + (var4 + 2) % var2);
            if (var4 == 0) {
               var3.append("M").append(String.valueOf(var5.x)).append(" ").append(String.valueOf(var5.y));
            }

            if (var5.onCurve && var6.onCurve) {
               if (var6.x == var5.x) {
                  var3.append("V").append(String.valueOf(var6.y));
               } else if (var6.y == var5.y) {
                  var3.append("H").append(String.valueOf(var6.x));
               } else {
                  var3.append("L").append(String.valueOf(var6.x)).append(" ").append(String.valueOf(var6.y));
               }

               ++var4;
            } else if (var5.onCurve && !var6.onCurve && var7.onCurve) {
               var3.append("Q").append(String.valueOf(var6.x)).append(" ").append(String.valueOf(var6.y)).append(" ").append(String.valueOf(var7.x)).append(" ").append(String.valueOf(var7.y));
               var4 += 2;
            } else if (var5.onCurve && !var6.onCurve && !var7.onCurve) {
               var3.append("Q").append(String.valueOf(var6.x)).append(" ").append(String.valueOf(var6.y)).append(" ").append(String.valueOf(midValue(var6.x, var7.x))).append(" ").append(String.valueOf(midValue(var6.y, var7.y)));
               var4 += 2;
            } else if (!var5.onCurve && !var6.onCurve) {
               var3.append("T").append(String.valueOf(midValue(var5.x, var6.x))).append(" ").append(String.valueOf(midValue(var5.y, var6.y)));
               ++var4;
            } else {
               if (var5.onCurve || !var6.onCurve) {
                  System.out.println("drawGlyph case not catered for!!");
                  break;
               }

               var3.append("T").append(String.valueOf(var6.x)).append(" ").append(String.valueOf(var6.y));
               ++var4;
            }
         }

         var3.append("Z");
         return var3.toString();
      }
   }

   protected static String getSVGFontFaceElement(Font var0) {
      StringBuffer var1 = new StringBuffer();
      String var2 = var0.getNameTable().getRecord((short)1);
      short var3 = var0.getHeadTable().getUnitsPerEm();
      String var4 = var0.getOS2Table().getPanose().toString();
      short var5 = var0.getHheaTable().getAscender();
      short var6 = var0.getHheaTable().getDescender();
      byte var7 = 0;
      var1.append("<").append("font-face").append(EOL).append("    ").append("font-family").append("=\"").append(var2).append(QUOT_EOL).append("    ").append("units-per-em").append("=\"").append(var3).append(QUOT_EOL).append("    ").append("panose-1").append("=\"").append(var4).append(QUOT_EOL).append("    ").append("ascent").append("=\"").append(var5).append(QUOT_EOL).append("    ").append("descent").append("=\"").append(var6).append(QUOT_EOL).append("    ").append("alphabetic").append("=\"").append(var7).append('"').append(" />").append(EOL);
      return var1.toString();
   }

   protected static void writeFontAsSVGFragment(PrintStream var0, Font var1, String var2, int var3, int var4, boolean var5, boolean var6) throws Exception {
      short var7 = var1.getOS2Table().getAvgCharWidth();
      var0.print("<");
      var0.print("font");
      var0.print(" ");
      if (var2 != null) {
         var0.print("id");
         var0.print("=\"");
         var0.print(var2);
         var0.print('"');
         var0.print(" ");
      }

      var0.print("horiz-adv-x");
      var0.print("=\"");
      var0.print(var7);
      var0.print('"');
      var0.print(" >");
      var0.print(getSVGFontFaceElement(var1));
      CmapFormat var8 = null;
      if (var6) {
         var8 = var1.getCmapTable().getCmapFormat((short)1, (short)0);
      } else {
         var8 = var1.getCmapTable().getCmapFormat((short)3, (short)1);
         if (var8 == null) {
            var8 = var1.getCmapTable().getCmapFormat((short)3, (short)0);
         }
      }

      if (var8 == null) {
         throw new Exception("Cannot find a suitable cmap table");
      } else {
         GsubTable var9 = (GsubTable)var1.getTable(1196643650);
         SingleSubst var10 = null;
         SingleSubst var11 = null;
         SingleSubst var12 = null;
         if (var9 != null) {
            Script var13 = var9.getScriptList().findScript("arab");
            if (var13 != null) {
               LangSys var14 = var13.getDefaultLangSys();
               if (var14 != null) {
                  Feature var15 = var9.getFeatureList().findFeature(var14, "init");
                  Feature var16 = var9.getFeatureList().findFeature(var14, "medi");
                  Feature var17 = var9.getFeatureList().findFeature(var14, "fina");
                  var10 = (SingleSubst)var9.getLookupList().getLookup(var15, 0).getSubtable(0);
                  var11 = (SingleSubst)var9.getLookupList().getLookup(var16, 0).getSubtable(0);
                  var12 = (SingleSubst)var9.getLookupList().getLookup(var17, 0).getSubtable(0);
               }
            }
         }

         var0.println(getGlyphAsSVG(var1, var1.getGlyph(0), 0, var7, var10, var11, var12, ""));

         try {
            if (var3 == -1) {
               if (!var5) {
                  var3 = 32;
               } else {
                  var3 = var8.getFirst();
               }
            }

            if (var4 == -1) {
               if (!var5) {
                  var4 = 128;
               } else {
                  var4 = var8.getLast();
               }
            }

            for(int var19 = var3; var19 <= var4; ++var19) {
               int var21 = var8.mapCharCode(var19);
               if (var21 > 0) {
                  var0.println(getGlyphAsSVG(var1, var1.getGlyph(var21), var21, var7, var10, var11, var12, 32 <= var19 && var19 <= 127 ? encodeEntities(String.valueOf((char)var19)) : "&#x" + Integer.toHexString(var19) + ";"));
               }
            }

            KernTable var20 = (KernTable)var1.getTable(1801810542);
            if (var20 != null) {
               KernSubtable var22 = var20.getSubtable(0);
               PostTable var23 = (PostTable)var1.getTable(1886352244);

               for(int var24 = 0; var24 < var22.getKerningPairCount(); ++var24) {
                  var0.println(getKerningPairAsSVG(var22.getKerningPair(var24), var23));
               }
            }
         } catch (Exception var18) {
            System.err.println(var18.getMessage());
         }

         var0.print("</");
         var0.print("font");
         var0.println(">");
      }
   }

   protected static String getGlyphAsSVG(Font var0, Glyph var1, int var2, int var3, String var4, String var5) {
      StringBuffer var6 = new StringBuffer();
      int var7 = 0;
      int var8 = 0;
      int var10 = var0.getHmtxTable().getAdvanceWidth(var2);
      if (var2 == 0) {
         var6.append("<");
         var6.append("missing-glyph");
      } else {
         var6.append("<").append("glyph").append(" ").append("unicode").append("=\"").append(var5).append('"');
         var6.append(" ").append("glyph-name").append("=\"").append(var0.getPostTable().getGlyphName(var2)).append('"');
      }

      if (var10 != var3) {
         var6.append(" ").append("horiz-adv-x").append("=\"").append(var10).append('"');
      }

      if (var4 != null) {
         var6.append(var4);
      }

      if (var1 != null) {
         var6.append(" ").append("d").append("=\"");

         for(int var9 = 0; var9 < var1.getPointCount(); ++var9) {
            ++var8;
            if (var1.getPoint(var9).endOfContour) {
               var6.append(getContourAsSVGPathData(var1, var7, var8));
               var7 = var9 + 1;
               var8 = 0;
            }
         }

         var6.append('"');
      }

      var6.append(" />");
      chopUpStringBuffer(var6);
      return var6.toString();
   }

   protected static String getGlyphAsSVG(Font var0, Glyph var1, int var2, int var3, SingleSubst var4, SingleSubst var5, SingleSubst var6, String var7) {
      StringBuffer var8 = new StringBuffer();
      boolean var9 = false;
      int var10 = var2;
      int var11 = var2;
      int var12 = var2;
      if (var4 != null) {
         var10 = var4.substitute(var2);
      }

      if (var5 != null) {
         var11 = var5.substitute(var2);
      }

      if (var6 != null) {
         var12 = var6.substitute(var2);
      }

      if (var10 != var2) {
         var8.append(getGlyphAsSVG(var0, var0.getGlyph(var10), var10, var3, " arabic-form=\"initial\"", var7));
         var8.append(EOL);
         var9 = true;
      }

      if (var11 != var2) {
         var8.append(getGlyphAsSVG(var0, var0.getGlyph(var11), var11, var3, " arabic-form=\"medial\"", var7));
         var8.append(EOL);
         var9 = true;
      }

      if (var12 != var2) {
         var8.append(getGlyphAsSVG(var0, var0.getGlyph(var12), var12, var3, " arabic-form=\"terminal\"", var7));
         var8.append(EOL);
         var9 = true;
      }

      if (var9) {
         var8.append(getGlyphAsSVG(var0, var1, var2, var3, " arabic-form=\"isolated\"", var7));
      } else {
         var8.append(getGlyphAsSVG(var0, var1, var2, var3, (String)null, var7));
      }

      return var8.toString();
   }

   protected static String getKerningPairAsSVG(KerningPair var0, PostTable var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("<").append("hkern").append(" ");
      var2.append("g1").append("=\"");
      var2.append(var1.getGlyphName(var0.getLeft()));
      var2.append('"').append(" ").append("g2").append("=\"");
      var2.append(var1.getGlyphName(var0.getRight()));
      var2.append('"').append(" ").append("k").append("=\"");
      var2.append(-var0.getValue());
      var2.append('"').append(" />");
      return var2.toString();
   }

   protected static void writeSvgBegin(PrintStream var0) {
      var0.println(Messages.formatMessage(CONFIG_SVG_BEGIN, new Object[]{"-//W3C//DTD SVG 1.0//EN", "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"}));
   }

   protected static void writeSvgDefsBegin(PrintStream var0) {
      var0.println("<defs >");
   }

   protected static void writeSvgDefsEnd(PrintStream var0) {
      var0.println("</defs>");
   }

   protected static void writeSvgEnd(PrintStream var0) {
      var0.println("</svg>");
   }

   protected static void writeSvgTestCard(PrintStream var0, String var1) {
      var0.println(Messages.formatMessage(CONFIG_SVG_TEST_CARD_START, (Object[])null));
      var0.println(var1);
      var0.println(Messages.formatMessage(CONFIG_SVG_TEST_CARD_END, (Object[])null));
   }

   public static void main(String[] var0) {
      try {
         String var1 = parseArgs(var0, (String)null);
         String var2 = parseArgs(var0, "-l");
         String var3 = parseArgs(var0, "-h");
         String var4 = parseArgs(var0, "-id");
         String var5 = parseArgs(var0, "-ascii");
         String var6 = parseArgs(var0, "-testcard");
         String var7 = parseArgs(var0, "-o");
         String var8 = parseArgs(var0, "-autorange");
         PrintStream var9 = null;
         FileOutputStream var10 = null;
         if (var7 != null) {
            var10 = new FileOutputStream(var7);
            var9 = new PrintStream(var10);
         } else {
            var9 = System.out;
         }

         if (var1 != null) {
            Font var11 = Font.create(var1);
            writeSvgBegin(var9);
            writeSvgDefsBegin(var9);
            writeFontAsSVGFragment(var9, var11, var4, var2 != null ? Integer.parseInt(var2) : -1, var3 != null ? Integer.parseInt(var3) : -1, var8 != null, var5 != null);
            writeSvgDefsEnd(var9);
            if (var6 != null) {
               String var12 = var11.getNameTable().getRecord((short)1);
               writeSvgTestCard(var9, var12);
            }

            writeSvgEnd(var9);
            if (var10 != null) {
               var10.close();
            }
         } else {
            usage();
         }
      } catch (Exception var13) {
         var13.printStackTrace();
         System.err.println(var13.getMessage());
         usage();
      }

   }

   private static void chopUpStringBuffer(StringBuffer var0) {
      if (var0.length() >= 256) {
         for(int var1 = 240; var1 < var0.length(); ++var1) {
            if (var0.charAt(var1) == ' ') {
               var0.setCharAt(var1, '\n');
               var1 += 240;
            }
         }

      }
   }

   private static int midValue(int var0, int var1) {
      return var0 + (var1 - var0) / 2;
   }

   private static String parseArgs(String[] var0, String var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         if (var1 == null) {
            if (var0[var2].charAt(0) != '-') {
               return var0[var2];
            }
         } else if (var1.equalsIgnoreCase(var0[var2])) {
            if (var2 < var0.length - 1 && var0[var2 + 1].charAt(0) != '-') {
               return var0[var2 + 1];
            }

            return var0[var2];
         }
      }

      return null;
   }

   private static void usage() {
      System.err.println(Messages.formatMessage(CONFIG_USAGE, (Object[])null));
   }

   static {
      String var0;
      try {
         var0 = System.getProperty("line.separator", "\n");
      } catch (SecurityException var2) {
         var0 = "\n";
      }

      EOL = var0;
      QUOT_EOL = '"' + EOL;
      CONFIG_USAGE = "SVGFont.config.usage";
      CONFIG_SVG_BEGIN = "SVGFont.config.svg.begin";
      CONFIG_SVG_TEST_CARD_START = "SVGFont.config.svg.test.card.start";
      CONFIG_SVG_TEST_CARD_END = "SVGFont.config.svg.test.card.end";
   }
}
