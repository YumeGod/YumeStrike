package org.apache.batik.css.dom;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.svg.SVGColorManager;
import org.apache.batik.css.engine.value.svg.SVGPaintManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSValue;

public class CSSOMSVGStyleDeclaration extends CSSOMStyleDeclaration {
   protected CSSEngine cssEngine;

   public CSSOMSVGStyleDeclaration(CSSOMStyleDeclaration.ValueProvider var1, CSSRule var2, CSSEngine var3) {
      super(var1, var2);
      this.cssEngine = var3;
   }

   protected CSSValue createCSSValue(String var1) {
      int var2 = this.cssEngine.getPropertyIndex(var1);
      if (var2 > 59) {
         if (this.cssEngine.getValueManagers()[var2] instanceof SVGPaintManager) {
            return new StyleDeclarationPaintValue(var1);
         }

         if (this.cssEngine.getValueManagers()[var2] instanceof SVGColorManager) {
            return new StyleDeclarationColorValue(var1);
         }
      } else {
         switch (var2) {
            case 15:
            case 45:
               return new StyleDeclarationPaintValue(var1);
            case 19:
            case 33:
            case 43:
               return new StyleDeclarationColorValue(var1);
         }
      }

      return super.createCSSValue(var1);
   }

   public class StyleDeclarationPaintValue extends CSSOMSVGPaint implements CSSOMSVGColor.ValueProvider {
      protected String property;

      public StyleDeclarationPaintValue(String var2) {
         super((CSSOMSVGColor.ValueProvider)null);
         this.valueProvider = this;
         this.setModificationHandler(new CSSOMSVGPaint.AbstractModificationHandler() {
            protected Value getValue() {
               return StyleDeclarationPaintValue.this.getValue();
            }

            public void textChanged(String var1) throws DOMException {
               if (StyleDeclarationPaintValue.this.handler == null) {
                  throw new DOMException((short)7, "");
               } else {
                  String var2 = CSSOMSVGStyleDeclaration.this.getPropertyPriority(StyleDeclarationPaintValue.this.property);
                  CSSOMSVGStyleDeclaration.this.handler.propertyChanged(StyleDeclarationPaintValue.this.property, var1, var2);
               }
            }
         });
         this.property = var2;
      }

      public Value getValue() {
         return CSSOMSVGStyleDeclaration.this.valueProvider.getValue(this.property);
      }
   }

   public class StyleDeclarationColorValue extends CSSOMSVGColor implements CSSOMSVGColor.ValueProvider {
      protected String property;

      public StyleDeclarationColorValue(String var2) {
         super((CSSOMSVGColor.ValueProvider)null);
         this.valueProvider = this;
         this.setModificationHandler(new CSSOMSVGColor.AbstractModificationHandler() {
            protected Value getValue() {
               return StyleDeclarationColorValue.this.getValue();
            }

            public void textChanged(String var1) throws DOMException {
               if (StyleDeclarationColorValue.this.handler == null) {
                  throw new DOMException((short)7, "");
               } else {
                  String var2 = CSSOMSVGStyleDeclaration.this.getPropertyPriority(StyleDeclarationColorValue.this.property);
                  CSSOMSVGStyleDeclaration.this.handler.propertyChanged(StyleDeclarationColorValue.this.property, var1, var2);
               }
            }
         });
         this.property = var2;
      }

      public Value getValue() {
         return CSSOMSVGStyleDeclaration.this.valueProvider.getValue(this.property);
      }
   }
}
