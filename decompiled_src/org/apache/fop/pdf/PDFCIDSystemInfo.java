package org.apache.fop.pdf;

public class PDFCIDSystemInfo extends PDFObject {
   private String registry;
   private String ordering;
   private int supplement;

   public PDFCIDSystemInfo(String registry, String ordering, int supplement) {
      this.registry = registry;
      this.ordering = ordering;
      this.supplement = supplement;
   }

   public String toPDFString() {
      StringBuffer p = new StringBuffer(64);
      p.setLength(0);
      p.append("/CIDSystemInfo << /Registry (");
      p.append(this.registry);
      p.append(") /Ordering (");
      p.append(this.ordering);
      p.append(") /Supplement ");
      p.append(this.supplement);
      p.append(" >>");
      return p.toString();
   }
}
