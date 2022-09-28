package org.apache.batik.transcoder.wmf.tosvg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.batik.ext.awt.geom.Polygon2D;
import org.apache.batik.ext.awt.geom.Polyline2D;

public class WMFPainter extends AbstractWMFPainter {
   private static final int INPUT_BUFFER_SIZE = 30720;
   private static final Integer INTEGER_0 = new Integer(0);
   private float scale;
   private float scaleX;
   private float scaleY;
   private float conv;
   private float xOffset;
   private float yOffset;
   private float vpX;
   private float vpY;
   private float vpW;
   private float vpH;
   private Color frgdColor;
   private Color bkgdColor;
   private boolean opaque;
   private transient boolean firstEffectivePaint;
   private static BasicStroke solid = new BasicStroke(1.0F, 0, 1);
   private static BasicStroke textSolid = new BasicStroke(1.0F, 0, 1);
   private transient ImageObserver observer;
   private transient BufferedInputStream bufStream;

   public WMFPainter(WMFRecordStore var1, float var2) {
      this(var1, 0, 0, var2);
   }

   public WMFPainter(WMFRecordStore var1, int var2, int var3, float var4) {
      this.opaque = false;
      this.firstEffectivePaint = true;
      this.observer = new ImageObserver() {
         public boolean imageUpdate(Image var1, int var2, int var3, int var4, int var5, int var6) {
            return false;
         }
      };
      this.bufStream = null;
      this.setRecordStore(var1);
      TextureFactory.getInstance().reset();
      this.conv = var4;
      this.xOffset = (float)(-var2);
      this.yOffset = (float)(-var3);
      this.scale = (float)var1.getWidthPixels() / (float)var1.getWidthUnits() * var4;
      this.scale = this.scale * (float)var1.getWidthPixels() / (float)var1.getVpW();
      float var5 = (float)var1.getVpW() / (float)var1.getWidthPixels() * (float)var1.getWidthUnits() / (float)var1.getWidthPixels();
      float var6 = (float)var1.getVpH() / (float)var1.getHeightPixels() * (float)var1.getHeightUnits() / (float)var1.getHeightPixels();
      this.xOffset *= var5;
      this.yOffset *= var6;
      this.scaleX = this.scale;
      this.scaleY = this.scale;
   }

