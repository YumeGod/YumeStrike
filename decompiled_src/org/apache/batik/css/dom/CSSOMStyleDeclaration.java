package org.apache.batik.css.dom;

import java.util.HashMap;
import java.util.Map;
import org.apache.batik.css.engine.value.Value;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

public class CSSOMStyleDeclaration implements CSSStyleDeclaration {
   protected ValueProvider valueProvider;
   protected ModificationHandler handler;
   protected CSSRule parentRule;
   protected Map values;

   public CSSOMStyleDeclaration(ValueProvider var1, CSSRule var2) {
      this.valueProvider = var1;
      this.parentRule = var2;
   }

   public void setModificationHandler(ModificationHandler var1) {
      this.handler = var1;
   }

   public String getCssText() {
      return this.valueProvider.getText();
   }

   public void setCssText(String var1) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.values = null;
         this.handler.textChanged(var1);
      }
   }

   public String getPropertyValue(String var1) {
      Value var2 = this.valueProvider.getValue(var1);
      return var2 == null ? "" : var2.getCssText();
   }

   public CSSValue getPropertyCSSValue(String var1) {
      Value var2 = this.valueProvider.getValue(var1);
      return var2 == null ? null : this.getCSSValue(var1);
   }

   public String removeProperty(String var1) throws DOMException {
      String var2 = this.getPropertyValue(var1);
      if (var2.length() > 0) {
         if (this.handler == null) {
            throw new DOMException((short)7, "");
         }

         if (this.values != null) {
            this.values.remove(var1);
         }

         this.handler.propertyRemoved(var1);
      }

      return var2;
   }

   public String getPropertyPriority(String var1) {
      return this.valueProvider.isImportant(var1) ? "important" : "";
   }

   public void setProperty(String var1, String var2, String var3) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.handler.propertyChanged(var1, var2, var3);
      }
   }

   public int getLength() {
      return this.valueProvider.getLength();
   }

   public String item(int var1) {
      return this.valueProvider.item(var1);
   }

   public CSSRule getParentRule() {
      return this.parentRule;
   }

   protected CSSValue getCSSValue(String var1) {
      CSSValue var2 = null;
      if (this.values != null) {
         var2 = (CSSValue)this.values.get(var1);
      }

      if (var2 == null) {
         var2 = this.createCSSValue(var1);
         if (this.values == null) {
            this.values = new HashMap(11);
         }

         this.values.put(var1, var2);
      }

      return var2;
   }

   protected CSSValue createCSSValue(String var1) {
      return new StyleDeclarationValue(var1);
   }

   public class StyleDeclarationValue extends CSSOMValue implements CSSOMValue.ValueProvider {
      protected String property;

      public StyleDeclarationValue(String var2) {
         super((CSSOMValue.ValueProvider)null);
         this.valueProvider = this;
         this.setModificationHandler(new CSSOMValue.AbstractModificationHandler() {
            protected Value getValue() {
               return StyleDeclarationValue.this.getValue();
            }

            public void textChanged(String var1) throws DOMException {
               if (CSSOMStyleDeclaration.this.values != null && CSSOMStyleDeclaration.this.values.get(this) != null && StyleDeclarationValue.this.handler != null) {
                  String var2 = CSSOMStyleDeclaration.this.getPropertyPriority(StyleDeclarationValue.this.property);
                  CSSOMStyleDeclaration.this.handler.propertyChanged(StyleDeclarationValue.this.property, var1, var2);
               } else {
                  throw new DOMException((short)7, "");
               }
            }
         });
         this.property = var2;
      }

      public Value getValue() {
         return CSSOMStyleDeclaration.this.valueProvider.getValue(this.property);
      }
   }

   public interface ModificationHandler {
      void textChanged(String var1) throws DOMException;

      void propertyRemoved(String var1) throws DOMException;

      void propertyChanged(String var1, String var2, String var3) throws DOMException;
   }

   public interface ValueProvider {
      Value getValue(String var1);

      boolean isImportant(String var1);

      String getText();

      int getLength();

      String item(int var1);
   }
}
