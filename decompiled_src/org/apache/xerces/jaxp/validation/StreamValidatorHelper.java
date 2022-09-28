package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Locale;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.parsers.XML11Configuration;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.xml.sax.SAXException;

final class StreamValidatorHelper implements ValidatorHelper {
   private static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
   private static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   private static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
   private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
   private SoftReference fConfiguration = new SoftReference((Object)null);
   private XMLSchemaValidator fSchemaValidator;
   private XMLSchemaValidatorComponentManager fComponentManager;

   public StreamValidatorHelper(XMLSchemaValidatorComponentManager var1) {
      this.fComponentManager = var1;
      this.fSchemaValidator = (XMLSchemaValidator)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema");
   }

   public void validate(Source var1, Result var2) throws SAXException, IOException {
      if (var2 == null) {
         StreamSource var3 = (StreamSource)var1;
         XMLInputSource var4 = new XMLInputSource(var3.getPublicId(), var3.getSystemId(), (String)null);
         var4.setByteStream(var3.getInputStream());
         var4.setCharacterStream(var3.getReader());
         XMLParserConfiguration var5 = (XMLParserConfiguration)this.fConfiguration.get();
         if (var5 == null) {
            var5 = this.initialize();
         } else if (this.fComponentManager.getFeature("http://apache.org/xml/features/internal/parser-settings")) {
            var5.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver"));
            var5.setProperty("http://apache.org/xml/properties/internal/error-handler", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-handler"));
         }

         this.fComponentManager.reset();
         this.fSchemaValidator.setDocumentHandler((XMLDocumentHandler)null);

         try {
            var5.parse(var4);
         } catch (XMLParseException var8) {
            throw Util.toSAXParseException(var8);
         } catch (XNIException var9) {
            throw Util.toSAXException(var9);
         }
      } else {
         throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "SourceResultMismatch", new Object[]{var1.getClass().getName(), var2.getClass().getName()}));
      }
   }

   private XMLParserConfiguration initialize() {
      XML11Configuration var1 = new XML11Configuration();
      var1.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver"));
      var1.setProperty("http://apache.org/xml/properties/internal/error-handler", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-handler"));
      XMLErrorReporter var2 = (XMLErrorReporter)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
      var1.setProperty("http://apache.org/xml/properties/internal/error-reporter", var2);
      if (var2.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
         XMLMessageFormatter var3 = new XMLMessageFormatter();
         var2.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", var3);
         var2.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", var3);
      }

      var1.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
      var1.setProperty("http://apache.org/xml/properties/internal/validation-manager", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
      var1.setDocumentHandler(this.fSchemaValidator);
      var1.setDTDHandler((XMLDTDHandler)null);
      var1.setDTDContentModelHandler((XMLDTDContentModelHandler)null);
      this.fConfiguration = new SoftReference(var1);
      return var1;
   }
}
