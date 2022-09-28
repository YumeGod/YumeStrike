package org.apache.batik.dom.svg;

import org.apache.batik.parser.DefaultPreserveAspectRatioHandler;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PreserveAspectRatioParser;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGPreserveAspectRatio;

public abstract class AbstractSVGPreserveAspectRatio implements SVGPreserveAspectRatio, SVGConstants {
   protected static final String[] ALIGN_VALUES = new String[]{null, "none", "xMinYMin", "xMidYMin", "xMaxYMin", "xMinYMid", "xMidYMid", "xMaxYMid", "xMinYMax", "xMidYMax", "xMaxYMax"};
   protected static final String[] MEET_OR_SLICE_VALUES = new String[]{null, "meet", "slice"};
   protected short align = 6;
   protected short meetOrSlice = 1;

   public static String getValueAsString(short var0, short var1) {
      if (var0 >= 1 && var0 <= 10) {
         String var2 = ALIGN_VALUES[var0];
         if (var0 == 1) {
            return var2;
         } else {
            return var1 >= 1 && var1 <= 2 ? var2 + ' ' + MEET_OR_SLICE_VALUES[var1] : null;
         }
      } else {
         return null;
      }
   }

   public short getAlign() {
      return this.align;
   }

   public short getMeetOrSlice() {
      return this.meetOrSlice;
   }

   public void setAlign(short var1) {
      this.align = var1;
      this.setAttributeValue(this.getValueAsString());
   }

   public void setMeetOrSlice(short var1) {
      this.meetOrSlice = var1;
      this.setAttributeValue(this.getValueAsString());
   }

   public void reset() {
      this.align = 6;
      this.meetOrSlice = 1;
   }

   protected abstract void setAttributeValue(String var1) throws DOMException;

   protected abstract DOMException createDOMException(short var1, String var2, Object[] var3);

   protected void setValueAsString(String var1) throws DOMException {
      PreserveAspectRatioParserHandler var2 = new PreserveAspectRatioParserHandler();

      try {
         PreserveAspectRatioParser var3 = new PreserveAspectRatioParser();
         var3.setPreserveAspectRatioHandler(var2);
         var3.parse(var1);
         this.align = var2.getAlign();
         this.meetOrSlice = var2.getMeetOrSlice();
      } catch (ParseException var4) {
         throw this.createDOMException((short)13, "preserve.aspect.ratio", new Object[]{var1});
      }
   }

   protected String getValueAsString() {
      if (this.align >= 1 && this.align <= 10) {
         String var1 = ALIGN_VALUES[this.align];
         if (this.align == 1) {
            return var1;
         } else if (this.meetOrSlice >= 1 && this.meetOrSlice <= 2) {
            return var1 + ' ' + MEET_OR_SLICE_VALUES[this.meetOrSlice];
         } else {
            throw this.createDOMException((short)13, "preserve.aspect.ratio.meet.or.slice", new Object[]{new Integer(this.meetOrSlice)});
         }
      } else {
         throw this.createDOMException((short)13, "preserve.aspect.ratio.align", new Object[]{new Integer(this.align)});
      }
   }

   protected class PreserveAspectRatioParserHandler extends DefaultPreserveAspectRatioHandler {
      public short align = 6;
      public short meetOrSlice = 1;

      public short getAlign() {
         return this.align;
      }

      public short getMeetOrSlice() {
         return this.meetOrSlice;
      }

      public void none() throws ParseException {
         this.align = 1;
      }

      public void xMaxYMax() throws ParseException {
         this.align = 10;
      }

      public void xMaxYMid() throws ParseException {
         this.align = 7;
      }

      public void xMaxYMin() throws ParseException {
         this.align = 4;
      }

      public void xMidYMax() throws ParseException {
         this.align = 9;
      }

      public void xMidYMid() throws ParseException {
         this.align = 6;
      }

      public void xMidYMin() throws ParseException {
         this.align = 3;
      }

      public void xMinYMax() throws ParseException {
         this.align = 8;
      }

      public void xMinYMid() throws ParseException {
         this.align = 5;
      }

      public void xMinYMin() throws ParseException {
         this.align = 2;
      }

      public void meet() throws ParseException {
         this.meetOrSlice = 1;
      }

      public void slice() throws ParseException {
         this.meetOrSlice = 2;
      }
   }
}
