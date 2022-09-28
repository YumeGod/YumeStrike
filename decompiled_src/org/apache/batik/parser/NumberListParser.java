package org.apache.batik.parser;

import java.io.IOException;

public class NumberListParser extends NumberParser {
   protected NumberListHandler numberListHandler;

   public NumberListParser() {
      this.numberListHandler = DefaultNumberListHandler.INSTANCE;
   }

   public void setNumberListHandler(NumberListHandler var1) {
      this.numberListHandler = var1;
   }

   public NumberListHandler getNumberListHandler() {
      return this.numberListHandler;
   }

   protected void doParse() throws ParseException, IOException {
      this.numberListHandler.startNumberList();
      this.current = this.reader.read();
      this.skipSpaces();

      try {
         do {
            this.numberListHandler.startNumber();
            float var1 = this.parseFloat();
            this.numberListHandler.numberValue(var1);
            this.numberListHandler.endNumber();
            this.skipCommaSpaces();
         } while(this.current != -1);
      } catch (NumberFormatException var2) {
         this.reportUnexpectedCharacterError(this.current);
      }

      this.numberListHandler.endNumberList();
   }
}
