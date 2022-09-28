package org.apache.xerces.jaxp;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XSMessageFormatter;
import org.apache.xerces.jaxp.validation.XSGrammarPoolContainer;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

final class SchemaValidatorConfiguration implements XMLComponentManager {
   private static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
   private static final String VALIDATION = "http://xml.org/sax/features/validation";
   private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
   private static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
   private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   private final XMLComponentManager fParentComponentManager;
   private final XMLGrammarPool fGrammarPool;
   private final boolean fUseGrammarPoolOnly;
   private final ValidationManager fValidationManager;

   public SchemaValidatorConfiguration(XMLComponentManager var1, XSGrammarPoolContainer var2, ValidationManager var3) {
      this.fParentComponentManager = var1;
      this.fGrammarPool = var2.getGrammarPool();
      this.fUseGrammarPoolOnly = var2.isFullyComposed();
      this.fValidationManager = var3;

      try {
         XMLErrorReporter var4 = (XMLErrorReporter)this.fParentComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
         if (var4 != null) {
            var4.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
         }
      } catch (XMLConfigurationException var5) {
      }

   }

   public boolean getFeature(String var1) throws XMLConfigurationException {
      if ("http://apache.org/xml/features/internal/parser-settings".equals(var1)) {
         return this.fParentComponentManager.getFeature(var1);
      } else if (!"http://xml.org/sax/features/validation".equals(var1) && !"http://apache.org/xml/features/validation/schema".equals(var1)) {
         return "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(var1) ? this.fUseGrammarPoolOnly : this.fParentComponentManager.getFeature(var1);
      } else {
         return true;
      }
   }

   public Object getProperty(String var1) throws XMLConfigurationException {
      if ("http://apache.org/xml/properties/internal/grammar-pool".equals(var1)) {
         return this.fGrammarPool;
      } else {
         return "http://apache.org/xml/properties/internal/validation-manager".equals(var1) ? this.fValidationManager : this.fParentComponentManager.getProperty(var1);
      }
   }
}
