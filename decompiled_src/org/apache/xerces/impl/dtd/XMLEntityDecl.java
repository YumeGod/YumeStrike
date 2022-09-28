package org.apache.xerces.impl.dtd;

public class XMLEntityDecl {
   public String name;
   public String publicId;
   public String systemId;
   public String baseSystemId;
   public String notation;
   public boolean isPE;
   public boolean inExternal;
   public String value;

   public void setValues(String var1, String var2, String var3, String var4, String var5, boolean var6, boolean var7) {
      this.setValues(var1, var2, var3, var4, var5, (String)null, var6, var7);
   }

   public void setValues(String var1, String var2, String var3, String var4, String var5, String var6, boolean var7, boolean var8) {
      this.name = var1;
      this.publicId = var2;
      this.systemId = var3;
      this.baseSystemId = var4;
      this.notation = var5;
      this.value = var6;
      this.isPE = var7;
      this.inExternal = var8;
   }

   public void clear() {
      this.name = null;
      this.publicId = null;
      this.systemId = null;
      this.baseSystemId = null;
      this.notation = null;
      this.value = null;
      this.isPE = false;
      this.inExternal = false;
   }
}
