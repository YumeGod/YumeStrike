package org.apache.fop.render.intermediate;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.fop.util.GenerationHelperContentHandler;
import org.xml.sax.ContentHandler;

public abstract class AbstractXMLWritingIFDocumentHandler extends AbstractIFDocumentHandler {
   protected SAXTransformerFactory tFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
   protected GenerationHelperContentHandler handler;

   public void setResult(Result result) throws IFException {
      if (result instanceof SAXResult) {
         SAXResult saxResult = (SAXResult)result;
         this.handler = new GenerationHelperContentHandler(saxResult.getHandler(), this.getMainNamespace());
      } else {
         this.handler = new GenerationHelperContentHandler(this.createContentHandler(result), this.getMainNamespace());
      }

   }

   protected abstract String getMainNamespace();

   protected ContentHandler createContentHandler(Result result) throws IFException {
      try {
         TransformerHandler tHandler = this.tFactory.newTransformerHandler();
         Transformer transformer = tHandler.getTransformer();
         transformer.setOutputProperty("indent", "yes");
         transformer.setOutputProperty("method", "xml");
         tHandler.setResult(result);
         return tHandler;
      } catch (TransformerConfigurationException var4) {
         throw new IFException("Error while setting up the serializer for XML output", var4);
      }
   }
}
