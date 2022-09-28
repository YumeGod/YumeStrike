package org.apache.fop.fonts.substitute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.fonts.FontUtil;

public class FontQualifier {
   private static Log log;
   private AttributeValue fontFamilyAttributeValue = null;
   private AttributeValue fontStyleAttributeValue = null;
   private AttributeValue fontWeightAttributeValue = null;

   public void setFontFamily(String fontFamily) {
      AttributeValue fontFamilyAttribute = AttributeValue.valueOf(fontFamily);
      if (fontFamilyAttribute == null) {
         log.error("Invalid font-family value '" + fontFamily + "'");
      } else {
         this.fontFamilyAttributeValue = fontFamilyAttribute;
      }
   }

   public void setFontStyle(String fontStyle) {
      AttributeValue fontStyleAttribute = AttributeValue.valueOf(fontStyle);
      if (fontStyleAttribute != null) {
         this.fontStyleAttributeValue = fontStyleAttribute;
      }

   }

   public void setFontWeight(String fontWeight) {
      AttributeValue fontWeightAttribute = AttributeValue.valueOf(fontWeight);
      if (fontWeightAttribute != null) {
         Iterator it = fontWeightAttribute.iterator();

         while(it.hasNext()) {
            Object weightObj = it.next();
            if (weightObj instanceof String) {
               String weightString = ((String)weightObj).trim();

               try {
                  FontUtil.parseCSS2FontWeight(weightString);
               } catch (IllegalArgumentException var7) {
                  log.error("Invalid font-weight value '" + weightString + "'");
                  return;
               }
            }
         }

         this.fontWeightAttributeValue = fontWeightAttribute;
      }

   }

   public AttributeValue getFontFamily() {
      return this.fontFamilyAttributeValue;
   }

   public AttributeValue getFontStyle() {
      return this.fontStyleAttributeValue == null ? AttributeValue.valueOf("normal") : this.fontStyleAttributeValue;
   }

   public AttributeValue getFontWeight() {
      return this.fontWeightAttributeValue == null ? AttributeValue.valueOf(Integer.toString(400)) : this.fontWeightAttributeValue;
   }

   public boolean hasFontWeight() {
      return this.fontWeightAttributeValue != null;
   }

   public boolean hasFontStyle() {
      return this.fontStyleAttributeValue != null;
   }

   protected List match(FontInfo fontInfo) {
      AttributeValue fontFamilyValue = this.getFontFamily();
      AttributeValue weightValue = this.getFontWeight();
      AttributeValue styleValue = this.getFontStyle();
      List matchingTriplets = new ArrayList();
      Iterator attrIt = fontFamilyValue.iterator();

      label71:
      while(true) {
         String fontFamilyString;
         Map triplets;
         do {
            if (!attrIt.hasNext()) {
               return matchingTriplets;
            }

            fontFamilyString = (String)attrIt.next();
            triplets = fontInfo.getFontTriplets();
         } while(triplets == null);

         Set tripletSet = triplets.keySet();
         Iterator tripletIt = tripletSet.iterator();

         while(true) {
            FontTriplet triplet;
            String fontName;
            do {
               if (!tripletIt.hasNext()) {
                  continue label71;
               }

               triplet = (FontTriplet)tripletIt.next();
               fontName = triplet.getName();
            } while(!fontFamilyString.toLowerCase().equals(fontName.toLowerCase()));

            boolean weightMatched = false;
            int fontWeight = triplet.getWeight();
            Iterator weightIt = weightValue.iterator();

            while(weightIt.hasNext()) {
               Object weightObj = weightIt.next();
               if (weightObj instanceof FontWeightRange) {
                  FontWeightRange intRange = (FontWeightRange)weightObj;
                  if (intRange.isWithinRange(fontWeight)) {
                     weightMatched = true;
                  }
               } else {
                  int fontWeightValue;
                  if (weightObj instanceof String) {
                     String fontWeightString = (String)weightObj;
                     fontWeightValue = FontUtil.parseCSS2FontWeight(fontWeightString);
                     if (fontWeightValue == fontWeight) {
                        weightMatched = true;
                     }
                  } else if (weightObj instanceof Integer) {
                     Integer fontWeightInteger = (Integer)weightObj;
                     fontWeightValue = fontWeightInteger;
                     if (fontWeightValue == fontWeight) {
                        weightMatched = true;
                     }
                  }
               }
            }

            boolean styleMatched = false;
            String fontStyleString = triplet.getStyle();
            Iterator styleIt = styleValue.iterator();

            while(styleIt.hasNext()) {
               String style = (String)styleIt.next();
               if (fontStyleString.equals(style)) {
                  styleMatched = true;
               }
            }

            if (weightMatched && styleMatched) {
               matchingTriplets.add(triplet);
            }
         }
      }
   }

