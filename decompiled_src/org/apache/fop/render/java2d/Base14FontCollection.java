package org.apache.fop.render.java2d;

import java.awt.Graphics2D;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;

public class Base14FontCollection implements FontCollection {
   private Graphics2D graphics2d = null;

   public Base14FontCollection(Graphics2D graphics2d) {
      this.graphics2d = graphics2d;
   }

   public int setup(int start, FontInfo fontInfo) {
      int normal = false;
      int bold = true;
      int italic = true;
      int bolditalic = true;
      FontMetricsMapper metric = new SystemFontMetricsMapper("SansSerif", 0, this.graphics2d);
      fontInfo.addMetrics("F1", metric);
      metric = new SystemFontMetricsMapper("SansSerif", 2, this.graphics2d);
      fontInfo.addMetrics("F2", metric);
      metric = new SystemFontMetricsMapper("SansSerif", 1, this.graphics2d);
      fontInfo.addMetrics("F3", metric);
      metric = new SystemFontMetricsMapper("SansSerif", 3, this.graphics2d);
      fontInfo.addMetrics("F4", metric);
      metric = new SystemFontMetricsMapper("Serif", 0, this.graphics2d);
      fontInfo.addMetrics("F5", metric);
      metric = new SystemFontMetricsMapper("Serif", 2, this.graphics2d);
      fontInfo.addMetrics("F6", metric);
      metric = new SystemFontMetricsMapper("Serif", 1, this.graphics2d);
      fontInfo.addMetrics("F7", metric);
      metric = new SystemFontMetricsMapper("Serif", 3, this.graphics2d);
      fontInfo.addMetrics("F8", metric);
      metric = new SystemFontMetricsMapper("MonoSpaced", 0, this.graphics2d);
      fontInfo.addMetrics("F9", metric);
      metric = new SystemFontMetricsMapper("MonoSpaced", 2, this.graphics2d);
      fontInfo.addMetrics("F10", metric);
      metric = new SystemFontMetricsMapper("MonoSpaced", 1, this.graphics2d);
      fontInfo.addMetrics("F11", metric);
      metric = new SystemFontMetricsMapper("MonoSpaced", 3, this.graphics2d);
      fontInfo.addMetrics("F12", metric);
      metric = new SystemFontMetricsMapper("Serif", 0, this.graphics2d);
      fontInfo.addMetrics("F13", metric);
      fontInfo.addMetrics("F14", metric);
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
