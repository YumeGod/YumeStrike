package org.apache.fop.accessibility;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.xml.sax.helpers.DefaultHandler;

public final class Accessibility {
   public static final String ACCESSIBILITY = "accessibility";
   private static SAXTransformerFactory tfactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
   private static Templates addPtrTemplates;
   private static Templates reduceFOTreeTemplates;

   private Accessibility() {
   }

   public static DefaultHandler decorateDefaultHandler(DefaultHandler handler, FOUserAgent userAgent) throws FOPException {
      try {
         setupTemplates();
         TransformerHandler addPtr = tfactory.newTransformerHandler(addPtrTemplates);
         Transformer reduceFOTree = reduceFOTreeTemplates.newTransformer();
         return new AccessibilityPreprocessor(addPtr, reduceFOTree, userAgent, handler);
      } catch (TransformerConfigurationException var4) {
         throw new FOPException(var4);
      }
   }

   private static synchronized void setupTemplates() throws TransformerConfigurationException {
      if (addPtrTemplates == null) {
         addPtrTemplates = loadTemplates("addPtr.xsl");
      }

      if (reduceFOTreeTemplates == null) {
         reduceFOTreeTemplates = loadTemplates("reduceFOTree.xsl");
      }

   }

   private static Templates loadTemplates(String source) throws TransformerConfigurationException {
      Source src = new StreamSource(Accessibility.class.getResource(source).toExternalForm());
      return tfactory.newTemplates(src);
   }
}
