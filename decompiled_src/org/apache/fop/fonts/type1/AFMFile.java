package org.apache.fop.fonts.type1;

import java.awt.geom.Dimension2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.fop.fonts.NamedCharacter;
import org.apache.fop.fonts.SingleByteEncoding;
import org.apache.xmlgraphics.java2d.Dimension2DDouble;

public class AFMFile {
   private String fontName;
   private String fullName;
   private String familyName;
   private String weight;
   private RectangularShape fontBBox;
   private String encodingScheme;
   private String characterSet;
   private Number capHeight;
   private Number xHeight;
   private Number ascender;
   private Number descender;
   private Number stdHW;
   private Number stdVW;
   private AFMWritingDirectionMetrics[] writingDirectionMetrics = new AFMWritingDirectionMetrics[3];
   private List charMetrics = new ArrayList();
   private Map charNameToMetrics = new HashMap();
   private int firstChar = -1;
   private int lastChar = -1;
   private Map kerningMap;

   public String getFontName() {
      return this.fontName;
   }

   public void setFontName(String fontName) {
      this.fontName = fontName;
   }

   public String getFullName() {
      return this.fullName;
   }

   public void setFullName(String fullName) {
      this.fullName = fullName;
   }

   public String getFamilyName() {
      return this.familyName;
   }

   public void setFamilyName(String familyName) {
      this.familyName = familyName;
   }

   public String getWeight() {
      return this.weight;
   }

   public void setWeight(String weight) {
      this.weight = weight;
   }

   public RectangularShape getFontBBox() {
      return this.fontBBox;
   }

   public int[] getFontBBoxAsIntArray() {
      RectangularShape rect = this.getFontBBox();
      return new int[]{(int)Math.floor(rect.getMinX()), (int)Math.floor(rect.getMinY()), (int)Math.ceil(rect.getMaxX()), (int)Math.ceil(rect.getMaxY())};
   }

   public void setFontBBox(RectangularShape fontBBox) {
      this.fontBBox = fontBBox;
   }

   public String getEncodingScheme() {
      return this.encodingScheme;
   }

   public void setEncodingScheme(String encodingScheme) {
      this.encodingScheme = encodingScheme;
   }

   public String getCharacterSet() {
      return this.characterSet;
   }

   public void setCharacterSet(String characterSet) {
      this.characterSet = characterSet;
   }

   public Number getCapHeight() {
      return this.capHeight;
   }

   public void setCapHeight(Number capHeight) {
      this.capHeight = capHeight;
   }

   public Number getXHeight() {
      return this.xHeight;
   }

   public void setXHeight(Number height) {
      this.xHeight = height;
   }

   public Number getAscender() {
      return this.ascender;
   }

   public void setAscender(Number ascender) {
      this.ascender = ascender;
   }

   public Number getDescender() {
      return this.descender;
   }

   public void setDescender(Number descender) {
      this.descender = descender;
   }

   public Number getStdHW() {
      return this.stdHW;
   }

   public void setStdHW(Number stdHW) {
      this.stdHW = stdHW;
   }

   public Number getStdVW() {
      return this.stdVW;
   }

   public void setStdVW(Number stdVW) {
      this.stdVW = stdVW;
   }

   public AFMWritingDirectionMetrics getWritingDirectionMetrics(int index) {
      return this.writingDirectionMetrics[index];
   }

   public void setWritingDirectionMetrics(int index, AFMWritingDirectionMetrics metrics) {
      this.writingDirectionMetrics[index] = metrics;
   }

   public void addCharMetrics(AFMCharMetrics metrics) {
      String name = metrics.getCharName();
      if (metrics.getUnicodeSequence() != null) {
         this.charMetrics.add(metrics);
         if (name != null) {
            this.charNameToMetrics.put(name, metrics);
         }

         int idx = metrics.getCharCode();
         if (idx >= 0) {
            if (this.firstChar < 0 || idx < this.firstChar) {
               this.firstChar = idx;
            }

            if (this.lastChar < 0 || idx > this.lastChar) {
               this.lastChar = idx;
            }
         }

      }
   }

   public int getCharCount() {
      return this.charMetrics.size();
   }

   public int getFirstChar() {
      return this.firstChar;
   }

   public int getLastChar() {
      return this.lastChar;
   }

   public AFMCharMetrics getChar(String name) {
      return (AFMCharMetrics)this.charNameToMetrics.get(name);
   }

   public List getCharMetrics() {
      return Collections.unmodifiableList(this.charMetrics);
   }

   public void addXKerning(String name1, String name2, double kx) {
      if (this.kerningMap == null) {
         this.kerningMap = new HashMap();
      }

      Map entries = (Map)this.kerningMap.get(name1);
      if (entries == null) {
         entries = new HashMap();
         this.kerningMap.put(name1, entries);
      }

      ((Map)entries).put(name2, new Dimension2DDouble(kx, 0.0));
   }

   public boolean hasKerning() {
      return this.kerningMap != null;
   }

   public Map createXKerningMapEncoded() {
      if (!this.hasKerning()) {
         return null;
      } else {
         Map m = new HashMap();
         Iterator iterFrom = this.kerningMap.entrySet().iterator();

         while(true) {
            Map.Entry entryFrom;
            AFMCharMetrics chm1;
            do {
               do {
                  if (!iterFrom.hasNext()) {
                     return m;
                  }

                  entryFrom = (Map.Entry)iterFrom.next();
                  String name1 = (String)entryFrom.getKey();
                  chm1 = this.getChar(name1);
               } while(chm1 == null);
            } while(!chm1.hasCharCode());

            Map container = null;
            Map entriesTo = (Map)entryFrom.getValue();
            Iterator iterTo = entriesTo.entrySet().iterator();

            while(iterTo.hasNext()) {
               Map.Entry entryTo = (Map.Entry)iterTo.next();
               String name2 = (String)entryTo.getKey();
               AFMCharMetrics chm2 = this.getChar(name2);
               if (chm2 != null && chm2.hasCharCode()) {
                  if (container == null) {
                     Integer k1 = new Integer(chm1.getCharCode());
                     container = (Map)m.get(k1);
                     if (container == null) {
                        container = new HashMap();
                        m.put(k1, container);
                     }
                  }

                  Dimension2D dim = (Dimension2D)entryTo.getValue();
                  ((Map)container).put(new Integer(chm2.getCharCode()), new Integer((int)Math.round(dim.getWidth())));
               }
            }
         }
      }
   }

   public void overridePrimaryEncoding(SingleByteEncoding encoding) {
      Iterator iter = this.charMetrics.iterator();

      while(iter.hasNext()) {
         AFMCharMetrics cm = (AFMCharMetrics)iter.next();
         NamedCharacter nc = cm.getCharacter();
         if (nc.hasSingleUnicodeValue()) {
            int mapped = encoding.mapChar(nc.getSingleUnicodeValue());
            if (mapped > 0) {
               cm.setCharCode(mapped);
            } else {
               cm.setCharCode(-1);
            }
         } else {
            cm.setCharCode(-1);
         }
      }

   }

   public String toString() {
      return "AFM: " + this.getFullName();
   }
}
