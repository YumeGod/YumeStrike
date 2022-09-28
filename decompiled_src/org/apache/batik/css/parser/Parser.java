package org.apache.batik.css.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import org.apache.batik.i18n.Localizable;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.util.ParsedURL;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SimpleSelector;

public class Parser implements ExtendedParser, Localizable {
   public static final String BUNDLE_CLASSNAME = "org.apache.batik.css.parser.resources.Messages";
   protected LocalizableSupport localizableSupport;
   protected Scanner scanner;
   protected int current;
   protected DocumentHandler documentHandler;
   protected SelectorFactory selectorFactory;
   protected ConditionFactory conditionFactory;
   protected ErrorHandler errorHandler;
   protected String pseudoElement;
   protected String documentURI;
   // $FF: synthetic field
   static Class class$org$apache$batik$css$parser$Parser;

   public Parser() {
      this.localizableSupport = new LocalizableSupport("org.apache.batik.css.parser.resources.Messages", (class$org$apache$batik$css$parser$Parser == null ? (class$org$apache$batik$css$parser$Parser = class$("org.apache.batik.css.parser.Parser")) : class$org$apache$batik$css$parser$Parser).getClassLoader());
      this.documentHandler = DefaultDocumentHandler.INSTANCE;
      this.selectorFactory = DefaultSelectorFactory.INSTANCE;
      this.conditionFactory = DefaultConditionFactory.INSTANCE;
      this.errorHandler = DefaultErrorHandler.INSTANCE;
   }

   public String getParserVersion() {
      return "http://www.w3.org/TR/REC-CSS2";
   }

   public void setLocale(Locale var1) throws CSSException {
      this.localizableSupport.setLocale(var1);
   }

   public Locale getLocale() {
      return this.localizableSupport.getLocale();
   }

   public String formatMessage(String var1, Object[] var2) throws MissingResourceException {
      return this.localizableSupport.formatMessage(var1, var2);
   }

   public void setDocumentHandler(DocumentHandler var1) {
      this.documentHandler = var1;
   }

   public void setSelectorFactory(SelectorFactory var1) {
      this.selectorFactory = var1;
   }

   public void setConditionFactory(ConditionFactory var1) {
      this.conditionFactory = var1;
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandler = var1;
   }

   public void parseStyleSheet(InputSource var1) throws CSSException, IOException {
      this.scanner = this.createScanner(var1);

      try {
         this.documentHandler.startDocument(var1);
         this.current = this.scanner.next();
         switch (this.current) {
            case 18:
               this.documentHandler.comment(this.scanner.getStringValue());
               break;
            case 30:
               if (this.nextIgnoreSpaces() != 19) {
                  this.reportError("charset.string");
               } else {
                  if (this.nextIgnoreSpaces() != 8) {
                     this.reportError("semicolon");
                  }

                  this.next();
               }
         }

         this.skipSpacesAndCDOCDC();

         while(this.current == 28) {
            this.nextIgnoreSpaces();
            this.parseImportRule();
            this.nextIgnoreSpaces();
         }

         while(true) {
            switch (this.current) {
               case 0:
                  return;
               case 29:
                  this.nextIgnoreSpaces();
                  this.parseAtRule();
                  break;
               case 31:
                  this.nextIgnoreSpaces();
                  this.parseFontFaceRule();
                  break;
               case 32:
                  this.nextIgnoreSpaces();
                  this.parseMediaRule();
                  break;
               case 33:
                  this.nextIgnoreSpaces();
                  this.parsePageRule();
                  break;
               default:
                  this.parseRuleSet();
            }

            this.skipSpacesAndCDOCDC();
         }
      } finally {
         this.documentHandler.endDocument(var1);
         this.scanner = null;
      }
   }

   public void parseStyleSheet(String var1) throws CSSException, IOException {
      this.parseStyleSheet(new InputSource(var1));
   }

   public void parseStyleDeclaration(InputSource var1) throws CSSException, IOException {
      this.scanner = this.createScanner(var1);
      this.parseStyleDeclarationInternal();
   }

   protected void parseStyleDeclarationInternal() throws CSSException, IOException {
      this.nextIgnoreSpaces();

      try {
         this.parseStyleDeclaration(false);
      } catch (CSSParseException var5) {
         this.reportError(var5);
      } finally {
         this.scanner = null;
      }

   }

   public void parseRule(InputSource var1) throws CSSException, IOException {
      this.scanner = this.createScanner(var1);
      this.parseRuleInternal();
   }

   protected void parseRuleInternal() throws CSSException, IOException {
      this.nextIgnoreSpaces();
      this.parseRule();
      this.scanner = null;
   }

   public SelectorList parseSelectors(InputSource var1) throws CSSException, IOException {
      this.scanner = this.createScanner(var1);
      return this.parseSelectorsInternal();
   }

   protected SelectorList parseSelectorsInternal() throws CSSException, IOException {
      this.nextIgnoreSpaces();
      SelectorList var1 = this.parseSelectorList();
      this.scanner = null;
      return var1;
   }

   public LexicalUnit parsePropertyValue(InputSource var1) throws CSSException, IOException {
      this.scanner = this.createScanner(var1);
      return this.parsePropertyValueInternal();
   }

