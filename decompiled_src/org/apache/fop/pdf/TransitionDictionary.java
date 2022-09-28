package org.apache.fop.pdf;

public class TransitionDictionary extends PDFDictionary {
   public TransitionDictionary() {
      this.put("Type", new PDFName("Trans"));
   }
}
