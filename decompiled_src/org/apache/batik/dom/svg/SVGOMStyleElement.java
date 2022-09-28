package org.apache.batik.dom.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStyleSheetNode;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.stylesheets.LinkStyle;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.svg.SVGStyleElement;

public class SVGOMStyleElement extends SVGOMElement implements CSSStyleSheetNode, SVGStyleElement, LinkStyle {
   protected static final AttributeInitializer attributeInitializer = new AttributeInitializer(1);
   protected transient StyleSheet sheet;
   protected transient org.apache.batik.css.engine.StyleSheet styleSheet;
   protected transient EventListener domCharacterDataModifiedListener = new DOMCharacterDataModifiedListener();

   protected SVGOMStyleElement() {
   }

   public SVGOMStyleElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "style";
   }

   public org.apache.batik.css.engine.StyleSheet getCSSStyleSheet() {
      if (this.styleSheet == null && this.getType().equals("text/css")) {
         SVGOMDocument var1 = (SVGOMDocument)this.getOwnerDocument();
         CSSEngine var2 = var1.getCSSEngine();
         String var3 = "";
         Node var4 = this.getFirstChild();
         if (var4 != null) {
            StringBuffer var5 = new StringBuffer();

            while(true) {
               if (var4 == null) {
                  var3 = var5.toString();
                  break;
               }

               if (var4.getNodeType() == 4 || var4.getNodeType() == 3) {
                  var5.append(var4.getNodeValue());
               }

               var4 = var4.getNextSibling();
            }
         }

         ParsedURL var8 = null;
         String var6 = this.getBaseURI();
         if (var6 != null) {
            var8 = new ParsedURL(var6);
         }

         String var7 = this.getAttributeNS((String)null, "media");
         this.styleSheet = var2.parseStyleSheet(var3, var8, var7);
         this.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", this.domCharacterDataModifiedListener, false, (Object)null);
      }

      return this.styleSheet;
   }

   public StyleSheet getSheet() {
      throw new UnsupportedOperationException("LinkStyle.getSheet() is not implemented");
   }

   public String getXMLspace() {
      return XMLSupport.getXMLSpace(this);
   }

   public void setXMLspace(String var1) throws DOMException {
      this.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:space", var1);
   }

   public String getType() {
      return this.getAttributeNS((String)null, "type");
   }

   public void setType(String var1) throws DOMException {
      this.setAttributeNS((String)null, "type", var1);
   }

   public String getMedia() {
      return this.getAttribute("media");
   }

   public void setMedia(String var1) throws DOMException {
      this.setAttribute("media", var1);
   }

   public String getTitle() {
      return this.getAttribute("title");
   }

   public void setTitle(String var1) throws DOMException {
      this.setAttribute("title", var1);
   }

   protected AttributeInitializer getAttributeInitializer() {
      return attributeInitializer;
   }

   protected Node newNode() {
      return new SVGOMStyleElement();
   }

   static {
      attributeInitializer.addAttribute("http://www.w3.org/XML/1998/namespace", "xml", "space", "preserve");
   }

   protected class DOMCharacterDataModifiedListener implements EventListener {
      public void handleEvent(Event var1) {
         SVGOMStyleElement.this.styleSheet = null;
      }
   }
}
