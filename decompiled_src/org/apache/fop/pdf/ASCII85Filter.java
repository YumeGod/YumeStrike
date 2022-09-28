package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.xmlgraphics.util.io.ASCII85OutputStream;

public class ASCII85Filter extends PDFFilter {
   public String getName() {
      return "/ASCII85Decode";
   }

   public boolean isASCIIFilter() {
      return true;
   }

   public PDFObject getDecodeParms() {
      return null;
   }

   public OutputStream applyFilter(OutputStream out) throws IOException {
      return (OutputStream)(this.isApplied() ? out : new ASCII85OutputStream(out));
   }
}
