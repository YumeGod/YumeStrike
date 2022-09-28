package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.xmlgraphics.util.io.ASCIIHexOutputStream;

public class ASCIIHexFilter extends PDFFilter {
   public String getName() {
      return "/ASCIIHexDecode";
   }

   public boolean isASCIIFilter() {
      return true;
   }

   public PDFObject getDecodeParms() {
      return null;
   }

   public OutputStream applyFilter(OutputStream out) throws IOException {
      return (OutputStream)(this.isApplied() ? out : new ASCIIHexOutputStream(out));
   }
}
