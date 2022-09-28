package org.apache.batik.util.resources;

public class ResourceFormatException extends RuntimeException {
   protected String className;
   protected String key;

   public ResourceFormatException(String var1, String var2, String var3) {
      super(var1);
      this.className = var2;
      this.key = var3;
   }

   public String getClassName() {
      return this.className;
   }

   public String getKey() {
      return this.key;
   }

   public String toString() {
      return super.toString() + " (" + this.getKey() + ", bundle: " + this.getClassName() + ")";
   }
}
