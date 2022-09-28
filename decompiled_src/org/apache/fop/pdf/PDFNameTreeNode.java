package org.apache.fop.pdf;

public class PDFNameTreeNode extends PDFDictionary {
   private static final String KIDS = "Kids";
   private static final String NAMES = "Names";
   private static final String LIMITS = "Limits";

   public void setKids(PDFArray kids) {
      this.put("Kids", kids);
   }

   public PDFArray getKids() {
      return (PDFArray)this.get("Kids");
   }

   public void setNames(PDFArray names) {
      this.put("Names", names);
   }

   public PDFArray getNames() {
      return (PDFArray)this.get("Names");
   }

   public void setLowerLimit(String key) {
      PDFArray limits = this.prepareLimitsArray();
      limits.set(0, key);
   }

   public String getLowerLimit() {
      PDFArray limits = this.prepareLimitsArray();
      return (String)limits.get(0);
   }

   public void setUpperLimit(String key) {
      PDFArray limits = this.prepareLimitsArray();
      limits.set(1, key);
   }

   public String getUpperLimit() {
      PDFArray limits = this.prepareLimitsArray();
      return (String)limits.get(1);
   }

   private PDFArray prepareLimitsArray() {
      PDFArray limits = (PDFArray)this.get("Limits");
      if (limits == null) {
         limits = new PDFArray(this, new Object[2]);
         this.put("Limits", limits);
      }

      if (limits.length() != 2) {
         throw new IllegalStateException("Limits array must have 2 entries");
      } else {
         return limits;
      }
   }
}
