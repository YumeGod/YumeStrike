package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.PercentBase;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.pagination.ColorProfile;
import org.apache.fop.fo.pagination.Declarations;
import org.apache.fop.fo.properties.ColorProperty;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.util.ColorUtil;

class ICCColorFunction extends FunctionBase {
   public int nbArgs() {
      return -4;
   }

   public PercentBase getPercentBase() {
      return new ICCPercentBase();
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      String colorProfileName = args[3].getString();
      Declarations decls = pInfo.getFO().getRoot().getDeclarations();
      ColorProfile cp = null;
      if (decls == null) {
         if (!ColorUtil.isPseudoProfile(colorProfileName)) {
            Property[] rgbArgs = new Property[3];
            System.arraycopy(args, 0, rgbArgs, 0, 3);
            return (new RGBColorFunction()).eval(rgbArgs, pInfo);
         }
      } else {
         cp = decls.getColorProfile(colorProfileName);
         if (cp == null && !ColorUtil.isPseudoProfile(colorProfileName)) {
            PropertyException pe = new PropertyException("The " + colorProfileName + " color profile was not declared");
            pe.setPropertyInfo(pInfo);
            throw pe;
         }
      }

      String src = cp != null ? cp.getSrc() : "";
      float red = 0.0F;
      float green = 0.0F;
      float blue = 0.0F;
      red = args[0].getNumber().floatValue();
      green = args[1].getNumber().floatValue();
      blue = args[2].getNumber().floatValue();
      if (!(red < 0.0F) && !(red > 255.0F) && !(green < 0.0F) && !(green > 255.0F) && !(blue < 0.0F) && !(blue > 255.0F)) {
         StringBuffer sb = new StringBuffer();
         sb.append("fop-rgb-icc(");
         sb.append(red / 255.0F);
         sb.append(',').append(green / 255.0F);
         sb.append(',').append(blue / 255.0F);

         for(int ix = 3; ix < args.length; ++ix) {
            if (ix == 3) {
               sb.append(',').append(colorProfileName);
               sb.append(',').append(src);
            } else {
               sb.append(',').append(args[ix]);
            }
         }

         sb.append(")");
         return ColorProperty.getInstance(pInfo.getUserAgent(), sb.toString());
      } else {
         throw new PropertyException("Color values out of range. Arguments to rgb-icc() must be [0..255] or [0%..100%]");
      }
   }

   private static final class ICCPercentBase implements PercentBase {
      private ICCPercentBase() {
      }

      public int getBaseLength(PercentBaseContext context) throws PropertyException {
         return 0;
      }

      public double getBaseValue() {
         return 255.0;
      }

      public int getDimension() {
         return 0;
      }

      // $FF: synthetic method
      ICCPercentBase(Object x0) {
         this();
      }
   }
}
