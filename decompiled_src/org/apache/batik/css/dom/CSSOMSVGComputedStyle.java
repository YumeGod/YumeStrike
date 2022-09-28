package org.apache.batik.css.dom;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.svg.SVGColorManager;
import org.apache.batik.css.engine.value.svg.SVGPaintManager;
import org.w3c.dom.css.CSSValue;

public class CSSOMSVGComputedStyle extends CSSOMComputedStyle {
   public CSSOMSVGComputedStyle(CSSEngine var1, CSSStylableElement var2, String var3) {
      super(var1, var2, var3);
   }

   protected CSSValue createCSSValue(int var1) {
      if (var1 > 59) {
         if (this.cssEngine.getValueManagers()[var1] instanceof SVGPaintManager) {
            return new ComputedCSSPaintValue(var1);
         }

         if (this.cssEngine.getValueManagers()[var1] instanceof SVGColorManager) {
            return new ComputedCSSColorValue(var1);
         }
      } else {
         switch (var1) {
            case 15:
            case 45:
               return new ComputedCSSPaintValue(var1);
            case 19:
            case 33:
            case 43:
               return new ComputedCSSColorValue(var1);
         }
      }

      return super.createCSSValue(var1);
   }

   public class ComputedCSSPaintValue extends CSSOMSVGPaint implements CSSOMSVGColor.ValueProvider {
      protected int index;

      public ComputedCSSPaintValue(int var2) {
         super((CSSOMSVGColor.ValueProvider)null);
         this.valueProvider = this;
         this.index = var2;
      }

      public Value getValue() {
         return CSSOMSVGComputedStyle.this.cssEngine.getComputedStyle(CSSOMSVGComputedStyle.this.element, CSSOMSVGComputedStyle.this.pseudoElement, this.index);
      }
   }

   protected class ComputedCSSColorValue extends CSSOMSVGColor implements CSSOMSVGColor.ValueProvider {
      protected int index;

      public ComputedCSSColorValue(int var2) {
         super((CSSOMSVGColor.ValueProvider)null);
         this.valueProvider = this;
         this.index = var2;
      }

      public Value getValue() {
         return CSSOMSVGComputedStyle.this.cssEngine.getComputedStyle(CSSOMSVGComputedStyle.this.element, CSSOMSVGComputedStyle.this.pseudoElement, this.index);
      }
   }
}
