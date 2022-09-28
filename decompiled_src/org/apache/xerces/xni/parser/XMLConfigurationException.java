package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XNIException;

public class XMLConfigurationException extends XNIException {
   static final long serialVersionUID = -5437427404547669188L;
   public static final short NOT_RECOGNIZED = 0;
   public static final short NOT_SUPPORTED = 1;
   protected short fType;
   protected String fIdentifier;

   public XMLConfigurationException(short var1, String var2) {
      super(var2);
      this.fType = var1;
      this.fIdentifier = var2;
   }

   public XMLConfigurationException(short var1, String var2, String var3) {
      super(var3);
      this.fType = var1;
      this.fIdentifier = var2;
   }

   public short getType() {
      return this.fType;
   }

   public String getIdentifier() {
      return this.fIdentifier;
   }
}
