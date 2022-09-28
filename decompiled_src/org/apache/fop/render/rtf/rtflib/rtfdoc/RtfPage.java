package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfPage extends RtfContainer {
   private final RtfAttributes attrib;
   public static final String PAGE_WIDTH = "paperw";
   public static final String PAGE_HEIGHT = "paperh";
   public static final String LANDSCAPE = "landscape";
   public static final String MARGIN_TOP = "margt";
   public static final String MARGIN_BOTTOM = "margb";
   public static final String MARGIN_LEFT = "margl";
   public static final String MARGIN_RIGHT = "margr";
   public static final String HEADERY = "headery";
   public static final String FOOTERY = "footery";
   public static final String ITAP = "itap";
   public static final String[] PAGE_ATTR = new String[]{"paperw", "paperh", "landscape", "margt", "margb", "margl", "margr", "headery", "footery", "itap"};

   RtfPage(RtfPageArea parent, Writer w, RtfAttributes attrs) throws IOException {
      super(parent, w);
      this.attrib = attrs;
   }

   protected void writeRtfContent() throws IOException {
      this.writeAttributes(this.attrib, PAGE_ATTR);
      if (this.attrib != null) {
         Object widthRaw = this.attrib.getValue("paperw");
         Object heightRaw = this.attrib.getValue("paperh");
         if (widthRaw instanceof Integer && heightRaw instanceof Integer && (Integer)widthRaw > (Integer)heightRaw) {
            this.writeControlWord("landscape");
         }
      }

   }

   public RtfAttributes getAttributes() {
      return this.attrib;
   }

   protected boolean okToWriteRtf() {
      return true;
   }
}