   protected LexicalUnit parsePropertyValueInternal() throws CSSException, IOException {
      this.nextIgnoreSpaces();
      LexicalUnit var1 = null;

      try {
         var1 = this.parseExpression(false);
      } catch (CSSParseException var3) {
         this.reportError(var3);
         throw var3;
      }

      CSSParseException var2 = null;
      if (this.current != 0) {
         var2 = this.createCSSParseException("eof.expected");
      }

      this.scanner = null;
      if (var2 != null) {
         this.errorHandler.fatalError(var2);
      }

      return var1;
   }

   public boolean parsePriority(InputSource var1) throws CSSException, IOException {
      this.scanner = this.createScanner(var1);
      return this.parsePriorityInternal();
   }

   protected boolean parsePriorityInternal() throws CSSException, IOException {
      this.nextIgnoreSpaces();
      this.scanner = null;
      switch (this.current) {
         case 0:
            return false;
         case 28:
            return true;
         default:
            this.reportError("token", new Object[]{new Integer(this.current)});
            return false;
      }
   }

   protected void parseRule() {
      switch (this.scanner.getType()) {
         case 28:
            this.nextIgnoreSpaces();
            this.parseImportRule();
            break;
         case 29:
            this.nextIgnoreSpaces();
            this.parseAtRule();
            break;
         case 30:
         default:
            this.parseRuleSet();
            break;
         case 31:
            this.nextIgnoreSpaces();
            this.parseFontFaceRule();
            break;
         case 32:
            this.nextIgnoreSpaces();
            this.parseMediaRule();
            break;
         case 33:
            this.nextIgnoreSpaces();
            this.parsePageRule();
      }

   }

   protected void parseAtRule() {
      this.scanner.scanAtRule();
      this.documentHandler.ignorableAtRule(this.scanner.getStringValue());
      this.nextIgnoreSpaces();
   }

   protected void parseImportRule() {
      String var1 = null;
      switch (this.current) {
         case 19:
         case 51:
            var1 = this.scanner.getStringValue();
            this.nextIgnoreSpaces();
            CSSSACMediaList var2;
            if (this.current != 20) {
               var2 = new CSSSACMediaList();
               var2.append("all");
            } else {
               var2 = this.parseMediaList();
            }

            this.documentHandler.importStyle(var1, var2, (String)null);
            if (this.current != 8) {
               this.reportError("semicolon");
            } else {
               this.next();
            }

            return;
         default:
            this.reportError("string.or.uri");
      }
   }

   protected CSSSACMediaList parseMediaList() {
      CSSSACMediaList var1 = new CSSSACMediaList();
      var1.append(this.scanner.getStringValue());
      this.nextIgnoreSpaces();

      while(this.current == 6) {
         this.nextIgnoreSpaces();
         switch (this.current) {
            case 20:
               var1.append(this.scanner.getStringValue());
               this.nextIgnoreSpaces();
               break;
            default:
               this.reportError("identifier");
         }
      }

      return var1;
   }

   protected void parseFontFaceRule() {
      try {
         this.documentHandler.startFontFace();
         if (this.current != 1) {
            this.reportError("left.curly.brace");
         } else {
            this.nextIgnoreSpaces();

            try {
               this.parseStyleDeclaration(true);
            } catch (CSSParseException var5) {
               this.reportError(var5);
            }
         }
      } finally {
         this.documentHandler.endFontFace();
      }

   }

   protected void parsePageRule() {
      String var1 = null;
      String var2 = null;
      if (this.current == 20) {
         var1 = this.scanner.getStringValue();
         this.nextIgnoreSpaces();
         if (this.current == 16) {
            this.nextIgnoreSpaces();
            if (this.current != 20) {
               this.reportError("identifier");
               return;
            }

            var2 = this.scanner.getStringValue();
            this.nextIgnoreSpaces();
         }
      }

      try {
         this.documentHandler.startPage(var1, var2);
         if (this.current != 1) {
            this.reportError("left.curly.brace");
         } else {
            this.nextIgnoreSpaces();

            try {
               this.parseStyleDeclaration(true);
            } catch (CSSParseException var7) {
               this.reportError(var7);
            }
         }
      } finally {
         this.documentHandler.endPage(var1, var2);
      }

   }

   protected void parseMediaRule() {
      if (this.current != 20) {
         this.reportError("identifier");
      } else {
         CSSSACMediaList var1 = this.parseMediaList();

         try {
            this.documentHandler.startMedia(var1);
            if (this.current != 1) {
               this.reportError("left.curly.brace");
            } else {
               this.nextIgnoreSpaces();

               while(true) {
                  switch (this.current) {
                     case 0:
                     case 2:
                        this.nextIgnoreSpaces();
                        return;
                     default:
                        this.parseRuleSet();
                  }
               }
            }
         } finally {
            this.documentHandler.endMedia(var1);
         }

      }
   }

