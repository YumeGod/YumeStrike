package org.apache.fop.pdf;

import org.apache.fop.fonts.CIDFontType;

public class PDFCIDFont extends PDFObject {
   private String basefont;
   private CIDFontType cidtype;
   private Integer dw;
   private PDFWArray w;
   private int[] dw2;
   private PDFWArray w2;
   private PDFCIDSystemInfo systemInfo;
   private PDFCIDFontDescriptor descriptor;
   private PDFCMap cmap;
   private PDFStream cidMap;

   public PDFCIDFont(String basefont, CIDFontType cidtype, int dw, int[] w, String registry, String ordering, int supplement, PDFCIDFontDescriptor descriptor) {
      this(basefont, cidtype, dw, new PDFWArray(w), new PDFCIDSystemInfo(registry, ordering, supplement), descriptor);
   }

   public PDFCIDFont(String basefont, CIDFontType cidtype, int dw, int[] w, PDFCIDSystemInfo systemInfo, PDFCIDFontDescriptor descriptor) {
      this(basefont, cidtype, dw, new PDFWArray(w), systemInfo, descriptor);
   }

   public PDFCIDFont(String basefont, CIDFontType cidtype, int dw, PDFWArray w, PDFCIDSystemInfo systemInfo, PDFCIDFontDescriptor descriptor) {
      this.basefont = basefont;
      this.cidtype = cidtype;
      this.dw = new Integer(dw);
      this.w = w;
      this.dw2 = null;
      this.w2 = null;
      this.systemInfo = systemInfo;
      this.descriptor = descriptor;
      this.cidMap = null;
      this.cmap = null;
   }

   public void setDW(int dw) {
      this.dw = new Integer(dw);
   }

   public void setW(PDFWArray w) {
      this.w = w;
   }

   public void setDW2(int[] dw2) {
      this.dw2 = dw2;
   }

   public void setDW2(int posY, int displacementY) {
      this.dw2 = new int[]{posY, displacementY};
   }

   public void setCMAP(PDFCMap cmap) {
      this.cmap = cmap;
   }

   public void setW2(PDFWArray w2) {
      this.w2 = w2;
   }

   public void setCIDMap(PDFStream map) {
      this.cidMap = map;
   }

   public void setCIDMapIdentity() {
      this.cidMap = null;
   }

   protected String getPDFNameForCIDFontType(CIDFontType cidFontType) {
      if (cidFontType == CIDFontType.CIDTYPE0) {
         return cidFontType.getName();
      } else if (cidFontType == CIDFontType.CIDTYPE2) {
         return cidFontType.getName();
      } else {
         throw new IllegalArgumentException("Unsupported CID font type: " + cidFontType.getName());
      }
   }

   public String toPDFString() {
      StringBuffer p = new StringBuffer(128);
      p.append(this.getObjectID());
      p.append("<< /Type /Font");
      p.append("\n/BaseFont /");
      p.append(this.basefont);
      p.append(" \n/CIDToGIDMap ");
      if (this.cidMap != null) {
         p.append(this.cidMap.referencePDF());
      } else {
         p.append("/Identity");
      }

      p.append(" \n/Subtype /");
      p.append(this.getPDFNameForCIDFontType(this.cidtype));
      p.append("\n");
      p.append(this.systemInfo.toPDFString());
      p.append("\n/FontDescriptor ");
      p.append(this.descriptor.referencePDF());
      if (this.cmap != null) {
         p.append("\n/ToUnicode ");
         p.append(this.cmap.referencePDF());
      }

      if (this.dw != null) {
         p.append("\n/DW ");
         p.append(this.dw);
      }

      if (this.w != null) {
         p.append("\n/W ");
         p.append(this.w.toPDFString());
      }

      if (this.dw2 != null) {
         p.append("\n/DW2 [");
         p.append(this.dw2[0]);
         p.append(this.dw2[1]);
         p.append("] \n>>\nendobj\n");
      }

      if (this.w2 != null) {
         p.append("\n/W2 ");
         p.append(this.w2.toPDFString());
         p.append(" \n>>\nendobj\n");
      }

      p.append(" \n>>\nendobj\n");
      return p.toString();
   }
}
