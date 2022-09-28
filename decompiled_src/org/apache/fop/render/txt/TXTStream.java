package org.apache.fop.render.txt;

import java.io.IOException;
import java.io.OutputStream;

public class TXTStream {
   private static final String DEFAULT_ENCODING = "UTF-8";
   private OutputStream out = null;
   private boolean doOutput = true;
   private String encoding = "UTF-8";

   public TXTStream(OutputStream os) {
      this.out = os;
   }

   public void add(String str) {
      if (this.doOutput) {
         try {
            byte[] buff = str.getBytes(this.encoding);
            this.out.write(buff);
         } catch (IOException var3) {
            throw new RuntimeException(var3.toString());
         }
      }
   }

   public void setDoOutput(boolean doout) {
      this.doOutput = doout;
   }

   public void setEncoding(String encoding) {
      if (encoding != null) {
         this.encoding = encoding;
      } else {
         this.encoding = "UTF-8";
      }

   }
}
