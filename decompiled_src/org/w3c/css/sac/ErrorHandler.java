package org.w3c.css.sac;

public interface ErrorHandler {
   void warning(CSSParseException var1) throws CSSException;

   void error(CSSParseException var1) throws CSSException;

   void fatalError(CSSParseException var1) throws CSSException;
}