   public void paint(Graphics var1) {
      float var2 = 10.0F;
      float var3 = 0.0F;
      float var4 = 0.0F;
      float var5 = 0.0F;
      float var6 = 0.0F;
      boolean var7 = true;
      boolean var8 = true;
      boolean var9 = true;
      Object var10 = null;
      Stack var12 = new Stack();
      int var13 = this.currentStore.getNumRecords();
      int var14 = this.currentStore.getNumObjects();
      this.vpX = this.currentStore.getVpX() * this.scale;
      this.vpY = this.currentStore.getVpY() * this.scale;
      this.vpW = (float)this.currentStore.getVpW() * this.scale;
      this.vpH = (float)this.currentStore.getVpH() * this.scale;
      if (!this.currentStore.isReading()) {
         var1.setPaintMode();
         Graphics2D var17 = (Graphics2D)var1;
         var17.setStroke(solid);
         int var52 = -1;
         int var53 = -1;
         int var54 = -1;
         this.frgdColor = null;
         this.bkgdColor = Color.white;

         GdiObject var15;
         for(int var18 = 0; var18 < var14; ++var18) {
            var15 = this.currentStore.getObject(var18);
            var15.clear();
         }

         float var55 = this.vpW;
         float var19 = this.vpH;
         var17.setColor(Color.black);

         for(int var20 = 0; var20 < var13; ++var20) {
            MetaRecord var21 = this.currentStore.getRecord(var20);
            int var16;
            int var22;
            int var23;
            float var24;
            float var25;
            int var27;
            int var28;
            float var29;
            double var38;
            double var40;
            double var42;
            double var44;
            double var46;
            Arc2D.Double var48;
            byte[] var56;
            double var57;
            String var58;
            float var59;
            float var60;
            int var61;
            int var63;
            double var64;
            float var65;
            float[] var66;
            byte var67;
            FontRenderContext var68;
            int var69;
            double var70;
            float var71;
            float var73;
            float[] var77;
            Color var79;
            double var81;
            byte[] var84;
            TextLayout var86;
            int var91;
            BufferedImage var92;
            Color var94;
            double var95;
            int var102;
            int var105;
            double var107;
            int var110;
            int var113;
            double var114;
            double var116;
            switch (var21.functionId) {
               case 0:
               case 524:
                  this.vpW = (float)var21.elementAt(0);
                  this.vpH = (float)var21.elementAt(1);
                  this.scaleX = this.scale;
                  this.scaleY = this.scale;
                  solid = new BasicStroke(this.scaleX * 2.0F, 0, 1);
                  break;
               case 30:
                  var12.push(new Float(var4));
                  var12.push(new Float(var5));
                  var12.push(new Float(var6));
                  var12.push(new Integer(var52));
                  var12.push(new Integer(var53));
                  var12.push(new Integer(var54));
                  var12.push(this.frgdColor);
                  var12.push(this.bkgdColor);
               case 53:
               case 55:
               case 259:
               case 261:
               case 262:
               case 263:
               case 264:
               case 298:
               case 299:
               case 300:
               case 313:
               case 522:
               case 525:
               case 526:
               case 527:
               case 529:
               case 544:
               case 552:
               case 561:
               case 564:
               case 1040:
               case 1042:
               case 1045:
               case 1046:
               case 1049:
               case 1055:
               case 1065:
               case 1078:
               case 1574:
               case 2338:
               case 2851:
               case 3379:
               default:
                  break;
               case 247:
                  var22 = this.addObjectAt(this.currentStore, 8, INTEGER_0, 0);
                  break;
               case 248:
               case 505:
               case 765:
               case 1790:
               case 1791:
                  var22 = this.addObjectAt(this.currentStore, 6, INTEGER_0, 0);
                  break;
               case 258:
                  var22 = var21.elementAt(0);
                  this.opaque = var22 == 2;
                  break;
               case 260:
                  var59 = (float)var21.ElementAt(0);
                  Object var76 = null;
                  boolean var99 = false;
                  if (var59 == 66.0F) {
                     var76 = Color.black;
                     var99 = true;
                  } else if (var59 == 1.6711778E7F) {
                     var76 = Color.white;
                     var99 = true;
                  } else if (var59 == 1.5728673E7F && var52 >= 0) {
                     var76 = this.getStoredPaint(this.currentStore, var52);
                     var99 = true;
                  }

                  if (var99) {
                     if (var76 != null) {
                        var17.setPaint((Paint)var76);
                     } else {
                        this.setBrushPaint(this.currentStore, var17, var52);
                     }
                  }
                  break;
               case 295:
                  this.bkgdColor = (Color)var12.pop();
                  this.frgdColor = (Color)var12.pop();
                  var54 = (Integer)var12.pop();
                  var53 = (Integer)var12.pop();
                  var52 = (Integer)var12.pop();
                  var6 = (Float)var12.pop();
                  var5 = (Float)var12.pop();
                  var4 = (Float)var12.pop();
                  break;
               case 301:
                  var16 = var21.elementAt(0);
                  if ((var16 & Integer.MIN_VALUE) == 0) {
                     if (var16 >= var14) {
                        var16 -= var14;
                        switch (var16) {
                           case 0:
                           case 1:
                           case 2:
                           case 3:
                           case 4:
                           case 6:
                           case 7:
                           case 9:
                           case 10:
                           case 11:
                           case 12:
                           case 13:
                           case 14:
                           case 15:
                           case 16:
                           default:
                              break;
                           case 5:
                              var52 = -1;
                              break;
                           case 8:
                              var53 = -1;
                        }
                     } else {
                        var15 = this.currentStore.getObject(var16);
                        if (var15.used) {
                           switch (var15.type) {
                              case 1:
                                 var17.setColor((Color)var15.obj);
                                 var53 = var16;
                                 break;
                              case 2:
                                 if (var15.obj instanceof Color) {
                                    var17.setColor((Color)var15.obj);
                                 } else if (var15.obj instanceof Paint) {
                                    var17.setPaint((Paint)var15.obj);
                                 } else {
                                    var17.setPaint(this.getPaint((byte[])var15.obj));
                                 }

                                 var52 = var16;
                                 break;
                              case 3:
                                 this.wmfFont = (WMFFont)var15.obj;
                                 Font var78 = this.wmfFont.font;
                                 var17.setFont(var78);
                                 var54 = var16;
                                 break;
                              case 4:
                                 var53 = -1;
                                 break;
                              case 5:
                                 var52 = -1;
                           }
                        }
                     }
                  }
                  break;
               case 302:
                  this.currentHorizAlign = WMFUtilities.getHorizontalAlignment(var21.elementAt(0));
                  this.currentVertAlign = WMFUtilities.getVerticalAlignment(var21.elementAt(0));
                  break;
               case 322:
                  var67 = 0;
                  byte[] var74 = ((MetaRecord.ByteRecord)var21).bstr;
                  this.addObjectAt(this.currentStore, 2, var74, var67);
                  break;
               case 496:
                  var16 = var21.elementAt(0);
                  var15 = this.currentStore.getObject(var16);
                  if (var16 == var52) {
                     var52 = -1;
                  } else if (var16 == var53) {
                     var53 = -1;
                  } else if (var16 == var54) {
                     var54 = -1;
                  }

                  var15.clear();
                  break;
               case 513:
                  this.bkgdColor = new Color(var21.elementAt(0), var21.elementAt(1), var21.elementAt(2));
                  var17.setColor(this.bkgdColor);
                  break;
               case 521:
                  this.frgdColor = new Color(var21.elementAt(0), var21.elementAt(1), var21.elementAt(2));
                  var17.setColor(this.frgdColor);
                  break;
               case 523:
                  this.currentStore.setVpX(this.vpX = -((float)var21.elementAt(0)));
                  this.currentStore.setVpY(this.vpY = -((float)var21.elementAt(1)));
                  this.vpX *= this.scale;
                  this.vpY *= this.scale;
                  break;
               case 531:
                  var59 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(0));
                  var60 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(1));
                  Line2D.Float var96 = new Line2D.Float(var5, var6, var59, var60);
                  this.paintWithPen(var53, var96, var17);
                  var5 = var59;
                  var6 = var60;
                  break;
               case 532:
                  var5 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(0));
                  var6 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(1));
                  break;
               case 762:
                  var67 = 0;
                  var23 = var21.elementAt(0);
                  if (var23 == 5) {
                     var79 = Color.white;
                     this.addObjectAt(this.currentStore, 4, var79, var67);
                  } else {
                     var4 = (float)var21.elementAt(4);
                     this.setStroke(var17, var23, var4, this.scaleX);
                     var79 = new Color(var21.elementAt(1), var21.elementAt(2), var21.elementAt(3));
                     this.addObjectAt(this.currentStore, 1, var79, var67);
                  }
                  break;
               case 763:
                  var59 = (float)((int)(this.scaleY * (float)var21.elementAt(0)));
                  var23 = var21.elementAt(3);
                  var61 = var21.elementAt(1);
                  var63 = var21.elementAt(2);
                  var69 = var61 > 0 ? 2 : 0;
                  var69 |= var63 > 400 ? 1 : 0;
                  String var88 = ((MetaRecord.StringRecord)var21).text;