   protected void parseRuleSet() {
      SelectorList var1 = null;

      try {
         var1 = this.parseSelectorList();
      } catch (CSSParseException var9) {
         this.reportError(var9);
         return;
      }

      try {
         this.documentHandler.startSelector(var1);
         if (this.current != 1) {
            this.reportError("left.curly.brace");
            if (this.current == 2) {
               this.nextIgnoreSpaces();
            }
         } else {
            this.nextIgnoreSpaces();

            try {
               this.parseStyleDeclaration(true);
            } catch (CSSParseException var7) {
               this.reportError(var7);
            }
         }
      } finally {
         this.documentHandler.endSelector(var1);
      }

   }

   protected SelectorList parseSelectorList() {
      CSSSelectorList var1 = new CSSSelectorList();
      var1.append(this.parseSelector());

      while(this.current == 6) {
         this.nextIgnoreSpaces();
         var1.append(this.parseSelector());
      }

      return var1;
   }

   protected Selector parseSelector() {
      this.pseudoElement = null;
      Object var1 = this.parseSimpleSelector();

      while(true) {
         switch (this.current) {
            case 4:
               if (this.pseudoElement != null) {
                  throw this.createCSSParseException("pseudo.element.position");
               }

               this.nextIgnoreSpaces();
               var1 = this.selectorFactory.createDirectAdjacentSelector((short)1, (Selector)var1, this.parseSimpleSelector());
               break;
            case 5:
            case 6:
            case 8:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
            case 18:
            case 19:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
               if (this.pseudoElement != null) {
                  var1 = this.selectorFactory.createChildSelector((Selector)var1, this.selectorFactory.createPseudoElementSelector((String)null, this.pseudoElement));
               }

               return (Selector)var1;
            case 7:
            case 11:
            case 13:
            case 16:
            case 20:
            case 27:
               if (this.pseudoElement != null) {
                  throw this.createCSSParseException("pseudo.element.position");
               }

               var1 = this.selectorFactory.createDescendantSelector((Selector)var1, this.parseSimpleSelector());
               break;
            case 9:
               if (this.pseudoElement != null) {
                  throw this.createCSSParseException("pseudo.element.position");
               }

               this.nextIgnoreSpaces();
               var1 = this.selectorFactory.createChildSelector((Selector)var1, this.parseSimpleSelector());
         }
      }
   }

   protected SimpleSelector parseSimpleSelector() {
      Object var1;
      switch (this.current) {
         case 13:
            this.next();
         default:
            var1 = this.selectorFactory.createElementSelector((String)null, (String)null);
            break;
         case 20:
            var1 = this.selectorFactory.createElementSelector((String)null, this.scanner.getStringValue());
            this.next();
      }

      Object var2 = null;

      while(true) {
         Object var3;
         var3 = null;
         String var6;
         label74:
         switch (this.current) {
            case 7:
               if (this.next() != 20) {
                  throw this.createCSSParseException("identifier");
               }

               var3 = this.conditionFactory.createClassCondition((String)null, this.scanner.getStringValue());
               this.next();
               break;
            case 11:
               if (this.nextIgnoreSpaces() != 20) {
                  throw this.createCSSParseException("identifier");
               }

               String var4 = this.scanner.getStringValue();
               int var5 = this.nextIgnoreSpaces();
               switch (var5) {
                  case 3:
                  case 25:
                  case 26:
                     var6 = null;
                     switch (this.nextIgnoreSpaces()) {
                        case 19:
                        case 20:
                           var6 = this.scanner.getStringValue();
                           this.nextIgnoreSpaces();
                           if (this.current != 12) {
                              throw this.createCSSParseException("right.bracket");
                           }

                           this.next();
                           switch (var5) {
                              case 3:
                                 var3 = this.conditionFactory.createAttributeCondition(var4, (String)null, false, var6);
                                 break label74;
                              case 26:
                                 var3 = this.conditionFactory.createOneOfAttributeCondition(var4, (String)null, false, var6);
                                 break label74;
                              default:
                                 var3 = this.conditionFactory.createBeginHyphenAttributeCondition(var4, (String)null, false, var6);
                                 break label74;
                           }
                        default:
                           throw this.createCSSParseException("identifier.or.string");
                     }
                  case 12:
                     this.nextIgnoreSpaces();
                     var3 = this.conditionFactory.createAttributeCondition(var4, (String)null, false, (String)null);
                     break label74;
                  default:
                     throw this.createCSSParseException("right.bracket");
               }
            case 16:
               switch (this.nextIgnoreSpaces()) {
                  case 20:
                     var6 = this.scanner.getStringValue();
                     if (this.isPseudoElement(var6)) {
                        if (this.pseudoElement != null) {
                           throw this.createCSSParseException("duplicate.pseudo.element");
                        }

                        this.pseudoElement = var6;
                     } else {
                        var3 = this.conditionFactory.createPseudoClassCondition((String)null, var6);
                     }

                     this.next();
                     break label74;
                  case 52:
                     String var7 = this.scanner.getStringValue();
                     if (this.nextIgnoreSpaces() != 20) {
                        throw this.createCSSParseException("identifier");
                     }

                     String var8 = this.scanner.getStringValue();
                     if (this.nextIgnoreSpaces() != 15) {
                        throw this.createCSSParseException("right.brace");
                     }

                     if (!var7.equalsIgnoreCase("lang")) {
                        throw this.createCSSParseException("pseudo.function");
                     }

                     var3 = this.conditionFactory.createLangCondition(var8);
                     this.next();
                     break label74;
                  default:
                     throw this.createCSSParseException("identifier");
               }
            case 27:
               var3 = this.conditionFactory.createIdCondition(this.scanner.getStringValue());
               this.next();
               break;
            default:
               this.skipSpaces();
               if (var2 != null) {
                  var1 = this.selectorFactory.createConditionalSelector((SimpleSelector)var1, (Condition)var2);
               }

               return (SimpleSelector)var1;
         }

         if (var3 != null) {
            if (var2 == null) {
               var2 = var3;
            } else {
               var2 = this.conditionFactory.createAndCondition((Condition)var2, (Condition)var3);
            }
         }
      }
   }

