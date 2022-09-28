package org.apache.fop.accessibility;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.fop.util.DelegatingContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public final class StructureTreeBuilder {
   private final SAXTransformerFactory factory;
   private final StructureTree structureTree = new StructureTree();

   public StructureTreeBuilder(SAXTransformerFactory factory) {
      this.factory = factory;
   }

   public StructureTree getStructureTree() {
      return this.structureTree;
   }

   public ContentHandler getHandlerForNextPageSequence() throws SAXException {
      TransformerHandler structureTreeBuilder;
      try {
         structureTreeBuilder = this.factory.newTransformerHandler();
      } catch (TransformerConfigurationException var3) {
         throw new SAXException(var3);
      }

      final DOMResult domResult = new DOMResult();
      structureTreeBuilder.setResult(domResult);
      return new DelegatingContentHandler(structureTreeBuilder) {
         public void characters(char[] ch, int start, int length) throws SAXException {
         }

         public void endDocument() throws SAXException {
            super.endDocument();
            StructureTreeBuilder.this.structureTree.addPageSequenceStructure(domResult.getNode().getFirstChild().getChildNodes());
         }
      };
   }
}
