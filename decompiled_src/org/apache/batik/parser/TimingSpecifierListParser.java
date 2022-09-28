package org.apache.batik.parser;

import java.io.IOException;

public class TimingSpecifierListParser extends TimingSpecifierParser {
   public TimingSpecifierListParser(boolean var1, boolean var2) {
      super(var1, var2);
      this.timingSpecifierHandler = DefaultTimingSpecifierListHandler.INSTANCE;
   }

   public void setTimingSpecifierListHandler(TimingSpecifierListHandler var1) {
      this.timingSpecifierHandler = var1;
   }

   public TimingSpecifierListHandler getTimingSpecifierListHandler() {
      return (TimingSpecifierListHandler)this.timingSpecifierHandler;
   }

   protected void doParse() throws ParseException, IOException {
      this.current = this.reader.read();
      ((TimingSpecifierListHandler)this.timingSpecifierHandler).startTimingSpecifierList();
      this.skipSpaces();
      if (this.current != -1) {
         while(true) {
            Object[] var1 = this.parseTimingSpecifier();
            this.handleTimingSpecifier(var1);
            this.skipSpaces();
            if (this.current == -1) {
               break;
            }

            if (this.current == 59) {
               this.current = this.reader.read();
            } else {
               this.reportUnexpectedCharacterError(this.current);
            }
         }
      }

      this.skipSpaces();
      if (this.current != -1) {
         this.reportUnexpectedCharacterError(this.current);
      }

      ((TimingSpecifierListHandler)this.timingSpecifierHandler).endTimingSpecifierList();
   }
}