   protected boolean isPseudoElement(String var1) {
      switch (var1.charAt(0)) {
         case 'A':
         case 'a':
            return var1.equalsIgnoreCase("after");
         case 'B':
         case 'b':
            return var1.equalsIgnoreCase("before");
         case 'F':
         case 'f':
            return var1.equalsIgnoreCase("first-letter") || var1.equalsIgnoreCase("first-line");
         default:
            return false;
      }
   }

   protected void parseStyleDeclaration(boolean var1) throws CSSException {
      while(true) {
         switch (this.current) {
            case 0:
               if (var1) {
                  throw this.createCSSParseException("eof");
               }

               return;
            case 2:
               if (!var1) {
                  throw this.createCSSParseException("eof.expected");
               }

               this.nextIgnoreSpaces();
               return;
            case 8:
               this.nextIgnoreSpaces();
               break;
            case 20:
               String var2 = this.scanner.getStringValue();
               if (this.nextIgnoreSpaces() != 16) {
                  throw this.createCSSParseException("colon");
               }

               this.nextIgnoreSpaces();
               LexicalUnit var3 = null;

               try {
                  var3 = this.parseExpression(false);
               } catch (CSSParseException var5) {
                  this.reportError(var5);
               }

               if (var3 == null) {
                  break;
               }

               boolean var4 = false;
               if (this.current == 23) {
                  var4 = true;
                  this.nextIgnoreSpaces();
               }

               this.documentHandler.property(var2, var3, var4);
               break;
            default:
               throw this.createCSSParseException("identifier");
         }
      }
   }

   protected LexicalUnit parseExpression(boolean var1) {
      LexicalUnit var2 = this.parseTerm((LexicalUnit)null);
      Object var3 = var2;

      while(true) {
         boolean var4 = false;
         switch (this.current) {
            case 6:
               var4 = true;
               var3 = CSSLexicalUnit.createSimple((short)0, (LexicalUnit)var3);
               this.nextIgnoreSpaces();
               break;
            case 10:
               var4 = true;
               var3 = CSSLexicalUnit.createSimple((short)4, (LexicalUnit)var3);
               this.nextIgnoreSpaces();
         }

         if (var1) {
            if (this.current == 15) {
               if (var4) {
                  throw this.createCSSParseException("token", new Object[]{new Integer(this.current)});
               }

               return var2;
            }

            var3 = this.parseTerm((LexicalUnit)var3);
         } else {
            switch (this.current) {
               case 0:
               case 2:
               case 8:
               case 23:
                  if (var4) {
                     throw this.createCSSParseException("token", new Object[]{new Integer(this.current)});
                  }

                  return var2;
               default:
                  var3 = this.parseTerm((LexicalUnit)var3);
            }
         }
      }
   }

