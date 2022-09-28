package org.w3c.css.sac;

public interface DocumentHandler {
   void startDocument(InputSource var1) throws CSSException;

   void endDocument(InputSource var1) throws CSSException;

   void comment(String var1) throws CSSException;

   void ignorableAtRule(String var1) throws CSSException;

   void namespaceDeclaration(String var1, String var2) throws CSSException;

   void importStyle(String var1, SACMediaList var2, String var3) throws CSSException;

   void startMedia(SACMediaList var1) throws CSSException;

   void endMedia(SACMediaList var1) throws CSSException;

   void startPage(String var1, String var2) throws CSSException;

   void endPage(String var1, String var2) throws CSSException;

   void startFontFace() throws CSSException;

   void endFontFace() throws CSSException;

   void startSelector(SelectorList var1) throws CSSException;

   void endSelector(SelectorList var1) throws CSSException;

   void property(String var1, LexicalUnit var2, boolean var3) throws CSSException;
}
