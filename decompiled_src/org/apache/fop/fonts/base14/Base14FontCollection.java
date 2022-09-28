package org.apache.fop.fonts.base14;

import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;

public class Base14FontCollection implements FontCollection {
   private boolean kerning = false;

   public Base14FontCollection(boolean kerning) {
      this.kerning = kerning;
   }

   public int setup(int start, FontInfo fontInfo) {
      fontInfo.addMetrics("F1", new Helvetica(this.kerning));
      fontInfo.addMetrics("F2", new HelveticaOblique(this.kerning));
      fontInfo.addMetrics("F3", new HelveticaBold(this.kerning));
      fontInfo.addMetrics("F4", new HelveticaBoldOblique(this.kerning));
      fontInfo.addMetrics("F5", new TimesRoman(this.kerning));
      fontInfo.addMetrics("F6", new TimesItalic(this.kerning));
      fontInfo.addMetrics("F7", new TimesBold(this.kerning));
      fontInfo.addMetrics("F8", new TimesBoldItalic(this.kerning));
      fontInfo.addMetrics("F9", new Courier(this.kerning));
      fontInfo.addMetrics("F10", new CourierOblique(this.kerning));
      fontInfo.addMetrics("F11", new CourierBold(this.kerning));
      fontInfo.addMetrics("F12", new CourierBoldOblique(this.kerning));
      fontInfo.addMetrics("F13", new Symbol());
      fontInfo.addMetrics("F14", new ZapfDingbats());
      fontInfo.addFontProperties("F5", (String)"any", "normal", 400);
      fontInfo.addFontProperties("F6", (String)"any", "italic", 400);
      fontInfo.addFontProperties("F6", (String)"any", "oblique", 400);
      fontInfo.addFontProperties("F7", (String)"any", "normal", 700);
      fontInfo.addFontProperties("F8", (String)"any", "italic", 700);
      fontInfo.addFontProperties("F8", (String)"any", "oblique", 700);
      fontInfo.addFontProperties("F1", (String)"sans-serif", "normal", 400);
      fontInfo.addFontProperties("F2", (String)"sans-serif", "oblique", 400);
      fontInfo.addFontProperties("F2", (String)"sans-serif", "italic", 400);
      fontInfo.addFontProperties("F3", (String)"sans-serif", "normal", 700);
      fontInfo.addFontProperties("F4", (String)"sans-serif", "oblique", 700);
      fontInfo.addFontProperties("F4", (String)"sans-serif", "italic", 700);
      fontInfo.addFontProperties("F1", (String)"SansSerif", "normal", 400);
      fontInfo.addFontProperties("F2", (String)"SansSerif", "oblique", 400);
      fontInfo.addFontProperties("F2", (String)"SansSerif", "italic", 400);
      fontInfo.addFontProperties("F3", (String)"SansSerif", "normal", 700);
      fontInfo.addFontProperties("F4", (String)"SansSerif", "oblique", 700);
      fontInfo.addFontProperties("F4", (String)"SansSerif", "italic", 700);
      fontInfo.addFontProperties("F5", (String)"serif", "normal", 400);
      fontInfo.addFontProperties("F6", (String)"serif", "oblique", 400);
      fontInfo.addFontProperties("F6", (String)"serif", "italic", 400);
      fontInfo.addFontProperties("F7", (String)"serif", "normal", 700);
      fontInfo.addFontProperties("F8", (String)"serif", "oblique", 700);
      fontInfo.addFontProperties("F8", (String)"serif", "italic", 700);
      fontInfo.addFontProperties("F9", (String)"monospace", "normal", 400);
      fontInfo.addFontProperties("F10", (String)"monospace", "oblique", 400);
      fontInfo.addFontProperties("F10", (String)"monospace", "italic", 400);
      fontInfo.addFontProperties("F11", (String)"monospace", "normal", 700);
      fontInfo.addFontProperties("F12", (String)"monospace", "oblique", 700);
      fontInfo.addFontProperties("F12", (String)"monospace", "italic", 700);
      fontInfo.addFontProperties("F9", (String)"Monospaced", "normal", 400);
      fontInfo.addFontProperties("F10", (String)"Monospaced", "oblique", 400);
      fontInfo.addFontProperties("F10", (String)"Monospaced", "italic", 400);
      fontInfo.addFontProperties("F11", (String)"Monospaced", "normal", 700);
      fontInfo.addFontProperties("F12", (String)"Monospaced", "oblique", 700);
      fontInfo.addFontProperties("F12", (String)"Monospaced", "italic", 700);
      fontInfo.addFontProperties("F1", (String)"Helvetica", "normal", 400);
      fontInfo.addFontProperties("F2", (String)"Helvetica", "oblique", 400);
      fontInfo.addFontProperties("F2", (String)"Helvetica", "italic", 400);
      fontInfo.addFontProperties("F3", (String)"Helvetica", "normal", 700);
      fontInfo.addFontProperties("F4", (String)"Helvetica", "oblique", 700);
      fontInfo.addFontProperties("F4", (String)"Helvetica", "italic", 700);
      fontInfo.addFontProperties("F5", (String)"Times", "normal", 400);
      fontInfo.addFontProperties("F6", (String)"Times", "oblique", 400);
      fontInfo.addFontProperties("F6", (String)"Times", "italic", 400);
      fontInfo.addFontProperties("F7", (String)"Times", "normal", 700);
      fontInfo.addFontProperties("F8", (String)"Times", "oblique", 700);
      fontInfo.addFontProperties("F8", (String)"Times", "italic", 700);
      fontInfo.addFontProperties("F9", (String)"Courier", "normal", 400);
      fontInfo.addFontProperties("F10", (String)"Courier", "oblique", 400);
      fontInfo.addFontProperties("F10", (String)"Courier", "italic", 400);
      fontInfo.addFontProperties("F11", (String)"Courier", "normal", 700);
      fontInfo.addFontProperties("F12", (String)"Courier", "oblique", 700);
      fontInfo.addFontProperties("F12", (String)"Courier", "italic", 700);
      fontInfo.addFontProperties("F13", (String)"Symbol", "normal", 400);
      fontInfo.addFontProperties("F14", (String)"ZapfDingbats", "normal", 400);
      fontInfo.addFontProperties("F5", (String)"Times-Roman", "normal", 400);
      fontInfo.addFontProperties("F6", (String)"Times-Roman", "oblique", 400);
      fontInfo.addFontProperties("F6", (String)"Times-Roman", "italic", 400);
      fontInfo.addFontProperties("F7", (String)"Times-Roman", "normal", 700);
      fontInfo.addFontProperties("F8", (String)"Times-Roman", "oblique", 700);
      fontInfo.addFontProperties("F8", (String)"Times-Roman", "italic", 700);
      fontInfo.addFontProperties("F5", (String)"Times Roman", "normal", 400);
      fontInfo.addFontProperties("F6", (String)"Times Roman", "oblique", 400);
      fontInfo.addFontProperties("F6", (String)"Times Roman", "italic", 400);
      fontInfo.addFontProperties("F7", (String)"Times Roman", "normal", 700);
      fontInfo.addFontProperties("F8", (String)"Times Roman", "oblique", 700);
      fontInfo.addFontProperties("F8", (String)"Times Roman", "italic", 700);
      fontInfo.addFontProperties("F9", (String)"Computer-Modern-Typewriter", "normal", 400);
      return 15;
   }
}
