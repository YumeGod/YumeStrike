package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.css.dom.CSSOMSVGColor;
import org.apache.batik.css.dom.CSSOMSVGPaint;
import org.apache.batik.css.dom.CSSOMStoredStyleDeclaration;
import org.apache.batik.css.dom.CSSOMValue;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleDeclarationProvider;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.svg.SVGColorManager;
import org.apache.batik.css.engine.value.svg.SVGPaintManager;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.anim.AnimationTargetListener;
import org.apache.batik.util.DoublyIndexedTable;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.svg.SVGAnimatedString;

public abstract class SVGStylableElement extends SVGOMElement implements CSSStylableElement {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected StyleMap computedStyleMap;
   protected OverrideStyleDeclaration overrideStyleDeclaration;
   protected SVGOMAnimatedString className;
   protected StyleDeclaration style;

   protected SVGStylableElement() {
   }

   protected SVGStylableElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.className = this.createLiveAnimatedString((String)null, "class");
   }

   public CSSStyleDeclaration getOverrideStyle() {
      if (this.overrideStyleDeclaration == null) {
         CSSEngine var1 = ((SVGOMDocument)this.getOwnerDocument()).getCSSEngine();
         this.overrideStyleDeclaration = new OverrideStyleDeclaration(var1);
      }

      return this.overrideStyleDeclaration;
   }

   public StyleMap getComputedStyleMap(String var1) {
      return this.computedStyleMap;
   }

   public void setComputedStyleMap(String var1, StyleMap var2) {
      this.computedStyleMap = var2;
   }

   public String getXMLId() {
      return this.getAttributeNS((String)null, "id");
   }

   public String getCSSClass() {
      return this.getAttributeNS((String)null, "class");
   }

   public ParsedURL getCSSBase() {
      if (this.getXblBoundElement() != null) {
         return null;
      } else {
         String var1 = this.getBaseURI();
         return var1 == null ? null : new ParsedURL(var1);
      }
   }

   public boolean isPseudoInstanceOf(String var1) {
      if (!var1.equals("first-child")) {
         return false;
      } else {
         Node var2;
         for(var2 = this.getPreviousSibling(); var2 != null && var2.getNodeType() != 1; var2 = var2.getPreviousSibling()) {
         }

         return var2 == null;
      }
   }

   public StyleDeclarationProvider getOverrideStyleDeclarationProvider() {
      return (StyleDeclarationProvider)this.getOverrideStyle();
   }

   public void updatePropertyValue(String var1, AnimatableValue var2) {
      CSSStyleDeclaration var3 = this.getOverrideStyle();
      if (var2 == null) {
         var3.removeProperty(var1);
      } else {
         var3.setProperty(var1, var2.getCssText(), "");
      }

   }

   public boolean useLinearRGBColorInterpolation() {
      CSSEngine var1 = ((SVGOMDocument)this.getOwnerDocument()).getCSSEngine();
      Value var2 = var1.getComputedStyle(this, (String)null, 6);
      return var2.getStringValue().charAt(0) == 'l';
   }

   public void addTargetListener(String var1, String var2, boolean var3, AnimationTargetListener var4) {
      if (var3 && this.svgContext != null) {
         ((SVGAnimationTargetContext)this.svgContext).addTargetListener(var2, var4);
      } else {
         super.addTargetListener(var1, var2, var3, var4);
      }

   }

   public void removeTargetListener(String var1, String var2, boolean var3, AnimationTargetListener var4) {
      if (var3) {
         ((SVGAnimationTargetContext)this.svgContext).removeTargetListener(var2, var4);
      } else {
         super.removeTargetListener(var1, var2, var3, var4);
      }

   }

   public CSSStyleDeclaration getStyle() {
      if (this.style == null) {
         CSSEngine var1 = ((SVGOMDocument)this.getOwnerDocument()).getCSSEngine();
         this.style = new StyleDeclaration(var1);
         this.putLiveAttributeValue((String)null, "style", this.style);
      }

      return this.style;
   }

   public CSSValue getPresentationAttribute(String var1) {
      Object var2 = (CSSValue)this.getLiveAttributeValue((String)null, var1);
      if (var2 != null) {
         return (CSSValue)var2;
      } else {
         CSSEngine var3 = ((SVGOMDocument)this.getOwnerDocument()).getCSSEngine();
         int var4 = var3.getPropertyIndex(var1);
         if (var4 == -1) {
            return null;
         } else {
            if (var4 > 59) {
               if (var3.getValueManagers()[var4] instanceof SVGPaintManager) {
                  var2 = new PresentationAttributePaintValue(var3, var1);
               }

               if (var3.getValueManagers()[var4] instanceof SVGColorManager) {
                  var2 = new PresentationAttributeColorValue(var3, var1);
               }
            } else {
               switch (var4) {
                  case 15:
                  case 45:
                     var2 = new PresentationAttributePaintValue(var3, var1);
                     break;
                  case 19:
                  case 33:
                  case 43:
                     var2 = new PresentationAttributeColorValue(var3, var1);
                     break;
                  default:
                     var2 = new PresentationAttributeValue(var3, var1);
               }
            }

            this.putLiveAttributeValue((String)null, var1, (LiveAttributeValue)var2);
            return (CSSValue)(this.getAttributeNS((String)null, var1).length() == 0 ? null : var2);
         }
      }
   }

   public SVGAnimatedString getClassName() {
      return this.className;
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGOMElement.xmlTraitInformation);
      var0.put((Object)null, "class", new TraitInformation(true, 16));
      xmlTraitInformation = var0;
   }

   protected class OverrideStyleDeclaration extends CSSOMStoredStyleDeclaration {
      protected OverrideStyleDeclaration(CSSEngine var2) {
         super(var2);
         this.declaration = new org.apache.batik.css.engine.StyleDeclaration();
      }

      public void textChanged(String var1) throws DOMException {
         ((SVGOMDocument)SVGStylableElement.this.ownerDocument).overrideStyleTextChanged(SVGStylableElement.this, var1);
      }

      public void propertyRemoved(String var1) throws DOMException {
         ((SVGOMDocument)SVGStylableElement.this.ownerDocument).overrideStylePropertyRemoved(SVGStylableElement.this, var1);
      }

      public void propertyChanged(String var1, String var2, String var3) throws DOMException {
         ((SVGOMDocument)SVGStylableElement.this.ownerDocument).overrideStylePropertyChanged(SVGStylableElement.this, var1, var2, var3);
      }
   }

   public class StyleDeclaration extends CSSOMStoredStyleDeclaration implements LiveAttributeValue, CSSEngine.MainPropertyReceiver {
      protected boolean mutate;

      public StyleDeclaration(CSSEngine var2) {
         super(var2);
         this.declaration = this.cssEngine.parseStyleDeclaration(SVGStylableElement.this, SVGStylableElement.this.getAttributeNS((String)null, "style"));
      }

      public void attrAdded(Attr var1, String var2) {
         if (!this.mutate) {
            this.declaration = this.cssEngine.parseStyleDeclaration(SVGStylableElement.this, var2);
         }

      }

      public void attrModified(Attr var1, String var2, String var3) {
         if (!this.mutate) {
            this.declaration = this.cssEngine.parseStyleDeclaration(SVGStylableElement.this, var3);
         }

      }

      public void attrRemoved(Attr var1, String var2) {
         if (!this.mutate) {
            this.declaration = new org.apache.batik.css.engine.StyleDeclaration();
         }

      }

      public void textChanged(String var1) throws DOMException {
         this.declaration = this.cssEngine.parseStyleDeclaration(SVGStylableElement.this, var1);
         this.mutate = true;
         SVGStylableElement.this.setAttributeNS((String)null, "style", var1);
         this.mutate = false;
      }

      public void propertyRemoved(String var1) throws DOMException {
         int var2 = this.cssEngine.getPropertyIndex(var1);

         for(int var3 = 0; var3 < this.declaration.size(); ++var3) {
            if (var2 == this.declaration.getIndex(var3)) {
               this.declaration.remove(var3);
               this.mutate = true;
               SVGStylableElement.this.setAttributeNS((String)null, "style", this.declaration.toString(this.cssEngine));
               this.mutate = false;
               return;
            }
         }

      }

      public void propertyChanged(String var1, String var2, String var3) throws DOMException {
         boolean var4 = var3 != null && var3.length() > 0;
         this.cssEngine.setMainProperties(SVGStylableElement.this, this, var1, var2, var4);
         this.mutate = true;
         SVGStylableElement.this.setAttributeNS((String)null, "style", this.declaration.toString(this.cssEngine));
         this.mutate = false;
      }

      public void setMainProperty(String var1, Value var2, boolean var3) {
         int var4 = this.cssEngine.getPropertyIndex(var1);
         if (var4 != -1) {
            int var5;
            for(var5 = 0; var5 < this.declaration.size() && var4 != this.declaration.getIndex(var5); ++var5) {
            }

            if (var5 < this.declaration.size()) {
               this.declaration.put(var5, var2, var4, var3);
            } else {
               this.declaration.append(var2, var4, var3);
            }

         }
      }
   }

   public class PresentationAttributePaintValue extends CSSOMSVGPaint implements LiveAttributeValue, CSSOMSVGColor.ValueProvider {
      protected CSSEngine cssEngine;
      protected String property;
      protected Value value;
      protected boolean mutate;

      public PresentationAttributePaintValue(CSSEngine var2, String var3) {
         super((CSSOMSVGColor.ValueProvider)null);
         this.valueProvider = this;
         this.setModificationHandler(new CSSOMSVGPaint.AbstractModificationHandler() {
            protected Value getValue() {
               return PresentationAttributePaintValue.this.getValue();
            }

            public void textChanged(String var1) throws DOMException {
               PresentationAttributePaintValue.this.value = PresentationAttributePaintValue.this.cssEngine.parsePropertyValue(SVGStylableElement.this, PresentationAttributePaintValue.this.property, var1);
               PresentationAttributePaintValue.this.mutate = true;
               SVGStylableElement.this.setAttributeNS((String)null, PresentationAttributePaintValue.this.property, var1);
               PresentationAttributePaintValue.this.mutate = false;
            }
         });
         this.cssEngine = var2;
         this.property = var3;
         Attr var4 = SVGStylableElement.this.getAttributeNodeNS((String)null, var3);
         if (var4 != null) {
            this.value = this.cssEngine.parsePropertyValue(SVGStylableElement.this, var3, var4.getValue());
         }

      }

      public Value getValue() {
         if (this.value == null) {
            throw new DOMException((short)11, "");
         } else {
            return this.value;
         }
      }

      public void attrAdded(Attr var1, String var2) {
         if (!this.mutate) {
            this.value = this.cssEngine.parsePropertyValue(SVGStylableElement.this, this.property, var2);
         }

      }

      public void attrModified(Attr var1, String var2, String var3) {
         if (!this.mutate) {
            this.value = this.cssEngine.parsePropertyValue(SVGStylableElement.this, this.property, var3);
         }

      }

      public void attrRemoved(Attr var1, String var2) {
         if (!this.mutate) {
            this.value = null;
         }

      }
   }

   public class PresentationAttributeColorValue extends CSSOMSVGColor implements LiveAttributeValue, CSSOMSVGColor.ValueProvider {
      protected CSSEngine cssEngine;
      protected String property;
      protected Value value;
      protected boolean mutate;

      public PresentationAttributeColorValue(CSSEngine var2, String var3) {
         super((CSSOMSVGColor.ValueProvider)null);
         this.valueProvider = this;
         this.setModificationHandler(new CSSOMSVGColor.AbstractModificationHandler() {
            protected Value getValue() {
               return PresentationAttributeColorValue.this.getValue();
            }

            public void textChanged(String var1) throws DOMException {
               PresentationAttributeColorValue.this.value = PresentationAttributeColorValue.this.cssEngine.parsePropertyValue(SVGStylableElement.this, PresentationAttributeColorValue.this.property, var1);
               PresentationAttributeColorValue.this.mutate = true;
               SVGStylableElement.this.setAttributeNS((String)null, PresentationAttributeColorValue.this.property, var1);
               PresentationAttributeColorValue.this.mutate = false;
            }
         });
         this.cssEngine = var2;
         this.property = var3;
         Attr var4 = SVGStylableElement.this.getAttributeNodeNS((String)null, var3);
         if (var4 != null) {
            this.value = this.cssEngine.parsePropertyValue(SVGStylableElement.this, var3, var4.getValue());
         }

      }

      public Value getValue() {
         if (this.value == null) {
            throw new DOMException((short)11, "");
         } else {
            return this.value;
         }
      }

      public void attrAdded(Attr var1, String var2) {
         if (!this.mutate) {
            this.value = this.cssEngine.parsePropertyValue(SVGStylableElement.this, this.property, var2);
         }

      }

      public void attrModified(Attr var1, String var2, String var3) {
         if (!this.mutate) {
            this.value = this.cssEngine.parsePropertyValue(SVGStylableElement.this, this.property, var3);
         }

      }

      public void attrRemoved(Attr var1, String var2) {
         if (!this.mutate) {
            this.value = null;
         }

      }
   }

   public class PresentationAttributeValue extends CSSOMValue implements LiveAttributeValue, CSSOMValue.ValueProvider {
      protected CSSEngine cssEngine;
      protected String property;
      protected Value value;
      protected boolean mutate;

      public PresentationAttributeValue(CSSEngine var2, String var3) {
         super((CSSOMValue.ValueProvider)null);
         this.valueProvider = this;
         this.setModificationHandler(new CSSOMValue.AbstractModificationHandler() {
            protected Value getValue() {
               return PresentationAttributeValue.this.getValue();
            }

            public void textChanged(String var1) throws DOMException {
               PresentationAttributeValue.this.value = PresentationAttributeValue.this.cssEngine.parsePropertyValue(SVGStylableElement.this, PresentationAttributeValue.this.property, var1);
               PresentationAttributeValue.this.mutate = true;
               SVGStylableElement.this.setAttributeNS((String)null, PresentationAttributeValue.this.property, var1);
               PresentationAttributeValue.this.mutate = false;
            }
         });
         this.cssEngine = var2;
         this.property = var3;
         Attr var4 = SVGStylableElement.this.getAttributeNodeNS((String)null, var3);
         if (var4 != null) {
            this.value = this.cssEngine.parsePropertyValue(SVGStylableElement.this, var3, var4.getValue());
         }

      }

      public Value getValue() {
         if (this.value == null) {
            throw new DOMException((short)11, "");
         } else {
            return this.value;
         }
      }

      public void attrAdded(Attr var1, String var2) {
         if (!this.mutate) {
            this.value = this.cssEngine.parsePropertyValue(SVGStylableElement.this, this.property, var2);
         }

      }

      public void attrModified(Attr var1, String var2, String var3) {
         if (!this.mutate) {
            this.value = this.cssEngine.parsePropertyValue(SVGStylableElement.this, this.property, var3);
         }

      }

      public void attrRemoved(Attr var1, String var2) {
         if (!this.mutate) {
            this.value = null;
         }

      }
   }
}
