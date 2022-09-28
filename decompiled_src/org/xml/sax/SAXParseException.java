package org.xml.sax;

public class SAXParseException extends SAXException {
   private String publicId;
   private String systemId;
   private int lineNumber;
   private int columnNumber;
   static final long serialVersionUID = -5651165872476709336L;

   public SAXParseException(String var1, Locator var2) {
      super(var1);
      if (var2 != null) {
         this.init(var2.getPublicId(), var2.getSystemId(), var2.getLineNumber(), var2.getColumnNumber());
      } else {
         this.init((String)null, (String)null, -1, -1);
      }

   }

   public SAXParseException(String var1, Locator var2, Exception var3) {
      super(var1, var3);
      if (var2 != null) {
         this.init(var2.getPublicId(), var2.getSystemId(), var2.getLineNumber(), var2.getColumnNumber());
      } else {
         this.init((String)null, (String)null, -1, -1);
      }

   }

   public SAXParseException(String var1, String var2, String var3, int var4, int var5) {
      super(var1);
      this.init(var2, var3, var4, var5);
   }

   public SAXParseException(String var1, String var2, String var3, int var4, int var5, Exception var6) {
      super(var1, var6);
      this.init(var2, var3, var4, var5);
   }

   private void init(String var1, String var2, int var3, int var4) {
      this.publicId = var1;
      this.systemId = var2;
      this.lineNumber = var3;
      this.columnNumber = var4;
   }

   public String getPublicId() {
      return this.publicId;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int getColumnNumber() {
      return this.columnNumber;
   }
}
