package org.apache.fop.util;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.xmlgraphics.java2d.color.DeviceCMYKColorSpace;

public final class ColorUtil {
   public static final String CMYK_PSEUDO_PROFILE = "#CMYK";
   private static Map colorMap;
   protected static Log log;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   private ColorUtil() {
   }

   public static Color parseColorString(FOUserAgent foUserAgent, String value) throws PropertyException {
      if (value == null) {
         return null;
      } else {
         Color parsedColor = (Color)colorMap.get(value.toLowerCase());
         if (parsedColor == null) {
            if (value.startsWith("#")) {
               parsedColor = parseWithHash(value);
            } else if (value.startsWith("rgb(")) {
               parsedColor = parseAsRGB(value);
            } else {
               if (value.startsWith("url(")) {
                  throw new PropertyException("Colors starting with url( are not yet supported!");
               }

               if (value.startsWith("java.awt.Color")) {
                  parsedColor = parseAsJavaAWTColor(value);
               } else if (value.startsWith("system-color(")) {
                  parsedColor = parseAsSystemColor(value);
               } else if (value.startsWith("fop-rgb-icc")) {
                  parsedColor = parseAsFopRgbIcc(foUserAgent, value);
               } else if (value.startsWith("cmyk")) {
                  parsedColor = parseAsCMYK(value);
               }
            }

            if (parsedColor == null) {
               throw new PropertyException("Unknown Color: " + value);
            }

            colorMap.put(value, parsedColor);
         }

         return parsedColor;
      }
   }

   private static Color parseAsSystemColor(String value) throws PropertyException {
      int poss = value.indexOf("(");
      int pose = value.indexOf(")");
      if (poss != -1 && pose != -1) {
         value = value.substring(poss + 1, pose);
         return (Color)colorMap.get(value);
      } else {
         throw new PropertyException("Unknown color format: " + value + ". Must be system-color(x)");
      }
   }

   private static Color parseAsJavaAWTColor(String value) throws PropertyException {
      float red = 0.0F;
      float green = 0.0F;
      float blue = 0.0F;
      int poss = value.indexOf("[");
      int pose = value.indexOf("]");

      try {
         if (poss != -1 && pose != -1) {
            value = value.substring(poss + 1, pose);
            String[] args = value.split(",");
            if (args.length != 3) {
               throw new PropertyException("Invalid number of arguments for a java.awt.Color: " + value);
            } else {
               red = Float.parseFloat(args[0].trim().substring(2)) / 255.0F;
               green = Float.parseFloat(args[1].trim().substring(2)) / 255.0F;
               blue = Float.parseFloat(args[2].trim().substring(2)) / 255.0F;
               if (!((double)red < 0.0) && !((double)red > 1.0) && !((double)green < 0.0) && !((double)green > 1.0) && !((double)blue < 0.0) && !((double)blue > 1.0)) {
                  return new Color(red, green, blue);
               } else {
                  throw new PropertyException("Color values out of range");
               }
            }
         } else {
            throw new IllegalArgumentException("Invalid format for a java.awt.Color: " + value);
         }
      } catch (PropertyException var7) {
         throw var7;
      } catch (Exception var8) {
         throw new PropertyException(var8);
      }
   }

   private static Color parseAsRGB(String value) throws PropertyException {
      int poss = value.indexOf("(");
      int pose = value.indexOf(")");
      if (poss != -1 && pose != -1) {
         value = value.substring(poss + 1, pose);

         try {
            String[] args = value.split(",");
            if (args.length != 3) {
               throw new PropertyException("Invalid number of arguments: rgb(" + value + ")");
            } else {
               float red = 0.0F;
               float green = 0.0F;
               float blue = 0.0F;
               String str = args[0].trim();
               if (str.endsWith("%")) {
                  red = Float.parseFloat(str.substring(0, str.length() - 1)) / 100.0F;
               } else {
                  red = Float.parseFloat(str) / 255.0F;
               }

               str = args[1].trim();
               if (str.endsWith("%")) {
                  green = Float.parseFloat(str.substring(0, str.length() - 1)) / 100.0F;
               } else {
                  green = Float.parseFloat(str) / 255.0F;
               }

               str = args[2].trim();
               if (str.endsWith("%")) {
                  blue = Float.parseFloat(str.substring(0, str.length() - 1)) / 100.0F;
               } else {
                  blue = Float.parseFloat(str) / 255.0F;
               }

               if (!((double)red < 0.0) && !((double)red > 1.0) && !((double)green < 0.0) && !((double)green > 1.0) && !((double)blue < 0.0) && !((double)blue > 1.0)) {
                  Color parsedColor = new Color(red, green, blue);
                  return parsedColor;
               } else {
                  throw new PropertyException("Color values out of range");
               }
            }
         } catch (PropertyException var9) {
            throw var9;
         } catch (Exception var10) {
            throw new PropertyException(var10);
         }
      } else {
         throw new PropertyException("Unknown color format: " + value + ". Must be rgb(r,g,b)");
      }
   }

