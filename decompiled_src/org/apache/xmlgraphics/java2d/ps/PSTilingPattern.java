package org.apache.xmlgraphics.java2d.ps;

import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class PSTilingPattern {
   public static final int PATTERN_TYPE_TILING = 1;
   public static final int PATTERN_TYPE_SHADING = 2;
   protected int patternType;
   protected String patternName;
   protected List xUID;
   protected StringBuffer paintProc;
   protected Rectangle2D bBox;
   protected double xStep;
   protected double yStep;
   protected int paintType;
   protected int tilingType;
   protected TexturePaint texture;

   public PSTilingPattern(String patternName, StringBuffer paintProc, Rectangle bBox, double xStep, double yStep, int paintType, int tilingType, List xUID) {
      this.patternType = 1;
      this.patternName = null;
      this.xUID = null;
      this.paintProc = null;
      this.bBox = null;
      this.xStep = 0.0;
      this.yStep = 0.0;
      this.paintType = 2;
      this.tilingType = 1;
      this.texture = null;
      this.patternName = patternName;
      this.paintProc = paintProc;
      this.setBoundingBox(bBox);
      this.setXStep(xStep);
      this.setYStep(yStep);
      this.setPaintType(paintType);
      this.setTilingType(tilingType);
      this.xUID = xUID;
   }

   public PSTilingPattern(String patternName, TexturePaint texture, double xStep, double yStep, int tilingType, List xUID) {
      this(patternName, (StringBuffer)null, new Rectangle(), 1.0, 1.0, 1, tilingType, xUID);
      this.texture = texture;
      Rectangle2D anchor = texture.getAnchorRect();
      this.bBox = new Rectangle2D.Double(anchor.getX(), anchor.getY(), anchor.getX() + anchor.getWidth(), anchor.getY() + anchor.getHeight());
      this.xStep = xStep == 0.0 ? anchor.getWidth() : xStep;
      this.yStep = yStep == 0.0 ? anchor.getHeight() : yStep;
   }

   public String getName() {
      return this.patternName;
   }

   public void setName(String name) {
      if (name == null) {
         throw new NullPointerException("Parameter patternName must not be null");
      } else if (name.length() == 0) {
         throw new IllegalArgumentException("Parameter patternName must not be empty");
      } else if (name.indexOf(" ") >= 0) {
         throw new IllegalArgumentException("Pattern name must not contain any spaces");
      } else {
         this.patternName = name;
      }
   }

   public Rectangle2D getBoundingBox() {
      return this.bBox;
   }

   public void setBoundingBox(Rectangle2D bBox) {
      if (bBox == null) {
         throw new NullPointerException("Parameter bBox must not be null");
      } else {
         this.bBox = bBox;
      }
   }

   public StringBuffer getPaintProc() {
      return this.paintProc;
   }

   public void setPaintProc(StringBuffer paintProc) {
      this.paintProc = paintProc;
   }

   public double getXStep() {
      return this.xStep;
   }

   public void setXStep(double xStep) {
      if (xStep == 0.0) {
         throw new IllegalArgumentException("Parameter xStep must not be 0");
      } else {
         this.xStep = xStep;
      }
   }

   public double getYStep() {
      return this.yStep;
   }

   public void setYStep(double yStep) {
      if (yStep == 0.0) {
         throw new IllegalArgumentException("Parameter yStep must not be 0");
      } else {
         this.yStep = yStep;
      }
   }

   public int getPaintType() {
      return this.paintType;
   }

   public void setPaintType(int paintType) {
      if (paintType != 1 && paintType != 2) {
         throw new IllegalArgumentException("Parameter paintType must not be " + paintType + " (only 1 or 2)");
      } else {
         this.paintType = paintType;
      }
   }

   public int getTilingType() {
      return this.tilingType;
   }

   public void setTilingType(int tilingType) {
      if (tilingType <= 3 && tilingType >= 1) {
         this.tilingType = tilingType;
      } else {
         throw new IllegalArgumentException("Parameter tilingType must not be " + tilingType + " (only 1, 2 or 3)");
      }
   }

   public TexturePaint getTexturePaint() {
      return this.texture;
   }

   public void setTexturePaint(TexturePaint texturePaint) {
      this.texture = texturePaint;
   }

   public List getXUID() {
      return this.xUID;
   }

   public void setXUID(List xUID) {
      this.xUID = xUID;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("<<\n");
      sb.append("/PatternType " + this.patternType + "\n");
      sb.append("/PaintType " + this.paintType + "\n");
      sb.append("/TilingType " + this.tilingType + "\n");
      sb.append("/XStep " + this.xStep + "\n");
      sb.append("/YStep " + this.yStep + "\n");
      sb.append("/BBox [" + this.bBox.getX() + " " + this.bBox.getY() + " " + this.bBox.getWidth() + " " + this.bBox.getHeight() + "]" + "\n");
      sb.append("/PaintProc\n{\n");
      if (this.paintProc == null || this.paintProc.indexOf("pop") != 0) {
         sb.append("pop\n");
      }

      if (this.texture != null) {
         int width = this.texture.getImage().getWidth();
         int height = this.texture.getImage().getHeight();
         Rectangle2D anchor = this.texture.getAnchorRect();
         if (anchor.getX() != 0.0 || anchor.getY() != 0.0) {
            sb.append(anchor.getX() + " " + anchor.getY() + " translate\n");
         }

         double scaleX = anchor.getWidth() / (double)width;
         double scaleY = anchor.getHeight() / (double)height;
         if (scaleX != 1.0 || scaleY != 1.0) {
            sb.append(scaleX + " " + scaleY + " scale\n");
         }

         sb.append(width + " " + height + " 8 " + "matrix\n");
         int[] argb = new int[width * height];
         sb.append("{<");
         this.texture.getImage().getRGB(0, 0, width, height, argb, 0, width);
         int count = 0;

         for(int i = 0; i < argb.length; ++i) {
            if (i % width == 0 || count > 249) {
               sb.append("\n");
               count = 0;
            }

            StringBuffer sRGB = new StringBuffer(Integer.toHexString(argb[i] & 16777215));
            if (sRGB.length() != 6) {
               sRGB.insert(0, "000000");
               sRGB = new StringBuffer(sRGB.substring(sRGB.length() - 6));
            }

            sb.append(sRGB);
            count += 6;
         }

         sb.append("\n>} false 3 colorimage");
      } else {
         sb.append(this.paintProc);
      }

      sb.append("\n} bind \n");
      sb.append(">>\n");
      sb.append("matrix\n");
      sb.append("makepattern\n");
      sb.append("/" + this.patternName + " exch def\n");
      return sb.toString();
   }

   public boolean equals(Object pattern) {
      if (pattern == null) {
         return false;
      } else if (!(pattern instanceof PSTilingPattern)) {
         return false;
      } else if (this == pattern) {
         return true;
      } else {
         PSTilingPattern patternObj = (PSTilingPattern)pattern;
         if (this.patternType != patternObj.patternType) {
            return false;
         } else {
            TexturePaint patternTexture = patternObj.getTexturePaint();
            if ((patternTexture != null || this.texture == null) && (patternTexture == null || this.texture != null)) {
               if (patternTexture != null && this.texture != null) {
                  int width = this.texture.getImage().getWidth();
                  int height = this.texture.getImage().getHeight();
                  int widthPattern = patternTexture.getImage().getWidth();
                  int heightPattern = patternTexture.getImage().getHeight();
                  if (width != widthPattern) {
                     return false;
                  }

                  if (height != heightPattern) {
                     return false;
                  }

                  int[] rgbData = new int[width * height];
                  int[] rgbDataPattern = new int[widthPattern * heightPattern];
                  this.texture.getImage().getRGB(0, 0, width, height, rgbData, 0, width);
                  patternTexture.getImage().getRGB(0, 0, widthPattern, heightPattern, rgbDataPattern, 0, widthPattern);

                  for(int i = 0; i < rgbData.length; ++i) {
                     if (rgbData[i] != rgbDataPattern[i]) {
                        return false;
                     }
                  }
               } else if (!this.paintProc.toString().equals(patternObj.getPaintProc().toString())) {
                  return false;
               }

               if (this.xStep != patternObj.getXStep()) {
                  return false;
               } else if (this.yStep != patternObj.getYStep()) {
                  return false;
               } else if (this.paintType != patternObj.getPaintType()) {
                  return false;
               } else if (this.tilingType != patternObj.getTilingType()) {
                  return false;
               } else if (!this.bBox.equals(patternObj.getBoundingBox())) {
                  return false;
               } else {
                  return this.xUID == null || patternObj.getXUID() == null || this.xUID.equals(patternObj.getXUID());
               }
            } else {
               return false;
            }
         }
      }
   }
}