   protected LexicalUnit parseTerm(LexicalUnit var1) {
      boolean var2 = true;
      boolean var3 = false;
      switch (this.current) {
         case 5:
            var2 = false;
         case 4:
            this.next();
            var3 = true;
         default:
            String var4;
            switch (this.current) {
               case 24:
                  var4 = this.scanner.getStringValue();
                  if (!var2) {
                     var4 = "-" + var4;
                  }

                  long var5 = Long.parseLong(var4);
                  if (var5 >= -2147483648L && var5 <= 2147483647L) {
                     int var7 = (int)var5;
                     this.nextIgnoreSpaces();
                     return CSSLexicalUnit.createInteger(var7, var1);
                  }
               case 54:
                  return CSSLexicalUnit.createFloat((short)14, this.number(var2), var1);
               case 25:
               case 26:
               case 27:
               case 28:
               case 29:
               case 30:
               case 31:
               case 32:
               case 33:
               case 51:
               case 53:
               default:
                  if (var3) {
                     throw this.createCSSParseException("token", new Object[]{new Integer(this.current)});
                  } else {
                     switch (this.current) {
                        case 19:
                           var4 = this.scanner.getStringValue();
                           this.nextIgnoreSpaces();
                           return CSSLexicalUnit.createString((short)36, var4, var1);
                        case 20:
                           var4 = this.scanner.getStringValue();
                           this.nextIgnoreSpaces();
                           if (var4.equalsIgnoreCase("inherit")) {
                              return CSSLexicalUnit.createSimple((short)12, var1);
                           }

                           return CSSLexicalUnit.createString((short)35, var4, var1);
                        case 27:
                           return this.hexcolor(var1);
                        case 51:
                           var4 = this.scanner.getStringValue();
                           this.nextIgnoreSpaces();
                           return CSSLexicalUnit.createString((short)24, var4, var1);
                        default:
                           throw this.createCSSParseException("token", new Object[]{new Integer(this.current)});
                     }
                  }
               case 34:
                  return this.dimension(var2, var1);
               case 35:
                  return CSSLexicalUnit.createFloat((short)16, this.number(var2), var1);
               case 36:
                  return CSSLexicalUnit.createFloat((short)15, this.number(var2), var1);
               case 37:
                  return CSSLexicalUnit.createFloat((short)19, this.number(var2), var1);
               case 38:
                  return CSSLexicalUnit.createFloat((short)20, this.number(var2), var1);
               case 39:
                  return CSSLexicalUnit.createFloat((short)18, this.number(var2), var1);
               case 40:
                  return CSSLexicalUnit.createFloat((short)31, this.number(var2), var1);
               case 41:
                  return CSSLexicalUnit.createFloat((short)33, this.number(var2), var1);
               case 42:
                  return CSSLexicalUnit.createFloat((short)23, this.number(var2), var1);
               case 43:
                  return CSSLexicalUnit.createFloat((short)32, this.number(var2), var1);
               case 44:
                  return CSSLexicalUnit.createFloat((short)22, this.number(var2), var1);
               case 45:
                  return CSSLexicalUnit.createFloat((short)21, this.number(var2), var1);
               case 46:
                  return CSSLexicalUnit.createFloat((short)17, this.number(var2), var1);
               case 47:
                  return CSSLexicalUnit.createFloat((short)28, this.number(var2), var1);
               case 48:
                  return CSSLexicalUnit.createFloat((short)30, this.number(var2), var1);
               case 49:
                  return CSSLexicalUnit.createFloat((short)29, this.number(var2), var1);
               case 50:
                  return CSSLexicalUnit.createFloat((short)34, this.number(var2), var1);
               case 52:
                  return this.parseFunction(var2, var1);
            }
      }
   }