   private static Color parseWithHash(String value) throws PropertyException {
      Color parsedColor = null;

      try {
         int len = value.length();
         int alpha;
         if (len != 5 && len != 9) {
            alpha = 255;
         } else {
            alpha = Integer.parseInt(value.substring(len == 5 ? 3 : 7), 16);
         }

         int red = false;
         int green = false;
         int blue = false;
         int red;
         int green;
         int blue;
         if (len != 4 && len != 5) {
            if (len != 7 && len != 9) {
               throw new NumberFormatException();
            }

            red = Integer.parseInt(value.substring(1, 3), 16);
            green = Integer.parseInt(value.substring(3, 5), 16);
            blue = Integer.parseInt(value.substring(5, 7), 16);
         } else {
            red = Integer.parseInt(value.substring(1, 2), 16) * 17;
            green = Integer.parseInt(value.substring(2, 3), 16) * 17;
            blue = Integer.parseInt(value.substring(3, 4), 16) * 17;
         }

         parsedColor = new Color(red, green, blue, alpha);
         return parsedColor;
      } catch (Exception var7) {
         throw new PropertyException("Unknown color format: " + value + ". Must be #RGB. #RGBA, #RRGGBB, or #RRGGBBAA");
      }
   }

   private static Color parseAsFopRgbIcc(FOUserAgent foUserAgent, String value) throws PropertyException {
      int poss = value.indexOf("(");
      int pose = value.indexOf(")");
      if (poss != -1 && pose != -1) {
         String[] args = value.substring(poss + 1, pose).split(",");

         try {
            if (args.length < 5) {
               throw new PropertyException("Too few arguments for rgb-icc() function");
            } else {
               String iccProfileName = args[3].trim();
               if (iccProfileName != null && !"".equals(iccProfileName)) {
                  ColorSpace colorSpace = null;
                  String iccProfileSrc = null;
                  if (isPseudoProfile(iccProfileName)) {
                     if ("#CMYK".equalsIgnoreCase(iccProfileName)) {
                        colorSpace = DeviceCMYKColorSpace.getInstance();
                     } else if (!$assertionsDisabled) {
                        throw new AssertionError("Incomplete implementation");
                     }
                  } else {
                     iccProfileSrc = args[4].trim();
                     if (iccProfileSrc == null || "".equals(iccProfileSrc)) {
                        throw new PropertyException("ICC profile source missing");
                     }

                     if (iccProfileSrc.startsWith("\"") || iccProfileSrc.startsWith("'")) {
                        iccProfileSrc = iccProfileSrc.substring(1);
                     }

                     if (iccProfileSrc.endsWith("\"") || iccProfileSrc.endsWith("'")) {
                        iccProfileSrc = iccProfileSrc.substring(0, iccProfileSrc.length() - 1);
                     }
                  }

                  float[] iccComponents = new float[args.length - 5];
                  int ix = 4;

                  while(true) {
                     ++ix;
                     if (ix >= args.length) {
                        float red = 0.0F;
                        float green = 0.0F;
                        float blue = 0.0F;
                        red = Float.parseFloat(args[0].trim());
                        green = Float.parseFloat(args[1].trim());
                        blue = Float.parseFloat(args[2].trim());
                        if (!(red < 0.0F) && !(red > 1.0F) && !(green < 0.0F) && !(green > 1.0F) && !(blue < 0.0F) && !(blue > 1.0F)) {
                           if (foUserAgent != null && iccProfileSrc != null) {
                              colorSpace = foUserAgent.getFactory().getColorSpace(foUserAgent.getBaseURL(), iccProfileSrc);
                           }

                           Object parsedColor;
                           if (colorSpace != null) {
                              parsedColor = ColorExt.createFromFoRgbIcc(red, green, blue, iccProfileName, iccProfileSrc, (ColorSpace)colorSpace, iccComponents);
                           } else {
                              log.warn("Color profile '" + iccProfileSrc + "' not found. Using rgb replacement values.");
                              parsedColor = new Color(Math.round(red * 255.0F), Math.round(green * 255.0F), Math.round(blue * 255.0F));
                           }

                           return (Color)parsedColor;
                        }

                        throw new PropertyException("Color values out of range. Fallback RGB arguments to fop-rgb-icc() must be [0..1]");
                     }

                     iccComponents[ix - 5] = Float.parseFloat(args[ix].trim());
                  }
               } else {
                  throw new PropertyException("ICC profile name missing");
               }
            }
         } catch (PropertyException var13) {
            throw var13;
         } catch (Exception var14) {
            throw new PropertyException(var14);
         }
      } else {
         throw new PropertyException("Unknown color format: " + value + ". Must be fop-rgb-icc(r,g,b,NCNAME,src,....)");
      }
   }

