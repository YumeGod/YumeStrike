package org.apache.batik.css.parser;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

public class DefaultDocumentHandler implements DocumentHandler {
   public static final DocumentHandler INSTANCE = new DefaultDocumentHandler();

   protected DefaultDocumentHandler() {
   }

   public void startDocument(InputSource var1) throws CSSException {
   }

   public void endDocument(InputSource var1) throws CSSException {
   }

   public void comment(String var1) throws CSSException {
   }

   public void ignorableAtRule(String var1) throws CSSException {
   }

   public void namespaceDeclaration(String var1, String var2) throws CSSException {
   }

   public void importStyle(String var1, SACMediaList var2, String var3) throws CSSException {
   }

   public void startMedia(SACMediaList var1) throws CSSException {
   }

   public void endMedia(SACMediaList var1) throws CSSException {
   }

   public void startPage(String var1, String var2) throws CSSException {
   }

   public void endPage(String var1, String var2) throws CSSException {
   }

   public void startFontFace() throws CSSException {
   }

   public void endFontFace() throws CSSException {
   }

   public void startSelector(SelectorList var1) throws CSSException {
   }

   public void endSelector(SelectorList var1) throws CSSException {
   }

   public void property(String var1, LexicalUnit var2, boolean var3) throws CSSException {
   }
}
