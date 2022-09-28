package org.apache.xerces.xni.grammars;

import org.apache.xerces.xni.XMLResourceIdentifier;

public interface XMLGrammarDescription extends XMLResourceIdentifier {
   String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
   String XML_DTD = "http://www.w3.org/TR/REC-xml";

   String getGrammarType();
}
