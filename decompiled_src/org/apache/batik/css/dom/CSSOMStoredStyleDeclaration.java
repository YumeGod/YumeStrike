package org.apache.batik.css.dom;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.StyleDeclaration;
import org.apache.batik.css.engine.StyleDeclarationProvider;
import org.apache.batik.css.engine.value.Value;
import org.w3c.dom.css.CSSRule;

public abstract class CSSOMStoredStyleDeclaration extends CSSOMSVGStyleDeclaration implements CSSOMStyleDeclaration.ValueProvider, CSSOMStyleDeclaration.ModificationHandler, StyleDeclarationProvider {
   protected StyleDeclaration declaration;

   public CSSOMStoredStyleDeclaration(CSSEngine var1) {
      super((CSSOMStyleDeclaration.ValueProvider)null, (CSSRule)null, var1);
      this.valueProvider = this;
      this.setModificationHandler(this);
   }

   public StyleDeclaration getStyleDeclaration() {
      return this.declaration;
   }

   public void setStyleDeclaration(StyleDeclaration var1) {
      this.declaration = var1;
   }

   public Value getValue(String var1) {
      int var2 = this.cssEngine.getPropertyIndex(var1);

      for(int var3 = 0; var3 < this.declaration.size(); ++var3) {
         if (var2 == this.declaration.getIndex(var3)) {
            return this.declaration.getValue(var3);
         }
      }

      return null;
   }

   public boolean isImportant(String var1) {
      int var2 = this.cssEngine.getPropertyIndex(var1);

      for(int var3 = 0; var3 < this.declaration.size(); ++var3) {
         if (var2 == this.declaration.getIndex(var3)) {
            return this.declaration.getPriority(var3);
         }
      }

      return false;
   }

   public String getText() {
      return this.declaration.toString(this.cssEngine);
   }

   public int getLength() {
      return this.declaration.size();
   }

   public String item(int var1) {
      return this.cssEngine.getPropertyName(this.declaration.getIndex(var1));
   }
}
