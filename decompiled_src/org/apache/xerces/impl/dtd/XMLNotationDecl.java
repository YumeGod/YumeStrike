package org.apache.xerces.impl.dtd;

public class XMLNotationDecl {
   public String name;
   public String publicId;
   public String systemId;
   public String baseSystemId;

   public void setValues(String var1, String var2, String var3, String var4) {
      this.name = var1;
      this.publicId = var2;
      this.systemId = var3;
      this.baseSystemId = var4;
   }

   public void clear() {
      this.name = null;
      this.publicId = null;
      this.systemId = null;
      this.baseSystemId = null;
   }
}