   protected FontTriplet bestMatch(FontInfo fontInfo) {
      List matchingTriplets = this.match(fontInfo);
      FontTriplet bestTriplet = null;
      if (matchingTriplets.size() == 1) {
         bestTriplet = (FontTriplet)matchingTriplets.get(0);
      } else {
         Iterator iterator = matchingTriplets.iterator();

         while(iterator.hasNext()) {
            FontTriplet triplet = (FontTriplet)iterator.next();
            if (bestTriplet == null) {
               bestTriplet = triplet;
            } else {
               int priority = triplet.getPriority();
               if (priority < bestTriplet.getPriority()) {
                  bestTriplet = triplet;
               }
            }
         }
      }

      return bestTriplet;
   }

   public List getTriplets() {
      List triplets = new ArrayList();
      AttributeValue fontFamilyValue = this.getFontFamily();
      Iterator fontFamilyIt = fontFamilyValue.iterator();

      while(fontFamilyIt.hasNext()) {
         String name = (String)fontFamilyIt.next();
         AttributeValue styleValue = this.getFontStyle();
         Iterator styleIt = styleValue.iterator();

         label45:
         while(styleIt.hasNext()) {
            String style = (String)styleIt.next();
            AttributeValue weightValue = this.getFontWeight();
            Iterator weightIt = weightValue.iterator();

            while(true) {
               while(true) {
                  if (!weightIt.hasNext()) {
                     continue label45;
                  }

                  Object weightObj = weightIt.next();
                  if (weightObj instanceof FontWeightRange) {
                     FontWeightRange fontWeightRange = (FontWeightRange)weightObj;
                     int[] weightRange = fontWeightRange.toArray();

                     for(int i = 0; i < weightRange.length; ++i) {
                        triplets.add(new FontTriplet(name, style, weightRange[i]));
                     }
                  } else {
                     int weight;
                     if (weightObj instanceof String) {
                        String weightString = (String)weightObj;
                        weight = FontUtil.parseCSS2FontWeight(weightString);
                        triplets.add(new FontTriplet(name, style, weight));
                     } else if (weightObj instanceof Integer) {
                        Integer weightInteger = (Integer)weightObj;
                        weight = weightInteger;
                        triplets.add(new FontTriplet(name, style, weight));
                     }
                  }
               }
            }
         }
      }

      return triplets;
   }

   public String toString() {
      String str = new String();
      if (this.fontFamilyAttributeValue != null) {
         str = str + "font-family=" + this.fontFamilyAttributeValue;
      }

      if (this.fontStyleAttributeValue != null) {
         if (str.length() > 0) {
            str = str + ", ";
         }

         str = str + "font-style=" + this.fontStyleAttributeValue;
      }

      if (this.fontWeightAttributeValue != null) {
         if (str.length() > 0) {
            str = str + ", ";
         }

         str = str + "font-weight=" + this.fontWeightAttributeValue;
      }

      return str;
   }

   static {
      log = LogFactory.getLog(FontQualifier.class);
   }
}
