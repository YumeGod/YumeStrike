package org.apache.xerces.util;

final class XMLErrorCode {
   private String fDomain;
   private String fKey;

   public XMLErrorCode(String var1, String var2) {
      this.fDomain = var1;
      this.fKey = var2;
   }

   public void setValues(String var1, String var2) {
      this.fDomain = var1;
      this.fKey = var2;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof XMLErrorCode)) {
         return false;
      } else {
         XMLErrorCode var2 = (XMLErrorCode)var1;
         return this.fDomain.equals(var2.fDomain) && this.fKey.equals(var2.fKey);
      }
   }

   public int hashCode() {
      return this.fDomain.hashCode() + this.fKey.hashCode();
   }
}
