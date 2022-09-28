package org.apache.fop.afp.fonts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RasterFont extends AFPFont {
   protected static final Log log = LogFactory.getLog("org.apache.fop.afp.fonts");
   private final SortedMap charSets = new TreeMap();
   private Map substitutionCharSets;
   private CharacterSet charSet = null;

   public RasterFont(String name) {
      super(name);
   }

   public void addCharacterSet(int size, CharacterSet characterSet) {
      this.charSets.put(new Integer(size), characterSet);
      this.charSet = characterSet;
   }

   public CharacterSet getCharacterSet(int size) {
      Integer requestedSize = new Integer(size);
      CharacterSet csm = (CharacterSet)this.charSets.get(requestedSize);
      if (csm != null) {
         return csm;
      } else {
         if (this.substitutionCharSets != null) {
            csm = (CharacterSet)this.substitutionCharSets.get(requestedSize);
         }

         if (csm == null && !this.charSets.isEmpty()) {
            SortedMap smallerSizes = this.charSets.headMap(requestedSize);
            SortedMap largerSizes = this.charSets.tailMap(requestedSize);
            int smallerSize = smallerSizes.isEmpty() ? 0 : (Integer)smallerSizes.lastKey();
            int largerSize = largerSizes.isEmpty() ? Integer.MAX_VALUE : (Integer)largerSizes.firstKey();
            Integer fontSize;
            if (!smallerSizes.isEmpty() && size - smallerSize <= largerSize - size) {
               fontSize = new Integer(smallerSize);
            } else {
               fontSize = new Integer(largerSize);
            }

            csm = (CharacterSet)this.charSets.get(fontSize);
            if (csm != null) {
               if (this.substitutionCharSets == null) {
                  this.substitutionCharSets = new HashMap();
               }

               this.substitutionCharSets.put(requestedSize, csm);
               String msg = "No " + (float)size / 1000.0F + "pt font " + this.getFontName() + " found, substituted with " + (float)fontSize / 1000.0F + "pt font";
               log.warn(msg);
            }
         }

         if (csm == null) {
            String msg = "No font found for font " + this.getFontName() + " with point size " + (float)size / 1000.0F;
            log.error(msg);
            throw new FontRuntimeException(msg);
         } else {
            return csm;
         }
      }
   }

   public int getFirstChar() {
      Iterator it = this.charSets.values().iterator();
      if (it.hasNext()) {
         CharacterSet csm = (CharacterSet)it.next();
         return csm.getFirstChar();
      } else {
         String msg = "getFirstChar() - No character set found for font:" + this.getFontName();
         log.error(msg);
         throw new FontRuntimeException(msg);
      }
   }

   public int getLastChar() {
      Iterator it = this.charSets.values().iterator();
      if (it.hasNext()) {
         CharacterSet csm = (CharacterSet)it.next();
         return csm.getLastChar();
      } else {
         String msg = "getLastChar() - No character set found for font:" + this.getFontName();
         log.error(msg);
         throw new FontRuntimeException(msg);
      }
   }

   private int metricsToAbsoluteSize(CharacterSet cs, int value, int givenSize) {
      int nominalVerticalSize = cs.getNominalVerticalSize();
      return nominalVerticalSize != 0 ? value * nominalVerticalSize : value * givenSize;
   }

   public int getAscender(int size) {
      CharacterSet cs = this.getCharacterSet(size);
      return this.metricsToAbsoluteSize(cs, cs.getAscender(), size);
   }

   public int getCapHeight(int size) {
      CharacterSet cs = this.getCharacterSet(size);
      return this.metricsToAbsoluteSize(cs, cs.getCapHeight(), size);
   }

   public int getDescender(int size) {
      CharacterSet cs = this.getCharacterSet(size);
      return this.metricsToAbsoluteSize(cs, cs.getDescender(), size);
   }

   public int getXHeight(int size) {
      CharacterSet cs = this.getCharacterSet(size);
      return this.metricsToAbsoluteSize(cs, cs.getXHeight(), size);
   }

   public int getWidth(int character, int size) {
      CharacterSet cs = this.getCharacterSet(size);
      return this.metricsToAbsoluteSize(cs, cs.getWidth(toUnicodeCodepoint(character)), size);
   }

   public int[] getWidths(int size) {
      CharacterSet cs = this.getCharacterSet(size);
      int[] widths = cs.getWidths();
      int i = 0;

      for(int c = widths.length; i < c; ++i) {
         widths[i] = this.metricsToAbsoluteSize(cs, widths[i], size);
      }

      return widths;
   }

   public int[] getWidths() {
      return this.getWidths(1000);
   }

   public boolean hasChar(char c) {
      return this.charSet.hasChar(c);
   }

   public char mapChar(char c) {
      return this.charSet.mapChar(c);
   }

   public String getEncodingName() {
      return this.charSet.getEncoding();
   }
}
