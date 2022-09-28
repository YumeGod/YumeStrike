package org.apache.batik.css.parser;

import java.io.IOException;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

public interface ExtendedParser extends org.w3c.css.sac.Parser {
   void parseStyleDeclaration(String var1) throws CSSException, IOException;

   void parseRule(String var1) throws CSSException, IOException;

   SelectorList parseSelectors(String var1) throws CSSException, IOException;

   LexicalUnit parsePropertyValue(String var1) throws CSSException, IOException;

   SACMediaList parseMedia(String var1) throws CSSException, IOException;

   boolean parsePriority(String var1) throws CSSException, IOException;
}