                  for(var28 = 0; var28 < var88.length() && (Character.isLetterOrDigit(var88.charAt(var28)) || Character.isWhitespace(var88.charAt(var28))); ++var28) {
                  }

                  if (var28 > 0) {
                     var88 = var88.substring(0, var28);
                  } else {
                     var88 = "System";
                  }

                  if (var59 < 0.0F) {
                     var59 = -var59;
                  }

                  byte var106 = 0;
                  Font var111 = new Font(var88, var69, (int)var59);
                  var111 = var111.deriveFont(var59);
                  var102 = var21.elementAt(4);
                  var105 = var21.elementAt(5);
                  var110 = var21.elementAt(6);
                  var113 = var21.elementAt(7);
                  WMFFont var115 = new WMFFont(var111, var23, var102, var105, var61, var63, var110, var113);
                  this.addObjectAt(this.currentStore, 3, var115, var106);
                  break;
               case 764:
                  var67 = 0;
                  var23 = var21.elementAt(0);
                  var79 = new Color(var21.elementAt(1), var21.elementAt(2), var21.elementAt(3));
                  if (var23 == 0) {
                     this.addObjectAt(this.currentStore, 2, var79, var67);
                  } else if (var23 == 2) {
                     var63 = var21.elementAt(4);
                     Paint var87;
                     if (!this.opaque) {
                        var87 = TextureFactory.getInstance().getTexture(var63, var79);
                     } else {
                        var87 = TextureFactory.getInstance().getTexture(var63, var79, this.bkgdColor);
                     }

                     if (var87 != null) {
                        this.addObjectAt(this.currentStore, 2, var87, var67);
                     } else {
                        var79 = Color.black;
                        this.addObjectAt(this.currentStore, 5, var79, var67);
                     }
                  } else {
                     var79 = Color.black;
                     this.addObjectAt(this.currentStore, 5, var79, var67);
                  }
                  break;
               case 804:
                  var22 = var21.elementAt(0);
                  var66 = new float[var22];
                  var77 = new float[var22];

