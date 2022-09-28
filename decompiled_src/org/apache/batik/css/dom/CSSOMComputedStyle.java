package org.apache.batik.css.dom;

import java.util.HashMap;
import java.util.Map;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.value.Value;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

public class CSSOMComputedStyle implements CSSStyleDeclaration {
   protected CSSEngine cssEngine;
   protected CSSStylableElement element;
   protected String pseudoElement;
   protected Map values = new HashMap();

   public CSSOMComputedStyle(CSSEngine var1, CSSStylableElement var2, String var3) {
      this.cssEngine = var1;
      this.element = var2;
      this.pseudoElement = var3;
   }

   public String getCssText() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.cssEngine.getNumberOfProperties(); ++var2) {
         var1.append(this.cssEngine.getPropertyName(var2));
         var1.append(": ");
         var1.append(this.cssEngine.getComputedStyle(this.element, this.pseudoElement, var2).getCssText());
         var1.append(";\n");
      }

      return var1.toString();
   }

   public void setCssText(String var1) throws DOMException {
      throw new DOMException((short)7, "");
   }

   public String getPropertyValue(String var1) {
      int var2 = this.cssEngine.getPropertyIndex(var1);
      if (var2 == -1) {
         return "";
      } else {
         Value var3 = this.cssEngine.getComputedStyle(this.element, this.pseudoElement, var2);
         return var3.getCssText();
      }
   }

   public CSSValue getPropertyCSSValue(String var1) {
      CSSValue var2 = (CSSValue)this.values.get(var1);
      if (var2 == null) {
         int var3 = this.cssEngine.getPropertyIndex(var1);
         if (var3 != -1) {
            var2 = this.createCSSValue(var3);
            this.values.put(var1, var2);
         }
      }

      return var2;
   }

   public String removeProperty(String var1) throws DOMException {
      throw new DOMException((short)7, "");
   }

   public String getPropertyPriority(String var1) {
      return "";
   }

   public void setProperty(String var1, String var2, String var3) throws DOMException {
      throw new DOMException((short)7, "");
   }

   public int getLength() {
      return this.cssEngine.getNumberOfProperties();
   }

   public String item(int var1) {
      return var1 >= 0 && var1 < this.cssEngine.getNumberOfProperties() ? this.cssEngine.getPropertyName(var1) : "";
   }

   public CSSRule getParentRule() {
      return null;
   }

   protected CSSValue createCSSValue(int var1) {
      return new ComputedCSSValue(var1);
   }

   public class ComputedCSSValue extends CSSOMValue implements CSSOMValue.ValueProvider {
      protected int index;

      public ComputedCSSValue(int var2) {
         super((CSSOMValue.ValueProvider)null);
         this.valueProvider = this;
         this.index = var2;
      }

      public Value getValue() {
         return CSSOMComputedStyle.this.cssEngine.getComputedStyle(CSSOMComputedStyle.this.element, CSSOMComputedStyle.this.pseudoElement, this.index);
      }
   }
}
