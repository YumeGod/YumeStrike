package org.apache.fop.pdf;

public class PDFNumberTreeNode extends PDFDictionary {
   private static final String KIDS = "Kids";
   private static final String NUMS = "Nums";
   private static final String LIMITS = "Limits";

   public void setKids(PDFArray kids) {
      this.put("Kids", kids);
   }

   public PDFArray getKids() {
      return (PDFArray)this.get("Kids");
   }

   public void setNums(PDFNumsArray nums) {
      this.put("Nums", nums);
   }

   public PDFNumsArray getNums() {
      return (PDFNumsArray)this.get("Nums");
   }

   public void setLowerLimit(Integer key) {
      PDFArray limits = this.prepareLimitsArray();
      limits.set(0, key);
   }

   public Integer getLowerLimit() {
      PDFArray limits = this.prepareLimitsArray();
      return (Integer)limits.get(0);
   }

   public void setUpperLimit(Integer key) {
      PDFArray limits = this.prepareLimitsArray();
      limits.set(1, key);
   }

   public Integer getUpperLimit() {
      PDFArray limits = this.prepareLimitsArray();
      return (Integer)limits.get(1);
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
