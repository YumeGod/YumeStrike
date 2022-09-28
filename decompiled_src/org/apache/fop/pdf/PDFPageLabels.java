package org.apache.fop.pdf;

public class PDFPageLabels extends PDFNumberTreeNode {
   public PDFNumsArray getNums() {
      PDFNumsArray nums = super.getNums();
      if (nums == null) {
         nums = new PDFNumsArray(this);
         this.setNums(nums);
      }

      return nums;
   }
}
