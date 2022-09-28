package org.apache.batik.bridge;

import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

public class URIResolver {
   protected SVGOMDocument document;
   protected String documentURI;
   protected DocumentLoader documentLoader;

   public URIResolver(SVGDocument var1, DocumentLoader var2) {
      this.document = (SVGOMDocument)var1;
      this.documentLoader = var2;
   }

   public Element getElement(String var1, Element var2) throws MalformedURLException, IOException {
      Node var3 = this.getNode(var1, var2);
      if (var3 == null) {
         return null;
      } else if (var3.getNodeType() == 9) {
         throw new IllegalArgumentException();
      } else {
         return (Element)var3;
      }
   }

   public Node getNode(String var1, Element var2) throws MalformedURLException, IOException, SecurityException {
      String var3 = this.getRefererBaseURI(var2);
      if (var3 == null && var1.charAt(0) == '#') {
         return this.getNodeByFragment(var1.substring(1), var2);
      } else {
         ParsedURL var4 = new ParsedURL(var3, var1);
         if (this.documentURI == null) {
            this.documentURI = this.document.getURL();
         }

         String var5 = var4.getRef();
         ParsedURL var6;
         if (var5 != null && this.documentURI != null) {
            var6 = new ParsedURL(this.documentURI);
            if (var6.sameFile(var4)) {
               return this.document.getElementById(var5);
            }
         }

         var6 = null;
         if (this.documentURI != null) {
            var6 = new ParsedURL(this.documentURI);
         }

         UserAgent var7 = this.documentLoader.getUserAgent();
         var7.checkLoadExternalResource(var4, var6);
         String var8 = var4.toString();
         if (var5 != null) {
            var8 = var8.substring(0, var8.length() - (var5.length() + 1));
         }

         Document var9 = this.documentLoader.loadDocument(var8);
         return (Node)(var5 != null ? var9.getElementById(var5) : var9);
      }
   }

   protected String getRefererBaseURI(Element var1) {
      return ((AbstractNode)var1).getBaseURI();
   }

   protected Node getNodeByFragment(String var1, Element var2) {
      return var2.getOwnerDocument().getElementById(var1);
   }
}