   protected LexicalUnit parseFunction(boolean var1, LexicalUnit var2) {
      String var3 = this.scanner.getStringValue();
      this.nextIgnoreSpaces();
      LexicalUnit var4 = this.parseExpression(true);
      if (this.current != 15) {
         throw this.createCSSParseException("token", new Object[]{new Integer(this.current)});
      } else {
         this.nextIgnoreSpaces();
         LexicalUnit var5;
         switch (var3.charAt(0)) {
            case 'A':
            case 'a':
               if (var3.equalsIgnoreCase("attr") && var4 != null) {
                  switch (var4.getLexicalUnitType()) {
                     case 35:
                        var5 = var4.getNextLexicalUnit();
                        if (var5 == null) {
                           return CSSLexicalUnit.createString((short)37, var4.getStringValue(), var2);
                        }
                  }
               }
               break;
            case 'C':
            case 'c':
               if (var3.equalsIgnoreCase("counter")) {
                  if (var4 != null) {
                     switch (var4.getLexicalUnitType()) {
                        case 35:
                           var5 = var4.getNextLexicalUnit();
                           if (var5 != null) {
                              switch (var5.getLexicalUnitType()) {
                                 case 0:
                                    var5 = var5.getNextLexicalUnit();
                                    if (var5 != null) {
                                       switch (var5.getLexicalUnitType()) {
                                          case 35:
                                             var5 = var5.getNextLexicalUnit();
                                             if (var5 == null) {
                                                return CSSLexicalUnit.createPredefinedFunction((short)25, var4, var2);
                                             }
                                       }
                                    }
                              }
                           }
                     }
                  }
               } else if (var3.equalsIgnoreCase("counters") && var4 != null) {
                  switch (var4.getLexicalUnitType()) {
                     case 35:
                        var5 = var4.getNextLexicalUnit();
                        if (var5 != null) {
                           switch (var5.getLexicalUnitType()) {
                              case 0:
                                 var5 = var5.getNextLexicalUnit();
                                 if (var5 != null) {
                                    switch (var5.getLexicalUnitType()) {
                                       case 36:
                                          var5 = var5.getNextLexicalUnit();
                                          if (var5 != null) {
                                             switch (var5.getLexicalUnitType()) {
                                                case 0:
                                                   var5 = var5.getNextLexicalUnit();
                                                   if (var5 != null) {
                                                      switch (var5.getLexicalUnitType()) {
                                                         case 35:
                                                            var5 = var5.getNextLexicalUnit();
                                                            if (var5 == null) {
                                                               return CSSLexicalUnit.createPredefinedFunction((short)26, var4, var2);
                                                            }
                                                      }
                                                   }
                                             }
                                          }
                                    }
                                 }
                           }
                        }
                  }
               }
               break;
            case 'R':
            case 'r':
               if (var3.equalsIgnoreCase("rgb")) {
                  if (var4 != null) {
                     switch (var4.getLexicalUnitType()) {
                        case 13:
                        case 23:
                           var5 = var4.getNextLexicalUnit();
                           if (var5 != null) {
                              switch (var5.getLexicalUnitType()) {
                                 case 0:
                                    var5 = var5.getNextLexicalUnit();
                                    if (var5 != null) {
                                       switch (var5.getLexicalUnitType()) {
                                          case 13:
                                          case 23:
                                             var5 = var5.getNextLexicalUnit();
                                             if (var5 != null) {
                                                switch (var5.getLexicalUnitType()) {
                                                   case 0:
                                                      var5 = var5.getNextLexicalUnit();
                                                      if (var5 != null) {
                                                         switch (var5.getLexicalUnitType()) {
                                                            case 13:
                                                            case 23:
                                                               var5 = var5.getNextLexicalUnit();
                                                               if (var5 == null) {
                                                                  return CSSLexicalUnit.createPredefinedFunction((short)27, var4, var2);
                                                               }
                                                         }
                                                      }
                                                }
                                             }
                                       }
                                    }
                              }
                           }
                     }
                  }
               } else if (var3.equalsIgnoreCase("rect") && var4 != null) {
                  switch (var4.getLexicalUnitType()) {
                     case 13:
                        if (var4.getIntegerValue() == 0) {
                           var5 = var4.getNextLexicalUnit();
                           break;
                        }

                        return CSSLexicalUnit.createFunction(var3, var4, var2);
                     case 14:
                     case 24:
                     case 25:
                     case 26:
                     case 27:
                     case 28:
                     case 29:
                     case 30:
                     case 31:
                     case 32:
                     case 33:
                     case 34:
                     default:
                        return CSSLexicalUnit.createFunction(var3, var4, var2);
                     case 15:
                     case 16:
                     case 17:
                     case 18:
                     case 19:
                     case 20:
                     case 21:
                     case 22:
                     case 23:
                        var5 = var4.getNextLexicalUnit();
                        break;
                     case 35:
                        if (!var4.getStringValue().equalsIgnoreCase("auto")) {
                           return CSSLexicalUnit.createFunction(var3, var4, var2);
                        }

                        var5 = var4.getNextLexicalUnit();
                  }

                  if (var5 != null) {
                     switch (var5.getLexicalUnitType()) {
                        case 0:
                           var5 = var5.getNextLexicalUnit();
                           if (var5 != null) {
                              switch (var5.getLexicalUnitType()) {
                                 case 13:
                                    if (var5.getIntegerValue() == 0) {
                                       var5 = var5.getNextLexicalUnit();
                                       break;
                                    }

                                    return CSSLexicalUnit.createFunction(var3, var4, var2);
                                 case 14:
                                 case 24:
                                 case 25:
                                 case 26:
                                 case 27:
                                 case 28:
                                 case 29:
                                 case 30:
                                 case 31:
                                 case 32:
                                 case 33:
                                 case 34:
                                 default:
                                    return CSSLexicalUnit.createFunction(var3, var4, var2);
                                 case 15:
                                 case 16:
                                 case 17:
                                 case 18:
                                 case 19:
                                 case 20:
                                 case 21:
                                 case 22:
                                 case 23:
                                    var5 = var5.getNextLexicalUnit();
                                    break;
                                 case 35:
                                    if (!var5.getStringValue().equalsIgnoreCase("auto")) {
                                       return CSSLexicalUnit.createFunction(var3, var4, var2);
                                    }

                                    var5 = var5.getNextLexicalUnit();
                              }

                              if (var5 != null) {
                                 switch (var5.getLexicalUnitType()) {
                                    case 0:
                                       var5 = var5.getNextLexicalUnit();
                                       if (var5 != null) {
                                          switch (var5.getLexicalUnitType()) {
                                             case 13:
                                                if (var5.getIntegerValue() == 0) {
                                                   var5 = var5.getNextLexicalUnit();
                                                   break;
                                                }

                                                return CSSLexicalUnit.createFunction(var3, var4, var2);
                                             case 14:
                                             case 24:
                                             case 25:
                                             case 26:
                                             case 27:
                                             case 28:
                                             case 29:
                                             case 30:
                                             case 31:
                                             case 32:
                                             case 33:
                                             case 34:
                                             default:
                                                return CSSLexicalUnit.createFunction(var3, var4, var2);
                                             case 15:
                                             case 16:
                                             case 17:
                                             case 18:
                                             case 19:
                                             case 20:
                                             case 21:
                                             case 22:
                                             case 23:
                                                var5 = var5.getNextLexicalUnit();
                                                break;
                                             case 35:
                                                if (!var5.getStringValue().equalsIgnoreCase("auto")) {
                                                   return CSSLexicalUnit.createFunction(var3, var4, var2);
                                                }

                                                var5 = var5.getNextLexicalUnit();
                                          }

                                          if (var5 != null) {
                                             switch (var5.getLexicalUnitType()) {
                                                case 0:
                                                   var5 = var5.getNextLexicalUnit();
                                                   if (var5 != null) {
                                                      switch (var5.getLexicalUnitType()) {
                                                         case 13:
                                                            if (var5.getIntegerValue() == 0) {
                                                               var5 = var5.getNextLexicalUnit();
                                                               break;
                                                            }

                                                            return CSSLexicalUnit.createFunction(var3, var4, var2);
                                                         case 14:
                                                         case 24:
                                                         case 25:
                                                         case 26:
                                                         case 27:
                                                         case 28:
                                                         case 29:
                                                         case 30:
                                                         case 31:
                                                         case 32:
                                                         case 33:
                                                         case 34:
                                                         default:
                                                            return CSSLexicalUnit.createFunction(var3, var4, var2);
                                                         case 15:
                                                         case 16:
                                                         case 17:
                                                         case 18:
                                                         case 19:
                                                         case 20:
                                                         case 21:
                                                         case 22:
                                                         case 23:
                                                            var5 = var5.getNextLexicalUnit();
                                                            break;
                                                         case 35:
                                                            if (!var5.getStringValue().equalsIgnoreCase("auto")) {
                                                               return CSSLexicalUnit.createFunction(var3, var4, var2);
                                                            }

                                                            var5 = var5.getNextLexicalUnit();
                                                      }

                                                      if (var5 == null) {
                                                         return CSSLexicalUnit.createPredefinedFunction((short)38, var4, var2);
                                                      }
                                                   }
                                             }
                                          }
                                       }
                                 }
                              }
                           }
                     }
                  }
               }
         }

         return CSSLexicalUnit.createFunction(var3, var4, var2);
      }
   }

