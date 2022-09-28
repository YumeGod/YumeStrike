package org.apache.fop.afp.fonts;

import org.apache.fop.fonts.Base14Font;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.base14.Courier;
import org.apache.fop.fonts.base14.CourierBold;
import org.apache.fop.fonts.base14.CourierBoldOblique;
import org.apache.fop.fonts.base14.CourierOblique;
import org.apache.fop.fonts.base14.Helvetica;
import org.apache.fop.fonts.base14.HelveticaBold;
import org.apache.fop.fonts.base14.HelveticaOblique;
import org.apache.fop.fonts.base14.TimesBold;
import org.apache.fop.fonts.base14.TimesBoldItalic;
import org.apache.fop.fonts.base14.TimesItalic;
import org.apache.fop.fonts.base14.TimesRoman;

public class AFPBase12FontCollection implements FontCollection {
   private static final int[] RASTER_SIZES = new int[]{6, 7, 8, 9, 10, 11, 12, 14, 16, 18, 20, 24, 30, 36};
   private static final String[] CHARSET_REF = new String[]{"60", "70", "80", "90", "00", "A0", "B0", "D0", "F0", "H0", "J0", "N0", "T0", "Z0"};

   private void addCharacterSet(RasterFont font, String charsetName, Base14Font base14) {
      for(int i = 0; i < RASTER_SIZES.length; ++i) {
         int size = RASTER_SIZES[i] * 1000;
         FopCharacterSet characterSet = new FopCharacterSet("T1V10500", "Cp500", charsetName + CHARSET_REF[i], base14);
         font.addCharacterSet(size, characterSet);
      }

   }

   private int addFontProperties(FontInfo fontInfo, AFPFont font, String[] names, String style, int weight, int num) {
      String internalFontKey = "F" + num;
      fontInfo.addMetrics(internalFontKey, font);
      fontInfo.addFontProperties(internalFontKey, names, style, weight);
      ++num;
      return num;
   }

   public int setup(int start, FontInfo fontInfo) {
      RasterFont font = null;
      String[] helveticaNames = new String[]{"Helvetica", "Arial", "sans-serif"};
      font = this.createReferencedRasterFont("Helvetica");
      this.addCharacterSet(font, "C0H200", new Helvetica());
      int num = this.addFontProperties(fontInfo, font, helveticaNames, "normal", 400, start);
      font = this.createReferencedRasterFont("Helvetica Italic");
      this.addCharacterSet(font, "C0H300", new HelveticaOblique());
      num = this.addFontProperties(fontInfo, font, helveticaNames, "italic", 400, num);
      font = this.createReferencedRasterFont("Helvetica (Semi) Bold");
      this.addCharacterSet(font, "C0H400", new HelveticaBold());
      num = this.addFontProperties(fontInfo, font, helveticaNames, "normal", 700, num);
      font = this.createReferencedRasterFont("Helvetica Italic (Semi) Bold");
      this.addCharacterSet(font, "C0H500", new HelveticaOblique());
      num = this.addFontProperties(fontInfo, font, helveticaNames, "italic", 700, num);
      String[] timesNames = new String[]{"Times", "TimesRoman", "Times Roman", "Times-Roman", "Times New Roman", "TimesNewRoman", "serif", "any"};
      font = this.createReferencedRasterFont("Times Roman");
      this.addCharacterSet(font, "CON200", new TimesRoman());
      num = this.addFontProperties(fontInfo, font, timesNames, "normal", 400, num);
      font = this.createReferencedRasterFont("Times Roman Italic");
      this.addCharacterSet(font, "CON300", new TimesItalic());
      num = this.addFontProperties(fontInfo, font, timesNames, "italic", 400, num);
      font = this.createReferencedRasterFont("Times Roman Bold");
      this.addCharacterSet(font, "CON400", new TimesBold());
      num = this.addFontProperties(fontInfo, font, timesNames, "normal", 700, num);
      font = this.createReferencedRasterFont("Times Roman Italic Bold");
      this.addCharacterSet(font, "CON500", new TimesBoldItalic());
      num = this.addFontProperties(fontInfo, font, timesNames, "italic", 700, num);
      String[] courierNames = new String[]{"Courier", "monospace"};
      font = this.createReferencedRasterFont("Courier");
      this.addCharacterSet(font, "C04200", new Courier());
      num = this.addFontProperties(fontInfo, font, courierNames, "normal", 400, num);
      font = this.createReferencedRasterFont("Courier Italic");
      this.addCharacterSet(font, "C04300", new CourierOblique());
      num = this.addFontProperties(fontInfo, font, courierNames, "italic", 400, num);
      font = this.createReferencedRasterFont("Courier Bold");
      this.addCharacterSet(font, "C04400", new CourierBold());
      num = this.addFontProperties(fontInfo, font, courierNames, "normal", 700, num);
      font = this.createReferencedRasterFont("Courier Italic Bold");
      this.addCharacterSet(font, "C04500", new CourierBoldOblique());
      num = this.addFontProperties(fontInfo, font, courierNames, "italic", 700, num);
      return num;
   }

   private RasterFont createReferencedRasterFont(String fontFamily) {
      RasterFont font = new RasterFont(fontFamily);
      font.setEmbeddable(false);
      return font;
   }
}
