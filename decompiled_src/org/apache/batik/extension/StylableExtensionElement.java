package org.apache.batik.extension;

import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleDeclarationProvider;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGStylable;

public abstract class StylableExtensionElement extends ExtensionElement implements CSSStylableElement, SVGStylable {
   protected ParsedURL cssBase;
   protected StyleMap computedStyleMap;

   protected StylableExtensionElement() {
   }

   protected StylableExtensionElement(String var1, AbstractDocument var2) {
      super(var1, var2);
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
      if (this.cssBase == null) {
         String var1 = this.getBaseURI();
         if (var1 == null) {
            return null;
         }

         this.cssBase = new ParsedURL(var1);
      }

      return this.cssBase;
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
      return null;
   }

   public CSSStyleDeclaration getStyle() {
      throw new UnsupportedOperationException("Not implemented");
   }

   public CSSValue getPresentationAttribute(String var1) {
      throw new UnsupportedOperationException("Not implemented");
   }

   public SVGAnimatedString getClassName() {
      throw new UnsupportedOperationException("Not implemented");
   }
}