                  for(var63 = 0; var63 < var22; ++var63) {
                     var66[var63] = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(var63 * 2 + 1));
                     var77[var63] = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(var63 * 2 + 2));
                  }

                  Polygon2D var90 = new Polygon2D(var66, var77, var22);
                  this.paint(var52, var53, var90, var17);
                  break;
               case 805:
                  var22 = var21.elementAt(0);
                  var66 = new float[var22];
                  var77 = new float[var22];

                  for(var63 = 0; var63 < var22; ++var63) {
                     var66[var63] = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(var63 * 2 + 1));
                     var77[var63] = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(var63 * 2 + 2));
                  }

                  Polyline2D var82 = new Polyline2D(var66, var77, var22);
                  this.paintWithPen(var53, var82, var17);
                  break;
               case 1048:
                  var59 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(0));
                  var60 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(2));
                  var24 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(1));
                  var25 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(3));
                  Ellipse2D.Float var83 = new Ellipse2D.Float(var59, var24, var60 - var59, var25 - var24);
                  this.paint(var52, var53, var83, var17);
                  break;
               case 1051:
                  var59 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(0));
                  var24 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(2));
                  var60 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(1));
                  var25 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(3));
                  Rectangle2D.Float var80 = new Rectangle2D.Float(var59, var60, var24 - var59, var25 - var60);
                  this.paint(var52, var53, var80, var17);
                  break;
               case 1313:
               case 1583:
                  try {
                     var56 = ((MetaRecord.ByteRecord)var21).bstr;
                     var58 = WMFUtilities.decodeString(this.wmfFont, var56);
                     var24 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(0));
                     var25 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(1));
                     if (this.frgdColor != null) {
                        var17.setColor(this.frgdColor);
                     } else {
                        var17.setColor(Color.black);
                     }

                     var68 = var17.getFontRenderContext();
                     new Point2D.Double(0.0, 0.0);
                     new GeneralPath(1);
                     var86 = new TextLayout(var58, var17.getFont(), var68);
                     this.firstEffectivePaint = false;
                     var25 += this.getVerticalAlignmentValue(var86, this.currentVertAlign);
                     this.drawString(-1, var17, this.getCharacterIterator(var17, var58, this.wmfFont), var24, var25, var86, this.wmfFont, this.currentHorizAlign);
                  } catch (Exception var49) {
                  }
                  break;
               case 1336:
                  var22 = var21.elementAt(0);
                  int[] var62 = new int[var22];

                  for(var61 = 0; var61 < var22; ++var61) {
                     var62[var61] = var21.elementAt(var61 + 1);
                  }

                  var61 = var22 + 1;
                  ArrayList var72 = new ArrayList(var22);

                  for(var69 = 0; var69 < var22; ++var69) {
                     var27 = var62[var69];
                     float[] var104 = new float[var27];
                     float[] var98 = new float[var27];

                     for(var91 = 0; var91 < var27; ++var91) {
                        var104[var91] = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(var61 + var91 * 2));
                        var98[var91] = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(var61 + var91 * 2 + 1));
                     }

                     var61 += var27 * 2;
                     Polygon2D var109 = new Polygon2D(var104, var98, var27);
                     var72.add(var109);
                  }

                  if (var52 >= 0) {
                     this.setBrushPaint(this.currentStore, var17, var52);
                     this.fillPolyPolygon(var17, var72);
                     this.firstEffectivePaint = false;
                  }

                  if (var53 >= 0) {
                     this.setPenColor(this.currentStore, var17, var53);
                     this.drawPolyPolygon(var17, var72);
                     this.firstEffectivePaint = false;
                  }
                  break;
               case 1564:
                  var59 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(0));
                  var24 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(2));
                  var65 = this.scaleX * (float)var21.elementAt(4);
                  var60 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(1));
                  var25 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(3));
                  var71 = this.scaleY * (float)var21.elementAt(5);
                  RoundRectangle2D.Float var100 = new RoundRectangle2D.Float(var59, var60, var24 - var59, var25 - var60, var65, var71);
                  this.paint(var52, var53, var100, var17);
                  break;
               case 1565:
                  var59 = (float)var21.elementAt(0);
                  var60 = this.scaleY * (float)var21.elementAt(1);
                  var24 = this.scaleX * (float)var21.elementAt(2);
                  var25 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(3));
                  var65 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(4));
                  Object var75 = null;
                  boolean var85 = false;
                  if (var59 == 66.0F) {
                     var75 = Color.black;
                     var85 = true;
                  } else if (var59 == 1.6711778E7F) {
                     var75 = Color.white;
                     var85 = true;
                  } else if (var59 == 1.5728673E7F && var52 >= 0) {
                     var75 = this.getStoredPaint(this.currentStore, var52);
                     var85 = true;
                  }

                  if (var85) {
                     var94 = var17.getColor();
                     if (var75 != null) {
                        var17.setPaint((Paint)var75);
                     } else {
                        this.setBrushPaint(this.currentStore, var17, var52);
                     }

                     Rectangle2D.Float var103 = new Rectangle2D.Float(var25, var65, var24, var60);
                     var17.fill(var103);
                     var17.setColor(var94);
                  }
                  break;
               case 2071:
               case 2074:
                  var57 = (double)(this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(0)));
                  var64 = (double)(this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(1)));
                  var70 = (double)(this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(2)));
                  var81 = (double)(this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(3)));
                  var95 = (double)(this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(4)));
                  var107 = (double)(this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(5)));
                  var114 = (double)(this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(6)));
                  var116 = (double)(this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(7)));
                  this.setBrushPaint(this.currentStore, var17, var52);
                  var38 = var57 + (var70 - var57) / 2.0;
                  var40 = var64 + (var81 - var64) / 2.0;
                  var42 = -Math.toDegrees(Math.atan2(var107 - var40, var95 - var38));
                  var44 = -Math.toDegrees(Math.atan2(var116 - var40, var114 - var38));
                  var46 = var44 - var42;
                  if (var46 < 0.0) {
                     var46 += 360.0;
                  }

                  if (var42 < 0.0) {
                     var42 += 360.0;
                  }

                  var48 = new Arc2D.Double(var57, var64, var70 - var57, var81 - var64, var42, var46, 0);
                  if (var21.functionId == 2071) {
                     var17.draw(var48);
                  } else {
                     var17.fill(var48);
                  }

                  this.firstEffectivePaint = false;
                  break;
               case 2096:
                  var57 = (double)(this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(0)));
                  var64 = (double)(this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(1)));
                  var70 = (double)(this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(2)));
                  var81 = (double)(this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(3)));
                  var95 = (double)(this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(4)));
                  var107 = (double)(this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(5)));
                  var114 = (double)(this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(6)));
                  var116 = (double)(this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(7)));
                  this.setBrushPaint(this.currentStore, var17, var52);
                  var38 = var57 + (var70 - var57) / 2.0;
                  var40 = var64 + (var81 - var64) / 2.0;
                  var42 = -Math.toDegrees(Math.atan2(var107 - var40, var95 - var38));
                  var44 = -Math.toDegrees(Math.atan2(var116 - var40, var114 - var38));
                  var46 = var44 - var42;
                  if (var46 < 0.0) {
                     var46 += 360.0;
                  }

                  if (var42 < 0.0) {
                     var42 += 360.0;
                  }

                  var48 = new Arc2D.Double(var57, var64, var70 - var57, var81 - var64, var42, var46, 1);
                  this.paint(var52, var53, var48, var17);
                  this.firstEffectivePaint = false;
                  break;
               case 2368:
                  var22 = var21.ElementAt(0);
                  var60 = (float)var21.ElementAt(1) * this.conv * this.currentStore.getVpWFactor();
                  var24 = (float)var21.ElementAt(2) * this.conv * this.currentStore.getVpHFactor();
                  var63 = var21.ElementAt(3);
                  var69 = var21.ElementAt(4);
                  var71 = this.conv * this.currentStore.getVpWFactor() * (this.vpY + this.yOffset + (float)var21.ElementAt(5));
                  var73 = this.conv * this.currentStore.getVpHFactor() * (this.vpX + this.xOffset + (float)var21.ElementAt(6));
                  if (var21 instanceof MetaRecord.ByteRecord) {
                     byte[] var89 = ((MetaRecord.ByteRecord)var21).bstr;
                     BufferedImage var93 = this.getImage(var89);
                     if (var93 != null) {
                        var102 = var93.getWidth();
                        var105 = var93.getHeight();
                        if (this.opaque) {
                           var17.drawImage(var93, (int)var73, (int)var71, (int)(var73 + var24), (int)(var71 + var60), var69, var63, var69 + var102, var63 + var105, this.bkgdColor, this.observer);
                        } else {
                           var17.drawImage(var93, (int)var73, (int)var71, (int)(var73 + var24), (int)(var71 + var60), var69, var63, var69 + var102, var63 + var105, this.observer);
                        }
                     }
                  } else if (this.opaque) {
                     var94 = var17.getColor();
                     var17.setColor(this.bkgdColor);
                     var17.fill(new Rectangle2D.Float(var73, var71, var24, var60));
                     var17.setColor(var94);
                  }
                  break;
               case 2610:
                  try {
                     var56 = ((MetaRecord.ByteRecord)var21).bstr;
                     var58 = WMFUtilities.decodeString(this.wmfFont, var56);
                     var24 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(0));
                     var25 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(1));
                     if (this.frgdColor != null) {
                        var17.setColor(this.frgdColor);
                     } else {
                        var17.setColor(Color.black);
                     }

                     var68 = var17.getFontRenderContext();
                     new Point2D.Double(0.0, 0.0);
                     new GeneralPath(1);
                     var86 = new TextLayout(var58, var17.getFont(), var68);
                     var91 = var21.elementAt(2);
                     boolean var97 = false;
                     boolean var101 = false;
                     boolean var108 = false;
                     boolean var112 = false;
                     boolean var35 = false;
                     Shape var36 = null;
                     if ((var91 & 4) != 0) {
                        var35 = true;
                        var102 = var21.elementAt(3);
                        var105 = var21.elementAt(4);
                        var110 = var21.elementAt(5);
                        var113 = var21.elementAt(6);
                        var36 = var17.getClip();
                        var17.setClip(var102, var105, var110, var113);
                     }

                     this.firstEffectivePaint = false;
                     var25 += this.getVerticalAlignmentValue(var86, this.currentVertAlign);
                     this.drawString(var91, var17, this.getCharacterIterator(var17, var58, this.wmfFont, this.currentHorizAlign), var24, var25, var86, this.wmfFont, this.currentHorizAlign);
                     if (var35) {
                        var17.setClip(var36);
                     }
                  } catch (Exception var50) {
                  }
                  break;
               case 2881:
                  var22 = var21.elementAt(1);
                  var23 = var21.elementAt(2);
                  var61 = var21.elementAt(3);
                  var63 = var21.elementAt(4);
                  var65 = this.conv * this.currentStore.getVpWFactor() * (this.vpY + this.yOffset + (float)var21.elementAt(7));
                  var71 = this.conv * this.currentStore.getVpHFactor() * (this.vpX + this.xOffset + (float)var21.elementAt(8));
                  var73 = (float)var21.elementAt(5);
                  var29 = (float)var21.elementAt(6);
                  var29 = var29 * this.conv * this.currentStore.getVpWFactor();
                  var73 = var73 * this.conv * this.currentStore.getVpHFactor();
                  var84 = ((MetaRecord.ByteRecord)var21).bstr;
                  var92 = this.getImage(var84, var23, var22);
                  if (var92 != null) {
                     var17.drawImage(var92, (int)var71, (int)var65, (int)(var71 + var29), (int)(var65 + var73), var63, var61, var63 + var23, var61 + var22, this.bkgdColor, this.observer);
                  }
                  break;
               case 3907:
                  var22 = var21.elementAt(1);
                  var23 = var21.elementAt(2);
                  var61 = var21.elementAt(3);
                  var63 = var21.elementAt(4);
                  var65 = this.conv * this.currentStore.getVpWFactor() * (this.vpY + this.yOffset + (float)var21.elementAt(7));
                  var71 = this.conv * this.currentStore.getVpHFactor() * (this.vpX + this.xOffset + (float)var21.elementAt(8));
                  var73 = (float)var21.elementAt(5);
                  var29 = (float)var21.elementAt(6);
                  var29 = var29 * this.conv * this.currentStore.getVpWFactor();
                  var73 = var73 * this.conv * this.currentStore.getVpHFactor();
                  var84 = ((MetaRecord.ByteRecord)var21).bstr;
                  var92 = this.getImage(var84, var23, var22);
                  if (var92 != null) {
                     if (this.opaque) {
                        var17.drawImage(var92, (int)var71, (int)var65, (int)(var71 + var29), (int)(var65 + var73), var63, var61, var63 + var23, var61 + var22, this.bkgdColor, this.observer);
                     } else {
                        var17.drawImage(var92, (int)var71, (int)var65, (int)(var71 + var29), (int)(var65 + var73), var63, var61, var63 + var23, var61 + var22, this.observer);
                     }
                  }
                  break;
               case 4096:
                  try {
                     this.setPenColor(this.currentStore, var17, var53);
                     var22 = var21.elementAt(0);
                     var23 = (var22 - 1) / 3;
                     var24 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(1));
                     var25 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(2));
                     GeneralPath var26 = new GeneralPath(1);
                     var26.moveTo(var24, var25);

                     for(var27 = 0; var27 < var23; ++var27) {
                        var28 = var27 * 6;
                        var29 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(var28 + 3));
                        float var30 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(var28 + 4));
                        float var31 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(var28 + 5));
                        float var32 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(var28 + 6));
                        float var33 = this.scaleX * (this.vpX + this.xOffset + (float)var21.elementAt(var28 + 7));
                        float var34 = this.scaleY * (this.vpY + this.yOffset + (float)var21.elementAt(var28 + 8));
                        var26.curveTo(var29, var30, var31, var32, var33, var34);
                     }

                     var17.setStroke(solid);
                     var17.draw(var26);
                     this.firstEffectivePaint = false;
                  } catch (Exception var51) {
                  }
            }
         }
      }

   }

   private Paint getPaint(byte[] var1) {
      Dimension var2 = this.getImageDimension(var1);
      BufferedImage var3 = this.getImage(var1);
      Rectangle2D.Float var4 = new Rectangle2D.Float(0.0F, 0.0F, (float)var2.width, (float)var2.height);
      TexturePaint var5 = new TexturePaint(var3, var4);
      return var5;
   }

   private void drawString(int var1, Graphics2D var2, AttributedCharacterIterator var3, float var4, float var5, TextLayout var6, WMFFont var7, int var8) {
      if (var7.escape == 0) {
         if (var1 != -1) {
            this.fillTextBackground(-1, var1, var2, var4, var5, 0.0F, var6);
         }

         float var9 = (float)var6.getBounds().getWidth();
         if (var8 == 6) {
            var2.drawString(var3, var4 - var9 / 2.0F, var5);
         } else if (var8 == 2) {
            var2.drawString(var3, var4 - var9, var5);
         } else {
            var2.drawString(var3, var4, var5);
         }
      } else {
         AffineTransform var14 = var2.getTransform();
         float var10 = -((float)((double)var7.escape * Math.PI / 1800.0));
         float var11 = (float)var6.getBounds().getWidth();
         float var12 = (float)var6.getBounds().getHeight();
         if (var8 == 6) {
            var2.translate((double)(-var11 / 2.0F), (double)(var12 / 2.0F));
            var2.rotate((double)var10, (double)(var4 - var11 / 2.0F), (double)var5);
         } else if (var8 == 2) {
            var2.translate((double)(-var11 / 2.0F), (double)(var12 / 2.0F));
            var2.rotate((double)var10, (double)(var4 - var11), (double)var5);
         } else {
            var2.translate(0.0, (double)(var12 / 2.0F));
            var2.rotate((double)var10, (double)var4, (double)var5);
         }

         if (var1 != -1) {
            this.fillTextBackground(var8, var1, var2, var4, var5, var11, var6);
         }

         Stroke var13 = var2.getStroke();
         var2.setStroke(textSolid);
         var2.drawString(var3, var4, var5);
         var2.setStroke(var13);
         var2.setTransform(var14);
      }

   }

   private void fillTextBackground(int var1, int var2, Graphics2D var3, float var4, float var5, float var6, TextLayout var7) {
      float var8 = var4;
      if (var1 == 6) {
         var8 = var4 - var6 / 2.0F;
      } else if (var1 == 2) {
         var8 = var4 - var6;
      }

      Color var9;
      AffineTransform var10;
      if ((var2 & 2) != 0) {
         var9 = var3.getColor();
         var10 = var3.getTransform();
         var3.setColor(this.bkgdColor);
         var3.translate((double)var8, (double)var5);
         var3.fill(var7.getBounds());
         var3.setColor(var9);
         var3.setTransform(var10);
      } else if (this.opaque) {
         var9 = var3.getColor();
         var10 = var3.getTransform();
         var3.setColor(this.bkgdColor);
         var3.translate((double)var8, (double)var5);
         var3.fill(var7.getBounds());
         var3.setColor(var9);
         var3.setTransform(var10);
      }

   }

   private void drawPolyPolygon(Graphics2D var1, List var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Polygon2D var4 = (Polygon2D)var3.next();
         var1.draw(var4);
      }

   }

   private void fillPolyPolygon(Graphics2D var1, List var2) {
      if (var2.size() == 1) {
         var1.fill((Polygon2D)var2.get(0));
      } else {
         GeneralPath var3 = new GeneralPath(0);

         for(int var4 = 0; var4 < var2.size(); ++var4) {
            Polygon2D var5 = (Polygon2D)var2.get(var4);
            var3.append(var5, false);
         }

         var1.fill(var3);
      }

   }

   private void setStroke(Graphics2D var1, int var2, float var3, float var4) {
      float var5;
      if (var3 == 0.0F) {
         var5 = 1.0F;
      } else {
         var5 = var3;
      }

      float var6 = (float)Toolkit.getDefaultToolkit().getScreenResolution() / (float)this.currentStore.getMetaFileUnitsPerInch();
      float var7 = var4 / var6;
      var5 = var5 * var6 * var7;
      var6 = (float)this.currentStore.getWidthPixels() * 1.0F / 350.0F;
      BasicStroke var8;
      if (var2 == 0) {
         var8 = new BasicStroke(var5, 0, 1);
         var1.setStroke(var8);
      } else {
         float[] var9;
         if (var2 == 2) {
            var9 = new float[]{1.0F * var6, 5.0F * var6};
            var8 = new BasicStroke(var5, 0, 1, 10.0F * var6, var9, 0.0F);
            var1.setStroke(var8);
         } else if (var2 == 1) {
            var9 = new float[]{5.0F * var6, 2.0F * var6};
            var8 = new BasicStroke(var5, 0, 1, 10.0F * var6, var9, 0.0F);
            var1.setStroke(var8);
         } else if (var2 == 3) {
            var9 = new float[]{5.0F * var6, 2.0F * var6, 1.0F * var6, 2.0F * var6};
            var8 = new BasicStroke(var5, 0, 1, 10.0F * var6, var9, 0.0F);
            var1.setStroke(var8);
         } else if (var2 == 4) {
            var9 = new float[]{5.0F * var6, 2.0F * var6, 1.0F * var6, 2.0F * var6, 1.0F * var6, 2.0F * var6};
            var8 = new BasicStroke(var5, 0, 1, 15.0F * var6, var9, 0.0F);
            var1.setStroke(var8);
         } else {
            var8 = new BasicStroke(var5, 0, 1);
            var1.setStroke(var8);
         }
      }

   }

   private void setPenColor(WMFRecordStore var1, Graphics2D var2, int var3) {
      if (var3 >= 0) {
         GdiObject var4 = var1.getObject(var3);
         var2.setColor((Color)var4.obj);
         boolean var5 = true;
      }

   }

   private int getHorizontalAlignement(int var1) {
      int var2 = var1 % 24;
      var2 %= 8;
      if (var2 >= 6) {
         return 6;
      } else {
         return var2 >= 2 ? 2 : 0;
      }
   }

   private void setBrushPaint(WMFRecordStore var1, Graphics2D var2, int var3) {
      if (var3 >= 0) {
         GdiObject var4 = var1.getObject(var3);
         if (var4.obj instanceof Color) {
            var2.setColor((Color)var4.obj);
         } else if (var4.obj instanceof Paint) {
            var2.setPaint((Paint)var4.obj);
         } else {
            var2.setPaint(this.getPaint((byte[])var4.obj));
         }

         boolean var5 = true;
      }

   }

   private Paint getStoredPaint(WMFRecordStore var1, int var2) {
      if (var2 >= 0) {
         GdiObject var3 = var1.getObject(var2);
         return var3.obj instanceof Paint ? (Paint)var3.obj : this.getPaint((byte[])var3.obj);
      } else {
         return null;
      }
   }

   private void paint(int var1, int var2, Shape var3, Graphics2D var4) {
      Paint var5;
      if (var1 >= 0) {
         var5 = this.getStoredPaint(this.currentStore, var1);
         if (!this.firstEffectivePaint || !var5.equals(Color.white)) {
            this.setBrushPaint(this.currentStore, var4, var1);
            var4.fill(var3);
            this.firstEffectivePaint = false;
         }
      }

      if (var2 >= 0) {
         var5 = this.getStoredPaint(this.currentStore, var2);
         if (!this.firstEffectivePaint || !var5.equals(Color.white)) {
            this.setPenColor(this.currentStore, var4, var2);
            var4.draw(var3);
            this.firstEffectivePaint = false;
         }
      }

   }

   private void paintWithPen(int var1, Shape var2, Graphics2D var3) {
      if (var1 >= 0) {
         Paint var4 = this.getStoredPaint(this.currentStore, var1);
         if (!this.firstEffectivePaint || !var4.equals(Color.white)) {
            this.setPenColor(this.currentStore, var3, var1);
            var3.draw(var2);
            this.firstEffectivePaint = false;
         }
      }

   }

   private float getVerticalAlignmentValue(TextLayout var1, int var2) {
      if (var2 == 8) {
         return -var1.getDescent();
      } else {
         return var2 == 0 ? var1.getAscent() : 0.0F;
      }
   }

   public WMFRecordStore getRecordStore() {
      return this.currentStore;
   }
}
