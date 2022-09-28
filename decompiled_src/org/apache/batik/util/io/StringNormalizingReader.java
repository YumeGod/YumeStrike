package org.apache.batik.util.io;

import java.io.IOException;

public class StringNormalizingReader extends NormalizingReader {
   protected String string;
   protected int length;
   protected int next;
   protected int line = 1;
   protected int column;

   public StringNormalizingReader(String var1) {
      this.string = var1;
      this.length = var1.length();
   }

   public int read() throws IOException {
      int var1 = this.length == this.next ? -1 : this.string.charAt(this.next++);
      if (var1 <= 13) {
         switch (var1) {
            case 10:
               this.column = 0;
               ++this.line;
               break;
            case 13:
               this.column = 0;
               ++this.line;
               int var2 = this.length == this.next ? -1 : this.string.charAt(this.next);
               if (var2 == 10) {
                  ++this.next;
               }

               return 10;
         }
      }

      return var1;
   }

   public int getLine() {
      return this.line;
   }

   public int getColumn() {
      return this.column;
   }

   public void close() throws IOException {
      this.string = null;
   }
}