   private static Color parseAsCMYK(String value) throws PropertyException {
      int poss = value.indexOf("(");
      int pose = value.indexOf(")");
      if (poss != -1 && pose != -1) {
         value = value.substring(poss + 1, pose);
         String[] args = value.split(",");

         try {
            if (args.length != 4) {
               throw new PropertyException("Invalid number of arguments: cmyk(" + value + ")");
            } else {
               float cyan = 0.0F;
               float magenta = 0.0F;
               float yellow = 0.0F;
               float black = 0.0F;
               String str = args[0].trim();
               if (str.endsWith("%")) {
                  cyan = Float.parseFloat(str.substring(0, str.length() - 1)) / 100.0F;
               } else {
                  cyan = Float.parseFloat(str);
               }

               str = args[1].trim();
               if (str.endsWith("%")) {
                  magenta = Float.parseFloat(str.substring(0, str.length() - 1)) / 100.0F;
               } else {
                  magenta = Float.parseFloat(str);
               }

               str = args[2].trim();
               if (str.endsWith("%")) {
                  yellow = Float.parseFloat(str.substring(0, str.length() - 1)) / 100.0F;
               } else {
                  yellow = Float.parseFloat(str);
               }

               str = args[3].trim();
               if (str.endsWith("%")) {
                  black = Float.parseFloat(str.substring(0, str.length() - 1)) / 100.0F;
               } else {
                  black = Float.parseFloat(str);
               }

               if (!((double)cyan < 0.0) && !((double)cyan > 1.0) && !((double)magenta < 0.0) && !((double)magenta > 1.0) && !((double)yellow < 0.0) && !((double)yellow > 1.0) && !((double)black < 0.0) && !((double)black > 1.0)) {
                  float[] cmyk = new float[]{cyan, magenta, yellow, black};
                  DeviceCMYKColorSpace cmykCs = DeviceCMYKColorSpace.getInstance();
                  float[] rgb = cmykCs.toRGB(cmyk);
                  Color parsedColor = ColorExt.createFromFoRgbIcc(rgb[0], rgb[1], rgb[2], "#CMYK", (String)null, cmykCs, cmyk);
                  return parsedColor;
               } else {
                  throw new PropertyException("Color values out of rangeArguments to cmyk() must be in the range [0%-100%] or [0.0-1.0]");
               }
            }
         } catch (PropertyException var13) {
            throw var13;
         } catch (Exception var14) {
            throw new PropertyException(var14);
         }
      } else {
         throw new PropertyException("Unknown color format: " + value + ". Must be cmyk(c,m,y,k)");
      }
   }

