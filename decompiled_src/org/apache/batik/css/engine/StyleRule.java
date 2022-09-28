package org.apache.batik.css.engine;

import org.w3c.css.sac.SelectorList;

public class StyleRule implements Rule {
   public static final short TYPE = 0;
   protected SelectorList selectorList;
   protected StyleDeclaration styleDeclaration;

   public short getType() {
      return 0;
   }

   public void setSelectorList(SelectorList var1) {
      this.selectorList = var1;
   }

   public SelectorList getSelectorList() {
      return this.selectorList;
   }

   public void setStyleDeclaration(StyleDeclaration var1) {
      this.styleDeclaration = var1;
   }

   public StyleDeclaration getStyleDeclaration() {
      return this.styleDeclaration;
   }

   public String toString(CSSEngine var1) {
      StringBuffer var2 = new StringBuffer();
      if (this.selectorList != null) {
         var2.append(this.selectorList.item(0));

         for(int var3 = 1; var3 < this.selectorList.getLength(); ++var3) {
            var2.append(", ");
            var2.append(this.selectorList.item(var3));
         }
      }

      var2.append(" {\n");
      if (this.styleDeclaration != null) {
         var2.append(this.styleDeclaration.toString(var1));
      }

      var2.append("}\n");
      return var2.toString();
   }
}
