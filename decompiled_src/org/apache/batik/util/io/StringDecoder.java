package org.apache.batik.util.io;

import java.io.IOException;

public class StringDecoder implements CharDecoder {
   protected String string;
   protected int length;
   protected int next;

   public StringDecoder(String var1) {
      this.string = var1;
      this.length = var1.length();
   }

   public int readChar() throws IOException {
      return this.next == this.length ? -1 : this.string.charAt(this.next++);
   }

   public void dispose() throws IOException {
      this.string = null;
   }
}
