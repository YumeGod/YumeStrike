package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.util.StringTokenizer;

public final class SdtImageData {
   private StringBuffer buffer;
   private String data;

   public SdtImageData() {
      this.clear();
   }

   public void clear() {
      this.buffer = new StringBuffer(16384);
      this.data = null;
   }

   public void append(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, " \t\n\r");

      while(var2.hasMoreTokens()) {
         this.buffer.append(var2.nextToken());
      }

   }

   public String data() {
      if (this.data == null) {
         this.data = this.buffer.toString();
      }

      return this.data;
   }

   public void write(File var1) throws Exception {
      byte[] var2 = Base64.decode(this.data());
      if (var2 == null) {
         throw new RuntimeException("cannot decode base64-encoded SdtImageData");
      } else {
         FileOutputStream var3 = new FileOutputStream(var1);

         try {
            var3.write(var2);
            var3.flush();
         } finally {
            var3.close();
         }

      }
   }
}
