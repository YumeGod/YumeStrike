package org.apache.fop.fo.properties;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class CommonTextDecoration {
   private static final int UNDERLINE = 1;
   private static final int OVERLINE = 2;
   private static final int LINE_THROUGH = 4;
   private static final int BLINK = 8;
   private int decoration;
   private Color underColor;
   private Color overColor;
   private Color throughColor;

   public static CommonTextDecoration createFromPropertyList(PropertyList pList) throws PropertyException {
      return calcTextDecoration(pList);
   }

   private static CommonTextDecoration calcTextDecoration(PropertyList pList) throws PropertyException {
      CommonTextDecoration deco = null;
      PropertyList parentList = pList.getParentPropertyList();
      if (parentList != null) {
         deco = calcTextDecoration(parentList);
      }

      Property textDecoProp = pList.getExplicit(248);
      if (textDecoProp != null) {
         List list = textDecoProp.getList();
         Iterator i = list.iterator();

         while(i.hasNext()) {
            Property prop = (Property)i.next();
            int propEnum = prop.getEnum();
            FOUserAgent ua = pList == null ? null : (pList.getFObj() == null ? null : pList.getFObj().getUserAgent());
            if (propEnum == 95) {
               if (deco != null) {
                  deco.decoration = 0;
               }

               return deco;
            }

            if (propEnum == 153) {
               if (deco == null) {
                  deco = new CommonTextDecoration();
               }

               deco.decoration |= 1;
               deco.underColor = pList.get(72).getColor(ua);
            } else if (propEnum == 92) {
               if (deco != null) {
                  deco.decoration &= 14;
                  deco.underColor = pList.get(72).getColor(ua);
               }
            } else if (propEnum == 103) {
               if (deco == null) {
                  deco = new CommonTextDecoration();
               }

               deco.decoration |= 2;
               deco.overColor = pList.get(72).getColor(ua);
            } else if (propEnum == 91) {
               if (deco != null) {
                  deco.decoration &= 13;
                  deco.overColor = pList.get(72).getColor(ua);
               }
            } else if (propEnum == 77) {
               if (deco == null) {
                  deco = new CommonTextDecoration();
               }

               deco.decoration |= 4;
               deco.throughColor = pList.get(72).getColor(ua);
            } else if (propEnum == 90) {
               if (deco != null) {
                  deco.decoration &= 11;
                  deco.throughColor = pList.get(72).getColor(ua);
               }
            } else if (propEnum == 17) {
               if (deco == null) {
                  deco = new CommonTextDecoration();
               }

               deco.decoration |= 8;
            } else {
               if (propEnum != 86) {
                  throw new PropertyException("Illegal value encountered: " + prop.getString());
               }

               if (deco != null) {
                  deco.decoration &= 7;
               }
            }
         }
      }

      return deco;
   }

   public boolean hasUnderline() {
      return (this.decoration & 1) != 0;
   }

   public boolean hasOverline() {
      return (this.decoration & 2) != 0;
   }

   public boolean hasLineThrough() {
      return (this.decoration & 4) != 0;
   }

   public boolean isBlinking() {
      return (this.decoration & 8) != 0;
   }

   public Color getUnderlineColor() {
      return this.underColor;
   }

   public Color getOverlineColor() {
      return this.overColor;
   }

   public Color getLineThroughColor() {
      return this.throughColor;
   }
}
