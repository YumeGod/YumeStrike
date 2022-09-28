package org.apache.xerces.impl.xs;

public class XMLSchemaException extends Exception {
   static final long serialVersionUID = -9096984648537046218L;
   String key;
   Object[] args;

   public XMLSchemaException(String var1, Object[] var2) {
      this.key = var1;
      this.args = var2;
   }

   public String getKey() {
      return this.key;
   }

   public Object[] getArgs() {
      return this.args;
   }
}
