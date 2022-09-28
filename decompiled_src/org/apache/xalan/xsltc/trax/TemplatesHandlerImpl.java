package org.apache.xalan.xsltc.trax;

import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.TemplatesHandler;
import org.apache.xalan.xsltc.compiler.CompilerException;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class TemplatesHandlerImpl implements ContentHandler, TemplatesHandler, SourceLoader {
   private String _systemId;
   private int _indentNumber;
   private URIResolver _uriResolver = null;
   private TransformerFactoryImpl _tfactory = null;
   private Parser _parser = null;
   private TemplatesImpl _templates = null;

   protected TemplatesHandlerImpl(int indentNumber, TransformerFactoryImpl tfactory) {
      this._indentNumber = indentNumber;
      this._tfactory = tfactory;
      XSLTC xsltc = new XSLTC();
      if (tfactory.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
         xsltc.setSecureProcessing(true);
      }

      this._parser = xsltc.getParser();
   }

   public String getSystemId() {
      return this._systemId;
   }

   public void setSystemId(String id) {
      this._systemId = id;
   }

   public void setURIResolver(URIResolver resolver) {
      this._uriResolver = resolver;
   }

   public Templates getTemplates() {
      return this._templates;
   }

   public InputSource loadSource(String href, String context, XSLTC xsltc) {
      try {
         Source source = this._uriResolver.resolve(href, context);
         if (source != null) {
            return Util.getInputSource(xsltc, source);
         }
      } catch (TransformerException var5) {
      }

      return null;
   }

   public void startDocument() {
      XSLTC xsltc = this._parser.getXSLTC();
      xsltc.init();
      xsltc.setOutputType(2);
      this._parser.startDocument();
   }

   public void endDocument() throws SAXException {
      this._parser.endDocument();

      try {
         XSLTC xsltc = this._parser.getXSLTC();
         String transletName = null;
         if (this._systemId != null) {
            transletName = Util.baseName(this._systemId);
         } else {
            transletName = (String)this._tfactory.getAttribute("translet-name");
         }

         xsltc.setClassName(transletName);
         transletName = xsltc.getClassName();
         Stylesheet stylesheet = null;
         SyntaxTreeNode root = this._parser.getDocumentRoot();
         if (!this._parser.errorsFound() && root != null) {
            stylesheet = this._parser.makeStylesheet(root);
            stylesheet.setSystemId(this._systemId);
            stylesheet.setParentStylesheet((Stylesheet)null);
            if (this._uriResolver != null) {
               stylesheet.setSourceLoader(this);
            }

            this._parser.setCurrentStylesheet(stylesheet);
            xsltc.setStylesheet(stylesheet);
            this._parser.createAST(stylesheet);
         }

         if (!this._parser.errorsFound() && stylesheet != null) {
            stylesheet.setMultiDocument(xsltc.isMultiDocument());
            stylesheet.setHasIdCall(xsltc.hasIdCall());
            Class var5 = xsltc.getClass();
            synchronized(var5) {
               stylesheet.translate();
            }
         }

         if (!this._parser.errorsFound()) {
            byte[][] bytecodes = xsltc.getBytecodes();
            if (bytecodes != null) {
               this._templates = new TemplatesImpl(xsltc.getBytecodes(), transletName, this._parser.getOutputProperties(), this._indentNumber, this._tfactory);
               if (this._uriResolver != null) {
                  this._templates.setURIResolver(this._uriResolver);
               }
            }

         } else {
            StringBuffer errorMessage = new StringBuffer();
            Vector errors = this._parser.getErrors();
            int count = errors.size();

            for(int i = 0; i < count; ++i) {
               if (errorMessage.length() > 0) {
                  errorMessage.append('\n');
               }

               errorMessage.append(errors.elementAt(i).toString());
            }

            throw new SAXException("JAXP_COMPILE_ERR", new TransformerException(errorMessage.toString()));
         }
      } catch (CompilerException var10) {
         throw new SAXException("JAXP_COMPILE_ERR", var10);
      }
   }

   public void startPrefixMapping(String prefix, String uri) {
      this._parser.startPrefixMapping(prefix, uri);
   }

   public void endPrefixMapping(String prefix) {
      this._parser.endPrefixMapping(prefix);
   }

   public void startElement(String uri, String localname, String qname, Attributes attributes) throws SAXException {
      this._parser.startElement(uri, localname, qname, attributes);
   }

   public void endElement(String uri, String localname, String qname) {
      this._parser.endElement(uri, localname, qname);
   }

   public void characters(char[] ch, int start, int length) {
      this._parser.characters(ch, start, length);
   }

   public void processingInstruction(String name, String value) {
      this._parser.processingInstruction(name, value);
   }

   public void ignorableWhitespace(char[] ch, int start, int length) {
      this._parser.ignorableWhitespace(ch, start, length);
   }

   public void skippedEntity(String name) {
      this._parser.skippedEntity(name);
   }

   public void setDocumentLocator(Locator locator) {
      this.setSystemId(locator.getSystemId());
      this._parser.setDocumentLocator(locator);
   }
}
