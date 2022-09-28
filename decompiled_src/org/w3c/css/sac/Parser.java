package org.w3c.css.sac;

import java.io.IOException;
import java.util.Locale;

public interface Parser {
   void setLocale(Locale var1) throws CSSException;

   void setDocumentHandler(DocumentHandler var1);

   void setSelectorFactory(SelectorFactory var1);

   void setConditionFactory(ConditionFactory var1);

   void setErrorHandler(ErrorHandler var1);

   void parseStyleSheet(InputSource var1) throws CSSException, IOException;

   void parseStyleSheet(String var1) throws CSSException, IOException;

   void parseStyleDeclaration(InputSource var1) throws CSSException, IOException;

   void parseRule(InputSource var1) throws CSSException, IOException;

   String getParserVersion();

   SelectorList parseSelectors(InputSource var1) throws CSSException, IOException;

   LexicalUnit parsePropertyValue(InputSource var1) throws CSSException, IOException;

   boolean parsePriority(InputSource var1) throws CSSException, IOException;
}
