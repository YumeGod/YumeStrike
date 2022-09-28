package org.apache.fop.pdf;

import java.util.List;

public class PDFDests extends PDFNameTreeNode {
   public PDFDests() {
   }

   public PDFDests(List destinationList) {
      this();
      this.setNames(new PDFArray(this, destinationList));
   }
}
