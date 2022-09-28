package org.apache.xerces.parsers;

import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.parser.XMLParserConfiguration;

public abstract class XMLGrammarParser extends XMLParser {
   protected DTDDVFactory fDatatypeValidatorFactory;

   protected XMLGrammarParser(SymbolTable var1) {
      super((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
      super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", var1);
   }
}