   public static String colorToString(Color color) {
      ColorSpace cs = color.getColorSpace();
      if (color instanceof ColorExt) {
         return ((ColorExt)color).toFunctionCall();
      } else {
         StringBuffer sbuf;
         if (cs != null && cs.getType() == 9) {
            sbuf = new StringBuffer(24);
            float[] cmyk = color.getColorComponents((float[])null);
            sbuf.append("cmyk(" + cmyk[0] + "," + cmyk[1] + "," + cmyk[2] + "," + cmyk[3] + ")");
            return sbuf.toString();
         } else {
            sbuf = new StringBuffer();
            sbuf.append('#');
            String s = Integer.toHexString(color.getRed());
            if (s.length() == 1) {
               sbuf.append('0');
            }

            sbuf.append(s);
            s = Integer.toHexString(color.getGreen());
            if (s.length() == 1) {
               sbuf.append('0');
            }

            sbuf.append(s);
            s = Integer.toHexString(color.getBlue());
            if (s.length() == 1) {
               sbuf.append('0');
            }

            sbuf.append(s);
            if (color.getAlpha() != 255) {
               s = Integer.toHexString(color.getAlpha());
               if (s.length() == 1) {
                  sbuf.append('0');
               }

               sbuf.append(s);
            }

            return sbuf.toString();
         }
      }
   }

