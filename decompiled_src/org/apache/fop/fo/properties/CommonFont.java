package org.apache.fop.fo.properties;

import java.util.List;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;

public final class CommonFont {
   private static final PropertyCache cache;
   private int hash = 0;
   private final FontFamilyProperty fontFamily;
   private final EnumProperty fontSelectionStrategy;
   private final EnumProperty fontStretch;
   private final EnumProperty fontStyle;
   private final EnumProperty fontVariant;
   private final EnumProperty fontWeight;
   public final Length fontSize;
   public final Numeric fontSizeAdjust;

   private CommonFont(FontFamilyProperty fontFamily, EnumProperty fontSelectionStrategy, EnumProperty fontStretch, EnumProperty fontStyle, EnumProperty fontVariant, EnumProperty fontWeight, Length fontSize, Numeric fontSizeAdjust) {
      this.fontFamily = fontFamily;
      this.fontSelectionStrategy = fontSelectionStrategy;
      this.fontStretch = fontStretch;
      this.fontStyle = fontStyle;
      this.fontVariant = fontVariant;
      this.fontWeight = fontWeight;
      this.fontSize = fontSize;
      this.fontSizeAdjust = fontSizeAdjust;
   }

   public static CommonFont getInstance(PropertyList pList) throws PropertyException {
      FontFamilyProperty fontFamily = (FontFamilyProperty)pList.get(101);
      EnumProperty fontSelectionStrategy = (EnumProperty)pList.get(102);
      EnumProperty fontStretch = (EnumProperty)pList.get(105);
      EnumProperty fontStyle = (EnumProperty)pList.get(106);
      EnumProperty fontVariant = (EnumProperty)pList.get(107);
      EnumProperty fontWeight = (EnumProperty)pList.get(108);
      Numeric fontSizeAdjust = pList.get(104).getNumeric();
      Length fontSize = pList.get(103).getLength();
      CommonFont commonFont = new CommonFont(fontFamily, fontSelectionStrategy, fontStretch, fontStyle, fontVariant, fontWeight, fontSize, fontSizeAdjust);
      return cache.fetch(commonFont);
   }

   private String[] getFontFamily() {
      List lst = this.fontFamily.getList();
      String[] fontFamily = new String[lst.size()];
      int i = 0;

      for(int c = lst.size(); i < c; ++i) {
         fontFamily[i] = ((Property)lst.get(i)).getString();
      }

      return fontFamily;
   }

   public String getFirstFontFamily() {
      return ((Property)this.fontFamily.list.get(0)).getString();
   }

   public int getFontSelectionStrategy() {
      return this.fontSelectionStrategy.getEnum();
   }

   public int getFontStretch() {
      return this.fontStretch.getEnum();
   }

   public int getFontStyle() {
      return this.fontStyle.getEnum();
   }

   public int getFontVariant() {
      return this.fontVariant.getEnum();
   }

   public int getFontWeight() {
      return this.fontWeight.getEnum();
   }

   public Length getFontSize() {
      return this.fontSize;
   }

   public Numeric getFontSizeAdjust() {
      return this.fontSizeAdjust;
   }

   public FontTriplet[] getFontState(FontInfo fontInfo) {
      short font_weight;
      switch (this.fontWeight.getEnum()) {
         case 169:
            font_weight = 100;
            break;
         case 170:
            font_weight = 200;
            break;
         case 171:
            font_weight = 300;
            break;
         case 172:
            font_weight = 400;
            break;
         case 173:
            font_weight = 500;
            break;
         case 174:
            font_weight = 600;
            break;
         case 175:
            font_weight = 700;
            break;
         case 176:
            font_weight = 800;
            break;
         case 177:
            font_weight = 900;
            break;
         default:
            font_weight = 400;
      }

      String style;
      switch (this.fontStyle.getEnum()) {
         case 164:
            style = "italic";
            break;
         case 165:
            style = "oblique";
            break;
         case 166:
            style = "backslant";
            break;
         default:
            style = "normal";
      }

      FontTriplet[] triplets = fontInfo.fontLookup((String[])this.getFontFamily(), style, font_weight);
      return triplets;
   }

   public boolean equals(Object o) {
      if (o == null) {
         return false;
      } else if (this == o) {
         return true;
      } else if (!(o instanceof CommonFont)) {
         return false;
      } else {
         CommonFont cf = (CommonFont)o;
         return cf.fontFamily == this.fontFamily && cf.fontSelectionStrategy == this.fontSelectionStrategy && cf.fontStretch == this.fontStretch && cf.fontStyle == this.fontStyle && cf.fontVariant == this.fontVariant && cf.fontWeight == this.fontWeight && cf.fontSize == this.fontSize && cf.fontSizeAdjust == this.fontSizeAdjust;
      }
   }

   public int hashCode() {
      if (this.hash == -1) {
         int hash = 17;
         hash = 37 * hash + (this.fontSize == null ? 0 : this.fontSize.hashCode());
         hash = 37 * hash + (this.fontSizeAdjust == null ? 0 : this.fontSizeAdjust.hashCode());
         hash = 37 * hash + (this.fontFamily == null ? 0 : this.fontFamily.hashCode());
         hash = 37 * hash + (this.fontSelectionStrategy == null ? 0 : this.fontSelectionStrategy.hashCode());
         hash = 37 * hash + (this.fontStretch == null ? 0 : this.fontStretch.hashCode());
         hash = 37 * hash + (this.fontStyle == null ? 0 : this.fontStyle.hashCode());
         hash = 37 * hash + (this.fontVariant == null ? 0 : this.fontVariant.hashCode());
         hash = 37 * hash + (this.fontStretch == null ? 0 : this.fontStretch.hashCode());
         this.hash = hash;
      }

      return this.hash;
   }

   static {
      cache = new PropertyCache(CommonFont.class);
   }
}
