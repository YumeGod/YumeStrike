package org.apache.fop.util;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.Arrays;

public final class ColorExt extends Color {
   private static final long serialVersionUID = 1L;
   private float rgbReplacementRed;
   private float rgbReplacementGreen;
   private float rgbReplacementBlue;
   private String iccProfileName;
   private String iccProfileSrc;
   private ColorSpace colorSpace;
   private float[] colorValues;

   private ColorExt(ColorSpace colorSpace, float[] colorValues, float opacity) {
      super(colorSpace, colorValues, opacity);
   }

   private ColorExt(float red, float green, float blue, float opacity) {
      super(red, green, blue, opacity);
   }

   public static ColorExt createFromFoRgbIcc(float redReplacement, float greenReplacement, float blueReplacement, String profileName, String profileSrc, ColorSpace colorSpace, float[] iccValues) {
      ColorExt ce = new ColorExt(colorSpace, iccValues, 1.0F);
      ce.rgbReplacementRed = redReplacement;
      ce.rgbReplacementGreen = greenReplacement;
      ce.rgbReplacementBlue = blueReplacement;
      ce.iccProfileName = profileName;
      ce.iccProfileSrc = profileSrc;
      ce.colorSpace = colorSpace;
      ce.colorValues = iccValues;
      return ce;
   }

   public static ColorExt createFromSvgIccColor(float red, float green, float blue, float opacity, String profileName, String profileHref, ColorSpace profileCS, float[] colorValues) {
      ColorExt ce = new ColorExt(red, green, blue, opacity);
      ce.rgbReplacementRed = -1.0F;
      ce.rgbReplacementGreen = -1.0F;
      ce.rgbReplacementBlue = -1.0F;
      ce.iccProfileName = profileName;
      ce.iccProfileSrc = profileHref;
      ce.colorSpace = profileCS;
      ce.colorValues = colorValues;
      return ce;
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!super.equals(obj)) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         ColorExt other = (ColorExt)obj;
         if (!Arrays.equals(this.colorValues, other.colorValues)) {
            return false;
         } else {
            if (this.iccProfileName == null) {
               if (other.iccProfileName != null) {
                  return false;
               }
            } else if (!this.iccProfileName.equals(other.iccProfileName)) {
               return false;
            }

            if (this.iccProfileSrc == null) {
               if (other.iccProfileSrc != null) {
                  return false;
               }
            } else if (!this.iccProfileSrc.equals(other.iccProfileSrc)) {
               return false;
            }

            if (Float.floatToIntBits(this.rgbReplacementBlue) != Float.floatToIntBits(other.rgbReplacementBlue)) {
               return false;
            } else if (Float.floatToIntBits(this.rgbReplacementGreen) != Float.floatToIntBits(other.rgbReplacementGreen)) {
               return false;
            } else {
               return Float.floatToIntBits(this.rgbReplacementRed) == Float.floatToIntBits(other.rgbReplacementRed);
            }
         }
      }
   }

   public String getIccProfileName() {
      return this.iccProfileName;
   }

   public String getIccProfileSrc() {
      return this.iccProfileSrc;
   }

   public ColorSpace getOrigColorSpace() {
      return this.colorSpace;
   }

   public float[] getOriginalColorComponents() {
      float[] copy = new float[this.colorValues.length];
      System.arraycopy(this.colorValues, 0, copy, 0, copy.length);
      return copy;
   }

   public String toFunctionCall() {
      StringBuffer sb = new StringBuffer(40);
      sb.append("fop-rgb-icc(");
      sb.append(this.rgbReplacementRed + ",");
      sb.append(this.rgbReplacementGreen + ",");
      sb.append(this.rgbReplacementBlue + ",");
      sb.append(this.iccProfileName + ",");
      if (this.iccProfileSrc != null) {
         sb.append("\"" + this.iccProfileSrc + "\"");
      }

      float[] colorComponents = this.getColorComponents((float[])null);

      for(int ix = 0; ix < colorComponents.length; ++ix) {
         sb.append(",");
         sb.append(colorComponents[ix]);
      }

      sb.append(")");
      return sb.toString();
   }
}
