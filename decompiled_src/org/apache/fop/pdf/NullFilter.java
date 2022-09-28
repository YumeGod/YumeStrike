package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;

public class NullFilter extends PDFFilter {
   public String getName() {
      return "";
   }

   public PDFObject getDecodeParms() {
      return null;
   }

   public OutputStream applyFilter(OutputStream out) throws IOException {
      return out;
   }
}
