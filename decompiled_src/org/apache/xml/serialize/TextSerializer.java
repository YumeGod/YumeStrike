package org.apache.xml.serialize;

import java.io.IOException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TextSerializer extends BaseMarkupSerializer {
   public TextSerializer() {
      super(new OutputFormat("text", (String)null, false));
   }

   public void setOutputFormat(OutputFormat var1) {
      super.setOutputFormat(var1 != null ? var1 : new OutputFormat("text", (String)null, false));
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      this.startElement(var3 == null ? var2 : var3, (AttributeList)null);
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      this.endElement(var3 == null ? var2 : var3);
   }

   public void startElement(String var1, AttributeList var2) throws SAXException {
      try {
         ElementState var4 = this.getElementState();
         if (this.isDocumentState() && !super._started) {
            this.startDocument(var1);
         }

         boolean var3 = var4.preserveSpace;
         this.enterElementState((String)null, (String)null, var1, var3);
      } catch (IOException var6) {
         throw new SAXException(var6);
      }
   }

   public void endElement(String var1) throws SAXException {
      try {
         this.endElementIO(var1);
      } catch (IOException var3) {
         throw new SAXException(var3);
      }
   }

   public void endElementIO(String var1) throws IOException {
      ElementState var2 = this.getElementState();
      var2 = this.leaveElementState();
      var2.afterElement = true;
      var2.empty = false;
      if (this.isDocumentState()) {
         super._printer.flush();
      }

   }

   public void processingInstructionIO(String var1, String var2) throws IOException {
   }

   public void comment(String var1) {
   }

   public void comment(char[] var1, int var2, int var3) {
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      try {
         ElementState var4 = this.content();
         var4.doCData = var4.inCData = false;
         this.printText(var1, var2, var3, true, true);
      } catch (IOException var6) {
         throw new SAXException(var6);
      }
   }

   protected void characters(String var1, boolean var2) throws IOException {
      ElementState var3 = this.content();
      var3.doCData = var3.inCData = false;
      this.printText(var1, true, true);
   }

   protected void startDocument(String var1) throws IOException {
      super._printer.leaveDTD();
      super._started = true;
      this.serializePreRoot();
   }

   protected void serializeElement(Element var1) throws IOException {
      String var5 = var1.getTagName();
      ElementState var3 = this.getElementState();
      if (this.isDocumentState() && !super._started) {
         this.startDocument(var5);
      }

      boolean var4 = var3.preserveSpace;
      if (var1.hasChildNodes()) {
         this.enterElementState((String)null, (String)null, var5, var4);

         for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            this.serializeNode(var2);
         }

         this.endElementIO(var5);
      } else if (!this.isDocumentState()) {
         var3.afterElement = true;
         var3.empty = false;
      }

   }

   protected void serializeNode(Node var1) throws IOException {
      String var3;
      switch (var1.getNodeType()) {
         case 1:
            this.serializeElement((Element)var1);
         case 2:
         case 5:
         case 6:
         case 7:
         case 8:
         case 10:
         default:
            break;
         case 3:
            var3 = var1.getNodeValue();
            if (var3 != null) {
               this.characters(var1.getNodeValue(), true);
            }
            break;
         case 4:
            var3 = var1.getNodeValue();
            if (var3 != null) {
               this.characters(var1.getNodeValue(), true);
            }
            break;
         case 9:
         case 11:
            for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
               this.serializeNode(var2);
            }
      }

   }

   protected ElementState content() {
      ElementState var1 = this.getElementState();
      if (!this.isDocumentState()) {
         if (var1.empty) {
            var1.empty = false;
         }

         var1.afterElement = false;
      }

      return var1;
   }

   protected String getEntityRef(int var1) {
      return null;
   }
}
