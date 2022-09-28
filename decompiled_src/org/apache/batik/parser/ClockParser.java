package org.apache.batik.parser;

import java.io.IOException;

public class ClockParser extends TimingParser {
   protected ClockHandler clockHandler;
   protected boolean parseOffset;

   public ClockParser(boolean var1) {
      super(false, false);
      this.parseOffset = var1;
   }

   public void setClockHandler(ClockHandler var1) {
      this.clockHandler = var1;
   }

   public ClockHandler getClockHandler() {
      return this.clockHandler;
   }

   protected void doParse() throws ParseException, IOException {
      this.current = this.reader.read();
      float var1 = this.parseOffset ? this.parseOffset() : this.parseClockValue();
      if (this.current != -1) {
         this.reportError("end.of.stream.expected", new Object[]{new Integer(this.current)});
      }

      if (this.clockHandler != null) {
         this.clockHandler.clockValue(var1);
      }

   }
}
