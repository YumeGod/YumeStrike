package org.apache.batik.parser;

import java.io.IOException;
import java.util.Calendar;

public class TimingSpecifierParser extends TimingParser {
   protected TimingSpecifierHandler timingSpecifierHandler;

   public TimingSpecifierParser(boolean var1, boolean var2) {
      super(var1, var2);
      this.timingSpecifierHandler = DefaultTimingSpecifierHandler.INSTANCE;
   }

   public void setTimingSpecifierHandler(TimingSpecifierHandler var1) {
      this.timingSpecifierHandler = var1;
   }

   public TimingSpecifierHandler getTimingSpecifierHandler() {
      return this.timingSpecifierHandler;
   }

   protected void doParse() throws ParseException, IOException {
      this.current = this.reader.read();
      Object[] var1 = this.parseTimingSpecifier();
      this.skipSpaces();
      if (this.current != -1) {
         this.reportError("end.of.stream.expected", new Object[]{new Integer(this.current)});
      }

      this.handleTimingSpecifier(var1);
   }

   protected void handleTimingSpecifier(Object[] var1) {
      int var2 = (Integer)var1[0];
      switch (var2) {
         case 0:
            this.timingSpecifierHandler.offset((Float)var1[1]);
            break;
         case 1:
            this.timingSpecifierHandler.syncbase((Float)var1[1], (String)var1[2], (String)var1[3]);
            break;
         case 2:
            this.timingSpecifierHandler.eventbase((Float)var1[1], (String)var1[2], (String)var1[3]);
            break;
         case 3:
            float var3 = (Float)var1[1];
            String var4 = (String)var1[2];
            if (var1[3] == null) {
               this.timingSpecifierHandler.repeat(var3, var4);
            } else {
               this.timingSpecifierHandler.repeat(var3, var4, (Integer)var1[3]);
            }
            break;
         case 4:
            this.timingSpecifierHandler.accesskey((Float)var1[1], (Character)var1[2]);
            break;
         case 5:
            this.timingSpecifierHandler.accessKeySVG12((Float)var1[1], (String)var1[2]);
            break;
         case 6:
            this.timingSpecifierHandler.mediaMarker((String)var1[1], (String)var1[2]);
            break;
         case 7:
            this.timingSpecifierHandler.wallclock((Calendar)var1[1]);
            break;
         case 8:
            this.timingSpecifierHandler.indefinite();
      }

   }
}
