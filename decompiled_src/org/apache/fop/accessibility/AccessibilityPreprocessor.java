package org.apache.fop.accessibility;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.util.TransformerDefaultHandler;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class AccessibilityPreprocessor extends TransformerDefaultHandler {
   private final ByteArrayOutputStream enrichedFOBuffer = new ByteArrayOutputStream();
   private final Transformer reduceFOTree;
   private final FOUserAgent userAgent;
   private final DefaultHandler fopHandler;

   public AccessibilityPreprocessor(TransformerHandler addPtr, Transformer reduceFOTree, FOUserAgent userAgent, DefaultHandler fopHandler) {
      super(addPtr);
      this.reduceFOTree = reduceFOTree;
      this.userAgent = userAgent;
      this.fopHandler = fopHandler;
      this.getTransformerHandler().setResult(new StreamResult(this.enrichedFOBuffer));
   }

   public void endDocument() throws SAXException {
      super.endDocument();

      try {
         byte[] enrichedFO = this.enrichedFOBuffer.toByteArray();
         Source src = new StreamSource(new ByteArrayInputStream(enrichedFO));
         DOMResult res = new DOMResult();
         this.reduceFOTree.transform(src, res);
         StructureTree structureTree = new StructureTree();
         NodeList pageSequences = res.getNode().getFirstChild().getChildNodes();

         for(int i = 0; i < pageSequences.getLength(); ++i) {
            structureTree.addPageSequenceStructure(pageSequences.item(i).getChildNodes());
         }

         this.userAgent.setStructureTree(structureTree);
         SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
         saxParserFactory.setNamespaceAware(true);
         saxParserFactory.setValidating(false);
         SAXParser saxParser = saxParserFactory.newSAXParser();
         InputStream in = new ByteArrayInputStream(enrichedFO);
         saxParser.parse((InputStream)in, (DefaultHandler)this.fopHandler);
      } catch (Exception var9) {
         throw new SAXException(var9);
      }
   }
}
