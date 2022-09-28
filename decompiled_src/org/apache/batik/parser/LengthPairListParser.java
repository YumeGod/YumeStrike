package org.apache.batik.parser;

import java.io.IOException;

public class LengthPairListParser extends LengthListParser {
   protected void doParse() throws ParseException, IOException {
      ((LengthListHandler)this.lengthHandler).startLengthList();
      this.current = this.reader.read();
      this.skipSpaces();

      try {
         while(true) {
            this.lengthHandler.startLength();
            this.parseLength();
            this.lengthHandler.endLength();
            this.skipCommaSpaces();
            this.lengthHandler.startLength();
            this.parseLength();
            this.lengthHandler.endLength();
            this.skipSpaces();
            if (this.current == -1) {
               break;
            }

            if (this.current != 59) {
               this.reportUnexpectedCharacterError(this.current);
            }

            this.current = this.reader.read();
            this.skipSpaces();
         }
      } catch (NumberFormatException var2) {
         this.reportUnexpectedCharacterError(this.current);
      }

      ((LengthListHandler)this.lengthHandler).endLengthList();
   }
}
