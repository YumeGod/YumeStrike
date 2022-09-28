package org.apache.batik.dom.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStyleSheetNode;
import org.apache.batik.css.engine.StyleSheet;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.StyleSheetFactory;
import org.apache.batik.dom.StyleSheetProcessingInstruction;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class SVGStyleSheetProcessingInstruction extends StyleSheetProcessingInstruction implements CSSStyleSheetNode {
   protected StyleSheet styleSheet;

   protected SVGStyleSheetProcessingInstruction() {
   }

   public SVGStyleSheetProcessingInstruction(String var1, AbstractDocument var2, StyleSheetFactory var3) {
      super(var1, var2, var3);
   }

   public String getStyleSheetURI() {
      SVGOMDocument var1 = (SVGOMDocument)this.getOwnerDocument();
      ParsedURL var2 = var1.getParsedURL();
      String var3 = (String)this.getPseudoAttributes().get("href");
      return var2 != null ? (new ParsedURL(var2, var3)).toString() : var3;
   }

   public StyleSheet getCSSStyleSheet() {
      if (this.styleSheet == null) {
         HashTable var1 = this.getPseudoAttributes();
         String var2 = (String)var1.get("type");
         if ("text/css".equals(var2)) {
            String var3 = (String)var1.get("title");
            String var4 = (String)var1.get("media");
            String var5 = (String)var1.get("href");
            String var6 = (String)var1.get("alternate");
            SVGOMDocument var7 = (SVGOMDocument)this.getOwnerDocument();
            ParsedURL var8 = var7.getParsedURL();
            ParsedURL var9 = new ParsedURL(var8, var5);
            CSSEngine var10 = var7.getCSSEngine();
            this.styleSheet = var10.parseStyleSheet(var9, var4);
            this.styleSheet.setAlternate("yes".equals(var6));
            this.styleSheet.setTitle(var3);
         }
      }

      return this.styleSheet;
   }

   public void setData(String var1) throws DOMException {
      super.setData(var1);
      this.styleSheet = null;
   }

   protected Node newNode() {
      return new SVGStyleSheetProcessingInstruction();
   }
}
