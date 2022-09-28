package org.apache.batik.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class GenericDecoder implements CharDecoder {
   protected Reader reader;

   public GenericDecoder(InputStream var1, String var2) throws IOException {
      this.reader = new InputStreamReader(var1, var2);
      this.reader = new BufferedReader(this.reader);
   }

   public GenericDecoder(Reader var1) {
      this.reader = var1;
      if (!(var1 instanceof BufferedReader)) {
         this.reader = new BufferedReader(this.reader);
      }

   }

   public int readChar() throws IOException {
      return this.reader.read();
   }

   public void dispose() throws IOException {
      this.reader.close();
      this.reader = null;
   }
}
