package org.apache.fop.pdf;

import java.util.Locale;
import org.apache.fop.util.XMLUtil;

public class PDFStructElem extends PDFDictionary {
   private PDFStructElem parentElement;

   PDFStructElem(PDFObject parent, PDFName structureType) {
      if (parent instanceof PDFStructElem) {
         this.parentElement = (PDFStructElem)parent;
      }

      this.put("Type", new PDFName("StructElem"));
      this.put("S", structureType);
      this.setParent(parent);
   }

   public PDFStructElem getParentStructElem() {
      return this.parentElement;
   }

   public void setParent(PDFObject parent) {
      if (parent != null) {
         this.put("P", new PDFReference(parent));
      }

   }

   private PDFArray getKids() {
      return (PDFArray)this.get("K");
   }

   public void addKid(PDFObject kid) {
      PDFArray kids = this.getKids();
      if (kids == null) {
         kids = new PDFArray();
         this.put("K", kids);
      }

      kids.add(kid);
      this.joinHierarchy();
   }

   private boolean containsKid(PDFObject kid) {
      PDFArray kids = this.getKids();
      return kids != null && kids.contains(kid);
   }

   private void joinHierarchy() {
      if (this.parentElement != null && !this.parentElement.containsKid(this)) {
         this.parentElement.addKid(this);
      }

   }

   public void setMCIDKid(int mcid) {
      this.put("K", mcid);
      this.joinHierarchy();
   }

   public void setPage(PDFPage page) {
      this.put("Pg", page);
   }

   public PDFName getStructureType() {
      return (PDFName)this.get("S");
   }

   private void setLanguage(String language) {
      this.put("Lang", language);
   }

   public void setLanguage(Locale language) {
      this.setLanguage(XMLUtil.toRFC3066(language));
   }

   public String getLanguage() {
      return (String)this.get("Lang");
   }
}
