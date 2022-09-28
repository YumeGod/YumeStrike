package org.apache.xerces.parsers;

import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLParserConfiguration;

public class XMLDocumentParser extends AbstractXMLDocumentParser {
   public XMLDocumentParser() {
      super((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
   }

   public XMLDocumentParser(XMLParserConfiguration var1) {
      super(var1);
   }

   public XMLDocumentParser(SymbolTable var1) {
      super((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
      super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", var1);
   }

   public XMLDocumentParser(SymbolTable var1, XMLGrammarPool var2) {
      super((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
      super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", var1);
      super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", var2);
   }
}
