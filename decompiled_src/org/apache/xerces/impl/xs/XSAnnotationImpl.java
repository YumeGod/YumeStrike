package org.apache.xerces.impl.xs;

import java.io.IOException;
import java.io.StringReader;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSNamespaceItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XSAnnotationImpl implements XSAnnotation {
   private String fData = null;
   private SchemaGrammar fGrammar = null;

   public XSAnnotationImpl(String var1, SchemaGrammar var2) {
      this.fData = var1;
      this.fGrammar = var2;
   }

   public boolean writeAnnotation(Object var1, short var2) {
      if (var2 != 1 && var2 != 3) {
         if (var2 == 2) {
            this.writeToSAX((ContentHandler)var1);
            return true;
         } else {
            return false;
         }
      } else {
         this.writeToDOM((Node)var1, var2);
         return true;
      }
   }

   public String getAnnotationString() {
      return this.fData;
   }

   public short getType() {
      return 12;
   }

   public String getName() {
      return null;
   }

   public String getNamespace() {
      return null;
   }

   public XSNamespaceItem getNamespaceItem() {
      return null;
   }

   private synchronized void writeToSAX(ContentHandler var1) {
      SAXParser var2 = this.fGrammar.getSAXParser();
      StringReader var3 = new StringReader(this.fData);
      InputSource var4 = new InputSource(var3);
      var2.setContentHandler(var1);

      try {
         var2.parse(var4);
      } catch (SAXException var7) {
      } catch (IOException var8) {
      }

   }

   private synchronized void writeToDOM(Node var1, short var2) {
      Document var3 = var2 == 1 ? var1.getOwnerDocument() : (Document)var1;
      DOMParser var4 = this.fGrammar.getDOMParser();
      StringReader var5 = new StringReader(this.fData);
      InputSource var6 = new InputSource(var5);

      try {
         var4.parse(var6);
      } catch (SAXException var10) {
      } catch (IOException var11) {
      }

      Document var7 = var4.getDocument();
      Element var8 = var7.getDocumentElement();
      Node var9 = var3.importNode(var8, true);
      var1.insertBefore(var9, var1.getFirstChild());
   }
}
