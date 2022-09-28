package org.apache.batik.dom;

import org.apache.batik.css.engine.CSSEngine;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.DocumentCSS;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

public abstract class AbstractStylableDocument extends AbstractDocument implements DocumentCSS, DocumentView {
   protected transient AbstractView defaultView;
   protected transient CSSEngine cssEngine;

   protected AbstractStylableDocument() {
   }

   protected AbstractStylableDocument(DocumentType var1, DOMImplementation var2) {
      super(var1, var2);
   }

   public void setCSSEngine(CSSEngine var1) {
      this.cssEngine = var1;
   }

   public CSSEngine getCSSEngine() {
      return this.cssEngine;
   }

   public StyleSheetList getStyleSheets() {
      throw new RuntimeException(" !!! Not implemented");
   }

   public AbstractView getDefaultView() {
      if (this.defaultView == null) {
         ExtensibleDOMImplementation var1 = (ExtensibleDOMImplementation)this.implementation;
         this.defaultView = var1.createViewCSS(this);
      }

      return this.defaultView;
   }

   public void clearViewCSS() {
      this.defaultView = null;
      if (this.cssEngine != null) {
         this.cssEngine.dispose();
      }

      this.cssEngine = null;
   }

   public CSSStyleDeclaration getOverrideStyle(Element var1, String var2) {
      throw new RuntimeException(" !!! Not implemented");
   }
}
