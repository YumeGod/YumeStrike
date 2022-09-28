package org.apache.xerces.xni.grammars;

import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;

public interface XMLSchemaDescription extends XMLGrammarDescription {
   short CONTEXT_INCLUDE = 0;
   short CONTEXT_REDEFINE = 1;
   short CONTEXT_IMPORT = 2;
   short CONTEXT_PREPARSE = 3;
   short CONTEXT_INSTANCE = 4;
   short CONTEXT_ELEMENT = 5;
   short CONTEXT_ATTRIBUTE = 6;
   short CONTEXT_XSITYPE = 7;

   short getContextType();

   String getTargetNamespace();

   String[] getLocationHints();

   QName getTriggeringComponent();

   QName getEnclosingElementName();

   XMLAttributes getAttributes();
}
