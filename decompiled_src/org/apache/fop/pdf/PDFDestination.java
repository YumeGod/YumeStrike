package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.commons.io.output.CountingOutputStream;

public class PDFDestination extends PDFObject {
   private String idRef;
   private Object goToReference;

   public PDFDestination(String idRef, Object goToRef) {
      this.goToReference = goToRef;
      this.idRef = idRef;
   }

   protected int output(OutputStream stream) throws IOException {
      CountingOutputStream cout = new CountingOutputStream(stream);
      Writer writer = PDFDocument.getWriterFor(cout);
      this.formatObject(this.getIDRef(), cout, writer);
      writer.write(32);
      this.formatObject(this.goToReference, cout, writer);
      writer.flush();
      return cout.getCount();
   }

   /** @deprecated */
   public void setGoToReference(String goToReference) {
      this.goToReference = goToReference;
   }

   public void setGoToReference(Object goToReference) {
      this.goToReference = goToReference;
   }

   public Object getGoToReference() {
      return this.goToReference;
   }

   public String getIDRef() {
      return this.idRef;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof PDFDestination) {
         PDFDestination dest = (PDFDestination)obj;
         return dest.getIDRef().equals(this.getIDRef());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.getIDRef().hashCode();
   }
}
