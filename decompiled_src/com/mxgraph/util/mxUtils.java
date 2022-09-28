package com.mxgraph.util;

import com.mxgraph.io.mxCodecRegistry;
import com.mxgraph.model.mxCellPath;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxCellState;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.text.html.HTMLDocument;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class mxUtils {
   protected static transient Graphics fontGraphics;

   public static mxRectangle getLabelSize(String var0, Map var1, boolean var2, int var3) {
      mxRectangle var4;
      if (var2) {
         var4 = getSizeForHtml(getBodyMarkup(var0, true), var1);
      } else {
         var4 = getSizeForString(var0, getFont(var1), var3);
      }

      return var4;
   }

   public static String getBodyMarkup(String var0, boolean var1) {
      String var2 = var0.toLowerCase();
      int var3 = var2.indexOf("<body>");
      if (var3 >= 0) {
         var3 += 7;
         int var4 = var2.lastIndexOf("</body>");
         if (var4 > var3) {
            var0 = var0.substring(var3, var4).trim();
         }
      }

      if (var1) {
         var0 = var0.replaceAll("\n", "<br>");
      }

      return var0;
   }

   public static mxRectangle getLabelPaintBounds(String var0, Map var1, boolean var2, mxPoint var3, mxRectangle var4, double var5) {
      boolean var7 = isTrue(var1, mxConstants.STYLE_HORIZONTAL, true);
      int var8 = 0;
      if (var4 != null && getString(var1, mxConstants.STYLE_WHITE_SPACE, "nowrap").equals("wrap")) {
         if (var7) {
            var8 = (int)(var4.getWidth() / var5 - (double)(2 * mxConstants.LABEL_INSET) - 2.0 * getDouble(var1, mxConstants.STYLE_SPACING) - getDouble(var1, mxConstants.STYLE_SPACING_LEFT) - getDouble(var1, mxConstants.STYLE_SPACING_RIGHT));
         } else {
            var8 = (int)(var4.getHeight() / var5 - (double)(2 * mxConstants.LABEL_INSET) - 2.0 * getDouble(var1, mxConstants.STYLE_SPACING) - getDouble(var1, mxConstants.STYLE_SPACING_TOP) + getDouble(var1, mxConstants.STYLE_SPACING_BOTTOM));
         }
      }

      mxRectangle var9 = getLabelSize(var0, var1, var2, var8);
      double var10 = var3.getX();
      double var12 = var3.getY();
      double var14 = 0.0;
      double var16 = 0.0;
      if (var4 != null) {
         var10 += var4.getX();
         var12 += var4.getY();
         if (getString(var1, mxConstants.STYLE_SHAPE, "").equals("swimlane")) {
            double var18 = getDouble(var1, mxConstants.STYLE_STARTSIZE, (double)mxConstants.DEFAULT_STARTSIZE) * var5;
            if (var7) {
               var14 += var4.getWidth();
               var16 += var18;
            } else {
               var14 += var18;
               var16 += var4.getHeight();
            }
         } else {
            var14 += var4.getWidth();
            var16 += var4.getHeight();
         }
      }

      return getScaledLabelBounds(var10, var12, var9, var14, var16, var1, var5);
   }

   public static mxRectangle getScaledLabelBounds(double var0, double var2, mxRectangle var4, double var5, double var7, Map var9, double var10) {
      double var12 = (double)mxConstants.LABEL_INSET * var10;
      double var14 = var4.getWidth() * var10 + 2.0 * var12;
      double var16 = var4.getHeight() * var10 + 2.0 * var12;
      boolean var18 = isTrue(var9, mxConstants.STYLE_HORIZONTAL, true);
      int var19 = (int)((double)getInt(var9, mxConstants.STYLE_SPACING) * var10);
      String var20 = getString(var9, mxConstants.STYLE_ALIGN, "center");
      String var21 = getString(var9, mxConstants.STYLE_VERTICAL_ALIGN, "middle");
      int var22 = (int)((double)getInt(var9, mxConstants.STYLE_SPACING_TOP) * var10);
      int var23 = (int)((double)getInt(var9, mxConstants.STYLE_SPACING_BOTTOM) * var10);
      int var24 = (int)((double)getInt(var9, mxConstants.STYLE_SPACING_LEFT) * var10);
      int var25 = (int)((double)getInt(var9, mxConstants.STYLE_SPACING_RIGHT) * var10);
      if (!var18) {
         int var26 = var22;
         var22 = var25;
         var25 = var23;
         var23 = var24;
         var24 = var26;
         double var27 = var14;
         var14 = var16;
         var16 = var27;
      }

      if ((!var18 || !var20.equals("center")) && (var18 || !var21.equals("middle"))) {
         if ((!var18 || !var20.equals("right")) && (var18 || !var21.equals("bottom"))) {
            var0 += (double)(var19 + var24);
         } else {
            var0 += var5 - var14 - (double)var19 - (double)var25;
         }
      } else {
         var0 += (var5 - var14) / 2.0 + (double)var24 - (double)var25;
      }

      if ((var18 || !var20.equals("center")) && (!var18 || !var21.equals("middle"))) {
         if ((var18 || !var20.equals("left")) && (!var18 || !var21.equals("bottom"))) {
            var2 += (double)(var19 + var22);
         } else {
            var2 += var7 - var16 - (double)var19 - (double)var23;
         }
      } else {
         var2 += (var7 - var16) / 2.0 + (double)var22 - (double)var23;
      }

      return new mxRectangle(var0, var2, var14, var16);
   }

   public static mxRectangle getSizeForString(String var0, Font var1, int var2) {
      FontRenderContext var3 = new FontRenderContext((AffineTransform)null, false, false);
      FontMetrics var4 = null;
      if (fontGraphics != null) {
         var4 = fontGraphics.getFontMetrics(var1);
      }

      double var5 = (double)mxConstants.LINESPACING;
      if (var4 != null) {
         var5 += (double)var4.getHeight();
      } else {
         var5 += (double)var1.getSize2D() * 1.27;
      }

      String[] var7;
      if (var2 > 0) {
         var7 = wordWrap(var0, var4, (int)((double)var2 * mxConstants.LABEL_SCALE_BUFFER));
      } else {
         var7 = var0.split("\n");
      }

      Rectangle2D var8 = null;

      for(int var9 = 0; var9 < var7.length; ++var9) {
         Rectangle2D var10 = var1.getStringBounds(var7[var9], var3);
         if (var8 == null) {
            var8 = var10;
         } else {
            var8.setFrame(0.0, 0.0, Math.max(var8.getWidth(), var10.getWidth()), var8.getHeight() + var5);
         }
      }

      return new mxRectangle(var8);
   }

   public static String[] wordWrap(String var0, FontMetrics var1, int var2) {
      ArrayList var3 = new ArrayList();
      String[] var4 = var0.split("\n");

      label81:
      for(int var5 = 0; var5 < var4.length; ++var5) {
         int var6 = 0;
         int var7 = 0;
         StringBuilder var8 = new StringBuilder();
         String[] var9 = var4[var5].split("\\s+");
         Stack var10 = new Stack();

         for(int var11 = var9.length - 1; var11 >= 0; --var11) {
            var10.push(var9[var11]);
         }

         while(true) {
            while(true) {
               while(!var10.isEmpty()) {
                  String var17 = (String)var10.pop();
                  int var12 = 0;
                  if (var17.length() > 0) {
                     char var13 = var17.charAt(0);
                     int var14 = var4[var5].indexOf(var13, var7);
                     String var15 = var4[var5].substring(var7, var14);
                     var12 = var15.length();
                     var17 = var15.concat(var17);
                  }

                  double var18;
                  if (var6 > 0) {
                     var18 = (double)var1.stringWidth(var17);
                  } else {
                     var18 = (double)var1.stringWidth(var17.trim());
                  }

                  if ((double)var6 + var18 > (double)var2) {
                     if (var6 > 0) {
                        var3.add(var8.toString());
                        var8 = new StringBuilder();
                        var10.push(var17.trim());
                        var6 = 0;
                     } else {
                        var17 = var17.trim();

                        for(int var19 = 1; var19 <= var17.length(); ++var19) {
                           var18 = (double)var1.stringWidth(var17.substring(0, var19));
                           if ((double)var6 + var18 > (double)var2) {
                              var19 = var19 > 1 ? var19 - 1 : var19;
                              String var16 = var17.substring(0, var19);
                              var8 = var8.append(var16);
                              var10.push(var17.substring(var19, var17.length()));
                              var3.add(var8.toString());
                              var8 = new StringBuilder();
                              var6 = 0;
                              var7 = var7 + var16.length() + var12;
                              break;
                           }
                        }
                     }
                  } else {
                     if (var6 > 0) {
                        var8 = var8.append(var17);
                     } else {
                        var8 = var8.append(var17.trim());
                     }

                     var6 = (int)((double)var6 + var18);
                     var7 += var17.length();
                  }
               }

               if (var8.length() > 0 || var3.isEmpty()) {
                  var3.add(var8.toString());
               }
               continue label81;
            }
         }
      }

      return (String[])var3.toArray(new String[var3.size()]);
   }

   public static mxRectangle getSizeForHtml(String var0, Map var1) {
      mxLightweightLabel var2 = mxLightweightLabel.getSharedInstance();
      if (var2 != null) {
         var2.setText(createHtmlDocument(var1, var0));
         Dimension var3 = var2.getPreferredSize();
         return new mxRectangle(0.0, 0.0, (double)var3.width, (double)var3.height);
      } else {
         return getSizeForString(var0, getFont(var1), 0);
      }
   }

   public static mxRectangle getBoundingBox(mxRectangle var0, double var1) {
      mxRectangle var3 = null;
      if (var0 != null && var1 != 0.0) {
         double var4 = Math.toRadians(var1);
         double var6 = Math.cos(var4);
         double var8 = Math.sin(var4);
         mxPoint var10 = new mxPoint(var0.getX() + var0.getWidth() / 2.0, var0.getY() + var0.getHeight() / 2.0);
         mxPoint var11 = new mxPoint(var0.getX(), var0.getY());
         mxPoint var12 = new mxPoint(var0.getX() + var0.getWidth(), var0.getY());
         mxPoint var13 = new mxPoint(var12.getX(), var0.getY() + var0.getHeight());
         mxPoint var14 = new mxPoint(var0.getX(), var13.getY());
         var11 = getRotatedPoint(var11, var6, var8, var10);
         var12 = getRotatedPoint(var12, var6, var8, var10);
         var13 = getRotatedPoint(var13, var6, var8, var10);
         var14 = getRotatedPoint(var14, var6, var8, var10);
         Rectangle var15 = new Rectangle((int)var11.getX(), (int)var11.getY(), 0, 0);
         var15.add(var12.getPoint());
         var15.add(var13.getPoint());
         var15.add(var14.getPoint());
         var3 = new mxRectangle(var15);
      } else if (var0 != null) {
         var3 = (mxRectangle)var0.clone();
      }

      return var3;
   }

   public static mxPoint getRotatedPoint(mxPoint var0, double var1, double var3) {
      return getRotatedPoint(var0, var1, var3, new mxPoint());
   }

   public static int findNearestSegment(mxCellState var0, double var1, double var3) {
      int var5 = -1;
      if (var0.getAbsolutePointCount() > 0) {
         mxPoint var6 = var0.getAbsolutePoint(0);
         double var7 = Double.MAX_VALUE;

         for(int var9 = 1; var9 < var0.getAbsolutePointCount(); ++var9) {
            mxPoint var10 = var0.getAbsolutePoint(var9);
            double var11 = (new Line2D.Double(var6.x, var6.y, var10.x, var10.y)).ptLineDist(var1, var3);
            if (var11 < var7) {
               var7 = var11;
               var5 = var9 - 1;
            }

            var6 = var10;
         }
      }

      return var5;
   }

   public static mxPoint getRotatedPoint(mxPoint var0, double var1, double var3, mxPoint var5) {
      double var6 = var0.getX() - var5.getX();
      double var8 = var0.getY() - var5.getY();
      double var10 = var6 * var1 - var8 * var3;
      double var12 = var8 * var1 + var6 * var3;
      return new mxPoint(var10 + var5.getX(), var12 + var5.getY());
   }

   public static void drawImageClip(Graphics var0, BufferedImage var1, ImageObserver var2) {
      Rectangle var3 = var0.getClipBounds();
      if (var3 != null) {
         int var4 = var1.getWidth();
         int var5 = var1.getHeight();
         int var6 = Math.max(0, Math.min(var3.x, var4));
         int var7 = Math.max(0, Math.min(var3.y, var5));
         var4 = Math.min(var3.width, var4 - var6);
         var5 = Math.min(var3.height, var5 - var7);
         if (var4 > 0 && var5 > 0) {
            var0.drawImage(var1.getSubimage(var6, var7, var4, var5), var3.x, var3.y, var2);
         }
      } else {
         var0.drawImage(var1, 0, 0, var2);
      }

   }

   public static void fillClippedRect(Graphics var0, int var1, int var2, int var3, int var4) {
      Rectangle var5 = new Rectangle(var1, var2, var3, var4);

      try {
         if (var0.getClipBounds() != null) {
            var5 = var5.intersection(var0.getClipBounds());
         }
      } catch (Exception var7) {
      }

      var0.fillRect(var5.x, var5.y, var5.width, var5.height);
   }

   public static List translatePoints(List var0, double var1, double var3) {
      ArrayList var5 = null;
      if (var0 != null) {
         var5 = new ArrayList(var0.size());
         Iterator var6 = var0.iterator();

         while(var6.hasNext()) {
            mxPoint var7 = (mxPoint)((mxPoint)var6.next()).clone();
            var7.setX(var7.getX() + var1);
            var7.setY(var7.getY() + var3);
            var5.add(var7);
         }
      }

      return var5;
   }

   public static mxPoint intersection(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      double var16 = (var14 - var10) * (var4 - var0) - (var12 - var8) * (var6 - var2);
      double var18 = (var12 - var8) * (var2 - var10) - (var14 - var10) * (var0 - var8);
      double var20 = (var4 - var0) * (var2 - var10) - (var6 - var2) * (var0 - var8);
      double var22 = var18 / var16;
      double var24 = var20 / var16;
      if (var22 >= 0.0 && var22 <= 1.0 && var24 >= 0.0 && var24 <= 1.0) {
         double var26 = var0 + var22 * (var4 - var0);
         double var28 = var2 + var22 * (var6 - var2);
         return new mxPoint(var26, var28);
      } else {
         return null;
      }
   }

   public static Object[] sortCells(Object[] var0, boolean var1) {
      return sortCells((Collection)Arrays.asList(var0), var1).toArray();
   }

   public static Collection sortCells(Collection var0, final boolean var1) {
      TreeSet var2 = new TreeSet(new Comparator() {
         public int compare(Object var1x, Object var2) {
            int var3 = mxCellPath.compare(mxCellPath.create((mxICell)var1x), mxCellPath.create((mxICell)var2));
            return var3 == 0 ? 0 : (var3 > 0 == var1 ? 1 : -1);
         }
      });
      var2.addAll(var0);
      return var2;
   }

   public static boolean contains(Object[] var0, Object var1) {
      return indexOf(var0, var1) >= 0;
   }

   public static int indexOf(Object[] var0, Object var1) {
      if (var1 != null && var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] == var1) {
               return var2;
            }
         }
      }

      return -1;
   }

   public static String getStylename(String var0) {
      if (var0 != null) {
         String[] var1 = var0.split(";");
         String var2 = var1[0];
         if (var2.indexOf("=") < 0) {
            return var2;
         }
      }

      return "";
   }

   public static String[] getStylenames(String var0) {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         String[] var2 = var0.split(";");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].indexOf("=") < 0) {
               var1.add(var2[var3]);
            }
         }
      }

      return (String[])((String[])var1.toArray(new String[var1.size()]));
   }

   public static int indexOfStylename(String var0, String var1) {
      if (var0 != null && var1 != null) {
         String[] var2 = var0.split(";");
         int var3 = 0;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               return var3;
            }

            var3 += var2[var4].length() + 1;
         }
      }

      return -1;
   }

   public String addStylename(String var1, String var2) {
      if (indexOfStylename(var1, var2) < 0) {
         if (var1 == null) {
            var1 = "";
         } else if (var1.length() > 0 && var1.charAt(var1.length() - 1) != ';') {
            var1 = var1 + ';';
         }

         var1 = var1 + var2;
      }

      return var1;
   }

   public String removeStylename(String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      if (var1 != null) {
         String[] var4 = var1.split(";");

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (!var4[var5].equals(var2)) {
               var3.append(var4[var5] + ";");
            }
         }
      }

      return var3.length() > 1 ? var3.substring(0, var3.length() - 1) : var3.toString();
   }

   public static String removeAllStylenames(String var0) {
      StringBuffer var1 = new StringBuffer();
      if (var0 != null) {
         String[] var2 = var0.split(";");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].indexOf(61) >= 0) {
               var1.append(var2[var3] + ";");
            }
         }
      }

      return var1.length() > 1 ? var1.substring(0, var1.length() - 1) : var1.toString();
   }

   public static void setCellStyles(mxIGraphModel var0, Object[] var1, String var2, String var3) {
      if (var1 != null && var1.length > 0) {
         var0.beginUpdate();

         try {
            for(int var4 = 0; var4 < var1.length; ++var4) {
               if (var1[var4] != null) {
                  String var5 = setStyle(var0.getStyle(var1[var4]), var2, var3);
                  var0.setStyle(var1[var4], var5);
               }
            }
         } finally {
            var0.endUpdate();
         }
      }

   }

   public static String setStyle(String var0, String var1, String var2) {
      boolean var3 = var2 != null && var2.length() > 0;
      if (var0 != null && var0.length() != 0) {
         int var4 = var0.indexOf(var1 + "=");
         String var5;
         if (var4 < 0) {
            if (var3) {
               var5 = var0.endsWith(";") ? "" : ";";
               var0 = var0 + var5 + var1 + '=' + var2;
            }
         } else {
            var5 = var3 ? var1 + "=" + var2 : "";
            int var6 = var0.indexOf(";", var4);
            if (!var3) {
               ++var6;
            }

            var0 = var0.substring(0, var4) + var5 + (var6 > var4 ? var0.substring(var6) : "");
         }
      } else if (var3) {
         var0 = var1 + "=" + var2;
      }

      return var0;
   }

   public static void setCellStyleFlags(mxIGraphModel var0, Object[] var1, String var2, int var3, Boolean var4) {
      if (var1 != null && var1.length > 0) {
         var0.beginUpdate();

         try {
            for(int var5 = 0; var5 < var1.length; ++var5) {
               if (var1[var5] != null) {
                  String var6 = setStyleFlag(var0.getStyle(var1[var5]), var2, var3, var4);
                  var0.setStyle(var1[var5], var6);
               }
            }
         } finally {
            var0.endUpdate();
         }
      }

   }

   public static String setStyleFlag(String var0, String var1, int var2, Boolean var3) {
      if (var0 != null && var0.length() != 0) {
         int var4 = var0.indexOf(var1 + "=");
         if (var4 < 0) {
            String var5 = var0.endsWith(";") ? "" : ";";
            if (var3 != null && !var3) {
               var0 = var0 + var5 + var1 + "=0";
            } else {
               var0 = var0 + var5 + var1 + "=" + var2;
            }
         } else {
            int var8 = var0.indexOf(";", var4);
            String var6 = "";
            boolean var7 = false;
            if (var8 < 0) {
               var6 = var0.substring(var4 + var1.length() + 1);
            } else {
               var6 = var0.substring(var4 + var1.length() + 1, var8);
            }

            int var9;
            if (var3 == null) {
               var9 = Integer.parseInt(var6) ^ var2;
            } else if (var3) {
               var9 = Integer.parseInt(var6) | var2;
            } else {
               var9 = Integer.parseInt(var6) & ~var2;
            }

            var0 = var0.substring(0, var4) + var1 + "=" + var9 + (var8 >= 0 ? var0.substring(var8) : "");
         }
      } else if (var3 != null && !var3) {
         var0 = var1 + "=0";
      } else {
         var0 = var1 + "=" + var2;
      }

      return var0;
   }

   public static boolean intersectsHotspot(mxCellState var0, int var1, int var2, double var3) {
      return intersectsHotspot(var0, var1, var2, var3, 0, 0);
   }

   public static boolean intersectsHotspot(mxCellState var0, int var1, int var2, double var3, int var5, int var6) {
      if (var3 > 0.0) {
         int var7 = (int)Math.round(var0.getCenterX());
         int var8 = (int)Math.round(var0.getCenterY());
         int var9 = (int)Math.round(var0.getWidth());
         int var10 = (int)Math.round(var0.getHeight());
         int var11;
         if (getString(var0.getStyle(), mxConstants.STYLE_SHAPE, "").equals("swimlane")) {
            var11 = getInt(var0.getStyle(), mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_STARTSIZE);
            if (isTrue(var0.getStyle(), mxConstants.STYLE_HORIZONTAL, true)) {
               var8 = (int)Math.round(var0.getY() + (double)(var11 / 2));
               var10 = var11;
            } else {
               var7 = (int)Math.round(var0.getX() + (double)(var11 / 2));
               var9 = var11;
            }
         }

         var11 = (int)Math.max((double)var5, (double)var9 * var3);
         int var12 = (int)Math.max((double)var5, (double)var10 * var3);
         if (var6 > 0) {
            var11 = Math.min(var11, var6);
            var12 = Math.min(var12, var6);
         }

         Rectangle var13 = new Rectangle(Math.round((float)(var7 - var11 / 2)), Math.round((float)(var8 - var12 / 2)), var11, var12);
         return var13.contains(var1, var2);
      } else {
         return true;
      }
   }

   public static boolean isTrue(Map var0, String var1) {
      return isTrue(var0, var1, false);
   }

   public static boolean isTrue(Map var0, String var1, boolean var2) {
      Object var3 = var0.get(var1);
      if (var3 == null) {
         return var2;
      } else {
         return var3.equals("1") || var3.toString().toLowerCase().equals("true");
      }
   }

   public static int getInt(Map var0, String var1) {
      return getInt(var0, var1, 0);
   }

   public static int getInt(Map var0, String var1, int var2) {
      Object var3 = var0.get(var1);
      return var3 == null ? var2 : (int)Float.parseFloat(var3.toString());
   }

   public static float getFloat(Map var0, String var1) {
      return getFloat(var0, var1, 0.0F);
   }

   public static float getFloat(Map var0, String var1, float var2) {
      Object var3 = var0.get(var1);
      return var3 == null ? var2 : Float.parseFloat(var3.toString());
   }

   public static float[] getFloatArray(Map var0, String var1, float[] var2) {
      Object var3 = var0.get(var1);
      if (var3 == null) {
         return var2;
      } else {
         String[] var4 = var3.toString().split(",");
         float[] var5 = new float[var4.length];

         for(int var6 = 0; var6 < var4.length; ++var6) {
            var5[var6] = Float.parseFloat(var4[var6]);
         }

         return var5;
      }
   }

   public static double getDouble(Map var0, String var1) {
      return getDouble(var0, var1, 0.0);
   }

   public static double getDouble(Map var0, String var1, double var2) {
      Object var4 = var0.get(var1);
      return var4 == null ? var2 : Double.parseDouble(var4.toString());
   }

   public static String getString(Map var0, String var1) {
      return getString(var0, var1, (String)null);
   }

   public static String getString(Map var0, String var1, String var2) {
      Object var3 = var0.get(var1);
      return var3 == null ? var2 : var3.toString();
   }

   public static Color getColor(Map var0, String var1) {
      return getColor(var0, var1, (Color)null);
   }

   public static Color getColor(Map var0, String var1, Color var2) {
      Object var3 = var0.get(var1);
      return var3 == null ? var2 : parseColor(var3.toString());
   }

   public static Font getFont(Map var0) {
      return getFont(var0, 1.0);
   }

   public static Font getFont(Map var0, double var1) {
      String var3 = getString(var0, mxConstants.STYLE_FONTFAMILY, mxConstants.DEFAULT_FONTFAMILY);
      int var4 = getInt(var0, mxConstants.STYLE_FONTSIZE, mxConstants.DEFAULT_FONTSIZE);
      int var5 = getInt(var0, mxConstants.STYLE_FONTSTYLE);
      int var6 = (var5 & 1) == 1 ? 1 : 0;
      var6 += (var5 & 2) == 2 ? 2 : 0;
      return new Font(var3, var6, (int)((double)var4 * var1));
   }

   public static String hexString(Color var0) {
      int var1 = var0.getRed();
      int var2 = var0.getGreen();
      int var3 = var0.getBlue();
      return String.format("#%02X%02X%02X", var1, var2, var3);
   }

   public static Color parseColor(String var0) throws NumberFormatException {
      if (var0.equalsIgnoreCase("white")) {
         return Color.white;
      } else if (var0.equalsIgnoreCase("black")) {
         return Color.black;
      } else if (var0.equalsIgnoreCase("red")) {
         return Color.red;
      } else if (var0.equalsIgnoreCase("green")) {
         return Color.green;
      } else if (var0.equalsIgnoreCase("blue")) {
         return Color.blue;
      } else if (var0.equalsIgnoreCase("orange")) {
         return Color.orange;
      } else if (var0.equalsIgnoreCase("yellow")) {
         return Color.yellow;
      } else if (var0.equalsIgnoreCase("pink")) {
         return Color.pink;
      } else if (var0.equalsIgnoreCase("turqoise")) {
         return new Color(0, 255, 255);
      } else if (var0.equalsIgnoreCase("gray")) {
         return Color.gray;
      } else if (var0.equalsIgnoreCase("none")) {
         return null;
      } else {
         int var1;
         try {
            var1 = (int)Long.parseLong(var0, 16);
         } catch (NumberFormatException var3) {
            var1 = Long.decode(var0).intValue();
         }

         return new Color(var1);
      }
   }

   public static String getHexColorString(Color var0) {
      return Integer.toHexString(var0.getRGB() & 16777215 | var0.getAlpha() << 24);
   }

   public static String readFile(String var0) throws IOException {
      BufferedReader var1 = new BufferedReader(new InputStreamReader(new FileInputStream(var0)));
      StringBuffer var2 = new StringBuffer();

      for(String var3 = var1.readLine(); var3 != null; var3 = var1.readLine()) {
         var2.append(var3 + "\n");
      }

      var1.close();
      return var2.toString();
   }

   public static void writeFile(String var0, String var1) throws IOException {
      FileWriter var2 = new FileWriter(var1);
      var2.write(var0);
      var2.flush();
      var2.close();
   }

   public static String getMd5Hash(String var0) {
      StringBuffer var1 = new StringBuffer(32);

      try {
         MessageDigest var2 = MessageDigest.getInstance("MD5");
         var2.update(var0.getBytes());
         Formatter var3 = new Formatter(var1);
         byte[] var4 = var2.digest();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var3.format("%02x", new Byte(var4[var5]));
         }
      } catch (NoSuchAlgorithmException var6) {
         var6.printStackTrace();
      }

      return var1.toString();
   }

   public static boolean isNode(Object var0, String var1) {
      return isNode(var0, var1, (String)null, (String)null);
   }

   public static boolean isNode(Object var0, String var1, String var2, String var3) {
      if (var0 instanceof Element) {
         Element var4 = (Element)var0;
         if (var1 == null || var4.getNodeName().equalsIgnoreCase(var1)) {
            String var5 = var2 != null ? var4.getAttribute(var2) : null;
            return var2 == null || var5 != null && var5.equals(var3);
         }
      }

      return false;
   }

   public static void setAntiAlias(Graphics2D var0, boolean var1, boolean var2) {
      var0.setRenderingHint(RenderingHints.KEY_ANTIALIASING, var1 ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
      var0.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, var2 ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
   }

   public static void clearRect(Graphics2D var0, Rectangle var1, Color var2) {
      if (var2 != null) {
         var0.setColor(var2);
         var0.fillRect(var1.x, var1.y, var1.width, var1.height);
      } else {
         var0.setComposite(AlphaComposite.getInstance(1, 0.0F));
         var0.fillRect(var1.x, var1.y, var1.width, var1.height);
         var0.setComposite(AlphaComposite.SrcOver);
      }

   }

   public static BufferedImage createBufferedImage(int var0, int var1, Color var2) throws OutOfMemoryError {
      BufferedImage var3 = null;
      if (var0 > 0 && var1 > 0) {
         Runtime var4 = Runtime.getRuntime();
         long var5 = var4.maxMemory();
         long var7 = var4.totalMemory();
         long var9 = var4.freeMemory();
         long var11 = (var9 + (var5 - var7)) / 1024L;
         byte var13 = 4;
         long var14 = (long)(var0 * var1 * var13 / 1024);
         if (var14 > var11) {
            throw new OutOfMemoryError("Not enough memory for image (" + var0 + " x " + var1 + ")");
         }

         int var16 = var2 != null ? 1 : 2;
         var3 = new BufferedImage(var0, var1, var16);
         if (var2 != null) {
            Graphics2D var17 = var3.createGraphics();
            clearRect(var17, new Rectangle(var0, var1), var2);
            var17.dispose();
         }
      }

      return var3;
   }

   public static Document createDocument() {
      Document var0 = null;

      try {
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var2 = var1.newDocumentBuilder();
         var0 = var2.newDocument();
      } catch (Exception var3) {
         System.out.println(var3.getMessage());
      }

      return var0;
   }

   public static Document createSvgDocument(int var0, int var1) {
      Document var2 = createDocument();
      Element var3 = var2.createElement("svg");
      String var4 = String.valueOf(var0);
      String var5 = String.valueOf(var1);
      var3.setAttribute("width", var4);
      var3.setAttribute("height", var5);
      var3.setAttribute("viewBox", "0 0 " + var4 + " " + var5);
      var3.setAttribute("version", "1.1");
      var3.setAttribute("xmlns", mxConstants.NS_SVG);
      var3.setAttribute("xmlns:xlink", mxConstants.NS_XLINK);
      var2.appendChild(var3);
      return var2;
   }

   public static Document createVmlDocument() {
      Document var0 = createDocument();
      Element var1 = var0.createElement("html");
      var1.setAttribute("xmlns:v", "urn:schemas-microsoft-com:vml");
      var1.setAttribute("xmlns:o", "urn:schemas-microsoft-com:office:office");
      var0.appendChild(var1);
      Element var2 = var0.createElement("head");
      Element var3 = var0.createElement("style");
      var3.setAttribute("type", "text/css");
      var3.appendChild(var0.createTextNode("<!-- v\\:* {behavior: url(#default#VML);} -->"));
      var2.appendChild(var3);
      var1.appendChild(var2);
      Element var4 = var0.createElement("body");
      var1.appendChild(var4);
      return var0;
   }

   public static Document createHtmlDocument() {
      Document var0 = createDocument();
      Element var1 = var0.createElement("html");
      var0.appendChild(var1);
      Element var2 = var0.createElement("head");
      var1.appendChild(var2);
      Element var3 = var0.createElement("body");
      var1.appendChild(var3);
      return var0;
   }

   public static String createHtmlDocument(Map var0, String var1) {
      return createHtmlDocument(var0, var1, 1.0);
   }

   public static String createHtmlDocument(Map var0, String var1, double var2) {
      StringBuffer var4 = new StringBuffer();
      var4.append("font-family:" + getString(var0, mxConstants.STYLE_FONTFAMILY, mxConstants.DEFAULT_FONTFAMILIES) + ";");
      var4.append("font-size:" + (int)((double)getInt(var0, mxConstants.STYLE_FONTSIZE, mxConstants.DEFAULT_FONTSIZE) * var2) + " pt;");
      String var5 = getString(var0, mxConstants.STYLE_FONTCOLOR);
      if (var5 != null) {
         var4.append("color:" + var5 + ";");
      }

      int var6 = getInt(var0, mxConstants.STYLE_FONTSTYLE);
      if ((var6 & 1) == 1) {
         var4.append("font-weight:bold;");
      }

      if ((var6 & 2) == 2) {
         var4.append("font-style:italic;");
      }

      if ((var6 & 4) == 4) {
         var4.append("text-decoration:underline;");
      }

      String var7 = getString(var0, mxConstants.STYLE_ALIGN, "left");
      if (var7.equals("center")) {
         var4.append("text-align:center;");
      } else if (var7.equals("right")) {
         var4.append("text-align:right;");
      }

      return "<html><body style=\"" + var4.toString() + "\">" + var1 + "</body></html>";
   }

   public static HTMLDocument createHtmlDocumentObject(Map var0, double var1) {
      HTMLDocument var3 = new HTMLDocument();
      StringBuffer var4 = new StringBuffer("body {");
      var4.append(" font-family: " + getString(var0, mxConstants.STYLE_FONTFAMILY, mxConstants.DEFAULT_FONTFAMILIES) + " ; ");
      var4.append(" font-size: " + (int)((double)getInt(var0, mxConstants.STYLE_FONTSIZE, mxConstants.DEFAULT_FONTSIZE) * var1) + " pt ;");
      String var5 = getString(var0, mxConstants.STYLE_FONTCOLOR);
      if (var5 != null) {
         var4.append("color: " + var5 + " ; ");
      }

      int var6 = getInt(var0, mxConstants.STYLE_FONTSTYLE);
      if ((var6 & 1) == 1) {
         var4.append(" font-weight: bold ; ");
      }

      if ((var6 & 2) == 2) {
         var4.append(" font-style: italic ; ");
      }

      if ((var6 & 4) == 4) {
         var4.append(" text-decoration: underline ; ");
      }

      String var7 = getString(var0, mxConstants.STYLE_ALIGN, "left");
      if (var7.equals("center")) {
         var4.append(" text-align: center ; ");
      } else if (var7.equals("right")) {
         var4.append(" text-align: right ; ");
      }

      var4.append(" } ");
      var3.getStyleSheet().addRule(var4.toString());
      return var3;
   }

   public static Element createTable(Document var0, String var1, int var2, int var3, int var4, int var5, double var6, Map var8) {
      Element var9 = var0.createElement("table");
      if (var1 != null && var1.length() > 0) {
         Element var10 = var0.createElement("tr");
         Element var11 = var0.createElement("td");
         var9.setAttribute("cellspacing", "0");
         var9.setAttribute("border", "0");
         var11.setAttribute("align", getString(var8, mxConstants.STYLE_ALIGN, "center"));
         String var12 = getString(var8, mxConstants.STYLE_FONTCOLOR, "black");
         String var13 = getString(var8, mxConstants.STYLE_FONTFAMILY, mxConstants.DEFAULT_FONTFAMILIES);
         int var14 = (int)((double)getInt(var8, mxConstants.STYLE_FONTSIZE, mxConstants.DEFAULT_FONTSIZE) * var6);
         String var15 = "position:absolute;left:" + String.valueOf(var2) + "px;" + "top:" + var3 + "px;" + "width:" + var4 + "px;" + "height:" + var5 + "px;" + "font-size:" + var14 + "px;" + "font-family:" + var13 + ";" + "color:" + var12 + ";";
         if (getString(var8, mxConstants.STYLE_WHITE_SPACE, "nowrap").equals("wrap")) {
            var15 = var15 + "whiteSpace:wrap;";
         }

         String var16 = getString(var8, mxConstants.STYLE_LABEL_BACKGROUNDCOLOR);
         if (var16 != null) {
            var15 = var15 + "background:" + var16 + ";";
         }

         String var17 = getString(var8, mxConstants.STYLE_LABEL_BORDERCOLOR);
         if (var17 != null) {
            var15 = var15 + "border:" + var17 + " solid 1pt;";
         }

         float var18 = getFloat(var8, mxConstants.STYLE_TEXT_OPACITY, 100.0F);
         if (var18 < 100.0F) {
            var15 = var15 + "filter:alpha(opacity=" + var18 + ");";
            var15 = var15 + "opacity:" + var18 / 100.0F + ";";
         }

         var11.setAttribute("style", var15);
         String[] var19 = var1.split("\n");

         for(int var20 = 0; var20 < var19.length; ++var20) {
            var11.appendChild(var0.createTextNode(var19[var20]));
            var11.appendChild(var0.createElement("br"));
         }

         var10.appendChild(var11);
         var9.appendChild(var10);
      }

      return var9;
   }

   public static Image loadImage(String var0) {
      BufferedImage var1 = null;
      URL var2 = null;

      try {
         var2 = new URL(var0);
      } catch (Exception var5) {
         var2 = mxUtils.class.getResource(var0);
      }

      if (var0 != null) {
         try {
            var1 = ImageIO.read(var2);
         } catch (Exception var4) {
         }
      }

      return var1;
   }

   public static Document loadDocument(String var0) {
      try {
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var2 = var1.newDocumentBuilder();
         return var2.parse(var0);
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Document parseXml(String var0) {
      try {
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var2 = var1.newDocumentBuilder();
         return var2.parse(new InputSource(new StringReader(var0)));
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Object eval(String var0) {
      int var1 = var0.lastIndexOf(".");
      if (var1 > 0) {
         Class var2 = mxCodecRegistry.getClassForName(var0.substring(0, var1));
         if (var2 != null) {
            try {
               return var2.getField(var0.substring(var1 + 1)).get((Object)null);
            } catch (Exception var4) {
            }
         }
      }

      return var0;
   }

   public static Node findNode(Node var0, String var1, String var2) {
      String var3 = var0 instanceof Element ? ((Element)var0).getAttribute(var1) : null;
      if (var3 != null && var3.equals(var2)) {
         return var0;
      } else {
         for(var0 = var0.getFirstChild(); var0 != null; var0 = var0.getNextSibling()) {
            Node var4 = findNode(var0, var1, var2);
            if (var4 != null) {
               return var4;
            }
         }

         return null;
      }
   }

   public static Node selectSingleNode(Document var0, String var1) {
      try {
         XPath var2 = XPathFactory.newInstance().newXPath();
         return (Node)var2.evaluate(var1, (Object)var0, XPathConstants.NODE);
      } catch (XPathExpressionException var3) {
         return null;
      }
   }

   public static String htmlEntities(String var0) {
      return var0.replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("'", "&prime;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
   }

   public static String getXml(Node var0) {
      try {
         TransformerFactory var1 = TransformerFactory.newInstance();
         Transformer var2 = var1.newTransformer();
         DOMSource var3 = new DOMSource(var0);
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         StreamResult var5 = new StreamResult(var4);
         var2.transform(var3, var5);
         return var4.toString("UTF-8");
      } catch (Exception var6) {
         return "";
      }
   }

   public static String getPrettyXml(Node var0) {
      return getPrettyXml(var0, "  ", "");
   }

   public static String getPrettyXml(Node var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      if (var0 != null) {
         if (var0.getNodeType() == 3) {
            var3.append(var0.getNodeValue());
         } else {
            var3.append(var2 + "<" + var0.getNodeName());
            NamedNodeMap var4 = var0.getAttributes();
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.getLength(); ++var5) {
                  String var6 = var4.item(var5).getNodeValue();
                  var6 = htmlEntities(var6);
                  var3.append(" " + var4.item(var5).getNodeName() + "=\"" + var6 + "\"");
               }
            }

            Node var7 = var0.getFirstChild();
            if (var7 != null) {
               var3.append(">\n");

               while(var7 != null) {
                  var3.append(getPrettyXml(var7, var1, var2 + var1));
                  var7 = var7.getNextSibling();
               }

               var3.append(var2 + "</" + var0.getNodeName() + ">\n");
            } else {
               var3.append("/>\n");
            }
         }
      }

      return var3.toString();
   }

   static {
      try {
         fontGraphics = (new BufferedImage(1, 1, 1)).getGraphics();
      } catch (Exception var1) {
      }

   }
}
