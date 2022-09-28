package org.apache.batik.css.engine.value;

public class URIValue extends StringValue {
   String cssText;

   public URIValue(String var1, String var2) {
      super((short)20, var2);
      this.cssText = var1;
   }

   public String getCssText() {
      return "url(" + this.cssText + ')';
   }
}
