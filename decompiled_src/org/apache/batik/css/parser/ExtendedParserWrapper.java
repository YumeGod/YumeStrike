package org.apache.batik.css.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;
import java.util.StringTokenizer;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SelectorList;

public class ExtendedParserWrapper implements ExtendedParser {
   public org.w3c.css.sac.Parser parser;

   public static ExtendedParser wrap(org.w3c.css.sac.Parser var0) {
      return (ExtendedParser)(var0 instanceof ExtendedParser ? (ExtendedParser)var0 : new ExtendedParserWrapper(var0));
   }

   public ExtendedParserWrapper(org.w3c.css.sac.Parser var1) {
      this.parser = var1;
   }

   public String getParserVersion() {
      return this.parser.getParserVersion();
   }

   public void setLocale(Locale var1) throws CSSException {
      this.parser.setLocale(var1);
   }

   public void setDocumentHandler(DocumentHandler var1) {
      this.parser.setDocumentHandler(var1);
   }

   public void setSelectorFactory(SelectorFactory var1) {
      this.parser.setSelectorFactory(var1);
   }

   public void setConditionFactory(ConditionFactory var1) {
      this.parser.setConditionFactory(var1);
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.parser.setErrorHandler(var1);
   }

   public void parseStyleSheet(InputSource var1) throws CSSException, IOException {
      this.parser.parseStyleSheet(var1);
   }

   public void parseStyleSheet(String var1) throws CSSException, IOException {
      this.parser.parseStyleSheet(var1);
   }

   public void parseStyleDeclaration(InputSource var1) throws CSSException, IOException {
      this.parser.parseStyleDeclaration(var1);
   }

   public void parseStyleDeclaration(String var1) throws CSSException, IOException {
      this.parser.parseStyleDeclaration(new InputSource(new StringReader(var1)));
   }

   public void parseRule(InputSource var1) throws CSSException, IOException {
      this.parser.parseRule(var1);
   }

   public void parseRule(String var1) throws CSSException, IOException {
      this.parser.parseRule(new InputSource(new StringReader(var1)));
   }

   public SelectorList parseSelectors(InputSource var1) throws CSSException, IOException {
      return this.parser.parseSelectors(var1);
   }

   public SelectorList parseSelectors(String var1) throws CSSException, IOException {
      return this.parser.parseSelectors(new InputSource(new StringReader(var1)));
   }

   public LexicalUnit parsePropertyValue(InputSource var1) throws CSSException, IOException {
      return this.parser.parsePropertyValue(var1);
   }

   public LexicalUnit parsePropertyValue(String var1) throws CSSException, IOException {
      return this.parser.parsePropertyValue(new InputSource(new StringReader(var1)));
   }

   public boolean parsePriority(InputSource var1) throws CSSException, IOException {
      return this.parser.parsePriority(var1);
   }

   public SACMediaList parseMedia(String var1) throws CSSException, IOException {
      CSSSACMediaList var2 = new CSSSACMediaList();
      if (!"all".equalsIgnoreCase(var1)) {
         StringTokenizer var3 = new StringTokenizer(var1, " ,");

         while(var3.hasMoreTokens()) {
            var2.append(var3.nextToken());
         }
      }

      return var2;
   }

   public boolean parsePriority(String var1) throws CSSException, IOException {
      return this.parser.parsePriority(new InputSource(new StringReader(var1)));
   }
}
