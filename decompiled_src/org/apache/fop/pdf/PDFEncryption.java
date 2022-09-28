package org.apache.fop.pdf;

public interface PDFEncryption {
   PDFEncryptionParams getParams();

   void setParams(PDFEncryptionParams var1);

   void applyFilter(AbstractPDFStream var1);

   byte[] encrypt(byte[] var1, PDFObject var2);

   String getTrailerEntry();
}
