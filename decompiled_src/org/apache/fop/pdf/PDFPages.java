package org.apache.fop.pdf;

import java.util.ArrayList;
import java.util.List;

public class PDFPages extends PDFObject {
   protected List kids = new ArrayList();
   protected int count = 0;

   public PDFPages(int objnum) {
      this.setObjectNumber(objnum);
   }

   public void addPage(PDFPage page) {
      page.setParent(this);
      this.incrementCount();
   }

   public void notifyKidRegistered(PDFPage page) {
      int idx = page.getPageIndex();
      if (idx < 0) {
         this.kids.add(page.referencePDF());
      } else {
         while(true) {
            if (idx <= this.kids.size() - 1) {
               if (this.kids.get(idx) != null) {
                  throw new IllegalStateException("A page already exists at index " + idx + " (zero-based).");
               }

               this.kids.set(idx, page.referencePDF());
               break;
            }

            this.kids.add((Object)null);
         }
      }

   }

   public int getCount() {
      return this.count;
   }

   public void incrementCount() {
      ++this.count;
   }

   public String toPDFString() {
      StringBuffer sb = new StringBuffer(64);
      sb.append(this.getObjectID()).append("<< /Type /Pages\n/Count ").append(this.getCount()).append("\n/Kids [");

      for(int i = 0; i < this.kids.size(); ++i) {
         Object kid = this.kids.get(i);
         if (kid == null) {
            throw new IllegalStateException("Gap in the kids list!");
         }

         sb.append(kid).append(" ");
      }

      sb.append("] >>\nendobj\n");
      return sb.toString();
   }
}
