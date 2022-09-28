package org.apache.fop.pdf;

public class PDFEncryptionParams {
   private String userPassword = "";
   private String ownerPassword = "";
   private boolean allowPrint = true;
   private boolean allowCopyContent = true;
   private boolean allowEditContent = true;
   private boolean allowEditAnnotations = true;

   public PDFEncryptionParams(String userPassword, String ownerPassword, boolean allowPrint, boolean allowCopyContent, boolean allowEditContent, boolean allowEditAnnotations) {
      this.setUserPassword(userPassword);
      this.setOwnerPassword(ownerPassword);
      this.setAllowPrint(allowPrint);
      this.setAllowCopyContent(allowCopyContent);
      this.setAllowEditContent(allowEditContent);
      this.setAllowEditAnnotations(allowEditAnnotations);
   }

   public PDFEncryptionParams() {
   }

   public boolean isAllowCopyContent() {
      return this.allowCopyContent;
   }

   public boolean isAllowEditAnnotations() {
      return this.allowEditAnnotations;
   }

   public boolean isAllowEditContent() {
      return this.allowEditContent;
   }

   public boolean isAllowPrint() {
      return this.allowPrint;
   }

   public String getOwnerPassword() {
      return this.ownerPassword;
   }

   public String getUserPassword() {
      return this.userPassword;
   }

   public void setAllowCopyContent(boolean allowCopyContent) {
      this.allowCopyContent = allowCopyContent;
   }

   public void setAllowEditAnnotations(boolean allowEditAnnotations) {
      this.allowEditAnnotations = allowEditAnnotations;
   }

   public void setAllowEditContent(boolean allowEditContent) {
      this.allowEditContent = allowEditContent;
   }

   public void setAllowPrint(boolean allowPrint) {
      this.allowPrint = allowPrint;
   }

   public void setOwnerPassword(String ownerPassword) {
      if (ownerPassword == null) {
         this.ownerPassword = "";
      } else {
         this.ownerPassword = ownerPassword;
      }

   }

   public void setUserPassword(String userPassword) {
      if (userPassword == null) {
         this.userPassword = "";
      } else {
         this.userPassword = userPassword;
      }

   }
}