   private static void initializeColorMap() {
      colorMap = Collections.synchronizedMap(new HashMap());
      colorMap.put("aliceblue", new Color(240, 248, 255));
      colorMap.put("antiquewhite", new Color(250, 235, 215));
      colorMap.put("aqua", new Color(0, 255, 255));
      colorMap.put("aquamarine", new Color(127, 255, 212));
      colorMap.put("azure", new Color(240, 255, 255));
      colorMap.put("beige", new Color(245, 245, 220));
      colorMap.put("bisque", new Color(255, 228, 196));
      colorMap.put("black", new Color(0, 0, 0));
      colorMap.put("blanchedalmond", new Color(255, 235, 205));
      colorMap.put("blue", new Color(0, 0, 255));
      colorMap.put("blueviolet", new Color(138, 43, 226));
      colorMap.put("brown", new Color(165, 42, 42));
      colorMap.put("burlywood", new Color(222, 184, 135));
      colorMap.put("cadetblue", new Color(95, 158, 160));
      colorMap.put("chartreuse", new Color(127, 255, 0));
      colorMap.put("chocolate", new Color(210, 105, 30));
      colorMap.put("coral", new Color(255, 127, 80));
      colorMap.put("cornflowerblue", new Color(100, 149, 237));
      colorMap.put("cornsilk", new Color(255, 248, 220));
      colorMap.put("crimson", new Color(220, 20, 60));
      colorMap.put("cyan", new Color(0, 255, 255));
      colorMap.put("darkblue", new Color(0, 0, 139));
      colorMap.put("darkcyan", new Color(0, 139, 139));
      colorMap.put("darkgoldenrod", new Color(184, 134, 11));
      colorMap.put("darkgray", new Color(169, 169, 169));
      colorMap.put("darkgreen", new Color(0, 100, 0));
      colorMap.put("darkgrey", new Color(169, 169, 169));
      colorMap.put("darkkhaki", new Color(189, 183, 107));
      colorMap.put("darkmagenta", new Color(139, 0, 139));
      colorMap.put("darkolivegreen", new Color(85, 107, 47));
      colorMap.put("darkorange", new Color(255, 140, 0));
      colorMap.put("darkorchid", new Color(153, 50, 204));
      colorMap.put("darkred", new Color(139, 0, 0));
      colorMap.put("darksalmon", new Color(233, 150, 122));
      colorMap.put("darkseagreen", new Color(143, 188, 143));
      colorMap.put("darkslateblue", new Color(72, 61, 139));
      colorMap.put("darkslategray", new Color(47, 79, 79));
      colorMap.put("darkslategrey", new Color(47, 79, 79));
      colorMap.put("darkturquoise", new Color(0, 206, 209));
      colorMap.put("darkviolet", new Color(148, 0, 211));
      colorMap.put("deeppink", new Color(255, 20, 147));
      colorMap.put("deepskyblue", new Color(0, 191, 255));
      colorMap.put("dimgray", new Color(105, 105, 105));
      colorMap.put("dimgrey", new Color(105, 105, 105));
      colorMap.put("dodgerblue", new Color(30, 144, 255));
      colorMap.put("firebrick", new Color(178, 34, 34));
      colorMap.put("floralwhite", new Color(255, 250, 240));
      colorMap.put("forestgreen", new Color(34, 139, 34));
      colorMap.put("fuchsia", new Color(255, 0, 255));
      colorMap.put("gainsboro", new Color(220, 220, 220));
      colorMap.put("ghostwhite", new Color(248, 248, 255));
      colorMap.put("gold", new Color(255, 215, 0));
      colorMap.put("goldenrod", new Color(218, 165, 32));
      colorMap.put("gray", new Color(128, 128, 128));
      colorMap.put("green", new Color(0, 128, 0));
      colorMap.put("greenyellow", new Color(173, 255, 47));
      colorMap.put("grey", new Color(128, 128, 128));
      colorMap.put("honeydew", new Color(240, 255, 240));
      colorMap.put("hotpink", new Color(255, 105, 180));
      colorMap.put("indianred", new Color(205, 92, 92));
      colorMap.put("indigo", new Color(75, 0, 130));
      colorMap.put("ivory", new Color(255, 255, 240));
      colorMap.put("khaki", new Color(240, 230, 140));
      colorMap.put("lavender", new Color(230, 230, 250));
      colorMap.put("lavenderblush", new Color(255, 240, 245));
      colorMap.put("lawngreen", new Color(124, 252, 0));
      colorMap.put("lemonchiffon", new Color(255, 250, 205));
      colorMap.put("lightblue", new Color(173, 216, 230));
      colorMap.put("lightcoral", new Color(240, 128, 128));
      colorMap.put("lightcyan", new Color(224, 255, 255));
      colorMap.put("lightgoldenrodyellow", new Color(250, 250, 210));
      colorMap.put("lightgray", new Color(211, 211, 211));
      colorMap.put("lightgreen", new Color(144, 238, 144));
      colorMap.put("lightgrey", new Color(211, 211, 211));
      colorMap.put("lightpink", new Color(255, 182, 193));
      colorMap.put("lightsalmon", new Color(255, 160, 122));
      colorMap.put("lightseagreen", new Color(32, 178, 170));
      colorMap.put("lightskyblue", new Color(135, 206, 250));
      colorMap.put("lightslategray", new Color(119, 136, 153));
      colorMap.put("lightslategrey", new Color(119, 136, 153));
      colorMap.put("lightsteelblue", new Color(176, 196, 222));
      colorMap.put("lightyellow", new Color(255, 255, 224));
      colorMap.put("lime", new Color(0, 255, 0));
      colorMap.put("limegreen", new Color(50, 205, 50));
      colorMap.put("linen", new Color(250, 240, 230));
      colorMap.put("magenta", new Color(255, 0, 255));
      colorMap.put("maroon", new Color(128, 0, 0));
      colorMap.put("mediumaquamarine", new Color(102, 205, 170));
      colorMap.put("mediumblue", new Color(0, 0, 205));
      colorMap.put("mediumorchid", new Color(186, 85, 211));
      colorMap.put("mediumpurple", new Color(147, 112, 219));
      colorMap.put("mediumseagreen", new Color(60, 179, 113));
      colorMap.put("mediumslateblue", new Color(123, 104, 238));
      colorMap.put("mediumspringgreen", new Color(0, 250, 154));
      colorMap.put("mediumturquoise", new Color(72, 209, 204));
      colorMap.put("mediumvioletred", new Color(199, 21, 133));
      colorMap.put("midnightblue", new Color(25, 25, 112));
      colorMap.put("mintcream", new Color(245, 255, 250));
      colorMap.put("mistyrose", new Color(255, 228, 225));
      colorMap.put("moccasin", new Color(255, 228, 181));
      colorMap.put("navajowhite", new Color(255, 222, 173));
      colorMap.put("navy", new Color(0, 0, 128));
      colorMap.put("oldlace", new Color(253, 245, 230));
      colorMap.put("olive", new Color(128, 128, 0));
      colorMap.put("olivedrab", new Color(107, 142, 35));
      colorMap.put("orange", new Color(255, 165, 0));
      colorMap.put("orangered", new Color(255, 69, 0));
      colorMap.put("orchid", new Color(218, 112, 214));
      colorMap.put("palegoldenrod", new Color(238, 232, 170));
      colorMap.put("palegreen", new Color(152, 251, 152));
      colorMap.put("paleturquoise", new Color(175, 238, 238));
      colorMap.put("palevioletred", new Color(219, 112, 147));
      colorMap.put("papayawhip", new Color(255, 239, 213));
      colorMap.put("peachpuff", new Color(255, 218, 185));
      colorMap.put("peru", new Color(205, 133, 63));
      colorMap.put("pink", new Color(255, 192, 203));
      colorMap.put("plum ", new Color(221, 160, 221));
      colorMap.put("plum", new Color(221, 160, 221));
      colorMap.put("powderblue", new Color(176, 224, 230));
      colorMap.put("purple", new Color(128, 0, 128));
      colorMap.put("red", new Color(255, 0, 0));
      colorMap.put("rosybrown", new Color(188, 143, 143));
      colorMap.put("royalblue", new Color(65, 105, 225));
      colorMap.put("saddlebrown", new Color(139, 69, 19));
      colorMap.put("salmon", new Color(250, 128, 114));
      colorMap.put("sandybrown", new Color(244, 164, 96));
      colorMap.put("seagreen", new Color(46, 139, 87));
      colorMap.put("seashell", new Color(255, 245, 238));
      colorMap.put("sienna", new Color(160, 82, 45));
      colorMap.put("silver", new Color(192, 192, 192));
      colorMap.put("skyblue", new Color(135, 206, 235));
      colorMap.put("slateblue", new Color(106, 90, 205));
      colorMap.put("slategray", new Color(112, 128, 144));
      colorMap.put("slategrey", new Color(112, 128, 144));
      colorMap.put("snow", new Color(255, 250, 250));
      colorMap.put("springgreen", new Color(0, 255, 127));
      colorMap.put("steelblue", new Color(70, 130, 180));
      colorMap.put("tan", new Color(210, 180, 140));
      colorMap.put("teal", new Color(0, 128, 128));
      colorMap.put("thistle", new Color(216, 191, 216));
      colorMap.put("tomato", new Color(255, 99, 71));
      colorMap.put("turquoise", new Color(64, 224, 208));
      colorMap.put("violet", new Color(238, 130, 238));
      colorMap.put("wheat", new Color(245, 222, 179));
      colorMap.put("white", new Color(255, 255, 255));
      colorMap.put("whitesmoke", new Color(245, 245, 245));
      colorMap.put("yellow", new Color(255, 255, 0));
      colorMap.put("yellowgreen", new Color(154, 205, 50));
      colorMap.put("transparent", new Color(0, 0, 0, 0));
   }

   public static Color lightenColor(Color col, float factor) {
      return org.apache.xmlgraphics.java2d.color.ColorUtil.lightenColor(col, factor);
   }

   public static boolean isPseudoProfile(String colorProfileName) {
      return "#CMYK".equalsIgnoreCase(colorProfileName);
   }

   public static boolean isGray(Color col) {
      return org.apache.xmlgraphics.java2d.color.ColorUtil.isGray(col);
   }

   public static Color toCMYKGrayColor(float black) {
      float[] cmyk = new float[]{0.0F, 0.0F, 0.0F, 1.0F - black};
      DeviceCMYKColorSpace cmykCs = DeviceCMYKColorSpace.getInstance();
      float[] rgb = cmykCs.toRGB(cmyk);
      return ColorExt.createFromFoRgbIcc(rgb[0], rgb[1], rgb[2], "#CMYK", (String)null, cmykCs, cmyk);
   }

   static {
      $assertionsDisabled = !ColorUtil.class.desiredAssertionStatus();
      colorMap = null;
      log = LogFactory.getLog(ColorUtil.class);
      initializeColorMap();
   }
}
