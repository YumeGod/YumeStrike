package org.apache.batik.dom.svg;

public class SVGOMAnimatedLength extends AbstractSVGAnimatedLength {
   protected String defaultValue;

   public SVGOMAnimatedLength(AbstractElement var1, String var2, String var3, String var4, short var5, boolean var6) {
      super(var1, var2, var3, var5, var6);
      this.defaultValue = var4;
   }

   protected String getDefaultValue() {
      return this.defaultValue;
   }
}
