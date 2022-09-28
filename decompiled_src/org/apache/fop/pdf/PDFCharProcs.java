package org.apache.fop.pdf;

import java.util.HashMap;
import java.util.Map;

public class PDFCharProcs extends PDFObject {
   protected Map keys = new HashMap();

   public void addCharacter(String name, PDFStream stream) {
      this.keys.put(name, stream);
   }

   public byte[] toPDF() {
      return new byte[0];
   }
}
