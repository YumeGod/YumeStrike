package org.apache.fop.pdf;

import java.io.IOException;

public abstract class PDFXObject extends AbstractPDFStream {
   public PDFName getName() {
      return (PDFName)this.get("Name");
   }

   protected void populateStreamDict(Object lengthEntry) {
      this.put("Type", new PDFName("XObject"));
      super.populateStreamDict(lengthEntry);
   }

   protected int getSizeHint() throws IOException {
      return 0;
   }
}