   protected LexicalUnit hexcolor(LexicalUnit var1) {
      String var2 = this.scanner.getStringValue();
      int var3 = var2.length();
      CSSLexicalUnit var4 = null;
      int var9;
      int var10;
      int var11;
      CSSLexicalUnit var12;
      switch (var3) {
         case 3:
            char var5 = Character.toLowerCase(var2.charAt(0));
            char var6 = Character.toLowerCase(var2.charAt(1));
            char var7 = Character.toLowerCase(var2.charAt(2));
            if (ScannerUtilities.isCSSHexadecimalCharacter(var5) && ScannerUtilities.isCSSHexadecimalCharacter(var6) && ScannerUtilities.isCSSHexadecimalCharacter(var7)) {
               int var8;
               var9 = var8 = var5 >= '0' && var5 <= '9' ? var5 - 48 : var5 - 97 + 10;
               var8 <<= 4;
               var9 |= var8;
               var10 = var8 = var6 >= '0' && var6 <= '9' ? var6 - 48 : var6 - 97 + 10;
               var8 <<= 4;
               var10 |= var8;
               var11 = var8 = var7 >= '0' && var7 <= '9' ? var7 - 48 : var7 - 97 + 10;
               var8 <<= 4;
               var11 |= var8;
               var4 = CSSLexicalUnit.createInteger(var9, (LexicalUnit)null);
               var12 = CSSLexicalUnit.createSimple((short)0, var4);
               var12 = CSSLexicalUnit.createInteger(var10, var12);
               var12 = CSSLexicalUnit.createSimple((short)0, var12);
               CSSLexicalUnit.createInteger(var11, var12);
               break;
            }

            throw this.createCSSParseException("rgb.color", new Object[]{var2});
         case 6:
            char var13 = Character.toLowerCase(var2.charAt(0));
            char var14 = Character.toLowerCase(var2.charAt(1));
            char var15 = Character.toLowerCase(var2.charAt(2));
            char var16 = Character.toLowerCase(var2.charAt(3));
            char var17 = Character.toLowerCase(var2.charAt(4));
            char var18 = Character.toLowerCase(var2.charAt(5));
            if (ScannerUtilities.isCSSHexadecimalCharacter(var13) && ScannerUtilities.isCSSHexadecimalCharacter(var14) && ScannerUtilities.isCSSHexadecimalCharacter(var15) && ScannerUtilities.isCSSHexadecimalCharacter(var16) && ScannerUtilities.isCSSHexadecimalCharacter(var17) && ScannerUtilities.isCSSHexadecimalCharacter(var18)) {
               var9 = var13 >= '0' && var13 <= '9' ? var13 - 48 : var13 - 97 + 10;
               var9 <<= 4;
               var9 |= var14 >= '0' && var14 <= '9' ? var14 - 48 : var14 - 97 + 10;
               var10 = var15 >= '0' && var15 <= '9' ? var15 - 48 : var15 - 97 + 10;
               var10 <<= 4;
               var10 |= var16 >= '0' && var16 <= '9' ? var16 - 48 : var16 - 97 + 10;
               var11 = var17 >= '0' && var17 <= '9' ? var17 - 48 : var17 - 97 + 10;
               var11 <<= 4;
               var11 |= var18 >= '0' && var18 <= '9' ? var18 - 48 : var18 - 97 + 10;
               var4 = CSSLexicalUnit.createInteger(var9, (LexicalUnit)null);
               var12 = CSSLexicalUnit.createSimple((short)0, var4);
               var12 = CSSLexicalUnit.createInteger(var10, var12);
               var12 = CSSLexicalUnit.createSimple((short)0, var12);
               CSSLexicalUnit.createInteger(var11, var12);
               break;
            }

            throw this.createCSSParseException("rgb.color");
         default:
            throw this.createCSSParseException("rgb.color", new Object[]{var2});
      }

      this.nextIgnoreSpaces();
      return CSSLexicalUnit.createPredefinedFunction((short)27, var4, var1);
   }

