package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;

public class PDFRoot extends PDFDictionary {
   public static final int PAGEMODE_USENONE = 0;
   public static final int PAGEMODE_USEOUTLINES = 1;
   public static final int PAGEMODE_USETHUMBS = 2;
   public static final int PAGEMODE_FULLSCREEN = 3;
   private static final PDFName[] PAGEMODE_NAMES = new PDFName[]{new PDFName("UseNone"), new PDFName("UseOutlines"), new PDFName("UseThumbs"), new PDFName("FullScreen")};

   public PDFRoot(int objnum, PDFPages pages) {
      this.setObjectNumber(objnum);
      this.put("Type", new PDFName("Catalog"));
      this.setRootPages(pages);
   }

   protected int output(OutputStream stream) throws IOException {
      this.getDocument().getProfile().verifyTaggedPDF();
      return super.output(stream);
   }

   public void setPageMode(int mode) {
      this.put("PageMode", PAGEMODE_NAMES[mode]);
   }

   public int getPageMode() {
      PDFName mode = (PDFName)this.get("PageMode");
      if (mode != null) {
         for(int i = 0; i < PAGEMODE_NAMES.length; ++i) {
            if (PAGEMODE_NAMES[i].equals(mode)) {
               return i;
            }
         }

         throw new IllegalStateException("Unknown /PageMode encountered: " + mode);
      } else {
         return 0;
      }
   }

   public void addPage(PDFPage page) {
      PDFPages pages = this.getRootPages();
      pages.addPage(page);
   }

   public void setRootPages(PDFPages pages) {
      this.put("Pages", pages.makeReference());
   }

   public PDFPages getRootPages() {
      PDFReference ref = (PDFReference)this.get("Pages");
      return ref != null ? (PDFPages)ref.getObject() : null;
   }

   public void setPageLabels(PDFPageLabels pageLabels) {
      this.put("PageLabels", pageLabels.makeReference());
   }

   public PDFPageLabels getPageLabels() {
      PDFReference ref = (PDFReference)this.get("PageLabels");
      return ref != null ? (PDFPageLabels)ref.getObject() : null;
   }

   public void setRootOutline(PDFOutline out) {
      this.put("Outlines", out.makeReference());
      PDFName mode = (PDFName)this.get("PageMode");
      if (mode == null) {
         this.setPageMode(1);
      }

   }

   public PDFOutline getRootOutline() {
      PDFReference ref = (PDFReference)this.get("Outlines");
      return ref != null ? (PDFOutline)ref.getObject() : null;
   }

   public void setNames(PDFNames names) {
      this.put("Names", names.makeReference());
   }

   public PDFNames getNames() {
      PDFReference ref = (PDFReference)this.get("Names");
      return ref != null ? (PDFNames)ref.getObject() : null;
   }

   public void setMetadata(PDFMetadata meta) {
      if (this.getDocumentSafely().getPDFVersion() >= 4) {
         this.put("Metadata", meta.makeReference());
      }

   }

   public PDFMetadata getMetadata() {
      PDFReference ref = (PDFReference)this.get("Metadata");
      return ref != null ? (PDFMetadata)ref.getObject() : null;
   }

   public PDFArray getOutputIntents() {
      return (PDFArray)this.get("OutputIntents");
   }

   public void addOutputIntent(PDFOutputIntent outputIntent) {
      if (this.getDocumentSafely().getPDFVersion() >= 4) {
         PDFArray outputIntents = this.getOutputIntents();
         if (outputIntents == null) {
            outputIntents = new PDFArray(this);
            this.put("OutputIntents", outputIntents);
         }

         outputIntents.add(outputIntent);
      }

   }

   public String getLanguage() {
      return (String)this.get("Lang");
   }

   public void setLanguage(String lang) {
      if (lang == null) {
         throw new NullPointerException("lang must not be null");
      } else {
         this.put("Lang", lang);
      }
   }

   public void setStructTreeRoot(PDFStructTreeRoot structTreeRoot) {
      if (structTreeRoot == null) {
         throw new NullPointerException("structTreeRoot must not be null");
      } else {
         this.put("StructTreeRoot", structTreeRoot);
      }
   }

   public PDFStructTreeRoot getStructTreeRoot() {
      return (PDFStructTreeRoot)this.get("StructTreeRoot");
   }

   public void makeTagged() {
      PDFDictionary dict = new PDFDictionary();
      dict.put("Marked", Boolean.TRUE);
      this.put("MarkInfo", dict);
   }

   public PDFDictionary getMarkInfo() {
      return (PDFDictionary)this.get("MarkInfo");
   }
}