   protected Scanner createScanner(InputSource var1) {
      this.documentURI = var1.getURI();
      if (this.documentURI == null) {
         this.documentURI = "";
      }

      Reader var2 = var1.getCharacterStream();
      if (var2 != null) {
         return new Scanner(var2);
      } else {
         InputStream var3 = var1.getByteStream();
         if (var3 != null) {
            return new Scanner(var3, var1.getEncoding());
         } else {
            String var4 = var1.getURI();
            if (var4 == null) {
               throw new CSSException(this.formatMessage("empty.source", (Object[])null));
            } else {
               try {
                  ParsedURL var5 = new ParsedURL(var4);
                  var3 = var5.openStreamRaw("text/css");
                  return new Scanner(var3, var1.getEncoding());
               } catch (IOException var6) {
                  throw new CSSException(var6);
               }
            }
         }
      }
   }

   protected int skipSpaces() {
      int var1;
      for(var1 = this.scanner.getType(); var1 == 17; var1 = this.next()) {
      }

      return var1;
   }

   protected int skipSpacesAndCDOCDC() {
      while(true) {
         switch (this.current) {
            case 17:
            case 18:
            case 21:
            case 22:
               this.scanner.clearBuffer();
               this.next();
               break;
            case 19:
            case 20:
            default:
               return this.current;
         }
      }
   }

   protected float number(boolean var1) {
      try {
         float var2 = var1 ? 1.0F : -1.0F;
         String var3 = this.scanner.getStringValue();
         this.nextIgnoreSpaces();
         return var2 * Float.parseFloat(var3);
      } catch (NumberFormatException var4) {
         throw this.createCSSParseException("number.format");
      }
   }

   protected LexicalUnit dimension(boolean var1, LexicalUnit var2) {
      try {
         float var3 = var1 ? 1.0F : -1.0F;
         String var4 = this.scanner.getStringValue();
         int var5 = 0;

         label25:
         while(var5 < var4.length()) {
            switch (var4.charAt(var5)) {
               case '.':
               case '0':
               case '1':
               case '2':
               case '3':
               case '4':
               case '5':
               case '6':
               case '7':
               case '8':
               case '9':
                  ++var5;
                  break;
               case '/':
               default:
                  break label25;
            }
         }

         this.nextIgnoreSpaces();
         return CSSLexicalUnit.createDimension(var3 * Float.parseFloat(var4.substring(0, var5)), var4.substring(var5), var2);
      } catch (NumberFormatException var6) {
         throw this.createCSSParseException("number.format");
      }
   }

   protected int next() {
      try {
         while(true) {
            this.scanner.clearBuffer();
            this.current = this.scanner.next();
            if (this.current != 18) {
               return this.current;
            }

            this.documentHandler.comment(this.scanner.getStringValue());
         }
      } catch (ParseException var2) {
         this.reportError(var2.getMessage());
         return this.current;
      }
   }

   protected int nextIgnoreSpaces() {
      try {
         while(true) {
            this.scanner.clearBuffer();
            this.current = this.scanner.next();
            switch (this.current) {
               case 17:
                  break;
               case 18:
                  this.documentHandler.comment(this.scanner.getStringValue());
                  break;
               default:
                  return this.current;
            }
         }
      } catch (ParseException var2) {
         this.errorHandler.error(this.createCSSParseException(var2.getMessage()));
         return this.current;
      }
   }

   protected void reportError(String var1) {
      this.reportError(var1, (Object[])null);
   }

   protected void reportError(String var1, Object[] var2) {
      this.reportError(this.createCSSParseException(var1, var2));
   }

   protected void reportError(CSSParseException var1) {
      this.errorHandler.error(var1);
      int var2 = 1;

      while(true) {
         switch (this.current) {
            case 0:
               return;
            case 2:
            case 8:
               --var2;
               if (var2 == 0) {
                  this.nextIgnoreSpaces();
                  return;
               }
            case 1:
               ++var2;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
               this.nextIgnoreSpaces();
         }
      }
   }

   protected CSSParseException createCSSParseException(String var1) {
      return this.createCSSParseException(var1, (Object[])null);
   }

   protected CSSParseException createCSSParseException(String var1, Object[] var2) {
      return new CSSParseException(this.formatMessage(var1, var2), this.documentURI, this.scanner.getLine(), this.scanner.getColumn());
   }

   public void parseStyleDeclaration(String var1) throws CSSException, IOException {
      this.scanner = new Scanner(var1);
      this.parseStyleDeclarationInternal();
   }

   public void parseRule(String var1) throws CSSException, IOException {
      this.scanner = new Scanner(var1);
      this.parseRuleInternal();
   }

   public SelectorList parseSelectors(String var1) throws CSSException, IOException {
      this.scanner = new Scanner(var1);
      return this.parseSelectorsInternal();
   }

   public LexicalUnit parsePropertyValue(String var1) throws CSSException, IOException {
      this.scanner = new Scanner(var1);
      return this.parsePropertyValueInternal();
   }

   public boolean parsePriority(String var1) throws CSSException, IOException {
      this.scanner = new Scanner(var1);
      return this.parsePriorityInternal();
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

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
